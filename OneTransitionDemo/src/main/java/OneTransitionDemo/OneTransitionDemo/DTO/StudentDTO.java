package OneTransitionDemo.OneTransitionDemo.DTO;
import OneTransitionDemo.OneTransitionDemo.Models.Student;
import OneTransitionDemo.OneTransitionDemo.Models.User;

public class StudentDTO {
    private Long id;
    private String name;
    private String gender;
    private Long year;
    private Long userId;
    private String username;
    private String email;
    private String role;  // Or Role enum
    private String firstname;
    private String lastname;
    private String phone;
    private String profilePicture;
    private String major;     // Assuming Major has getName()
    private String classGroup; // Assuming ClassGroup has getName()
    private String batch;
    private Long semester;
    private Long shiftId;
    private String shiftName;
    private String shiftTime;
    private Long locationId;
    private String location;

    public StudentDTO() {
    }// Assuming Batch has getName()
    public StudentDTO(Student student) {
        this.id = student.getId();
        this.name = student.getUser().getFirstname() + " " + student.getUser().getLastname();
        this.userId = student.getUser().getId();
        this.firstname = student.getUser().getFirstname();
        this.lastname = student.getUser().getLastname();
        this.gender = student.getUser().getGender(); // if in Student, else student.getUser().getGender()
        this.role = student.getUser().getRole().name();
        this.batch = student.getBatch().getStartYear() + " - " + student.getBatch().getEndYear();
        this.year = student.getYear();
        this.major = student.getMajor().getName() != null ? student.getMajor().getName() : null;
        this.email = student.getUser().getEmail();
        this.phone = student.getUser().getPhone();
        this.semester = student.getSemester().getNumber();
        this.shiftId = student.getShift().getId();
        this.shiftName = student.getShift().getName();
        this.shiftTime = student.getShift().getStartTime() + " - " + student.getShift().getEndTime();
        this.locationId = student.getLocation().getId();
        this.location = student.getLocation().getRomeName();
        this.profilePicture = student.getUser().getProfilePicture();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(String classGroup) {
        this.classGroup = classGroup;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Long getSemester() {
        return semester;
    }

    public String getLocation() {
        return location;
    }

    public String getShiftName() {
        return shiftName;
    }

    public String getShiftTime() {
        return shiftTime;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public Long getLocationId() {
        return locationId;
    }
}
