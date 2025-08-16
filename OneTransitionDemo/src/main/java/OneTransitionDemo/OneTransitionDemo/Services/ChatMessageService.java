package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Models.ChatMessage;
import OneTransitionDemo.OneTransitionDemo.Repositories.ChatMessageRepository;
import OneTransitionDemo.OneTransitionDemo.Request.MessageSendRequestForEveryone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository repository;

    @Value("${upload.path.profile}")
    private String uploadPath; // example: uploads/ or /var/data/uploads

    public ChatMessage saveMessage(MessageSendRequestForEveryone request) throws IOException {
        ChatMessage message = new ChatMessage();
        message.setType(request.getType());
        message.setContent(request.getContent());
        message.setSenderId(request.getSenderId());
        message.setSenderName(request.getSenderName());
        message.setTimestamp(LocalDateTime.now());
        message.setSeenBy(new ArrayList<>());
        message.setChatId(request.getChatId());
        message.getSeenBy().add(request.getSenderId());

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            String originalFilename = request.getFile().getOriginalFilename();
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;

            // Ensure folder exists
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Full file path
            Path filePath = uploadDir.resolve(uniqueFilename);

            // Save file
            Files.copy(request.getFile().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Save metadata in DB
            message.setFilename(originalFilename);
            message.setFileUrl(filePath.toString());  // store path (or relative URL if serving via controller)
            message.setFileType(Files.probeContentType(filePath));
        }

        return repository.save(message);
    }

    public List<ChatMessage> getMessagesForEveryone() {
        return repository.findByChatIdOrderByTimestampAsc(1L);
    }
}
