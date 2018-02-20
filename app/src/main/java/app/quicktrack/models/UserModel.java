package app.quicktrack.models;

import java.io.Serializable;

/**
 * Created by ask on 12/30/2017.
 */

public class UserModel implements Serializable{

    /**
     * username : sysadmin
     * password : dupa8
     * type : 1
     */

    private String username;
    private String password;
    private int type=1;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
