package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

@Entity
@Table( name = "TBL_ANSWER_OPTIONS")
public class AnswerOption {
    @Id
    @GeneratedValue
    private Long id;

    private String text;
    private boolean correct;

    @ManyToOne
    private Question question;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
}

