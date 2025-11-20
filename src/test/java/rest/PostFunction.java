package rest;

import Payload.PostClass;
import Util.ReusableFunction;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class PostFunction {

    List<String> ids = new LinkedList<>();

    @Test(dataProvider="BookData" , priority = 1)
    public void addBook(String isbn, String aisle)
    {
        RestAssured.baseURI = "http://216.10.245.166";

        String response = given().log().all()
                .header("Content-Type", "application/json")
                .body(PostClass.Create(isbn, aisle))
                .when()
                .post("/Library/Addbook.php")
                .then().log().all()
                .statusCode(200)
                .extract().asString();

        JsonPath js = new JsonPath(response);
        String id = js.getString("ID");

        ids.add(id);
        System.out.println("####################################################3");
        System.out.println("Response: " + response);

        System.out.println("Added ID: " + id);
        System.out.println("####################################################3");

    }


    @Test(dependsOnMethods = "addBook", priority = 2)
    public void retrieveBook()
    {
        RestAssured.baseURI = "http://216.10.245.166";

        for (String id : ids) {

            String res1 = given().log().all()
                    .queryParam("ID", id)
                    .when()
                    .get("/Library/GetBook.php")
                    .then().log().all()
                    .statusCode(200)
                    .extract().asString();
            System.out.println("####################################################3");
            System.out.println("Retrieved Book for ID " + id + ": " + res1);
            System.out.println("####################################################3");

        }
    }


    @Test(dependsOnMethods = "retrieveBook", priority = 3)
    public void deleteBook()
    {
        RestAssured.baseURI = "http://216.10.245.166";

        for (String id : ids) {

            String res = given().log().all()
                    .header("Content-Type", "application/json")
                    .body(PostClass.DeleteBook(id))
                    .when()
                    .delete("/Library/DeleteBook.php")
                    .then().log().all()
                    .statusCode(200)
                    .extract().asString();
            System.out.println("####################################################3");
            System.out.println("Deleted: " + res);
            System.out.println("####################################################3");

        }
    }


    @DataProvider(name="BookData")
    public Object[][] getData()
    {
        return new Object[][] {
                {"aaa", "111"},
                {"bbb", "222"},
                {"ccc", "333"}
        };
    }
}
