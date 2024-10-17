package httpClient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginHttpclientTests {
    Gson gson = new Gson();

    @Test
    public void loginDraftPositiveTest() throws IOException {
        String email = "ga@gmail.com";
        String password = "git branch -M main";

        Response response = Request.Post("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .bodyString("{\n" +
                        "  \"username\": \"" + email + "\",\n" +
                        "  \"password\": \"" + password + "\"\n" +
                        "}", ContentType.APPLICATION_JSON)
                .execute();

//  System.out.println(response);

        String responseJson = response.returnContent().asString();
        System.out.println(responseJson);

        JsonElement element = JsonParser.parseString(responseJson);
        JsonElement token = element.getAsJsonObject().get("token");
        System.out.println(token.getAsString());
    }

    @Test
    public void loginPositiveTest() throws IOException {

        AuthRequestDto requestDto = AuthRequestDto.builder()
                .username("...")
                .password("...")
                .build();

        Response response = Request.Post("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .bodyString(gson.toJson(requestDto), ContentType.APPLICATION_JSON)
                .execute();

        String responseJson = response.returnContent().asString();
        AuthResponseDto responseDto = gson.fromJson(responseJson, AuthResponseDto.class);
        System.out.println(responseDto.getToken());
    }

    @Test
    public void loginNegativeInvalidEmailTest() throws IOException {

        AuthRequestDto requestDto = AuthRequestDto.builder()
                .username("manuelgm.com")
                .password("...")
                .build();

        Response response = Request.Post("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .bodyString(gson.toJson(requestDto), ContentType.APPLICATION_JSON)
                .execute();

        HttpResponse httpResponse = response.returnResponse();
        //  System.out.println(httpResponse);

        System.out.println(httpResponse.getStatusLine().getStatusCode());

        InputStream inputStream = httpResponse.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        StringBuilder sb = new StringBuilder();
        while ((line=reader.readLine())!=null) {
            sb.append(line);
        }

        ErrorDto error = gson.fromJson(sb.toString(), ErrorDto.class);
        System.out.println(error.getError() + " *** " + error.getMessage());
    }
}
