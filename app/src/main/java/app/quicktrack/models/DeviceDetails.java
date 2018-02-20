package app.quicktrack.models;

import java.util.List;

/**
 * Created by rakhi on 12/19/2017.
 */

public class DeviceDetails {


    private String message;
    private boolean status;
    private List<ResponseBean> response;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ResponseBean> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseBean> response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * resellerid : sysadmin
         * deviceid : gt02a
         * datetime : 1515139200
         * latitude : 28.606529333333334
         * longitude : 77.3291435
         * speed : 0
         * address : George Marg, Block F, Kondli, New Delhi, Uttar Pradesh 110096, India
         * type : Car
         */

        private String resellerid;
        private String deviceid;
        private String datetime;
        private String latitude;
        private String longitude;
        private String speed;
        private String address;
        private String type;

        public String getResellerid() {
            return resellerid;
        }

        public void setResellerid(String resellerid) {
            this.resellerid = resellerid;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
