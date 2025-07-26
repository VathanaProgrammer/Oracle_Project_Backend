package OneTransitionDemo.OneTransitionDemo.Request;

public class loginRequest {
    private String email;
    private String password;
    private Boolean rememberMe;

    private String browser;
    private String device;
    private String deviceType;
    private String location;

    public String getBrowser() {
        return this.browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getDevice() {
        return this.device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public Boolean getRememberMe(){
        return this.rememberMe;
    }

    public void setRememberMe(Boolean rememberMe){
        this.rememberMe = rememberMe;
    }
}