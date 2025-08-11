package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.DepartmentDTO;
import OneTransitionDemo.OneTransitionDemo.DTO.DepartmentSummaryDTO;
import OneTransitionDemo.OneTransitionDemo.Models.Department;
import OneTransitionDemo.OneTransitionDemo.Repositories.DepartmentRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.MajorRepository;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MajorRepository majorRepository;

    public Map<String, Object> getAllDepartments(){
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDTO> dtoList = departments.stream()
                .map(DepartmentDTO::new)
                .toList();
        return ResponseUtil.success("Departments found!", dtoList);
    }
    public List<DepartmentSummaryDTO> getDepartmentsWithTeachersAndMajors() {
        List<Department> departments = departmentRepository.findByIsDeletedFalse();

        return departments.stream().map(dept -> {
            DepartmentSummaryDTO dto = new DepartmentSummaryDTO();
            dto.setId(dept.getId());
            dto.setName(dept.getName());
            dto.setTeacherCount((long) dept.getTeachers().size());
            dto.setMajorCount(majorRepository.countByDepartmentId(dept.getId()));
            return dto;
        }).toList();
    }

}
