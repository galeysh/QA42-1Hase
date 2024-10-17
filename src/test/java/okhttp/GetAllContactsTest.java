package okhttp;

import com.google.gson.Gson;
import dto.AllContactsDto;
import dto.ContactDto;
import dto.ErrorDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetAllContactsTest {
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZ2FAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3Mjk4MDA0MjksImlhdCI6MTcyOTIwMDQyOX0.ATADRcNxlYqhKO1zBSzDGxnEC2c0BEXYSjxEe2P4ohs";

    @Test
    public void getAllContactsSuccessTest() throws IOException {

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

        AllContactsDto contactsDto = gson.fromJson(response.body().string(), AllContactsDto.class);

        List<ContactDto> contacts = contactsDto.getContacts();
        for (ContactDto c : contacts) {
            System.out.println(c.getId());
            System.out.println(c.getLastName());
            System.out.println("=======================================");
        }
    }

    @Test
    public void getAllContactsUnauthorizedTest() throws IOException {

        String invalidToken = "eyJhbGciOiJIUzI1NiJ9eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZ2FAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3Mjk4MDA0MjksImlhdCI6MTcyOTIwMDQyOX0ATADRcNxlYqhKO1zBSzDGxnEC2c0BEXYSjxEe2P4ohj";

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization", invalidToken)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(), 401);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getError(), "Unauthorized");
        Assert.assertEquals(errorDto.getMessage(), "JWT strings must contain exactly 2 period characters. Found: 0");
    }


    @Test
    public void getAllContactsUnauthorizedTest1() throws IOException {

        String ungueltigToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZ2FAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3Mjk4MDA0MjksImlhdCI6MTcyOTIwMDQyOX0.ATADRcNxlYqhKO1zBSzDGxnEC2c0BEXYSjxEe2P4oh";

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization", ungueltigToken)
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(), 401);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getError(), "Unauthorized");
        Assert.assertEquals(errorDto.getMessage(), "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
    }
}