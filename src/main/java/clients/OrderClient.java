package clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.modelData.Courier;
import model.modelPojo.orderPojo.CreateOrderRequest;
import model.modelPojo.orderPojo.orderListPojo.Order;
import model.modelPojo.orderPojo.orderListPojo.OrderListResponse;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class OrderClient {
    public Response createOrder(CreateOrderRequest createOrderRequest){
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(createOrderRequest)
                .when()
                .post("/api/v1/orders");
    }

    public Response orderList(){
        return given().get("/api/v1/orders");
    }
    public Response orderListByCourierId(Courier courier){
        return given()
                .queryParam("courierId", courier.getCourierId())
                .get("/api/v1/orders");
    }

    public Response orderListByCourierIdAndNearestStation(Courier courier){
        return given().log().all()
                .queryParam("courierId", courier.getCourierId() )
                .queryParam("nearestStation", "[\"1\", \"2\"]")
                .get("/api/v1/orders");
    }
    public  Response orderListOf10OrdersAvailableForCourier(){
        return  given().log().all()
                .queryParam("limit",10 )
                .queryParam("page", 0)
                .get("api/v1/orders");
    }
    public void  setIdToModelOrder(List<Order> orderList){
        // список заказов из API
        List<Order> ordersFromApi = orderList().as(OrderListResponse.class).getOrders();
        Random random = new Random();
        int randomIndex = random.nextInt(ordersFromApi.size());
        Integer randomOrderId = ordersFromApi.get(randomIndex).getId();
        // 3. Сетаем этот id в каждую модель Order
        for (Order order : orderList) {
            order.setId(randomOrderId);
        }
    }
    public  Response acceptOrder(Courier courier, Integer orderId){
        return given()
                .queryParam("courierId", courier.getCourierId() )
                .put("api/v1/orders/accept/" + orderId);
    }

    public  Response notAcceptOrderWithoutOrderId(Courier courier){
       return given()
                .queryParam("courierId", courier.getCourierId() )
                .put("api/v1/orders/accept/");
    }
    public  Response notAcceptOrderWithNonExistingOrderId(Courier courier){
        return given()
                .queryParam("courierId", courier.getCourierId() )
                .put("api/v1/orders/accept/"+ 348);
    }

    public Response notAcceptOrderWithNinExistingCourierId(Integer orderId){
        return  given()
                .queryParam("courierId", 123 )
                .put("api/v1/orders/accept/"+ orderId);
    }



}
