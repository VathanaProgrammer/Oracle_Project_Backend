package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.Models.ChatMessage;
import OneTransitionDemo.OneTransitionDemo.Request.MessageSendRequestForEveryone;
import OneTransitionDemo.OneTransitionDemo.Services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin-chat")
public class AdminChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageService chatMessageService;

    @MessageMapping("/chat.send") // frontend sends here
    public void sendMessage(MessageSendRequestForEveryone request) {
        ChatMessage saved = chatMessageService.saveMessage(request);
        // broadcast to all subscribed clients
        messagingTemplate.convertAndSend("/topic/public", saved);
    }

    // New: fetch all messages for Everyone
    @GetMapping("/everyone")
    public List<ChatMessage> getEveryoneMessages() {
        return chatMessageService.getMessagesForEveryone();
    }

}
