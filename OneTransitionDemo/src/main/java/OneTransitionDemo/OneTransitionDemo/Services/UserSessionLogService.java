package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.UserSessionLogDTO;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserSessionLogRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserSessionLogService {

    @Autowired
    private UserSessionLogRepository userSessionLogRepository;

    public Map<String, Object> getRecentDevices(Long id){
        System.out.println("get recent devices for user id: "+ id);
        if(id == null){
            return ResponseUtil.error("User not found!");
        }
        List<UserSessionLogDTO> sessionLogs = userSessionLogRepository.findAllByUserId(id)
                .stream()
                .map(UserSessionLogDTO::new)
                .toList();

        return ResponseUtil.success("Recent devices found!", sessionLogs);
    }
}
