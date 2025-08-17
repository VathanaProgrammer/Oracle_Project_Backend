package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.ENUMS.ExamType;
import OneTransitionDemo.OneTransitionDemo.Models.AcademicYear;
import OneTransitionDemo.OneTransitionDemo.Models.Batch;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ExamSummaryDTO {

    private final Long id;
    private final ExamType type;
    private final String subject;
    private final String teacher;
    private final String teacherImage;
    private final String datetime;
    private final String duration;
    private final String deadline;
    private final String status;
    private final String title;
    private final String description;
    private final String batchName;
    private final Long batchId;
    private final String academicYear;
    private final String major;
    private final Long teacherId;
    private final String location;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ExamSummaryDTO(Exam exam) {
        this.id = exam.getId();
        this.type = exam.getType();
        this.subject = Optional.ofNullable(exam.getAssignedTo())
                .map(a -> a.getSubject().getName())
                .orElse("Unknown Subject");

        this.teacher = Optional.ofNullable(exam.getTeacher())
                .map(t -> t.getUser().getFirstname() + " " + t.getUser().getLastname())
                .orElse("Unknown Teacher");

        this.teacherImage = Optional.ofNullable(exam.getTeacher())
                .map(t -> t.getUser().getProfilePicture())
                .orElse(null);

        this.datetime = Optional.ofNullable(exam.getStartTime())
                .map(DATE_FORMATTER::format)
                .orElse("N/A");

        this.deadline = Optional.ofNullable(exam.getEndTime())
                .map(DATE_FORMATTER::format)
                .orElse("N/A");

        this.status = Optional.ofNullable(exam.getStatus())
                .map(Enum::toString)
                .orElse("N/A");

        this.duration = Optional.ofNullable(exam.getStartTime())
                .flatMap(start -> Optional.ofNullable(exam.getEndTime())
                        .map(end -> {
                            Duration d = Duration.between(start, end);
                            long hours = d.toHours();
                            long minutes = d.toMinutesPart();
                            return hours > 0 ? hours + "h " + minutes + "m" : minutes + "m";
                        }))
                .orElse("N/A");

        this.title = Optional.ofNullable(exam.getTitle()).orElse("");
        this.description = Optional.ofNullable(exam.getDescription()).orElse("");
        this.teacherId = exam.getTeacher().getUser().getId();

        Batch batch = Optional.ofNullable(exam.getAssignedTo()).map(a -> a.getBatch()).orElse(null);
        this.batchName = batch != null ? batch.getBatchName() : "N/A";
        this.batchId = batch != null ? batch.getId() : null;

        AcademicYear year = Optional.ofNullable(exam.getAssignedTo()).map(a -> a.getAcademicYear()).orElse(null);
        this.academicYear = year != null ? year.getName() : "N/A";

        this.major = Optional.ofNullable(exam.getAssignedTo())
                .map(a -> a.getMajor())
                .map(m -> m.getName())
                .orElse("N/A");

        this.location = Optional.ofNullable(exam.getAssignedTo())
                .map(a -> a.getLocation())
                .map(l -> l.getRomeName())
                .orElse("N/A");
    }

    // Getters
    public Long getId() { return id; }
    public ExamType getType() { return type; }
    public String getSubject() { return subject; }
    public String getTeacher() { return teacher; }
    public String getTeacherImage() { return teacherImage; }
    public String getDatetime() { return datetime; }
    public String getDeadline() { return deadline; }
    public String getDuration() { return duration; }
    public String getStatus() { return status; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getBatchName() { return batchName; }
    public Long getBatchId() { return batchId; }
    public String getAcademicYear() { return academicYear; }
    public String getMajor() { return major; }
    public String getLocation() { return location; }
    public Long getTeacherId(){return  teacherId;}
}
