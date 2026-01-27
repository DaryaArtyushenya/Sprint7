package modelPojo.orderPojo;

public class AcceptOrderRequest {
    private Integer id;
    private Integer courierId;

    public AcceptOrderRequest(Integer id, Integer courierId) {
        this.id = id;
        this.courierId = courierId;
    }

    public AcceptOrderRequest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }
}