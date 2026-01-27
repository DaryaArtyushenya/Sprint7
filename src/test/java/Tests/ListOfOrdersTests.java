package Tests;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import modelPojo.courierPojo.LoginSuccess;
import modelPojo.orderPojo.orderListPojo.OrderListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.CourierSteps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListOfOrdersTests{
    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }
    CourierSteps courierSteps = new CourierSteps();
    @Test
    @DisplayName("Получение списка всех заказов")
    void getOrderListTest(){
        OrderListResponse orderListResponse = given()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .extract()
                .as(OrderListResponse.class);
        assertThat(orderListResponse.getOrders())
                .isNotNull()
                .isNotEmpty();
        assertThat(orderListResponse.getOrders())
                .allSatisfy(order -> assertThat(order.getId()).isNotNull());

    }
    @Test
    @DisplayName("Получение заказов для заданного курьера")
    void getOrdersByCourierIdTest(){
        courierSteps.createCourierStep("dartyushenya1", "1234", "darya");
        Response response = courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response.as(LoginSuccess.class);
        Integer courierId = loginSuccess.getId();

        OrderListResponse orderListResponse = given()
                .queryParam("courierId", courierId)
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .extract().as(OrderListResponse.class);
        assertThat(orderListResponse.getOrders())
                .isNotNull()
                .isEmpty();
        courierSteps.deleteCourierStep(courierId);
    }

    @Test
    @DisplayName("Получение ордера по ид курьера и ближайшим станциям метро  ")
    void getOrderByIdAndStationTest(){
        courierSteps.createCourierStep("dartyushenya1", "1234", "darya");
        Response response = courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response.as(LoginSuccess.class);
        Integer courierId = loginSuccess.getId();
        OrderListResponse orderListResponse = given().log().all()
                .queryParam("courierId",courierId )
                .queryParam("nearestStation", "[\"1\", \"2\"]")
                .get("/api/v1/orders")
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(OrderListResponse.class);
        assertThat(orderListResponse.getOrders())
                .isNotNull() //проверка на то, что список существует
                .isEmpty(); // проверка, что список пустой
        courierSteps.deleteCourierStep(courierId);
    }
    @Test
    @DisplayName("Получение 10 заказов доступных для курьера")
    void get10OrdersAvailableForCourierTest(){
        courierSteps.createCourierStep("dartyushenya1", "1234", "darya");
        Response response = courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response.as(LoginSuccess.class);
        Integer courierId = loginSuccess.getId();
        OrderListResponse orderListResponse = given().log().all()
                .queryParam("limit",10 )
                .queryParam("page", 0)
                .get("api/v1/orders")
                .then()
                .statusCode(200)
                .extract()
                .as(OrderListResponse.class);
        assertEquals(10,orderListResponse.getOrders().size() );
        assertThat(orderListResponse.getOrders()).allSatisfy(order -> order.getId()).isNotNull();
        courierSteps.deleteCourierStep(courierId);
    }

}