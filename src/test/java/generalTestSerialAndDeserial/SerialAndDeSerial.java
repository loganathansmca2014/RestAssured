package generalTestSerialAndDeserial;
import generalPojoSerailAndDeserial.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SerialAndDeSerial {

    @Test
    public void serializationAndDeserialization_roundTrip() {

        // ---------------------------
        // 1) Build expected POJO (the object we want to send)
        // ---------------------------
        Roles r1 = new Roles();
        r1.setRole("admin");
        r1.setSalary(1232);

        Roles r2 = new Roles();
        r2.setRole("Developer");
        r2.setSalary(23333);

        List<Roles> rolesList = new ArrayList<>();
        rolesList.add(r1);
        rolesList.add(r2);

        Profile profile = new Profile();
        profile.setEmail("logan@example.com");
        profile.setRoles(rolesList);

        User user = new User();
        user.setId(123);
        user.setName("logan");
        user.setProfile(profile);

        RequestMetaData rm = new RequestMetaData();
        rm.setTraceId("abc-123");
        rm.setTimestamp("2025-11-20T10:00:00Z");

        GetInfo expected = new GetInfo();
        expected.setUser(user);
        expected.setRequestMeta(rm);

        // ---------------------------
        // 2) Serialize and POST the POJO (RestAssured will serialize to JSON)
        // ---------------------------
        RestAssured.baseURI = "https://httpbin.org";

        Response fullResp = given()
                .header("Content-Type", "application/json")
                .body(expected)                       // <-- POJO serialized to JSON
                .when()
                .post("/post")
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();

        // ---------------------------
        // 3) Extract 'json' node (httpbin wraps payload under 'json') and deserialize back to POJO
        // ---------------------------
        GetInfo actual = fullResp.jsonPath().getObject("json", GetInfo.class);

        // ---------------------------
        // 4) Assertions: compare expected vs actual (field-by-field)
        // ---------------------------
        Assert.assertNotNull(actual, "Deserialized GetInfo should not be null");
        Assert.assertNotNull(actual.getUser(), "User should not be null");
        Assert.assertNotNull(actual.getRequestMeta(), "requestMeta should not be null");

        // user fields
        Assert.assertEquals(actual.getUser().getId(), expected.getUser().getId(), "user.id mismatch");
        Assert.assertEquals(actual.getUser().getName(), expected.getUser().getName(), "user.name mismatch");

        // profile email
        Assert.assertEquals(actual.getUser().getProfile().getEmail(),
                expected.getUser().getProfile().getEmail(), "profile.email mismatch");

        // roles list
        List<Roles> actualRoles = actual.getUser().getProfile().getRoles();
        List<Roles> expectedRoles = expected.getUser().getProfile().getRoles();
        Assert.assertEquals(actualRoles.size(), expectedRoles.size(), "roles size mismatch");


        for(String role:actualRoles.stream().map(Roles::getRole).toList()){
            boolean found = actualRoles.stream().map(Roles::getRole).toList().contains(role);
            Assert.assertTrue(found, "Expected role not found: " + role);

        }
        for(double salary:actualRoles.stream().map(Roles::getSalary).toList())
        {
            boolean salaryFound=actualRoles.stream().map(Roles::getSalary).toList().contains(salary);
            Assert.assertTrue(salaryFound,"Expected salary not found: "+salary);
        }

        // requestMeta
        Assert.assertEquals(actual.getRequestMeta().getTraceId(), expected.getRequestMeta().getTraceId(), "traceId mismatch");
        Assert.assertEquals(actual.getRequestMeta().getTimestamp(), expected.getRequestMeta().getTimestamp(), "timestamp mismatch");

        // If you want, print confirmation
        System.out.println("     serialization/deserialization succeeded.");
    }
}