package app.quicktrack.models;

/**
 * Created by rakhi on 12/30/2017.
 */

public class DeviceListRequest {


    /**
     * resellerid : sysadmin
     * userid : test123
     * type : 2
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
