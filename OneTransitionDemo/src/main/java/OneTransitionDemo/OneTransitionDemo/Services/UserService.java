package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.AdminDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.StudentDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.UserDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.UserSummaryDTO;
import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import OneTransitionDemo.OneTransitionDemo.Models.*;
import OneTransitionDemo.OneTransitionDemo.Repositories.*;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final TeacherRepository teacherRepo;
    private final StudentRepository studentRepo;
    private final UserActionService userActionService;

    @Value("${upload.path.profile}")
    private String uploadPath;

    @Autowired
    private DepartmentRepository departmentRepo;

    private final PasswordEncoder passwordEncoder;
    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private JavaMailSender mailSender;

    public UserService(UserRepository userRepo, TeacherRepository teacherRepo, StudentRepository studentRepo, PasswordEncoder passwordEncoder, UserActionService userActionService) {
        this.userRepo = userRepo;
        this.teacherRepo = teacherRepo;
        this.studentRepo = studentRepo;
        this.passwordEncoder = passwordEncoder;
        this.userActionService = userActionService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

//    public Optional<User> findByUsername(String username) {
//        return userRepo.findByUsername(username);
//    }

    public Optional<User> findByEmail(String Email){
        return userRepo.findByEmail((Email));
    }

    @Transactional
    public Map<String, Object> registerUser(
            String firstName,
            String lastName,
            String password,
            String role,
            String phone,
            String email,
            MultipartFile profileImage,
            String gender,
            List<Long> departments,
            Long major,
            Long year,
            Long batch,
            Long semester,
            Long shift,
            Long location
    ) throws IOException {

        if (userRepo.existsByEmail(email)) {
            return ResponseUtil.error("Email already used");
        }

        User user = new User();
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setGender(gender);
        user.setPhone(phone);
        user.setRole(Role.valueOf(role.toUpperCase()));
        user.setPassword(passwordEncoder.encode(password));
        user.setDeleted(false);
        user.setEmail(email);

        // Handle image
        if (profileImage != null && !profileImage.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
            Path imagePath = uploadDir.resolve(filename);
            Files.copy(profileImage.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePicture(filename);
        }

        User savedUser = userRepo.save(user);
        userRepo.flush();
        System.out.println("User ID = " + savedUser.getId()); // Make sure this prints a real ID!

        switch (user.getRole()) {
            case TEACHER:
                Teacher teacher = new Teacher();
                System.out.println("user about to save: " + savedUser.getId());
                teacher.setUser(savedUser);  // This sets ID implicitly via @MapsId

                if (departments != null && !departments.isEmpty()) {
                    List<Department> deptEntities = departmentRepo.findAllById(departments);
                    for (Department dept : deptEntities) {
                        teacher.addDepartment(dept);
                    }
                }
                teacherRepo.save(teacher);

                break;


            case STUDENT:
                Student student = new Student();
                student.setUser(savedUser);

                // Set Major
                student.setMajor(
                        majorRepository.findById(major)
                                .orElseThrow(() -> new IllegalArgumentException("Major not found"))
                );

                // Set Batch
                student.setBatch(
                        batchRepository.findById(batch)
                                .orElseThrow(() -> new IllegalArgumentException("Batch not found"))
                );

                // Set Year
                student.setYear(year);

                // Optional: set default semester or other properties
                student.setSemester(
                        semesterRepository.findById(semester) // example if you want to default semester
                                .orElseThrow(() -> new IllegalArgumentException("Semester not found"))
                );

                student.setShift(shiftRepository.findById(shift)
                                .orElseThrow(() -> new IllegalArgumentException("Shift not found"))
                );

                student.setLocation(locationRepository.findById(location)
                                .orElseThrow(() -> new IllegalArgumentException("Location not found"))
                );

                studentRepo.save(student);
                break;


            case ADMIN:
                // No extra handling needed
                break;

            default:
                throw new IllegalArgumentException("Unsupported role: " + role);
        }

        return ResponseUtil.success("User registered successfully");
    }

//    public List<UserDTO> getAllUsers() {
//        List<User> users = userRepo.findAll();
//        System.out.println("Users found: " + users.size()); // Log the size
//        return users.stream()
//                .map(UserDTO::new) // Convert User to UserDTO
//                .collect(Collectors.toList());
//    }

//    public List<UserDTO> getUserFirends(Long userId){
//        return userRepo.findFriendsOfUser(userId)
//                .stream()
//                .map(UserDTO::new)
//                .collect(Collectors.toList());
//    }
//
//    public List<UserDTO> getNoUserFriends(Long UserId){
//        return userRepo.findNotFriendsOfUser(UserId)
//                .stream()
//                .map(UserDTO::new)
//                .collect(Collectors.toList());
//    }


    public boolean existsById(Long receiverId) {
        return userRepo.findById(receiverId).isPresent();
    }

    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public Map<String, Object> updateUser(Long id, String firstName, String lastName, String role, String email, MultipartFile profileImage, String gender) throws IOException {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (!user.getEmail().equals(email) && userRepo.existsByEmail(email)) {
            return ResponseUtil.error("This email is already use");
        }

        Role oldRole = user.getRole();
        Role newRole = Role.valueOf(role.toUpperCase());

        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setGender(gender);
        user.setRole(newRole);
        user.setEmail(email);

        if (profileImage != null && !profileImage.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path imagePath = uploadDir.resolve(filename);
            Files.copy(profileImage.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePicture(filename);
        }

        userRepo.save(user);

        // Handle role-specific entity management
        if (!oldRole.equals(newRole)) {
            // Clean up old role entities
            switch (oldRole) {
                case TEACHER:
                    teacherRepo.findByUserId(id).ifPresent(teacherRepo::delete);
                    break;
                case STUDENT:
                    studentRepo.findByUserId(id).ifPresent(studentRepo::delete);
                    break;
                // No cleanup needed for ADMIN
            }

            // Create a new role entity if needed
            switch (newRole) {
                case TEACHER:
                    Teacher teacher = new Teacher();
                    teacher.setUser(user);
                    teacherRepo.save(teacher);
                    break;
                case STUDENT:
                    Student student = new Student();
                    student.setUser(user);
                    student.setName(firstName + " " + lastName);
                    studentRepo.save(student);
                    break;
                // No creation needed for ADMIN
            }
        }

        return ResponseUtil.success("User updated successfully");
    }


    public List<UserDTO> getAllUsers(Role role, String search, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<User> usersPage;

        if (role != null && search != null && !search.isBlank()) {
            usersPage = userRepo.searchByRole(role, search, pageable);
        } else if (role != null) {
            usersPage = userRepo.findByRole(role, pageable);
        } else if (search != null && !search.isBlank()) {
            usersPage = userRepo.searchAllRoles(search, pageable);
        } else {
            usersPage = userRepo.findAll(pageable);
        }

        return usersPage.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public Map<String, Object> getUserSummary(Long id){
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseUtil.error("User not found!");
        }

        UserSummaryDTO dto = new UserSummaryDTO(userOpt.get());
        return ResponseUtil.success("User found!", dto);
    }

    public Map<String, Object> changeUserPassword(Long id, String oldPassword, String newPassword){
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseUtil.error("User not found!");
        }
        User user = userOpt.get();

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            return ResponseUtil.error("Old password is incorrect!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        return ResponseUtil.success("Password changed successfully!");
    }

    public Map<String, Object> changePasswordForAdmin(Long id, Long adminId, String newPassword){
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseUtil.error("User not found!");
        }
        User user = userOpt.get();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        userActionService.recordAction(user.getId(), "password_change", "Admin changed password for user", "Admin changed password for user", user.getFirstname(), user.getLastname(), user.getProfilePicture(),user.getRole());
        return ResponseUtil.success("Password changed successfully!");
    }

    public Map<String, Object> getAdminById(Long id){
        if(id == null){
            return ResponseUtil.error("User not found!");
        }

        Optional<User> opAdmin = userRepo.findById(id);
        if(opAdmin.isEmpty()){
            return ResponseUtil.error("User not found!");
        }

        AdminDTO dto = new AdminDTO(opAdmin.get());
        return ResponseUtil.success("User found!", dto);
    }
    @Autowired
    private UserRepository userRepository;
    @Transactional
    public  Map<String, Object> updatePassword(String email, String oldPassword, String newPassword) {
       Optional <User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return ResponseUtil.error("User not found!");
        }
        User user = optionalUser.get();

         if(!passwordEncoder.matches(oldPassword, user.getPassword())){
             return ResponseUtil.error("Incorrect Current password!");
         }
        // Encode the new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseUtil.success("Your password was changed!");
    }

    public Map<String, Object> getAllStudent() {
         List<StudentDTO>  studentDTO =  studentRepo.findAll().stream().map(StudentDTO::new).toList();
         return ResponseUtil.success("Success", studentDTO);
    }

    public List<User> getUsersForExport(List<Long> ids, String role, Integer limit) {
        // If specific IDs selected
        if (ids != null && !ids.isEmpty()) {
            return userRepository.findByIdIn(ids); // returns List<User>
        }

        // If role is ALL / null / empty
        if (role == null || role.isEmpty() || "ALL".equalsIgnoreCase(role) || "All Roles".equalsIgnoreCase(role)) {
            return userRepository.findAll(PageRequest.of(0, limit)).getContent(); // Page<User> -> List<User>
        }

        // Convert role string to enum
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }

        return userRepository.findByRole(roleEnum, PageRequest.of(0, limit)).getContent();
    }



    public ByteArrayInputStream generateExcelReport(List<User> users) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        // Header
        Row header = sheet.createRow(0);
        String[] columns = {"ID", "Full Name", "Email", "Role", "Phone"};
        for (int i = 0; i < columns.length; i++) header.createCell(i).setCellValue(columns[i]);

        // Data
        int rowIdx = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getFirstname() + " " + user.getLastname());
            row.createCell(2).setCellValue(user.getEmail());
            row.createCell(3).setCellValue(user.getRole().name());
            row.createCell(4).setCellValue(user.getPhone());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generatePdfReport(List<User> users) throws Exception {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 4, 5, 3, 3});

        // Header
        Stream.of("ID", "Full Name", "Email", "Role", "Phone")
                .forEach(headerTitle -> table.addCell(new PdfPCell(new Phrase(headerTitle))));

        // Rows
        for (User user : users) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getFirstname() + " " + user.getLastname());
            table.addCell(user.getEmail());
            table.addCell(user.getRole().name());
            table.addCell(user.getPhone());
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public Map<String, Object>  getUserInfo(Long id){
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseUtil.error("User not found!");
        }

        UserDTO dto = new UserDTO(userOpt.get());
        return ResponseUtil.success("User found!", dto);
    }
}
