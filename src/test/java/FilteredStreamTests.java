import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;


public class FilteredStreamTests {

    public final OkHttpClient httpClient = new OkHttpClient();

    @Test
    public void getRules_ValidRequest_ValidResponse() throws IOException {
        String bearerToken = System.getenv("BEARER_TOKEN");
        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/tweets/search/stream/rules")
                .header("Authorization", String.format("Bearer %s", bearerToken))
                .header("content-type", "application/json")
                .build();

        Call call = httpClient.newCall(request);
        Response response = call.execute();

        Assertions.assertEquals(response.code(), 200);
    }

    @Test
    public void getRules_NoAuthorization_ValidResponse() throws IOException {
        Request request = new Request.Builder()
                .url("https://api.twitter.com/2/tweets/search/stream/rules")
                .header("content-type", "application/json")
                .build();

        Call call = httpClient.newCall(request);
        Response response = call.execute();

        Assertions.assertEquals(response.code(), 401);
    }
}
