package OneTransitionDemo.OneTransitionDemo.Controllers;

import OneTransitionDemo.OneTransitionDemo.Models.UserAction;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserActionRepository;
import OneTransitionDemo.OneTransitionDemo.Services.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

}
