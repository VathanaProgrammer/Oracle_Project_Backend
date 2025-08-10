package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Models.Department;
import OneTransitionDemo.OneTransitionDemo.Models.Major;
import OneTransitionDemo.OneTransitionDemo.Models.Teacher;
import OneTransitionDemo.OneTransitionDemo.Repositories.DepartmentRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.MajorRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.TeacherRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Request.AdminDepartmentRequest;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AdminDepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private TeacherRepository teacherRepository;


    public Map<String, Object> createDepartmentWithAcceptNullTeacherOrMajor(AdminDepartmentRequest request) {
        if (request == null) {
            return ResponseUtil.error("Department request cannot be empty");
        }

        Department department = new Department();
        department.setName(request.getName());

        // Handle optional Major
        if (request.getMajorId() != null) {
            Optional<Major> majorOptional = majorRepository.findById(request.getMajorId());
            if (majorOptional.isEmpty()) {
                return ResponseUtil.error("Major not found with ID: " + request.getMajorId());
            }
            Major major = majorOptional.get();
            // Add major to department's major set (assuming relationship is OneToMany from the Department)
            department.getMajors().add(major);
            major.setDepartment(department);  // if bidirectional and needed
        }

        // Handle optional Teacher
        if (request.getUserId() != null) {
            Optional<Teacher> teacherOptional = teacherRepository.findByUserId(request.getUserId());
            if (teacherOptional.isEmpty()) {
                return ResponseUtil.error("Teacher not found with User ID: " + request.getUserId());
            }
            Teacher teacher = teacherOptional.get();
            // Add teacher to department's teachers' set (assuming ManyToMany)
            department.getTeachers().add(teacher);
            teacher.getDepartments().add(department);  // maintain a bidirectional relationship if needed
        }

        // Save the department (which should cascade save relations if configured)
        departmentRepository.save(department);

        return ResponseUtil.success("Department created successfully", department);
    }



}
