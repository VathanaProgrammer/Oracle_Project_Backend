package OneTransitionDemo.OneTransitionDemo.Request;

public class loginRequest {
    private String email;
    private String password;
    private Boolean rememberMe;

    public String getEmail(){
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