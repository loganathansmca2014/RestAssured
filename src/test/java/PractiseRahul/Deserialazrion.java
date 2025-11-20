package PractiseRahul;

import Pojo_rahu.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class Deserialazrion {
    public static String accessToken;

    @Test
    public void deserializationTest() {
        System.out.println("#####################################################################");
        System.out.println("To generate Access Token");

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String res = given()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParam("grant_type", "client_credentials")
                .formParam("scope", "trust")
                .when()
                .post("/oauthapi/oauth2/resourceOwner/token")
                .then().assertThat().statusCode(200)
                .extract().response().asString();
        JsonPath js = new JsonPath(res);
        accessToken = js.getString("access_token");
        System.out.println("#####################################################################");
        System.out.println("Access Token is :" + accessToken);
        System.out.println("#####################################################################");

    }

    @Test(dependsOnMethods = {"deserializationTest"})
    public void getUserDetails() {
        System.out.println("#####################################################################");
        System.out.println("DeserializationTest via Get Request");

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        Response response = given()
                .queryParam("access_token", accessToken)
                .when()
                .get("/oauthapi/getCourseDetails")
                .then().extract().response();
        GetCourseDetails getDetails = response.as(GetCourseDetails.class);
        String instructor = getDetails.getInstructor();
        String url = getDetails.getUrl();
        String services = getDetails.getServices();
        String expertise = getDetails.getExpertise();
        String linkedIn = getDetails.getLinkedIn();
        System.out.println("#####################################################################");
        System.out.println("Instructor is :" + instructor);
        System.out.println("Url is :" + url);
        System.out.println("Services is :" + services);
        System.out.println("Expertise is :" + expertise);
        System.out.println("LinkedIn is :" + linkedIn);
        System.out.println("#####################################################################");

        Courses courses = getDetails.getCourses();

        List<WebAutomation> webAuto = courses.getWebAutomation();
        for (WebAutomation course : webAuto) {
            System.out.println("#####################################################################");
            System.out.println("Course Title is :" + course.getCourseTitle());
            System.out.println("Course Title is :" + course.getPrice());
            System.out.println("#####################################################################");
            Assert.assertNotNull(course.getCourseTitle(),"Course Title is null");
            Assert.assertNotNull(course.getPrice(),"Course Price is null");

        }
        List<API> apiCourses = courses.getApi();
        for (API api : apiCourses) {
            System.out.println("#####################################################################");
            System.out.println("API Course Title is :" + api.getCourseTitle());
            System.out.println("API Course Price is :" + api.getPrice());
            System.out.println("#####################################################################");

            Assert.assertNotNull(api.getCourseTitle(),"Course Title is null");
            Assert.assertNotNull(api.getPrice(),"Course Price is null");
        }

        List<Mobile> mobile = courses.getMobile();
        for (Mobile mobileCourse : mobile) {
            System.out.println("#####################################################################");
            System.out.println("Mobile Course Title is :" + mobileCourse.getCourseTitle());
            System.out.println("Mobile Course Price is :" + mobileCourse.getPrice());
            System.out.println("#####################################################################");

            Assert.assertNotNull(mobileCourse.getCourseTitle(),"Course Title is null");
            Assert.assertNotNull(mobileCourse.getPrice(),"Course Price is null");
        }

    }
}
