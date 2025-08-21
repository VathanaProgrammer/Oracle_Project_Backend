package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.AssignedTo;

import java.time.LocalDateTime;
import java.util.List;

public class DetailedResultDTO {
    private Long studentId;
    private String studentName;
    private String profilePicture;
    private String studentAvatar; // optional
    private String examTitle;
    private AssignedTo assignedTo;
    private Long totalScore;
    private String teacherComment;
    private LocalDateTime submittedAt;
    private List<QuestionResultDTO> questions;

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }

    public Long getTotalScore() {
        return totalScore;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public Long getStudentId() {
        return studentId;
    }

    public AssignedTo getAssignmentInfo() {
        return assignedTo;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public String getStudentAvatar() {
        return studentAvatar;
    }

    public List<QuestionResultDTO> getQuestions() {
        return questions;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getTeacherComment() {
        return teacherComment;
    }

    public void setAssignmentInfo(AssignedTo assignmentInfo) {
        this.assignedTo = assignmentInfo;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public void setQuestions(List<QuestionResultDTO> questions) {
        this.questions = questions;
    }

    public void setStudentAvatar(String studentAvatar) {
        this.studentAvatar = studentAvatar;
    }

    public void setTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
    }


}
