package OneTransitionDemo.OneTransitionDemo.Request;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class MessageSendRequestForEveryone {
    private String type; // "text", "audio", "file"
    private String content; // text content (optional if it's file/audio)
    private String senderName;
    private Long senderId;
    private Long chatId;
    private String filename;
    private MultipartFile file;
    private LocalDateTime timestamp;

    // Getters & Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
