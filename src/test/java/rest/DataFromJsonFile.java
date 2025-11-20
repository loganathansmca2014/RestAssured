package rest;

import io.restassured.RestAssured;
import org.testng.annotations.Test;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
public class DataFromJsonFile {

    @Test
    public void addBook() throws IOException {
        RestAssured.baseURI="http://216.10.245.166";
        String response=given()
                .header("Content-type","application/json")
                .body(new String(Files.readAllBytes(Paths.get("src/test/resources/addBook.json"))))
                .when()
                .post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
        System.out.println(response);
    }
}
