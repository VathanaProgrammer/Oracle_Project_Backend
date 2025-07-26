package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.DTO.UserSessionLogDTO;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Models.UserSessionLog;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserSessionLogRepository;
import OneTransitionDemo.OneTransitionDemo.Services.UserSessionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/session-logs")
public class UserSessionLogController {
    @Autowired
    private UserSessionLogRepository userSessionLogRepository;

    @Autowired
    private UserSessionLogService userSessionLogService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecentDevicesForUser(@AuthenticationPrincipal User user, @PathVariable Long id){
        Map<String, Object> response = userSessionLogService.getRecentDevices(id);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }
}
