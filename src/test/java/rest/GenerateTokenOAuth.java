package rest;

import Util.ReusableFunction;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class GenerateTokenOAuth {
    public static String token;
    public static String data;
    public static String actualName;
    @Test
    public static void generatetokeOAuth() {
        RestAssured.baseURI = "https://demo.duendesoftware.com";
        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", "m2m");
        formData.put("client_secret", "secret");
        formData.put("scope", "api");
        formData.put("grant_type", "client_credentials");
        String response = given()
                .formParams(formData)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .when()
                .post("/connect/token")
                .then().assertThat().statusCode(200)
                .log().all()
                .extract()
                .response().asString();

        JsonPath js = ReusableFunction.rawJsonToPrettyJson(response);
        token = js.getString("access_token");
        System.out.println("The access token is :" + token);
        System.out.println("The Response is :" + response);


    }

    @Test(dependsOnMethods={"generatetokeOAuth"})
    public void CreateReq() {
        Map<String,String>header=new HashMap<>();
        header.put("content-type","application/json");
        header.put("Authorization","Bearer "+token);
        RestAssured.baseURI = "https://httpbin.org";
        String response = given().log().all()
                .headers(header)
                .body("{\n" +
                        "  \"name\": \"Logan\",\n" +
                        "  \"role\": \"Developer\"\n" +
                        "}")
                .when()
                .post("/post")
                .then().assertThat().statusCode(200)
                .log().all()
                .extract().response().asString();
        JsonPath js= ReusableFunction.rawJsonToPrettyJson(response);
        data=js.getString("json.name");
        System.out.println("The name is :"+data);

    }
    @Test(dependsOnMethods={"CreateReq"})
    public void updateRequest() throws IOException {
        Map<String,String>header=new HashMap<>();
        header.put("content-type","application/json");
        header.put("Authorization","Bearer "+token);
        RestAssured.baseURI="https://httpbin.org";
        String updateResponse=given()
                .headers(header)
                .body(Files.readAllBytes(Paths.get("src/test/resources/update.json")))
                .when()
                .put("/put")
                .then().assertThat().statusCode(200)
                .log().all()
                .extract().response().asString();
        JsonPath js= ReusableFunction.rawJsonToPrettyJson(updateResponse);
        data=js.getString("json.name");
        System.out.println("The name is :"+data);

    }

    @Test(dependsOnMethods = {"updateRequest"})
    public void getRequest()
    {

        RestAssured.baseURI="https://httpbin.org";
        String getresponse=given()
                .header("Authorization","Bearer"+token)
                .queryParam("name","logan")
                .when()
                .get("/get")
                .then().assertThat().statusCode(200)
                .log().all()
                .extract().response().asString();
        JsonPath js= ReusableFunction.rawJsonToPrettyJson(getresponse);
        String name=js.getString("args.name");
        System.out.println("The name is :"+name);
    }
    @Test(dependsOnMethods={"getRequest"})
    public void patchRequest() throws IOException {
        Map<String,String>header=new HashMap<>();
        header.put("content-type","application/json");
        header.put("Authorization","Bearer "+token);
        RestAssured.baseURI="https://httpbin.org";
        String patchResponse=given()
                .headers(header)
                .body(Files.readAllBytes(Paths.get("src/test/resources/patchRequest.json")))
                .when()
                .patch("/patch")
                .then().assertThat().statusCode(200)
                .log().all()
                .extract().response().asString();
        JsonPath js= ReusableFunction.rawJsonToPrettyJson(patchResponse);
        actualName=js.getString("json.name");
        System.out.println("The name is :"+actualName);

    }
    @Test(dependsOnMethods = {"patchRequest"})
    public void getAfterpatchRequest()
    {

        RestAssured.baseURI="https://httpbin.org";
        String getresponse=given()
                .header("Authorization","Bearer"+token)
                .queryParam("name",actualName)
                .when()
                .get("/get")
                .then().assertThat().statusCode(200)
                .log().all()
                .extract().response().asString();
        JsonPath js= ReusableFunction.rawJsonToPrettyJson(getresponse);
        String updatedName=js.getString("args.name");
        System.out.println("The name is :"+updatedName);
    }
    @Test(dependsOnMethods ={"getAfterpatchRequest"})
    public void deleteRequeat() throws IOException {
        Map<String,String>header=new HashMap<>();
        header.put("content-type","application/json");
        header.put("Authorization","Bearer "+token);
        RestAssured.baseURI="https://httpbin.org";
        String deleteResponse=given()
                .headers(header)
                //.queryParams("name",actualName)
                .body(Files.    readAllBytes(Paths.get("src/test/resources/deleteRequest.json")))
                .when()
                .delete("/delete")
                .then().assertThat().statusCode(200)
                .log().all()
                .extract().response().asString();

    }
    @Test(dependsOnMethods = {"deleteRequeat"})
    public void getAfterDeleteRequest()
    {

        RestAssured.baseURI="https://httpbin.org";
        String getresponse=given()
                .header("Authorization","Bearer"+token)
                .queryParam("name",actualName)
                .when()
                .get("/get")
                .then().assertThat().statusCode(200)
                .log().all()
                .extract().response().asString();
        JsonPath js= ReusableFunction.rawJsonToPrettyJson(getresponse);
        String updatedName=js.getString("args.name");
        System.out.println("The name is :"+updatedName);
    }
}