package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.Subject;

public class SubjectDTO {
    private Long id;
    private String name;
    private Long majorId;
    public SubjectDTO(){}
    public SubjectDTO(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.majorId = subject.getMajor().getId();
    }

    public Long getMajorId() {
        return majorId;
    }

    public void setMajorId(Long majorId) {
        this.majorId = majorId;
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
