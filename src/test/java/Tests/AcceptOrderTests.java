package Tests;

import clients.OrderClient;
import dataFactory.CourierFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.modelData.Courier;
import model.modelPojo.orderPojo.AcceptOrderResponse;
import model.modelPojo.orderPojo.NotAcceptOrderResponse;
import model.modelPojo.orderPojo.orderListPojo.Order;
import model.modelPojo.orderPojo.orderListPojo.OrderListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.CourierSteps;
import steps.OrderSteps;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcceptOrderTests{
    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    CourierSteps courierSteps = new CourierSteps();
    OrderSteps orderSteps = new OrderSteps();
    OrderClient orderClient = new OrderClient();
    @Test
    @DisplayName("Заказ успешно принят")
    void acceptOrderTest(){
        Courier courier = CourierFactory.validCourier();
        courierSteps.createCourierStep(courier);
        courierSteps.loginCourierStep(courier);
        Response response = orderSteps.orderListStep();
        List<Order> ordersFromApi = response.as(OrderListResponse.class).getOrders();
        Random random = new Random();
        //для теста каждый раз будет браться рандомный ордер ид из списка существующих ордеров
        int randomIndex = random.nextInt(ordersFromApi.size());
        Integer randomOrderId = ordersFromApi.get(randomIndex).getId();
        AcceptOrderResponse acceptOrderResponse = orderSteps.acceptOrderStep(courier, randomOrderId)
                        .then().log().all()
                        .statusCode(200)
                        .extract()
                        .as(AcceptOrderResponse.class);
        assertTrue(acceptOrderResponse.getOk());
        courierSteps.deleteCourierStep(courier);
    }

    @Test
    @DisplayName("Заказ не принят по причине отсутствия id")
    void acceptOrderWithoutIdTest(){

        Courier courier = CourierFactory.validCourier();
        courierSteps.createCourierStep(courier);
        courierSteps.loginCourierStep(courier);
        NotAcceptOrderResponse notAcceptOrderResponse = orderClient.notAcceptOrderWithoutOrderId(courier)
                        .then().log().all()
                        .statusCode(404)
                        .extract()
                        .as(NotAcceptOrderResponse.class);
        courierSteps.deleteCourierStep(courier);
    }

    @Test
    @DisplayName("Заказ не принят по причине несуществующего id")
    void acceptOrderWithIncorrectIdTest(){
        Courier courier = CourierFactory.validCourier();
        courierSteps.createCourierStep(courier);
        courierSteps.loginCourierStep(courier);
        NotAcceptOrderResponse notAcceptOrderResponse = orderClient.notAcceptOrderWithNonExistingOrderId(courier)
                        .then().log().all()
                        .statusCode(404)
                        .extract()
                        .as(NotAcceptOrderResponse.class);
        assertEquals("Заказа с таким id не существует", notAcceptOrderResponse.getMessage());
        courierSteps.deleteCourierStep(courier);
    }
    @Test
    @DisplayName("Заказ не принят по причине несуществующего courierId")
    void acceptOrderWithIncorrectCourierIdTest(){
        Response response = orderSteps.orderListStep();
        List<Order> ordersFromApi = response.as(OrderListResponse.class).getOrders();
        Random random = new Random();
        int randomIndex = random.nextInt(ordersFromApi.size());
        //для теста каждый раз будет браться рандомный ордер ид из списка существующих ордеров
        Integer randomOrderId = ordersFromApi.get(randomIndex).getId();
        NotAcceptOrderResponse notAcceptOrderResponse = orderClient.notAcceptOrderWithNinExistingCourierId(randomOrderId)
                        .then().log().all()
                        .statusCode(404)
                        .extract()
                        .as(NotAcceptOrderResponse.class);
        assertEquals("Курьера с таким id не существует", notAcceptOrderResponse.getMessage());
    }
    @Test
    @DisplayName("Заказ не принят по причине того, что уже находится в работе")
    void orderAlreadyAcceptedTest(){
        Courier courier = CourierFactory.validCourier();
        courierSteps.createCourierStep(courier);
        courierSteps.loginCourierStep(courier);
        Response response = orderSteps.orderListStep();
        List<Order> ordersFromApi = response.as(OrderListResponse.class).getOrders();
        Random random = new Random();
        int randomIndex = random.nextInt(ordersFromApi.size());
        //для теста каждый раз будет браться рандомный ордер ид из списка существующих ордеров
        Integer randomOrderId = ordersFromApi.get(randomIndex).getId();
        orderSteps.acceptOrder(courier, randomOrderId);
        NotAcceptOrderResponse notAcceptOrderResponse = orderSteps.acceptOrder(courier, randomOrderId)
                        .then().log().all()
                        .statusCode(409)
                        .extract()
                        .as(NotAcceptOrderResponse.class);
        assertEquals("Этот заказ уже в работе", notAcceptOrderResponse.getMessage());
        courierSteps.deleteCourierStep(courier);
    }
    @Test
    @DisplayName("Заказ не принят по причине отсутствия courierId")
    void acceptOrderWithoutCourierIdTest(){
        Response response = orderSteps.orderListStep();
        List<Order> ordersFromApi = response.as(OrderListResponse.class).getOrders();
        Random random = new Random();
        int randomIndex = random.nextInt(ordersFromApi.size());
        //для теста каждый раз будет браться рандомный ордер ид из списка существующих ордеров
        Integer randomOrderId = ordersFromApi.get(randomIndex).getId();
        NotAcceptOrderResponse notAcceptOrderResponse =
                given()
                        .queryParam("courierId" )
                        .put("api/v1/orders/accept/"+ randomOrderId)
                        .then().log().all()
                        .statusCode(400)
                        .extract()
                        .as(NotAcceptOrderResponse.class);
        assertEquals("Недостаточно данных для поиска", notAcceptOrderResponse.getMessage());
    }

}
