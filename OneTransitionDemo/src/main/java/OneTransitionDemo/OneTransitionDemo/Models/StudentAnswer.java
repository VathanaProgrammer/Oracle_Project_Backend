package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

@Entity
@Table( name = "TBL_STUDENT_ANSWERS")
public class StudentAnswer {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Question question;

    @ManyToOne
    private AnswerOption selectedOption;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public AnswerOption getSelectedOption() { return selectedOption; }
    public void setSelectedOption(AnswerOption selectedOption) { this.selectedOption = selectedOption; }
}

