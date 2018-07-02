package app.quicktrack.models;

import java.util.List;

/**
 * Created by rakhi on 12/14/2017.
 */

public class DeviceData {


    /**
     * message : success
     * response : [{"deviceid":"as01dd0092","type":"Truck","latitude":"25.95671777777778","longitude":"91.85204444444445"},{"deviceid":"as01jc3192","type":"Truck","latitude":"25.58773777777778","longitude":"92.0614311111111"},{"deviceid":"as01jc3592","type":"Truck","latitude":"25.013124444444443","longitude":"92.50256"}]
     * status : true
     */

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
         * deviceid : as01dd0092
         * type : Truck
         * latitude : 25.95671777777778
         * longitude : 91.85204444444445
         */

        private String deviceid;
        private String type;
        private String latitude;
        private String longitude;

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
    }
}
