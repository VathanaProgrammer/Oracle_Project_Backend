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
    @Scheduled(fixedRate = 60000) // every 1 minute
    public void updateExamStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Exam> exams = examRepository.findAll();

        for (Exam exam : exams) {
            LocalDateTime start = exam.getStartTime();
            LocalDateTime end = exam.getEndTime();

            // COMING -> PUBLISHED
            if (now.isAfter(start) && now.isBefore(end) && exam.getStatus() == Status.COMING) {
                exam.setStatus(Status.PUBLISHED);
            }

            // PUBLISHED -> EXPIRED
            if (now.isAfter(end) && exam.getStatus() != Status.EXPIRED) {
                exam.setStatus(Status.EXPIRED);
            }

            examRepository.save(exam);
        }
    }
}
