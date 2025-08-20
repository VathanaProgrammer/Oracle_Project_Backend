package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

@Entity
@Table(name ="answer_feedback")
public class AnswerFeedback {
    @Id
    @SequenceGenerator(name = "answer_feedback_seq", sequenceName = "ANSWER_FEEDBACK_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answer_feedback_seq")
    private Long Id;
    private Long score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // Getters
    public Long getId() {
        return Id;
    }

    public User getUser() {
        return user;
    }

    public Exam getExam() {
        return exam;
    }

    public Question getQuestion() {
        return question;
    }

    // Setters
    public void setId(Long id) {
        this.Id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
    public Long getScore(){
        return score;
    }

    public void setScore(Long score) {

    }
}
