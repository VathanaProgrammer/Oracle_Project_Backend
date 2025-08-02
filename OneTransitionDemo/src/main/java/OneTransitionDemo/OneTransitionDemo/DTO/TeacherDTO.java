package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import OneTransitionDemo.OneTransitionDemo.Models.Department;
import OneTransitionDemo.OneTransitionDemo.Models.Teacher;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TeacherDTO {
    private Long teacherId;
    private String name;
    private Role role;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String email;
    private String phone;
    private String gender;
    private List<DepartmentDTO> departments;

    public TeacherDTO(){}
    public TeacherDTO(Teacher teacher){
        this.teacherId = teacher.getUser().getId();
        this.name = teacher.getUser().getFirstname() + " " + teacher.getUser().getLastname();
        this.role = teacher.getUser().getRole();
        this.firstName = teacher.getUser().getFirstname();
        this.lastName = teacher.getUser().getLastname();
        this.profilePicture = teacher.getUser().getProfilePicture();
        this.email = teacher.getUser().getEmail();
        this.phone = teacher.getUser().getPhone();
        this.gender = teacher.getUser().getGender();
        // Convert Set<Department> to List<DepartmentDTO>
        this.departments = teacher.getDepartments().stream()
                .map(DepartmentDTO::new)
                .collect(Collectors.toList());
    }
    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long userId) {
        this.teacherId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<DepartmentDTO> getDepartments() {
        return departments;
    }

    public void setDepartments(List<DepartmentDTO> departments) {
        this.departments = departments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
