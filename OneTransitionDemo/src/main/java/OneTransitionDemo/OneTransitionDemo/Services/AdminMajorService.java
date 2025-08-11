package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.Models.Department;
import OneTransitionDemo.OneTransitionDemo.Models.Major;
import OneTransitionDemo.OneTransitionDemo.Repositories.DepartmentRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.MajorRepository;
import OneTransitionDemo.OneTransitionDemo.Request.AdminMajorRequest;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AdminMajorService {

    @Autowired
    private MajorService majorService;

    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public Map<String, Object> createMajor(AdminMajorRequest request) {
        if (request == null) {
            return ResponseUtil.error("Major request cannot be empty!");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return ResponseUtil.error("Major name cannot be empty!");
        }

        Major major = new Major(); // ✅ Create a new instance
        major.setName(request.getName());

        Optional<Department> departmentOpt = departmentRepository.findById(request.getDepartmentId());
        if (departmentOpt.isPresent()) {
            major.setDepartment(departmentOpt.get());
        } else {
            return ResponseUtil.error("Department not found!");
        }

        majorRepository.save(major);
        return ResponseUtil.success("Major created!", major);
    }

    @Transactional
    public Map<String, Object> updateMajor(AdminMajorRequest request) {
        if (request == null) {
            return ResponseUtil.error("Major request cannot be empty!");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return ResponseUtil.error("Major name cannot be empty!");
        }
        if (request.getId() == null) {
            return ResponseUtil.error("Major ID cannot be empty!");
        }

        Optional<Major> majorOpt = majorRepository.findById(request.getId());
        if (majorOpt.isEmpty()) {
            return ResponseUtil.error("Major not found!");
        }

        Major major = majorOpt.get();
        major.setName(request.getName());

        // ✅ Update department if provided
        if (request.getDepartmentId() != null) {
            Optional<Department> deptOpt = departmentRepository.findById(request.getDepartmentId());
            if (deptOpt.isEmpty()) {
                return ResponseUtil.error("Department not found!");
            }
            major.setDepartment(deptOpt.get());
        } else {
            major.setDepartment(null); // Or keep old department, depending on your logic
        }

        majorRepository.save(major);
        return ResponseUtil.success("Major updated!", major);
    }

    @Transactional
    public Map<String, Object> deleteMajor(Long majorId) {
        Optional<Major> majorOpt = majorRepository.findById(majorId);
        if (majorOpt.isEmpty()) {
            return ResponseUtil.error("Major not found!");
        }

        Major major = majorOpt.get();

        // Detach from department first (optional, but clean)
        if (major.getDepartment() != null) {
            major.getDepartment().getMajors().remove(major); // remove major from department set
            major.setDepartment(null); // unset department in major
        }

        majorRepository.delete(major);

        return ResponseUtil.success("Major deleted successfully!");
    }


}
