package authJirawithBugCreationandAttachment;

import Util.ReusableFunction;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import payloads.PayloadFunction;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BugTest {
    String id;
@Test
    public void bugCreation() {
    Map<String,String> header=new HashMap<>();
    header.put("Content-Type","Application/json");
    header.put("Authorization","Basic bG9ndW1jYTIwMTFAZ21haWwuY29tOkFUQVRUM3hGZkdGMFpkOU00Q3hEOTEwUUxYeXJoM0pjQ014S2dDakRSYkRmNExpU1RFOXFMMFBObFM5TVMxU2dDNlBpWXN2WXVmWUQ4OGg0VUMxU0x0NlBPRlAzUXRCZ2kxR0JvTDZydTBQRnRGTVZ5aDZWYkVIX1kyOENiVER4VE0tRXZZbmpNMG5VVWhLay14Z0lyM1M0T0hZdFhXcFhtZkstd1VyWEZBMXAxemxpcTBaMlBqVT1BN0UzNUFFMg==" );
    RestAssured.baseURI = "https://logumca2011.atlassian.net";
    String response = given()
            .headers(header)
            .body(PayloadFunction.CreatBug())
            .when()
            .post("/dummy/api/3/issue")
            .then()
            .log().all()
            .extract().response()
            .asString();
    JsonPath js = ReusableFunction.rawJsonToPrettyJson(response);
    id = js.getString("id");
    System.out.println(response);
    System.out.println(id);
}
@Test(dependsOnMethods="bugCreation")
public void addAttachment()
{
    Map<String,String> header=new HashMap<>();
    header.put("x-Atlassian-Token","no-check");
    header.put("Authorization","Basic bG9ndW1jYTIwMTFAZ21haWwuY29tOkFUQVRUM3hGZkdGMFpkOU00Q3hEOTEwUUxYeXJoM0pjQ014S2dDakRSYkRmNExpU1RFOXFMMFBObFM5TVMxU2dDNlBpWXN2WXVmWUQ4OGg0VUMxU0x0NlBPRlAzUXRCZ2kxR0JvTDZydTBQRnRGTVZ5aDZWYkVIX1kyOENiVER4VE0tRXZZbmpNMG5VVWhLay14Z0lyM1M0T0hZdFhXcFhtZkstd1VyWEZBMXAxemxpcTBaMlBqVT1BN0UzNUFFMg==" );

    String attachementRes=given()
                .pathParams("key",id)
            .headers(header)
            .multiPart("file", new File("result/SQL_Complete_Summary_Sheet.pdf.pdf"))
                .when()
                .post("/dummy/api/3/issue/{key}/attachments")

                .then()
                .log().all()
                .extract().response().asString();
        System.out.println(attachementRes);
    }
}
