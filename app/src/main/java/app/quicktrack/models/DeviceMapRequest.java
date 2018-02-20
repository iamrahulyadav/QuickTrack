package app.quicktrack.models;

/**
 * Created by rakhi on 12/30/2017.
 */

public class DeviceMapRequest {

    /**
     * deviceid : gt02a
     * limit : 200
     */

    private String deviceid;
    private int limit;

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
