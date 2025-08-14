package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.AssignedToDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.TeachingAssignmentDTO;
import OneTransitionDemo.OneTransitionDemo.Models.AssignedTo;
import OneTransitionDemo.OneTransitionDemo.Models.Teacher;
import OneTransitionDemo.OneTransitionDemo.Models.TeachingAssignment;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.AssignToRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.TeacherRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.TeachingAssignmentRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeachingAssignmentService {
    @Autowired
    private TeachingAssignmentRepository teachingAssignmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AssignToRepository assignToRepository;
    @Autowired
    private UserRepository userRepository;

    public List<AssignedToDTO> getAssignmentsForTeacher(Long teacherId) {
        List<TeachingAssignment> teachingAssignments = teachingAssignmentRepository.findByTeacherId(teacherId);

        return teachingAssignments.stream()
                .map(ta -> new AssignedToDTO(ta.getAssignedTo())) // ✅ use your DTO constructor
                .collect(Collectors.toList());
    }

    public List<TeachingAssignmentDTO> getAssignments() {
        List<TeachingAssignment> teachingAssignments = teachingAssignmentRepository.findAll();

        return teachingAssignments.stream()
                .map(TeachingAssignmentDTO::new) // ✅ use your DTO constructor
                .toList();
    }

    public Map<String, Object> changeTeacherAssignment(Long teaching_ass, Long teacherId){
        Optional<TeachingAssignment> assignedTo = teachingAssignmentRepository.findById(teaching_ass);
        if(assignedTo.isEmpty()) return ResponseUtil.error("Assign to not found!");
        Optional<User> teacher = userRepository.findById(teacherId);
        if(teacher.isEmpty()) return ResponseUtil.error("Teacher not found!");

        User t = teacher.get();

        TeachingAssignment ta = assignedTo.get();
        ta.setTeacher(t);
        teachingAssignmentRepository.save(ta);
        return ResponseUtil.success("Teacher assigned successfully!");
    }
}
