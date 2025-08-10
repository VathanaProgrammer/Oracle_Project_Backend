package OneTransitionDemo.OneTransitionDemo.Services;

import OneTransitionDemo.OneTransitionDemo.DTO.SubjectDTO;
import OneTransitionDemo.OneTransitionDemo.Models.Major;
import OneTransitionDemo.OneTransitionDemo.Models.Subject;
import OneTransitionDemo.OneTransitionDemo.Repositories.MajorRepository;
import OneTransitionDemo.OneTransitionDemo.Repositories.SubjectRepository;
import OneTransitionDemo.OneTransitionDemo.Request.SubjectRequest;
import OneTransitionDemo.OneTransitionDemo.Response.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private MajorRepository majorRepository;

    public Map<String, Object> getAllSubjects(){
        return ResponseUtil.success("Subject fetch!", subjectRepository.findByIsDeletedFalse()
                .stream()
                .map(SubjectDTO::new)
                .toList());
    }

    public Map<String, Object> getSubjectById(Long id){
        return null;
    }

    public Map<String, Object> createSubjectWithAcceptedMajorOrNot(SubjectRequest request) {
        Subject subject = null;
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            subject = new Subject();
            subject.setName(request.getName());

            if (request.getMajorId() != null) {
                Optional<Major> majorOpt = majorRepository.findById(request.getMajorId());
                if (majorOpt.isPresent()) {
                    subject.setMajor(majorOpt.get());
                } else {
                    return ResponseUtil.error("Major not found!");
                }
            }
        }


        assert subject != null;
        subjectRepository.save(subject);
        return ResponseUtil.success("Subject created!", subject);
    }

    public Map<String, Object> updateSubjectWithAcceptedMajorOrNot(SubjectRequest request) {
        if(request.getName() == null || request.getName().trim().isEmpty()) {
            return ResponseUtil.error("Subject name is empty!");
        }

        Optional<Subject> subjectOptional = subjectRepository.findById(request.getId());
        if(subjectOptional.isEmpty()) {
            return ResponseUtil.error("Subject not found!");
        }

        Major major = null;

        Optional<Major> majorOpt = majorRepository.findById(request.getMajorId());
        if (majorOpt.isPresent()) {
            major = majorOpt.get();
        }

        Subject subject = subjectOptional.get();

        subject.setName(request.getName());
        subject.setMajor(major);
        subjectRepository.save(subject);
        return ResponseUtil.success("Subject updated!", subject);
    }

    public Map<String, Object> deleteWithStatus(Long id){
        Optional<Subject> subjectOptional = subjectRepository.findById(id);

        if(subjectOptional.isEmpty()) {
            return ResponseUtil.error("Subject not found!");
        }

        Subject subject = subjectOptional.get();
        subject.setDeleted(true);

        subjectRepository.save(subject);
        return ResponseUtil.success("Subject deleted!", subject);
    }
}
