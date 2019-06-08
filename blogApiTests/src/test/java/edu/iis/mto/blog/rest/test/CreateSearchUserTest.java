package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class CreateSearchUserTest {

    @Test
    public void searchingRemovedUserShouldReturnZero() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size", is(0))
                   .when()
                   .get("/blog/user/find?searchString=" + "Novak");
    }

    @Test
    public void searchingUsersByEmailShouldReturnThreeUsers() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size", is(3))
                   .when()
                   .get("/blog/user/find?searchString=" + "@d");
    }

    @Test
    public void searchingUsersByNameShouldReturnThreeUsers() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size", is(1))
                   .when()
                   .get("/blog/user/find?searchString=" + "Roger");
    }

}
