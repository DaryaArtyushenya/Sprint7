package clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import modelPojo.orderPojo.CreateOrderRequest;

public class OrderClient {
    public Response createOrder(CreateOrderRequest createOrderRequest){
        return RestAssured.given()
                .header("Content-type", "application/json")
                .body(createOrderRequest)
                .when()
                .post("/api/v1/orders");
    }
}
