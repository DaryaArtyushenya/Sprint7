package modelPojo.orderPojo.orderListPojo;

import java.util.List;

public class OrderListResponse {
    private List<Order> orders;
    private PageInfo pageInfo;
    private List<Station> availableStations;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<Station> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<Station> availableStations) {
        this.availableStations = availableStations;
    }
}