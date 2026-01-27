package modelPojo.courierPojo;

public class DeleteRequest {
    private Integer id;

    public DeleteRequest(Integer id) {
        this.id = id;
    }

    public DeleteRequest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}