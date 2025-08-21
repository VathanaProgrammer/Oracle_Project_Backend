package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.AnswerFeedback;
import OneTransitionDemo.OneTransitionDemo.Models.CompleteExam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExamGRADED {
    private Long examId;
    private String subject;
    private String teacher;
    private String datetime; // completedAt
    private Long totalScore; // sum of feedback scores
    private String status;   // Passed / Failed

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");

    public ExamGRADED() {}
    public ExamGRADED(Long examId, String title, String subject, LocalDateTime completedAt, Long totalScore) {
        this.examId = examId;
        this.subject = subject;
        this.datetime = completedAt != null ? FORMATTER.format(completedAt) : "N/A";
        this.totalScore = totalScore;
        this.status = totalScore >= 60 ? "Passed" : "Failed"; // example
    }


    public String getStatus() {
        return status;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTotalScore() {
        return totalScore;
    }

    public String getDatetime() {
        return datetime;
    }

    public static DateTimeFormatter getFORMATTER() {
        return FORMATTER;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }

    // getters...
}
