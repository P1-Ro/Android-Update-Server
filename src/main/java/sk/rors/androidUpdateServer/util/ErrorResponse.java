package sk.rors.androidUpdateServer.util;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ErrorResponse extends Response {

    public ErrorResponse(String msg, int code) {
        super(msg, code);
    }

    @Override
    public String serialize() throws JsonProcessingException {

        ErrorHandler.handle(this.msg);
        return super.serialize();
    }
}
