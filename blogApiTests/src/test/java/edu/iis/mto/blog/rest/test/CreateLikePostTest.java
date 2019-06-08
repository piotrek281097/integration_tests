package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItem;

public class CreateLikePostTest {
    private static final String LIKE_POST_API= "/blog/user/";

    @Test
    public void likePostByNotConfirmedUserReturns401Code() {
        String userId = "2";
        String postId = "1";
        String POST_API = LIKE_POST_API + userId + "/like/" + postId;

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
    public void likePostByConfirmedUserReturns201Code() {
        String userId = "3";
        String postId = "1";
        String POST_API = LIKE_POST_API + userId + "/like/" + postId;

        JSONObject jsonObj = new JSONObject().put("entry", "entryBlogPost");
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .body(jsonObj.toString())
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post(POST_API);
    }


    @Test
    public void secondLikePostByTheSameConfirmedUserShouldBeCountedStillAsOne() {
        String userId = "3";
        String postId = "1";
        String POST_API = LIKE_POST_API + userId + "/like/" + postId;

        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post(POST_API);


        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post(POST_API);

    }
}
