package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import okhttp3.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginOkhttpTests {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    @Test
public void  loginPositiveTest() throws IOException {

        AuthRequestDto requestDto=AuthRequestDto.builder()
                .username( "ga@gmail.com")
                .password("Manuel!123")
                .build();

       RequestBody requestBody= RequestBody.create(gson.toJson(requestDto), JSON);

      Request request = new Request.Builder()
               .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
               .post(requestBody).build();

       Response response = client.newCall(request).execute();

     String responseJson = response.body().string();

    AuthResponseDto responseDto = gson.fromJson(responseJson, AuthResponseDto.class);
     System.out.println(responseDto.getToken());
    }
}
