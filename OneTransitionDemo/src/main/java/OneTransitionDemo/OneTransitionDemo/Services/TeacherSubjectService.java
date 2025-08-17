package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.TeacherSubjectDTO;
import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Repositories.SubjectRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.TeacherSubjectRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.UserRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TeacherSubjectService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherSubjectRepository teacherSubjectRepository;
    public Map<String, Object> getSubjectTeacher(Long id) {
        Optional<User> opUser = userRepository.findById(id);
        if (!opUser.isPresent()) {
            return ResponseUtil.error("User not found");
        }
        User user = opUser.get();
        List<TeacherSubjectDTO> teacherSubjectDTOList = teacherSubjectRepository.findAll()
                .stream()
                .filter(t -> Objects.equals(t.getId(), user.getId()))
                .map(TeacherSubjectDTO::new) // ✅ map entity to DTO
                .collect(Collectors.toList());
        return ResponseUtil.success("Subjects found", teacherSubjectDTOList);
    }
}
