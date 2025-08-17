package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Models.ChatMessage;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.ChatMessageRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Request.MessageSendRequestForEveryone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository repository;

    @Value("${upload.path.file.file.and.audio}")
    private String uploadPath;
    @Autowired
    private UserRepository userRepository;

    public ChatMessage saveMessage(MessageSendRequestForEveryone request) throws IOException, InterruptedException {
        ChatMessage message = new ChatMessage();
        message.setType(request.getType());
        message.setContent(request.getContent());
        message.setSenderId(request.getSenderId());
        message.setSenderName(request.getSenderName());
        message.setTimestamp(LocalDateTime.now());
        message.setChatId(request.getChatId());
        message.setSeenBy(new ArrayList<>());
        message.getSeenBy().add(request.getSenderId());

        // Add sender profile if exists
        userRepository.findById(request.getSenderId())
                .ifPresent(user -> message.setSenderProfile(user.getProfilePicture()));

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            String originalFilename = request.getFile().getOriginalFilename();
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;

            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Save file directly
            Path filePath = uploadDir.resolve(uniqueFilename);
            Files.copy(request.getFile().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String contentType = Files.probeContentType(filePath);

            if (contentType.equals("audio/webm")) {
                // Convert WebM → WAV
                String wavFilename = uniqueFilename.replaceAll("\\.webm$", ".wav");
                Path wavPath = uploadDir.resolve(wavFilename);

                ProcessBuilder pb = new ProcessBuilder(
                        "C:\\ffmpeg-7.1.1-essentials_build\\bin\\ffmpeg.exe",
                        "-y",
                        "-i",
                        filePath.toString(),
                        wavPath.toString()
                );

                pb.inheritIO();
                Process process = pb.start();
                int exitCode = process.waitFor();
                if (exitCode != 0) throw new RuntimeException("FFmpeg conversion failed");

                // Delete the original WebM
                Files.deleteIfExists(filePath);

                // Save WAV info in DB
                message.setFilename(wavFilename);
                message.setFileUrl(wavFilename);
                message.setFileType(Files.probeContentType(wavPath));
            } else {
                // It's an image or other file → save as is
                message.setFilename(uniqueFilename);
                message.setFileUrl(uniqueFilename);
                message.setFileType(contentType);
            }
        }


        return repository.save(message);
    }


    public List<ChatMessage> getMessagesForEveryone() {
        return repository.findByChatIdOrderByTimestampAsc(1L);
    }
    public List<ChatMessage> getMessagesForChat(Long chatId) {
        return repository.findByChatIdOrderByTimestampAsc(chatId);
    }

    public List<ChatMessage> getMessagesForAllStudents(Long chatId) {
        return repository.findByChatIdOrderByTimestampAsc(chatId);
    }


}
