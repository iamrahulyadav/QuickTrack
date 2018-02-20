package app.quicktrack.models;

/**
 * Created by rakhi on 12/19/2017.
 */

public class LoginResposnse {

    /**
     * GTSResponse : {"command":"version","Version":"2.6.3-B37","result":"success"}
     */

    private GTSResponseBean GTSResponse;

    public GTSResponseBean getGTSResponse() {
        return GTSResponse;
    }

    public void setGTSResponse(GTSResponseBean GTSResponse) {
        this.GTSResponse = GTSResponse;
    }

    public static class GTSResponseBean {
        /**
         * command : version
         * Version : 2.6.3-B37
         * result : success
         */

        private String command;
        private String Version;
        private String result;

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public String getVersion() {
            return Version;
        }

        public void setVersion(String Version) {
            this.Version = Version;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
