package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.User;

public class UserSummaryDTO {
    private String role;
    private String userId;
    
    public UserSummaryDTO(User user) {
        this.role = user.getRole().toString();
        this.userId = user.getId().toString();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
