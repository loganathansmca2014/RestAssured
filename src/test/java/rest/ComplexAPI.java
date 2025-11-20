package rest;

import Pojo.*;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.*;

public class ComplexAPI {


    @Test
    public void serailZation() {

        HashMap<String, Object> payload = getStringObjectHashMap();

        RestAssured.baseURI = "https://httpbin.org";

        // Post and extract full response
        Response resp = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post("/post")
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();

        // IMPORTANT: httpbin returns your payload under the "json" key.
        // Deserialize the nested "json" object into GetInfo
        GetInfo gl = resp.jsonPath().getObject("json", GetInfo.class);

        // Now access fields safely
        int userId = gl.getUser().getId();
        String name = gl.getUser().getName();
        String email = gl.getUser().getProfile().getEmail();

        System.out.println("userId = " + userId);
        System.out.println("name = " + name);
        System.out.println("email = " + email);

        List<Roles> roles = (List<Roles>) gl.getUser().getProfile().getRoles();
        if (roles != null) {
            for (int i = 0; i < roles.size(); i++) {
                String role = roles.get(i).getRole().toString();
                double salary = roles.get(i).getSalary();
                System.out.println("role[" + i + "] = " + role + ", salary = " + salary);
                Assert.assertEquals(role, roles.get(i).getRole());
                Assert.assertEquals(salary, roles.get(i).getSalary());

            }
        } else {
            System.out.println("roles is null");
        }

        String traceId = gl.getRequestMeta().getTraceId();
        String timestamp = gl.getRequestMeta().getTimestamp();
        System.out.println("traceId = " + traceId);
        System.out.println("timestamp = " + timestamp);


    }

    private static HashMap<String, Object> getStringObjectHashMap() {

        // Build roles list (each role is a Map -> list of maps)
        Map<String, Object> role1 = new HashMap<>();
        role1.put("role", "admin");
        role1.put("salary", 1232.0);

        Map<String, Object> role2 = new HashMap<>();
        role2.put("role", "Developer");
        role2.put("salary", 23333.0);

        List<Map<String, Object>> rolesList = new ArrayList<>();
        rolesList.add(role1);
        rolesList.add(role2);

        // Build profile
        Map<String, Object> profile = new HashMap<>();
        profile.put("email", "logan@example.com");
        profile.put("roles", rolesList); // <<< roles is a list

        // Build user
        Map<String, Object> user = new HashMap<>();
        user.put("id", 123);
        user.put("name", "logan");
        user.put("profile", profile);

        // Build requestMeta (note key name must match POJO: requestMeta)
        Map<String, Object> requestMeta = new HashMap<>();
        requestMeta.put("traceId", "abc-123");
        requestMeta.put("timestamp", "2025-11-20T10:00:00Z");

        // Final payload
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("user", user);
        payload.put("requestMeta", requestMeta);

        return payload;
    }
}
