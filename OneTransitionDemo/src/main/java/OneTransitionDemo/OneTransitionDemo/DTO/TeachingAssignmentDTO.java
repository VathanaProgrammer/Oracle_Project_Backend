package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.TeachingAssignment;

public class TeachingAssignmentDTO {
    private Long id;
    private Long teacherId;
    private String teacherName;
    private Long assignedToId;

    public TeachingAssignmentDTO(TeachingAssignment teachingAssignment) {
        this.teacherName = teachingAssignment.getTeacher().getFirstname() + " " + teachingAssignment.getTeacher().getLastname();
        this.id = teachingAssignment.getId();
        this.teacherId = teachingAssignment.getTeacher().getId();
        this.assignedToId = teachingAssignment.getAssignedTo().getId();
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public Long getAssignedToId() {
        return assignedToId;
    }

    public Long getId() {
        return id;
    }

    public String getTeacherName() {
        return teacherName;
    }
}
