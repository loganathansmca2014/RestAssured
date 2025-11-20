package seralizationTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import seralizationPojo.Org;
import seralizationPojo.Profile;
import seralizationPojo.RequestMeta;
import seralizationPojo.User;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Organizaation {

    @Test
    public void organizationSeralizationTest() {


        List<String> listof = new ArrayList<>();
        listof.add("Admin");
        listof.add("Testet");


        Profile pr = new Profile();
        pr.setEmail("logana@gamil.com");
        pr.setRoles(listof);


        User user = new User();
        user.setId(101);
        user.setName("Loganath");
        user.setProfile(pr);

        RequestMeta re = new RequestMeta();
        re.setTimestamp("2024-06-10T10:15:30Z");
        re.setTraceId("trace-12345");

        Org r1 = new Org();
        r1.setUser(user);
        r1.setRequestMeta(re);

        RestAssured.baseURI = "https://httpbin.org";
        Response resp = given()
                .header("Content-Type", "application/json")
                .body(r1)
                .when()
                .post("/post")
                .then()
                .extract().response();
        Org or = resp.jsonPath().getObject("json", Org.class);

        int userId = or.getUser().getId();
        String name = or.getUser().getName();
        String email = or.getUser().getProfile().getEmail();
        System.out.println(userId);
        System.out.println(name);
        System.out.println(email);
        String timestamp = or.getRequestMeta().getTimestamp();
        String traceId = or.getRequestMeta().getTraceId();
        System.out.println(timestamp);
        System.out.println(traceId);
        List<String> roles = or.getUser().getProfile().getRoles();
        for (String s : roles) {
            System.out.println(s);
            Assert.assertNotNull(s, "Role is null");
        }

        System.out.println(resp);

        Assert.assertEquals(userId, user.getId(), "User ID is not matching");
        Assert.assertEquals(name, user.getName(), "User Name is not matching");
        Assert.assertEquals(email, user.getProfile().getEmail(), "Email is not matching");
        Assert.assertEquals(timestamp, re.getTimestamp(), "Timestamp is not matching");
        Assert.assertEquals(traceId, re.getTraceId(), "TraceID is not matching");


    }
}
