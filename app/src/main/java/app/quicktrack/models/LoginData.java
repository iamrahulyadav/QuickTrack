package app.quicktrack.models;

/**
 * Created by rakhi on 11/24/2017.
 */

public class LoginData {

    /**
     * status : true
     * msg : Successfully LoggedIn
     * user_detail : {"user_id":"147","user_title":"","user_name":"","user_lname":null,"user_username":"","user_email":"brijesh@gmail.com","user_countrycode":"0","user_phone":"","user_age":"0","user_password":"693cfed9dd8adf7c63afbf53cf3a8043","user_zipcode":"","user_address":"","user_city":null,"user_country":null,"user_empcode":"","user_image":"","user_attachment":"","user_company_id":"0","user_branch_id":"0","user_type":"2","user_department_id":"0","user_language_id":"","user_status":"1"}
     */

    private boolean status;
    private String msg;
    private UserDetailBean user_detail;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserDetailBean getUser_detail() {
        return user_detail;
    }

    public void setUser_detail(UserDetailBean user_detail) {
        this.user_detail = user_detail;
    }

    public static class UserDetailBean {
        /**
         * user_id : 147
         * user_title :
         * user_name :
         * user_lname : null
         * user_username :
         * user_email : brijesh@gmail.com
         * user_countrycode : 0
         * user_phone :
         * user_age : 0
         * user_password : 693cfed9dd8adf7c63afbf53cf3a8043
         * user_zipcode :
         * user_address :
         * user_city : null
         * user_country : null
         * user_empcode :
         * user_image :
         * user_attachment :
         * user_company_id : 0
         * user_branch_id : 0
         * user_type : 2
         * user_department_id : 0
         * user_language_id :
         * user_status : 1
         */

        private String user_id;
        private String user_title;
        private String user_name;
        private Object user_lname;
        private String user_username;
        private String user_email;
        private String user_countrycode;
        private String user_phone;
        private String user_age;
        private String user_password;
        private String user_zipcode;
        private String user_address;
        private Object user_city;
        private Object user_country;
        private String user_empcode;
        private String user_image;
        private String user_attachment;
        private String user_company_id;
        private String user_branch_id;
        private String user_type;
        private String user_department_id;
        private String user_language_id;
        private String user_status;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_title() {
            return user_title;
        }

        public void setUser_title(String user_title) {
            this.user_title = user_title;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public Object getUser_lname() {
            return user_lname;
        }

        public void setUser_lname(Object user_lname) {
            this.user_lname = user_lname;
        }

        public String getUser_username() {
            return user_username;
        }

        public void setUser_username(String user_username) {
            this.user_username = user_username;
        }

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }

        public String getUser_countrycode() {
            return user_countrycode;
        }

        public void setUser_countrycode(String user_countrycode) {
            this.user_countrycode = user_countrycode;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getUser_age() {
            return user_age;
        }

        public void setUser_age(String user_age) {
            this.user_age = user_age;
        }

        public String getUser_password() {
            return user_password;
        }

        public void setUser_password(String user_password) {
            this.user_password = user_password;
        }

        public String getUser_zipcode() {
            return user_zipcode;
        }

        public void setUser_zipcode(String user_zipcode) {
            this.user_zipcode = user_zipcode;
        }

        public String getUser_address() {
            return user_address;
        }

        public void setUser_address(String user_address) {
            this.user_address = user_address;
        }

        public Object getUser_city() {
            return user_city;
        }

        public void setUser_city(Object user_city) {
            this.user_city = user_city;
        }

        public Object getUser_country() {
            return user_country;
        }

        public void setUser_country(Object user_country) {
            this.user_country = user_country;
        }

        public String getUser_empcode() {
            return user_empcode;
        }

        public void setUser_empcode(String user_empcode) {
            this.user_empcode = user_empcode;
        }

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }

        public String getUser_attachment() {
            return user_attachment;
        }

        public void setUser_attachment(String user_attachment) {
            this.user_attachment = user_attachment;
        }

        public String getUser_company_id() {
            return user_company_id;
        }

        public void setUser_company_id(String user_company_id) {
            this.user_company_id = user_company_id;
        }

        public String getUser_branch_id() {
            return user_branch_id;
        }

        public void setUser_branch_id(String user_branch_id) {
            this.user_branch_id = user_branch_id;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getUser_department_id() {
            return user_department_id;
        }

        public void setUser_department_id(String user_department_id) {
            this.user_department_id = user_department_id;
        }

        public String getUser_language_id() {
            return user_language_id;
        }

        public void setUser_language_id(String user_language_id) {
            this.user_language_id = user_language_id;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }
    }
}
