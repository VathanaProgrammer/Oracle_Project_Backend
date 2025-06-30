package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.AssignedToDTO;
import OneTransitionDemo.OneTransitionDemo.Models.AssignedTo;
import OneTransitionDemo.OneTransitionDemo.Models.TeachingAssignment;
import OneTransitionDemo.OneTransitionDemo.Repositories.AssignToRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.TeachingAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeachingAssignmentService {
    @Autowired
    private TeachingAssignmentRepository teachingAssignmentRepository;

    @Autowired
    private AssignToRepository assignToRepository;
    public List<AssignedToDTO> getAssignmentsForTeacher(Long teacherId) {
        List<TeachingAssignment> teachingAssignments = teachingAssignmentRepository.findByTeacherId(teacherId);

        return teachingAssignments.stream()
                .map(ta -> {
                    AssignedTo assigned = ta.getAssignedTo(); // ✅ get from TeachingAssignment
                    AssignedToDTO dto = new AssignedToDTO();
                    dto.setId(assigned.getId());
                    dto.setBatch(assigned.getBatch());
                    dto.setYear(assigned.getYear());
                    dto.setLocation(assigned.getLocation());
                    dto.setShiftName(assigned.getShiftName());
                    dto.setShiftTime(assigned.getShiftTime());
                    dto.setMajor(assigned.getMajor().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
