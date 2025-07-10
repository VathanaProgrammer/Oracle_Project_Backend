package OneTransitionDemo.OneTransitionDemo.Models;


import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "TBL_USERS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements UserDetails {
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

    @OneToMany(mappedBy = "user")
    private List<UserSessionLog> sessionLogs;

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

    public String getEmail(){ return this.email; }
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

    public String getFirstname(){ return this.firstname; }
    public String getLastname(){ return this.lastname; }

    public String getProfilePicture() {
        return profilePicture;
    }

    public List<UserSessionLog> getSessionLogs() {
        return sessionLogs;
    }

    public void setSessionLogs(List<UserSessionLog> userSessionLogs){
        this.sessionLogs = userSessionLogs;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // If you have roles, return them here. For now, return empty list.
        return Collections.emptyList();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}


