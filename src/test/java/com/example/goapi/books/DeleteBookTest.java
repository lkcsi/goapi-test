package com.example.goapi.books;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class DeleteBookTest {
    private static JSONObject testObject = new JSONObject();
    @BeforeAll
    public static void init() {
        RestAssured.baseURI = "http://localhost:8080/books";
    }

    @BeforeEach
    public void before() {
        testObject = new JSONObject();
        testObject.put("title", "new_book");
        testObject.put("author", "test_author");
        testObject.put("quantity", 5);
    }

    private void createEntityToDelete() {
        var resp = given()
                .body(testObject.toString())
                .contentType("application/json")
                .when()
                .post();
        resp.then()
                .log().all()
                .assertThat()
                .statusCode(201);

        JSONObject body = new JSONObject(resp.getBody().asString());
        testObject.put("id", body.get("id"));
    }
    @Test
    public void testDelete() {
        createEntityToDelete();
        given()
                .pathParams("id", testObject.get("id"))
                .request(Method.DELETE, "/{id}")
        .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    public void testDeleteNonExist() {
        var id = "no_id";
        given()
                .pathParams("id", id)
                .request(Method.DELETE, "/{id}")
                .then()
                .log().all()
                .statusCode(404)
                .body("error", is("book with id: " + id + " is not found"));
    }
}
