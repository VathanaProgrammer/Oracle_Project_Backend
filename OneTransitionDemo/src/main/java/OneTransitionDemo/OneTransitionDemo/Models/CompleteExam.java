package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CompleteExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    // Constructors
    public CompleteExam() {}

    public CompleteExam(User user, Exam exam, LocalDateTime completedAt) {
        this.user = user;
        this.exam = exam;
        this.completedAt = completedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }
}
