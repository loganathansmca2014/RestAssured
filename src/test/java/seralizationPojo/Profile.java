package seralizationPojo;

import java.util.List;

public class Profile {
    private String email;
    private List<String> roles;

    public  List<String> getRoles() {
        return roles;
    }

    public void setRoles( List<String> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
