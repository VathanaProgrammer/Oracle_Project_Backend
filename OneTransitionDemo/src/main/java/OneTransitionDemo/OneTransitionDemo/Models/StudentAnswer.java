package OneTransitionDemo.OneTransitionDemo.Models;

import OneTransitionDemo.OneTransitionDemo.DTO.StudentAnswerDTO;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_STUDENT_ANSWERS")
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many answers belong to one student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User student;
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;
    // Many answers belong to one question
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    private Long answerIndex;
    private String answerContent;
    private Boolean answerTrueFalse;
    private String answerFilePath;
    private LocalDateTime submitAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public Long getAnswerIndex() { return answerIndex; }
    public void setAnswerIndex(Long answerIndex) { this.answerIndex = answerIndex; }

    public String getAnswerContent() { return answerContent; }
    public void setAnswerContent(String answerContent) { this.answerContent = answerContent; }

    public Boolean getAnswerTrueFalse() { return answerTrueFalse; }
    public void setAnswerTrueFalse(Boolean answerTrueFalse) { this.answerTrueFalse = answerTrueFalse; }

    public String getAnswerFilePath() { return answerFilePath; }
    public void setAnswerFilePath(String answerFilePath) { this.answerFilePath = answerFilePath; }

    public LocalDateTime getSubmitAt() { return submitAt; }
    public void setSubmitAt(LocalDateTime submitAt) { this.submitAt = submitAt; }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }
}
