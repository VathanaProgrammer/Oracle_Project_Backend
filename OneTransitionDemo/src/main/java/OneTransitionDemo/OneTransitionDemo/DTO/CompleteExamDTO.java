package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import OneTransitionDemo.OneTransitionDemo.ENUMS.Status;
import OneTransitionDemo.OneTransitionDemo.Models.CompleteExam;

public class CompleteExamDTO {
    private String profile;
    private Long userId;
    private Long examId;
    private String firstName;
    private String lastName;
    private Role role;
    private String phone;
    private Status status;
    private String email;
    public CompleteExamDTO(CompleteExam completeExam) {
        this.userId = completeExam.getUser().getId();
        this.examId = completeExam.getExam().getId();
        this.firstName = completeExam.getUser().getFirstname();
        this.lastName = completeExam.getUser().getLastname();
        this.phone = completeExam.getUser().getPhone();
        this.status = completeExam.getExam().getStatus();
        this.profile = completeExam.getUser().getProfilePicture();
        this.role = completeExam.getUser().getRole();// optional
        this.email = completeExam.getUser().getEmail();
    }
    public String getProfile() { return profile; }
    public Long getUserId() { return userId; }
    public Long getExamId() { return examId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Role getRole() { return role; }
    public String getPhone() { return phone; }
    public Status getStatus() { return status; }
    public String getEmail(){return email;}
}
