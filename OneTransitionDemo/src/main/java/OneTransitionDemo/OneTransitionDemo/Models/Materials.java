package OneTransitionDemo.OneTransitionDemo.Models;

import OneTransitionDemo.OneTransitionDemo.ENUMS.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Materials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileUrl;

    private LocalDateTime createdAt;

    private String username;

    private Role role;

    private String title;

    private String userProfile;

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
            return title;
    }

    public String getUsername() {
        return username;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }
}
