package generalPojoSerailAndDeserial;

import java.util.List;

public class Profile {
    private String email;
    private List<Roles> roles;


    public  List<Roles> getRoles() {
        return roles;
    }

    public void setRoles( List<Roles> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
