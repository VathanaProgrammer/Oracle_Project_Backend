package OneTransitionDemo.OneTransitionDemo.DTO;

import OneTransitionDemo.OneTransitionDemo.Models.UserSessionLog;

import java.time.LocalDateTime;

public class UserSessionLogDTO {
    private Long id;

    private String browser;
    private String deviceType;
    private String device;
    private String location;
    private LocalDateTime createdAt;
    private String action;

    public UserSessionLogDTO(){}
    public UserSessionLogDTO(UserSessionLog userSessionLog) {
        this.id = userSessionLog.getId();
        this.browser = userSessionLog.getBrowser();
        this.deviceType = userSessionLog.getDeviceType();
        this.device = userSessionLog.getDevice();
        this.location = userSessionLog.getLocation();
        this.createdAt = userSessionLog.getCreatedAt();
        this.action = userSessionLog.getAction() != null ? userSessionLog.getAction() :" ";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
