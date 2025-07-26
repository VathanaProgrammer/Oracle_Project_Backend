package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Models.UserAction;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserActionRepository;
import OneTransitionDemo.OneTransitionDemo.Services.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/actions")
public class UserActionController {

    @Autowired
    private UserActionRepository userActionRepository;

    @Autowired
    private UserActionService userActionService;

    @GetMapping("/recent")
    public List<UserAction> getRecentActions() {
        return userActionRepository
                .findTop10ByOrderByTimestampDesc(); // latest 10
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecentActionsForUser(@AuthenticationPrincipal User user, @PathVariable Long id){

        System.out.println("user: "+ user);
        if(user == null){
            return ResponseEntity.status(403).build();
        }
        Map<String, Object> response = userActionService.getUserRecentActionsById(id);
        return ResponseEntity.status((Boolean) response.get("success") ? 200 : 400).body(response);
    }
}
