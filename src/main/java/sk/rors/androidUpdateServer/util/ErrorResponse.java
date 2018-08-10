package sk.rors.androidUpdateServer.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorResponse extends Response {

    public ErrorResponse(String msg, int code) {
        super(msg, code);
    }

    @Override
    public void serialize(HttpServletResponse resp) throws IOException {
        super.serialize(resp);
        ErrorHandler.handle(this.msg);
    }
}
