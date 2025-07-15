package OneTransitionDemo.OneTransitionDemo.Models;

import OneTransitionDemo.OneTransitionDemo.Custom.LocalDateTimeCustomDeserializer;
import OneTransitionDemo.OneTransitionDemo.ENUMS.ExamType;
import OneTransitionDemo.OneTransitionDemo.ENUMS.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table( name = "TBL_EXAMS")
public class Exam {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @JsonDeserialize(using = LocalDateTimeCustomDeserializer.class)
    private LocalDateTime startTime;

    @JsonDeserialize(using = LocalDateTimeCustomDeserializer.class)
    private LocalDateTime endTime;

    private int duration;
    private String duration_unit;

    @Enumerated(EnumType.STRING)
    @Column( name = "Exam_type")
    private ExamType type;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private AssignedTo assignedTo;

    @ManyToMany(cascade = CascadeType.ALL) // One Exam can have many question
    @JoinTable(name = "tbl_exam_questions",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<Question> questions;

    @OneToMany(mappedBy = "exam")
    private List<ExamResult> examResults;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }
    public ExamType getType() { return type; }
    public void setType(ExamType type) { this.type = type; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }


    public String getDuration_unit(){
        return this.duration_unit;
    }

    public void setDuration_unit(String duration_unit){
        this.duration_unit = duration_unit;
    }

    public int getDuration(){
        return this.duration;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public AssignedTo getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(AssignedTo assignedTo) {
        this.assignedTo = assignedTo;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setStatus(Status status){ this.status = status; }

    public Status getStatus(){ return this.status; }
}
