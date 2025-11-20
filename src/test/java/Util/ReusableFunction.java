package Util;

import io.restassured.path.json.JsonPath;

public class ReusableFunction {

    public static JsonPath rawJsonToPrettyJson(String response){
        JsonPath js=new JsonPath(response);
        return js;
    }

}
