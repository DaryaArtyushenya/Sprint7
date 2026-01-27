package modelPojo.orderPojo;

public class NotAcceptOrderResponse {
    private String message;

    public NotAcceptOrderResponse() {
    }

    public NotAcceptOrderResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}