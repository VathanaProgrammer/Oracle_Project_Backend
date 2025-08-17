package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.Models.ChatMessage;
import OneTransitionDemo.OneTransitionDemo.Models.User;
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
import java.util.List;

@RestController
@RequestMapping("/api/admin-chat")
public class AdminChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageService chatMessageService;

    @Value("${upload.path.file.file.and.audio}")
    private String uploadPath;

    @MessageMapping("/chat.send")
    @PostMapping("/send")// frontend sends here
    public ChatMessage sendMessage(@AuthenticationPrincipal User user,  @ModelAttribute MessageSendRequestForEveryone request) throws IOException, InterruptedException {
        ChatMessage saved = chatMessageService.saveMessage(request);
        // broadcast to all subscribed clients
        messagingTemplate.convertAndSend("/topic/public", saved);
        return saved;
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
        return chatMessageService.getMessagesForChat(3L); // chatId = 2 for All Teachers
    }


}
