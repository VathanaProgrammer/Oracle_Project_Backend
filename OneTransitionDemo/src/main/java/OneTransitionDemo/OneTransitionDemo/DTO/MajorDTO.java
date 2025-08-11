package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.Major;

public class MajorDTO {
    private Long id;
    private String name;
    private String departmentName;
    private Long departmentId;

    public MajorDTO(Major major) {
        this.id = major.getId();
        this.name = major.getName();
        this.departmentId = (major != null && major.getDepartment() != null) ? major.getDepartment().getId() : null;
        this.departmentName = major.getDepartment().getName();
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
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
}
