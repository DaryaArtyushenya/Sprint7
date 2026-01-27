package Tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import modelPojo.courierPojo.LoginSuccess;
import modelPojo.orderPojo.AcceptOrderResponse;
import modelPojo.orderPojo.NotAcceptOrderResponse;
import modelPojo.orderPojo.orderListPojo.Order;
import modelPojo.orderPojo.orderListPojo.OrderListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.CourierSteps;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcceptOrderTests{
    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    CourierSteps courierSteps = new CourierSteps();

    @Test
    @DisplayName("Заказ успешно принят")
    void acceptOrderTest(){

        /*
        заметила что список ордеров не обновляется, поэтому возьму ид первого ордера
        но если бы список обновлялся, то нашла бы идОрдера так
        Response response1 = createOrder(new CreateOrderRequest("имя",
                "фамилия",
                "адрес",
                "Пушкинская",
                "777777777777",
                23,
                "2026.12.01",
                "коммент",
                new String[]{"BLACK", "GREY"}));
        CreateOrderSuccess createOrderSuccess =response1.as(CreateOrderSuccess.class);
        Integer orderTrack = createOrderSuccess.getTrack();
        Integer orderId = orderList.stream()
                .filter(order -> order.getTrack().equals(orderTrack))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found"))
                .getId();
         */
        courierSteps.createCourierStep("dartyushenya1", "1234","darya");
        Response response = courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response.as(LoginSuccess.class);
        Integer courierId = loginSuccess.getId();
        OrderListResponse orderListResponse = given()
                .get("/api/v1/orders").as(OrderListResponse.class);
        List<Order> orderList = orderListResponse.getOrders();
        Integer orderId= orderList.get(0).getId();
        AcceptOrderResponse acceptOrderResponse =
                given()
                        .queryParam("courierId", courierId )
                        .put("api/v1/orders/accept/" + orderId)
                        .then().log().all()
                        .statusCode(200)
                        .extract()
                        .as(AcceptOrderResponse.class);
        assertTrue(acceptOrderResponse.getOk());
        courierSteps.deleteCourierStep(courierId);
    }

    @Test
    @DisplayName("Заказ не принят по причине отсутствия id")
    void acceptOrderWithoutIdTest(){
        courierSteps.createCourierStep("dartyushenya1", "1234","darya");
        Response response = courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response.as(LoginSuccess.class);
        Integer courierId = loginSuccess.getId();
        NotAcceptOrderResponse notAcceptOrderResponse =
                given()
                        .queryParam("courierId", courierId )
                        .put("api/v1/orders/accept/")
                        .then().log().all()
                        .statusCode(404)
                        .extract()
                        .as(NotAcceptOrderResponse.class);
        courierSteps.deleteCourierStep(courierId);
    }
    @Test
    @DisplayName("Заказ не принят по причине несуществующего id")
    void acceptOrderWithIncorrectIdTest(){
        courierSteps.createCourierStep("dartyushenya1", "1234","darya");
        Response response = courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response.as(LoginSuccess.class);
        Integer courierId = loginSuccess.getId();
        NotAcceptOrderResponse notAcceptOrderResponse =
                given()
                        .queryParam("courierId", courierId )
                        .put("api/v1/orders/accept/"+ 348)
                        .then().log().all()
                        .statusCode(404)
                        .extract()
                        .as(NotAcceptOrderResponse.class);
        assertEquals("Заказа с таким id не существует", notAcceptOrderResponse.getMessage());
        courierSteps.deleteCourierStep(courierId);
    }
    @Test
    @DisplayName("Заказ не принят по причине несуществующего courierId")
    void acceptOrderWithIncorrectCourierIdTest(){
        OrderListResponse orderListResponse = given()
                .get("/api/v1/orders").as(OrderListResponse.class);
        List<Order> orderList = orderListResponse.getOrders();
        Integer orderId= orderList.get(0).getId();
        NotAcceptOrderResponse notAcceptOrderResponse =
                given()
                        .queryParam("courierId", 123 )
                        .put("api/v1/orders/accept/"+ orderId)
                        .then().log().all()
                        .statusCode(404)
                        .extract()
                        .as(NotAcceptOrderResponse.class);
        assertEquals("Курьера с таким id не существует", notAcceptOrderResponse.getMessage());
    }
    @Test
    @DisplayName("Заказ не принят по причине того, что уже находится в работе")
    void orderAlreadyAcceptedTest(){
        courierSteps.createCourierStep("dartyushenya1", "1234","darya");
        Response response = courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response.as(LoginSuccess.class);
        Integer courierId = loginSuccess.getId();
        OrderListResponse orderListResponse = given()
                .get("/api/v1/orders").as(OrderListResponse.class);
        List<Order> orderList = orderListResponse.getOrders();
        Integer orderId= orderList.get(0).getId();
        given().log().all()
                .queryParam("courierId", courierId )
                .put("api/v1/orders/accept/"+ orderId);

        NotAcceptOrderResponse notAcceptOrderResponse =
                given()
                        .queryParam("courierId", courierId )
                        .put("api/v1/orders/accept/"+ orderId)
                        .then().log().all()
                        .statusCode(409)
                        .extract()
                        .as(NotAcceptOrderResponse.class);
        assertEquals("Этот заказ уже в работе", notAcceptOrderResponse.getMessage());
        courierSteps.deleteCourierStep(courierId);
    }
    @Test
    @DisplayName("Заказ не принят по причине отсутствия courierId")
    void acceptOrderWithoutCourierIdTest(){
        OrderListResponse orderListResponse = given()
                .get("/api/v1/orders").as(OrderListResponse.class);
        List<Order> orderList = orderListResponse.getOrders();
        Integer orderId= orderList.get(0).getId();

        NotAcceptOrderResponse notAcceptOrderResponse =
                given()
                        .queryParam("courierId" )
                        .put("api/v1/orders/accept/"+ orderId)
                        .then().log().all()
                        .statusCode(400)
                        .extract()
                        .as(NotAcceptOrderResponse.class);
        assertEquals("Недостаточно данных для поиска", notAcceptOrderResponse.getMessage());
    }
}