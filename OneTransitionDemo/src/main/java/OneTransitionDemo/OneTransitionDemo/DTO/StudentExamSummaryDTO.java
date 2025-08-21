package OneTransitionDemo.OneTransitionDemo.DTO;

import java.time.LocalDateTime;

public class StudentExamSummaryDTO {
    private Long examId;
    private String examTitle;
    private String subject;
    private double totalScore;
    private double maxScore;
    private String status; // Pass / Fail
    private LocalDateTime submittedAt;


    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    // getters & setters
    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}