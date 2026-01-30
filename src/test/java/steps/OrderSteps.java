package steps;

import clients.OrderClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.modelData.Courier;
import model.modelPojo.orderPojo.CreateOrderRequest;
import model.modelPojo.orderPojo.orderListPojo.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps extends OrderClient {
    @Step("Создание заказа номер: {}")
    public Response createOrderStep(String firstName, String lastName, String address, String metroStation, String phone, Integer rentTime, String deliveryDate, String comment, String[] color){
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(firstName, lastName,address, metroStation, phone, rentTime, deliveryDate, comment, color);
        return createOrder(createOrderRequest);
    }
    @Step("Получение списка заказов")
    public Response orderListStep(){
        return orderList();
    }
    @Step("Получение списка заказов для конкретного курьера")
    public Response orderListByCourierIdStep(Courier courier){
        return orderListByCourierId(courier);
    }
    @Step("Получение курьера по ид курьера и ближайшей станции")
    public Response orderListByCourierIdAndNearestStationStep(Courier courier){
        return orderListByCourierIdAndNearestStation(courier);
    }
    @Step("Получение 10 заказов, доступных для курьера")
    public Response orderListOf10OrdersAvailableForCourierStep(){
        return orderListOf10OrdersAvailableForCourier();
    }
    @Step("")
    public Response acceptOrderStep(Courier courier, Integer orderId){
        return acceptOrder(courier, orderId);
    }

}