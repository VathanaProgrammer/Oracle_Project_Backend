package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.UserActionDTO;
import OneTransitionDemo.OneTransitionDemo.Models.UserAction;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserActionRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserActionService {
    private static final Logger log = LoggerFactory.getLogger(UserActionService.class);
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

    public Map<String, Object> getUserRecentActionsById(Long userId){
        List<UserActionDTO> userActionDTOS = userActionRepository.findByUserId(userId)
                .stream()
                .map(action -> {
                    UserActionDTO dto = new UserActionDTO();
                    dto.setId(action.getId());
                    dto.setUserId(action.getUserId());
                    dto.setDescription(action.getDescription());
                    dto.setFirstName(action.getFirstName());
                    dto.setLastName(action.getLastName());
                    dto.setType(action.getType());
                    dto.setTimestamp(action.getTimestamp());
                    return dto;
                })
                .toList();

        return ResponseUtil.success("Recent actions found!", userActionDTOS);
    }
}
