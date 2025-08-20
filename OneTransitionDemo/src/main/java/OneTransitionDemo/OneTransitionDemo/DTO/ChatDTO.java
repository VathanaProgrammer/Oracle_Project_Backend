package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.ChatMessage;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class ChatDTO {
    private static final Logger log = LoggerFactory.getLogger(ChatDTO.class);
    private Long id;

    private String type; // text, image, audio, file
    private String content; // message text
    private String filename; // original filename
    private String fileUrl; // where the file is stored (disk or cloud)
    private String fileType; // "mp3", "png", "pdf", etc.

    private Long receiverId;
    private Long senderId;
    private String senderName;
    private String senderRole;
    private LocalDateTime timestamp;
    private String senderProfile;
    private String status;

    private List<Long> seenBy;

    private Long chatId;

    public ChatDTO() {}
    public ChatDTO(ChatMessage message, User sender, Long currentUserId){
        this.id = message.getId();
        this.chatId = message.getChatId();
        this.type = message.getType();
        this.content = message.getContent();
        this.filename = message.getFilename();
        this.fileUrl = message.getFileUrl();
        this.fileType = message.getFileType();
        this.receiverId = message.getReceiverId();
        this.senderId = message.getSenderId();
        this.senderName = message.getSenderName();
        this.timestamp = message.getTimestamp();
        this.senderProfile = message.getSenderProfile();
        this.seenBy = message.getSeenBy();
        this.senderRole = sender.getRole().toString();
        // Derive status
        if (seenBy != null && seenBy.contains(currentUserId)) {
            this.status = "READ";
        } else {
            this.status = "UNREAD";
        }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getSenderProfile() { return senderProfile; }
    public void setSenderProfile(String senderProfile) { this.senderProfile = senderProfile; }

    public List<Long> getSeenBy() { return seenBy; }
    public void setSeenBy(List<Long> seenBy) { this.seenBy = seenBy; }

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
