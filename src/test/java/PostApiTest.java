import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

public class PostApiTest {

    @Before
    public void setup(){
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    private static String post_create_payload = "{\n" +
            "  \"userId\": 123,\n" +
            "  \"title\": \"post title\",\n" +
            "  \"body\": \"post body\"\n" +
            "}";

    @Test
    public void createsNewPost() {
        given().
            contentType(ContentType.JSON).
            body(post_create_payload).
        when().
            post("/posts").
        then().
            statusCode(201).
            body(matchesJsonSchemaInClasspath("post_schema.json")).
            body("userId", equalTo(123)).
            body("id", equalTo(101)).
            body("title", equalTo("post title")).
            body("body", equalTo("post body"));
    }

    @Test
    public void returnsSpecificPost() {
        when().
            get("/posts/1").
        then().
            statusCode(200).
            body(matchesJsonSchemaInClasspath("post_schema.json")).
            body("userId", equalTo(1)).
            body("id", equalTo(1)).
            body("title", equalTo("sunt aut facere repellat provident occaecati excepturi optio reprehenderit")).
            body("body", equalTo("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"));
    }

    @Test
    public void returnsListOfPosts() {
        when().
            get("/posts").
        then().
            statusCode(200).
            body(matchesJsonSchemaInClasspath("post_list_schema.json")).
            body("", hasSize(100));
    }
}
