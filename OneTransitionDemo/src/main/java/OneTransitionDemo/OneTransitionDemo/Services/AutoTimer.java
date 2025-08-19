package OneTransitionDemo.OneTransitionDemo.Services;
import OneTransitionDemo.OneTransitionDemo.ENUMS.Status;
import OneTransitionDemo.OneTransitionDemo.Models.Exam;
import OneTransitionDemo.OneTransitionDemo.Repositories.ExamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class AutoTimer {

    private final ExamRepository examRepository;

    public AutoTimer(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 5000) // every 1 minute
    public void updateExamStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Exam> exams = examRepository.findAll();

        for (Exam exam : exams) {
            boolean changed = false;
            if (now.isAfter(exam.getStartTime()) && now.isBefore(exam.getEndTime()) && exam.getStatus() == Status.COMING) {
                exam.setStatus(Status.PUBLISHED);
                changed = true;
            }
            if (now.isAfter(exam.getEndTime()) && exam.getStatus() == Status.PUBLISHED) {
                exam.setStatus(Status.COMPLETED);
                changed = true;
            }
            if (changed) {
                examRepository.save(exam);
            }
        }
    }
}

