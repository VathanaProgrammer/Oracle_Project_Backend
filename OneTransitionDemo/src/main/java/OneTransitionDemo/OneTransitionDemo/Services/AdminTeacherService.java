package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.TeacherDTO;
import OneTransitionDemo.OneTransitionDemo.Repositories.TeacherRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminTeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public Map<String, Object> getAllTeachers(){
        List<TeacherDTO> teacherDTOS = teacherRepository.findAll()
                .stream()
                .filter(t -> t.getUser() != null && !t.getUser().isDeleted())
                .map(TeacherDTO::new)
                .toList();
        return ResponseUtil.success("Teacher found!", teacherDTOS);
    }
}
