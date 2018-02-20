package app.quicktrack.models;

/**
 * Created by Rakhi on 1/5/2018.
 * Mobile number 9958187463
 */

public class HistoryRequest {


    /**
     * deviceid : gt02a
     * date : 06-01-2018
     * limit : 500
     */

    private String deviceid;
    private String date;
    private String limit;

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
