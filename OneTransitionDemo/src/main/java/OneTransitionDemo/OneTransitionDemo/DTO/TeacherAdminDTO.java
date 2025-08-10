package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherAdminDTO {
    private final Long userId;
    private final String fullName;
    private final List<DepartmentDTO> departments;

    public TeacherAdminDTO(Teacher teacher){
        this.userId = teacher.getUser().getId();
        this.fullName = teacher.getUser().getFirstname() + " " + teacher.getUser().getLastname();
        this.departments = teacher.getDepartments().stream()
                .map(DepartmentDTO::new)
                .collect(Collectors.toList());
    }

    public Long getUserId() {
        return userId;
    }

    public List<DepartmentDTO> getDepartments() {
        return departments;
    }

    public String getFullName() {
        return fullName;
    }
}
