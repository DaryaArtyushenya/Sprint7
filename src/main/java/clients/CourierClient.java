package clients;

import io.restassured.RestAssured;
import model.modelPojo.courierPojo.CreateRequest;
import model.modelPojo.courierPojo.DeleteRequest;
import model.modelPojo.courierPojo.LoginRequest;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class CourierClient {

    public Response createCourier(CreateRequest courier){
        return given().log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier"); //возврат респонса

    }

    public Response loginCourier(LoginRequest loginRequest){
        return RestAssured.given()
                .header("Content-type","application/json")
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login");

    }

    public Response deleteCourier(DeleteRequest deleteRequest){
        return  RestAssured.given()
                .header("Content-type", "application/json")
                .body(deleteRequest)
                .delete("/api/v1/courier/" + deleteRequest.getId());
    }



}