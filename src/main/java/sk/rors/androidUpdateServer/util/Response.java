package sk.rors.androidUpdateServer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Response {

    private String msg;
    private int code;

    public Response(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    String getMsg() {
        return msg;
    }

    public String serialize() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
