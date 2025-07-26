package OneTransitionDemo.OneTransitionDemo.Request;

public class ChangePasswordForAdminRequest {
    private Long userId;
    private Long adminId;
    private String newPassword;
    private String adminUsername;

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
