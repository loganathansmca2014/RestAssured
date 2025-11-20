package payloads;

public class PayloadFunction {
    public static String Create(String isbn, String aisle)
    {
        String postpayload="{\n" +
                "  \"name\": \"Leam Appium \",\n" +
                "  \"isbn\": \""+isbn+"\",\n" +
                "  \"aisle\": \""+aisle+"\",\n" +
                "  \"author\": \"LOGA\"\n" +
                "}\n";

        return postpayload;
    }
    public static String DeleteBook(String id)
    {
        String postpayload="{\n" +
                "  \"ID\": \""+id+"\",\n" +
                "}\n";

        return postpayload;
    }
    public static String CreatBug()
    {
        String payload="{\n" +
            "  \"fields\": {\n" +
            "    \"project\": {\n" +
            "      \"key\": \"DEMO\"\n" +
            "    },\n" +
            "    \"summary\": \"image Not working\",\n" +
            "    \"issuetype\": {\n" +
            "      \"name\": \"[System] Incident\"\n" +
            "    }\n" +
            "  }\n" +
            "}";
        return payload;
    }


}
