package OneTransitionDemo.OneTransitionDemo.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_user_actions")
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String firstName; // optional but useful for display/debugging

    private String lastName;

    private String type; // e.g., "EXAM_SUBMITTED", "POST_CREATED", "PROFILE_UPDATED"

    private String targetType; // e.g., "Exam", "Message", "Profile"
    private Long targetId;     // e.g., examId, messageId, etc.

    private String label;      // short title to show in UI
    private String description; // more details about the action

    private LocalDateTime timestamp;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Long getTargetId() {
        return targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    // Getters and Setters
}

