package OneTransitionDemo.OneTransitionDemo.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table( name = "TBL_QUESTION")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content; // Question text
    private String type; // "true_false", "multiple_choice", "file_exam"

    private Boolean autoScore;
    private Integer score;

    private String correctAnswer; // For true/false
    private Integer correctAnswerIndex; // For multiple_choice

    @ElementCollection(fetch = FetchType.EAGER) // 	This is not another entity, just a list of basic values (like Strings, Integers, etc.)
    @CollectionTable( // Tells JPA to store this list in its own table
            name = "TBL_QUESTION_OPTIONS",
            joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "option_text")
    private List<String> options;
 // Still fine, no new class needed

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ExamFile> examFiles;

    @JsonIgnore
    @ManyToMany(mappedBy = "questions")
    private List<Exam> exams;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<Exam> getExam() { return exams; }
    public void setExam(List<Exam> exam) { this.exams = exam; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Boolean getAutoScore() { return autoScore; }
    public void setAutoScore(Boolean autoScore) { this.autoScore = autoScore; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public Integer getCorrectAnswerIndex() { return correctAnswerIndex; }
    public void setCorrectAnswerIndex(Integer correctAnswerIndex) { this.correctAnswerIndex = correctAnswerIndex; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public List<Exam> getExams() { return exams; }
    public void setExams(List<Exam> exams) { this.exams = exams; }

    public void setExamFiles(List<ExamFile> examFiles){
        this.examFiles = examFiles;
    }

    public List<ExamFile> getExamFiles(){
        return this.examFiles;
    }
}

