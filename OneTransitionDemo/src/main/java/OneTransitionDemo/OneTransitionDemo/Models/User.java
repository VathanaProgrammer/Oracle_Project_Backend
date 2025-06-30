package OneTransitionDemo.OneTransitionDemo.Models;


import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "TBL_USERS")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // STUDENT, TEACHER, ADMIN

    private String firstname;
    private String lastname;
    private String phone;
    private String profilePicture;

    @OneToMany(mappedBy = "teacher")
    private List<Exam> examsCreated;

    @OneToMany(mappedBy = "student")
    private List<ExamResult> examResults;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public void setEmail(String email){
        this.email = email;
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setProfilePicture(String profilePicture){
        this.profilePicture = profilePicture;
    }
}


