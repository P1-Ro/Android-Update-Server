package sk.rors.androidUpdateServer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sentry.Sentry;

public class ErrorResponse {

    private boolean sentryInitialized = false;

    private String msg;
    private int code;

    public ErrorResponse(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String serialize() throws JsonProcessingException {
        String sentryDsn = System.getenv().get("SENTRY_DSN");
        if(sentryDsn != null && !sentryInitialized){
            Sentry.init(sentryDsn);
            sentryInitialized = true;
        }

        if(sentryInitialized){
            Sentry.capture(msg);
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
