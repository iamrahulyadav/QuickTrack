package app.quicktrack.models;

/**
 * Created by rakhi on 12/30/2017.
 */

public class LoginResponse {


    /**
     * message : success
     * response : {"resellerid":"sysadmin","userid":"pravin","type":1}
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
         * resellerid : sysadmin
         * userid : pravin
         * type : 1
         */

        private String resellerid;
        private String userid;
        private int type;

        public String getResellerid() {
            return resellerid;
        }

        public void setResellerid(String resellerid) {
            this.resellerid = resellerid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
