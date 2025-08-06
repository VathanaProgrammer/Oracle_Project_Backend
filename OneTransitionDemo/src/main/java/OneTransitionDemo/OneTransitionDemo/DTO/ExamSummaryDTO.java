package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.ENUMS.ExamType;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;

import java.time.Duration;

public class ExamSummaryDTO {
    private Long id;
    private final ExamType type;
    private String subject;
    private String teacher;
    private String teacherImage;
    private String datetime;
    private String duration;
    private String deadline;
    private String status;
    private String title;
    private String description;

    public ExamSummaryDTO(Exam exam) {
        this.id = exam.getId();
        this.subject = exam.getAssignedTo().getSubject().getName(); // from examMeta.title
        this.teacher = exam.getTeacher().getUser().getFirstname() + " " + exam.getTeacher().getUser().getLastname(); // or fetch User.getFullName()
        this.teacherImage = exam.getTeacher().getUser().getProfilePicture();// hardcoded or from teacher profile
        this.datetime = exam.getStartTime().toString(); // or format as string
        this.deadline = exam.getEndTime().toString();
        this.status = exam.getStatus().toString();
        this.duration = Duration.between(exam.getStartTime(), exam.getEndTime()).toMinutes() + " mins";
        this.type = exam.getType(); // quiz / file / startable etc.
        this.title = exam.getTitle();
        this.description = exam.getDescription();
    }

    // Getters (or use Lombok @Getter)
    // Getters (required for JSON serialization)
    public String getSubject() { return subject; }
    public String getTeacher() { return teacher; }
    public String getTeacherImage() { return teacherImage; }
    public String getDatetime() { return datetime; }
    public String getDeadline() { return deadline; }
    public String getDuration() { return duration; }
    public ExamType getType() { return type; }
    public Long getId() { return id; }
    public String getDescription(){ return this.description;}
    public String getTitle(){return this.title;}

    public String getStatus() {
        return status;
    }
}
