import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilteredStream {

    public final OkHttpClient httpClient = new OkHttpClient();

    public static void main(String[] args) throws IOException {
        String bearerToken = System.getenv("BEARER_TOKEN");
        if (null != bearerToken) {
            Map<String, String> rules = new HashMap<>();
            rules.put("has:links url:'www.nytimes.com'", "new york times article");
            FilteredStream filteredStream = new FilteredStream();
            filteredStream.setupRules(bearerToken, rules);
            filteredStream.connectStream(bearerToken);
        } else {
            System.out.println("There was a problem getting your bearer token. Please make sure you set BEARER_TOKEN environment variable");
        }
    }

    private void connectStream(String bearerToken) {
    }

    private void setupRules(String bearerToken, Map<String, String> rules) throws IOException {
        List<String> existingRules = getRules(bearerToken);
        if (existingRules.size() > 0) {
            deleteRules(bearerToken, existingRules);
        }
        createRules(bearerToken, rules);
    }

    private static void createRules(String bearerToken, Map<String, String> rules) {
    }

    private static void deleteRules(String bearerToken, List<String> existingRules) {
    }

    private List<String> getRules(String bearerToken) throws IOException {
        List<String> rules = new ArrayList<>();
        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/tweets/search/stream/rules")
                .header("Authorization", String.format("Bearer %s", bearerToken))
                .header("content-type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("unexpected code " + response);
            JSONObject json = (JSONObject) JSONValue.parse(response.body().string());
            if (json.size() > 1) {
                JSONArray array = (JSONArray) json.get("data");
                for (Object o : array) {
                    JSONObject jsonObject = (JSONObject) o;
                    rules.add(jsonObject.get("id").toString());
                }
            }
        }
        return rules;
    }


}
