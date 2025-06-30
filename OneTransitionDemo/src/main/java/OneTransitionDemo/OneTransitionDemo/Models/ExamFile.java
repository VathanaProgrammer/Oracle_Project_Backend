package OneTransitionDemo.OneTransitionDemo.Models;

import OneTransitionDemo.OneTransitionDemo.Repositories.ExamRepository;
import jakarta.persistence.*;

@Entity
@Table(name = "TBL_EXAM_FILES")
public class ExamFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    // Constructors, getters, setters

    public ExamFile() {}

    public ExamFile(String title, String description, String fileUrl, Exam exam) {
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
        this.exam = exam;
    }

    // Getters and setters here...

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id  = id;
    }
    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getFileUrl(){
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl){
        this.fileUrl = fileUrl;
    }

    public Exam getExam(){
        return this.exam;
    }

    public void setExam(Exam exam){
        this.exam = exam;
    }

    public void setQuestion(Question question){
        this.question = question;
    }

    public Question getQuestion(){
        return this.question;
    }
}

