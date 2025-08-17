package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.TeacherSubject;

public class TeacherSubjectDTO {
    private Long id;
    private String name;
    private Long subjectId;
    public TeacherSubjectDTO(TeacherSubject teacherSubject){
        this.id = teacherSubject.getId();
        this.name = teacherSubject.getSubject().getName();
        this.subjectId = teacherSubject.getSubject().getId();
    }
    public Long getSubjectId() {
        return subjectId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
