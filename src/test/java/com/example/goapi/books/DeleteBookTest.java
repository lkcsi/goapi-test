package com.example.goapi.books;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class DeleteBookTest {
    private static JsonPath data;
    @BeforeAll
    public static void init() {
        RestAssured.baseURI = "http://localhost:8080/books";
        data = new JsonPath(DeleteBookTest.class.getResourceAsStream("/initial-data.json"));
    }

    @Test
    public void testDelete() {
        var id = "a4d5396b-dd25-499e-93f7-836a41772ba6";
        given()
                .pathParams("id", id)
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
