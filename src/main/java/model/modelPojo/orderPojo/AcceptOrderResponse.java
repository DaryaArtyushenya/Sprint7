package model.modelPojo.orderPojo;

public class AcceptOrderResponse {
    private Boolean ok;

    public AcceptOrderResponse(Boolean ok) {
        this.ok = ok;
    }

    public AcceptOrderResponse() {
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }
}