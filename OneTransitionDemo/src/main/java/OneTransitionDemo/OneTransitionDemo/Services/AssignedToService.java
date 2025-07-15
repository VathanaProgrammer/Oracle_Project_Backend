package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.AssignedToDTO;
import OneTransitionDemo.OneTransitionDemo.Models.AssignedTo;
import OneTransitionDemo.OneTransitionDemo.Repositories.AssignToRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignedToService {

    @Autowired
    private AssignToRepository assignToRepository;

    public List<AssignedToDTO> getAllAssignments(){
        List<AssignedTo> assignedTo = assignToRepository.findAll();
        return assignedTo.stream()
                .map(AssignedToDTO::new)
                .collect(Collectors.toList());
    }
}
