package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class CreatePostTest extends FunctionalTests{

    private static final String POST_API_FIRST_PART= "/blog/user/";
    private static final String POST_API_LAST_PART = "/post";
    private String POST_API;
    @Test
    public void postBlogPostByNewUserReturns401Code() {
        String userId = "2";
        POST_API = POST_API_FIRST_PART + userId + POST_API_LAST_PART;
        JSONObject jsonObj = new JSONObject().put("entry", "entryBlogPost");
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .body(jsonObj.toString())
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_UNAUTHORIZED)
                   .when()
                   .post(POST_API);
    }

    @Test
    public void postBlogPostByConfirmedUserReturns201Code() {
        String userId = "1";
        POST_API = POST_API_FIRST_PART + userId + POST_API_LAST_PART;
        JSONObject jsonObj = new JSONObject().put("entry", "entryBlogPost");
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .body(jsonObj.toString())
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_CREATED)
                   .when()
                   .post(POST_API);
    }
}
