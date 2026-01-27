package modelPojo.orderPojo;

public class CreateOrderSuccess {
    private Integer track;

    public CreateOrderSuccess(Integer track) {
        this.track = track;
    }

    public CreateOrderSuccess() {
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }
}