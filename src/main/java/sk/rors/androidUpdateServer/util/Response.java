package sk.rors.androidUpdateServer.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Response {

    public String msg;
    public int code;

    public Response(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public void serialize(HttpServletResponse resp) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String result = mapper.writeValueAsString(this);
        resp.getWriter().append(result);
        resp.setStatus(code);
    }
}
