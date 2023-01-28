package advisor.Controller;

import advisor.Controller.Client;
import advisor.Main;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class JSONHandler {

    //public static JsonObject getJO(String response) {
    //    JsonObject jo = JsonParser.parseString(response).getAsJsonObject();
    //    return jo;
    //}

    public static JsonObject getJO(String response) throws IOException, InterruptedException {
        JsonObject jo = JsonParser.parseString(Client.getRequestResponse(Main.resourceServerPath + response)).getAsJsonObject();
        return jo;
    }

    public static boolean checkError(JsonObject jo) {
        if (jo.has("error")){
            String message = jo.getAsJsonObject("error").get("message").getAsString();
            System.out.println(message);
            return true;
        }
        return false;
    }
}
