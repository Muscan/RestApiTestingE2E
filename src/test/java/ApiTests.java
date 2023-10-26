
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ApiTests {
    static FrameworkProperties properties;
    static String userId;
    static String token;


    //initialize the properties
    //this method will be executed before the tests
    @BeforeClass
    public static void initializeObjects() {
        properties = new FrameworkProperties();
    }

    @Test
    public void testGetUsers(){
        Response response = null;
        String targetURI = Constants.BASE_URL + Constants.GET_USERS;
        response = RestAssured
                .given()
                .header(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE)
                .when()
                .get(targetURI);
        assertEquals(Constants.SUCCESS_CODE, response.statusCode());
        System.out.println("testGetUsers has passed");
    }


    //With Json
    //get/users -> returns a list and it check the Object User to be in the arrayList<User>
    @Test
    //refactor this test
    public void testGetUsers2(){
        Response response = null;
        String targetURI = Constants.BASE_URL + Constants.GET_USERS;
        response = RestAssured
                .given()
                .header(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE)
                .when()
                .get(targetURI);
        assertEquals(Constants.SUCCESS_CODE, response.statusCode());
        var usersResponse = response.as(GetUsersResponse.class);
        var users = usersResponse.data;
        System.out.println(users);
        //System.out.println(users.get(0));
    }
    //get all resources
    @Test
    public void testGetResource(){
        Resource expectedResource = new Resource(1,"cerulean",2000,"#98B2D1","15-4020");
        Response response = null;
        String targetURI = Constants.BASE_URL + Constants.GET_RESOURCE;

        response = RestAssured
                .given()
                .header(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE)
                .when()
                .get(targetURI);
        assertEquals(Constants.SUCCESS_CODE, response.statusCode());
        var resourceResponse = response.as(GetResourceResponse.class);
        var resourceArray = resourceResponse.data;
        assertTrue(resourceArray.contains(expectedResource));
        //System.out.println(resourceArray.get(0));
        for(int i=0; i<2; i++){
            System.out.println(resourceArray.get(i));
        }
        System.out.println("Test  testGetResource() has passed");
    }
    //get user by an id
    @Test
    public void getUserByIdTest(){
        User expectedUser = new User(1,"george.bluth@reqres.in","George", "Bluth","https://reqres.in/img/faces/1-image.jpg" );
        Response response = null;
        String targetURI = Constants.BASE_URL + Constants.GET_USER_ID;
        response = RestAssured
                .given()
                .header(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE)
                .when()
                .get(targetURI);
        assertEquals(Constants.SUCCESS_CODE, response.statusCode());

        var userResponse = response.as(GetUserResponse.class);
        var user = userResponse.data;
        //since we are checking the object equality, we use Equal
        //v1
        assertTrue(user.equals(expectedUser));
        //v2
        assertEquals(expectedUser, user);
        System.out.println(user);
        System.out.println("Test getUserByIdTest() has passed");

    }

    //register a new user
    //bear in mind that the register accepts only "eve.holt@reqres.in",
    // "password": "test"

    @Test
    public void registerUser() throws IOException {
        Response response = null;

        String targetURI = Constants.BASE_URL + Constants.POST_REGISTER;
        JsonObject payload = new JsonObject();

        payload.addProperty(Constants.EMAIL, properties.getPropValues(Constants.EMAIL));
        payload.addProperty(Constants.PASSWORD, properties.getPropValues(Constants.PASSWORD));
        //sending the actual body
        //the response will be catched in the response
        response = RestAssured.given()
                .relaxedHTTPSValidation()
                .header(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_VALUE)
                .body(payload.toString())   //sending the actual body
                .when()
                .post(targetURI);
        assertEquals(Constants.SUCCESS_CODE, response.statusCode());
        //create new JSON object and store the ID from new user created
        var registerUserResponse = response.as(RegisterUserResponse.class);
        System.out.println(registerUserResponse.id + " " + registerUserResponse.token);
    }
}

