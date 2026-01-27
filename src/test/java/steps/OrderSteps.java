package steps;

import clients.OrderClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import modelPojo.orderPojo.CreateOrderRequest;

public class OrderSteps extends OrderClient {
    @Step("Создание заказа номер: {}")
    public Response createOrderStep(String firstName, String lastName, String address, String metroStation, String phone, Integer rentTime, String deliveryDate, String comment, String[] color){
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(firstName, lastName,address, metroStation, phone, rentTime, deliveryDate, comment, color);
        return createOrder(createOrderRequest);
    }
}