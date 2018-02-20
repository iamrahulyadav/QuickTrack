package app.quicktrack.models;

import java.io.Serializable;

/**
 * Created by rakhi on 11/27/2017.
 */

public class ProfileData implements Serializable{


    /**
     * message : success
     * response : {"accountID":"sysadmin","userID":"pravin","contactPhone":"","contactEmail":"","user_profile_pic":""}
     * status : true
     */

    private String message;
    private ResponseBean response;
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class ResponseBean {
        /**
         * accountID : sysadmin
         * userID : pravin
         * contactPhone :
         * contactEmail :
         * user_profile_pic :
         */

        private String accountID;
        private String userID;
        private String contactPhone;
        private String contactEmail;
        private String user_profile_pic;

        public String getAccountID() {
            return accountID;
        }

        public void setAccountID(String accountID) {
            this.accountID = accountID;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getContactEmail() {
            return contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getUser_profile_pic() {
            return user_profile_pic;
        }

        public void setUser_profile_pic(String user_profile_pic) {
            this.user_profile_pic = user_profile_pic;
        }
    }
}
