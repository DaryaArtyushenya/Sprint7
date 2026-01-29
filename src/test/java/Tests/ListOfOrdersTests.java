package Tests;


import dataFactory.CourierFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.modelData.Courier;
import model.modelPojo.courierPojo.LoginSuccess;
import model.modelPojo.orderPojo.orderListPojo.OrderListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.CourierSteps;
import steps.OrderSteps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListOfOrdersTests{
    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }
    CourierSteps courierSteps = new CourierSteps();
    OrderSteps orderSteps = new OrderSteps();

    @Test
    @DisplayName("Получение списка всех заказов")
    void getOrderListTest(){
        OrderListResponse orderListResponse = orderSteps.orderListStep()
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
        Courier courier = CourierFactory.validCourier();
        courierSteps.createCourierStep(courier);
        courierSteps.loginCourierStep(courier);
        OrderListResponse orderListResponse = orderSteps.orderListByCourierId(courier)
                .then()
                .statusCode(200)
                .extract().as(OrderListResponse.class);
        assertThat(orderListResponse.getOrders())
                .isNotNull()
                .isEmpty();
        courierSteps.deleteCourierStep(courier);
    }

    @Test
    @DisplayName("Получение ордера по ид курьера и ближайшим станциям метро  ")
    void getOrderByIdAndStationTest(){
        Courier courier =CourierFactory.validCourier();
        courierSteps.createCourierStep(courier);
        courierSteps.loginCourierStep(courier);
        OrderListResponse orderListResponse = orderSteps.orderListByCourierIdAndNearestStationStep(courier)
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(OrderListResponse.class);
        assertThat(orderListResponse.getOrders())
                .isNotNull() //проверка на то, что список существует
                .isEmpty(); // проверка, что список пустой
        courierSteps.deleteCourierStep(courier);
    }
    @Test
    @DisplayName("Получение 10 заказов доступных для курьера")
    void get10OrdersAvailableForCourierTest(){
        Courier courier = CourierFactory.validCourier();
        courierSteps.createCourierStep(courier);
        courierSteps.loginCourierStep(courier);
        OrderListResponse orderListResponse = orderSteps.orderListOf10OrdersAvailableForCourierStep()
                .then()
                .statusCode(200)
                .extract()
                .as(OrderListResponse.class);
        assertEquals(10,orderListResponse.getOrders().size() );
        assertThat(orderListResponse.getOrders()).allSatisfy(order -> order.getId()).isNotNull();
        courierSteps.deleteCourierStep(courier);
    }

}