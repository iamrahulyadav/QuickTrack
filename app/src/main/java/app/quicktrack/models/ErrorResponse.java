package app.quicktrack.models;

/**
 * Created by rakhi on 12/19/2017.
 */

public class ErrorResponse {

    /**
     * GTSResponse : {"command":"version","result":"error","Message":{"content":"Authorization failed","code":"AU0010"}}
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
         * result : error
         * Message : {"content":"Authorization failed","code":"AU0010"}
         */

        private String command;
        private String result;
        private MessageBean Message;

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public MessageBean getMessage() {
            return Message;
        }

        public void setMessage(MessageBean Message) {
            this.Message = Message;
        }

        public static class MessageBean {
            /**
             * content : Authorization failed
             * code : AU0010
             */

            private String content;
            private String code;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }
        }
    }
}
