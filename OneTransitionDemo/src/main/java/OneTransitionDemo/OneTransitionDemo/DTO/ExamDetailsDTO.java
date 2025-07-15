package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Custom.LocalDateTimeCustomDeserializer;
import OneTransitionDemo.OneTransitionDemo.ENUMS.Status;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.List;

// ExamDetailsDTO.jav

public class ExamDetailsDTO {
    private Long id;
    private String title;
    private String type;
    private String description;
    private String startTime;

    private String endTime;

    private int duration;
    private String duration_unit;
    private Status status;
    private String teacherName;
    private Long teacherId;
    private AssignedToDTO assignedToDTO;
    private String teacherImage;
    private List<QuestionDTO> questions;

    public ExamDetailsDTO(Exam exam) {
        this.id = exam.getId();
        this.description = exam.getDescription();
        this.startTime = exam.getStartTime().toString();
        this.endTime = exam.getEndTime().toString();
        this.duration = exam.getDuration();
        this.duration_unit = exam.getDuration_unit();
        this.status = exam.getStatus();
        this.teacherName = exam.getTeacher().getUser().getFirstname() + " " + exam.getTeacher().getUser().getLastname();
        this.teacherId = exam.getTeacher().getId();
        this.assignedToDTO= new AssignedToDTO(exam.getAssignedTo());
        this.teacherImage = exam.getTeacher().getUser().getProfilePicture();
        this.title = exam.getTitle();
        this.type = exam.getType().name();
        this.questions = exam.getQuestions().stream()
                .map(QuestionDTO::new)
                .toList();
    }

    // getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public List<QuestionDTO> getQuestions() { return questions; }

    public Long getTeacherId() {
        return teacherId;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public AssignedToDTO getAssignedToDTO() {
        return assignedToDTO;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getDuration() {
        return duration;
    }

    public Status getStatus() {
        return status;
    }

    public String getDuration_unit() {
        return duration_unit;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherImage() {
        return teacherImage;
    }
}

