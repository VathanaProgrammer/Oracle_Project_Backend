package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.Result;
import java.time.LocalDateTime;

public class ResultDTO {
    private Long id;
    private String studentName;
    private double score;
    private LocalDateTime submittedAt;
    private Long examId;
    private Long assignmentId;
    private Long studentId;

    public ResultDTO(Result r) {
        this.id = r.getId();
        this.studentName = r.getStudent().getFirstname() + " " + r.getStudent().getLastname() ;
        this.score = r.getScore();
        this.submittedAt = r.getSubmittedAt();
        this.examId = r.getExam().getId();
        this.assignmentId = r.getAssignment().getId();
        this.studentId = r.getStudent().getId();
    }

    // Getters (for JSON serialization)
    public Long getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public double getScore() {
        return score;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public Long getExamId() {
        return examId;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

}
