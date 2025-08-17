package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Custom.LocalDateTimeCustomDeserializer;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.List;

public class ExamDTO {
    private Long id;
    private String title;
    private String description;
    private String type;
    private Long createdBy;
    private String status;

    @JsonDeserialize(using = LocalDateTimeCustomDeserializer.class)
    private LocalDateTime startTime;

    @JsonDeserialize(using = LocalDateTimeCustomDeserializer.class)
    private LocalDateTime endTime;

    private int duration;
    private String durationUnit;

    private Long assignTo;

    private List<QuestionDTO> questions;

    public ExamDTO(Exam exam){
        this.id = exam.getId();
        this.title = exam.getTitle();
        this.description= exam.getDescription();
        this.type = String.valueOf(exam.getType());
        this.createdBy = exam.getTeacher().getUser().getId();
        this.status = String.valueOf(exam.getStatus());
        this.startTime = exam.getStartTime();
        this.endTime = exam.getEndTime();
        this.duration = exam.getDuration();
        if (exam.getQuestions() != null) {
            this.questions = exam.getQuestions().stream()
                    .map(QuestionDTO::new) // use your existing constructor
                    .toList();
        } else {
            this.questions = List.of(); // fallback empty list
        }
        this.assignTo = exam.getAssignedTo().getId();
    }
    // Getters and Setters

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Long getAssignTo() { return assignTo; }
    public void setAssignTo(Long assignTo) { this.assignTo = assignTo; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public List<QuestionDTO> getQuestions() { return questions; }
    public void setQuestions(List<QuestionDTO> questions) { this.questions = questions; }

    public String getDurationUnit() {
        return durationUnit;
    }

    public String getStatus(){ return status; }

    public void setStatus(String status){ this.status = status; }
    private String teacherProfilePicture;

    public String getTeacherProfilePicture() {
        return teacherProfilePicture;
    }
    public void setTeacherProfilePicture(String teacherProfilePicture) {
        this.teacherProfilePicture = teacherProfilePicture;
    }

    public Long getId() {
        return id;
    }
}
