package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.UserActionReportDTO;
import OneTransitionDemo.OneTransitionDemo.Models.UserAction;
import OneTransitionDemo.OneTransitionDemo.Models.UserSessionLog;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserActionRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserSessionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminReportService {

    @Autowired
    private UserActionRepository userActionRepository;

    @Autowired
    private UserSessionLogRepository userSessionLogRepository;

    public List<UserActionReportDTO> getUserActionReport() {
        List<UserAction> actions = userActionRepository.findAll();

        List<UserActionReportDTO> report = new ArrayList<>();

        for (UserAction action : actions) {
            UserSessionLog session = userSessionLogRepository
                    .findTopByUser_IdOrderByStartTimeDesc(action.getUserId())
                    .orElse(null); // get last session

            UserActionReportDTO dto = new UserActionReportDTO();
            dto.setId(action.getId());
            dto.setUserId(action.getUserId());
            dto.setFullName(action.getFirstName() + " " + action.getLastName());
            dto.setRole(action.getRole());
            dto.setActionType(action.getType());
            dto.setTargetType(action.getTargetType());
            dto.setDescription(action.getDescription());
            dto.setTimestamp(action.getTimestamp());
            dto.setProfilePicture(action.getProfilePicture());

            if (session != null) {
                dto.setBrowser(session.getBrowser());
                dto.setDeviceType(session.getDeviceType());
                dto.setDevice(session.getDevice());
                dto.setLocation(session.getLocation());
            }

            report.add(dto);
        }

        return report;
    }
}
