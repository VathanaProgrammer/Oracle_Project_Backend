package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Models.UserAction;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserActionService {
    @Autowired
    private UserActionRepository userActionRepository;

    public UserAction recordAction(Long userId, String type, String label, String description, String firstname, String lastname) {
        UserAction action = new UserAction();
        action.setUserId(userId);
        action.setFirstName(firstname);
        action.setLastName(lastname);
        action.setType(type);
        action.setLabel(label);
        action.setDescription(description);
        action.setTimestamp(LocalDateTime.now());
        return userActionRepository.save(action);
    }
}
