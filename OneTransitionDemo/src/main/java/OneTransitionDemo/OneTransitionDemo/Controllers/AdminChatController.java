package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.ChatDTO;
import OneTransitionDemo.OneTransitionDemo.Models.ChatMessage;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Request.MessageSendRequestForEveryone;
import OneTransitionDemo.OneTransitionDemo.Services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin-chat")
public class AdminChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageService chatMessageService;

    @Value("${upload.path.file.file.and.audio}")
    private String uploadPath;
    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/chat.send")
    @PostMapping("/send")// frontend sends here
    public ChatDTO sendMessage(@AuthenticationPrincipal User user, @ModelAttribute MessageSendRequestForEveryone request) throws IOException, InterruptedException {
        ChatMessage saved = chatMessageService.saveMessage(request);
        // broadcast to all subscribed clients
        // push into the announcements feed
        messagingTemplate.convertAndSend("/topic/ad-announcements", saved);
        messagingTemplate.convertAndSend("/topic/public", saved);
        Optional<User> userOptional = userRepository.findById(saved.getSenderId());
        User sender = userOptional.get();
        return new ChatDTO(saved, sender, user.getId());
    }

    // New: fetch all messages for Everyone
    @GetMapping("/everyone")
    public List<ChatMessage> getEveryoneMessages(@AuthenticationPrincipal User user) {
        return chatMessageService.getMessagesForEveryone();
    }

    // New: fetch all messages for All Teachers
    @GetMapping("/teachers")
    public List<ChatMessage> getAllTeachersMessages(@AuthenticationPrincipal User user) {
        return chatMessageService.getMessagesForChat(2L); // chatId = 2 for All Teachers
    }

    @GetMapping("/students")
    public List<ChatMessage> getAllStudentsMessages(@AuthenticationPrincipal User user) {
        return chatMessageService.getMessagesForChat(3L); // chatId = 3 for All Students
    }

    // Fetch all announcements (Everyone + All Teachers)
    @GetMapping("/ad-announcements")
    public List<ChatDTO> getAllAnnouncements(@AuthenticationPrincipal User user) {

        // Fetch messages
        List<ChatMessage> everyoneMessages = chatMessageService.getMessagesForEveryone();
        List<ChatMessage> teacherMessages = chatMessageService.getMessagesForChat(2L); // All Teachers

        // Combine both lists
        everyoneMessages.addAll(teacherMessages);

        // Optional: sort by datetime descending (newest first)
        everyoneMessages.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));

        // Convert to ChatDTO including sender role
        List<ChatDTO> dtos = new ArrayList<>();
        for (ChatMessage msg : everyoneMessages) {
            User sender = userRepository.findById(msg.getSenderId()).orElse(null);
            dtos.add(new ChatDTO(msg, sender, user.getId())); // use DTO constructor with sender
        }

        return dtos;
    }
    @PutMapping("/ad-announcements/{messageId}/seen")
    public void markMessageAsSeen(@PathVariable Long messageId, @AuthenticationPrincipal User currentUser) {
        chatMessageService.markAsSeen(messageId, currentUser.getId());
    }
}
