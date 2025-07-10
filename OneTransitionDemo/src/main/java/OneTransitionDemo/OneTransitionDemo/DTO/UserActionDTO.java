package OneTransitionDemo.OneTransitionDemo.DTO;

import java.time.LocalDateTime;

public class UserActionDTO {

    private Long userId;

    private String firstName;
    private String lastName;

    private String type;         // EXAM_SUBMITTED, POST_CREATED, etc.
    private String targetType;   // "Exam", "Message", etc.
    private Long targetId;       // e.g., 42

    private String label;        // short title for display
    private String description;  // full description

    private LocalDateTime timestamp;

    // Constructors
    public UserActionDTO() {}

    public UserActionDTO(Long userId, String firstName, String lastName,
                         String type,
                         String label, String description) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.label = label;
        this.description = description;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getTargetType() { return targetType; }

    public void setTargetType(String targetType) { this.targetType = targetType; }

    public Long getTargetId() { return targetId; }

    public void setTargetId(Long targetId) { this.targetId = targetId; }

    public String getLabel() { return label; }

    public void setLabel(String label) { this.label = label; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
