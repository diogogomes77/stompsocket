package dev.dgomes.backend.api;

public class PostResponse {

    String message;
    String extra;
    public String getExtra() {
        return extra;
    }
    public String getMessage() {
        return message;
    }
    public void setExtra(String extra) {
        this.extra = extra;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
