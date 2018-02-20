package app.quicktrack.models;

import java.util.List;

/**
 * Created by rakhi on 12/14/2017.
 */

public class DeviceData {


    /**
     * message : success
     * response : [{"deviceid":"gt02a"},{"deviceid":"tk103"}]
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
         * deviceid : gt02a
         */

        private String deviceid;

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }
    }
}
