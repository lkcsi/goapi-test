package com.example.goapi.books;

import io.restassured.RestAssured;
import io.restassured.http.Method;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

public class GetBooksTest {

    private static JsonPath data;
    @BeforeAll
    public static void init() {
        RestAssured.baseURI = "http://localhost:8080/books";
        data = new JsonPath(GetBooksTest.class.getResourceAsStream("/initial-data.json"));
    }

    @Test
    public void testGetItems() {
        given()
                .request(Method.GET)
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("books-schema.json"))
                .body("", is(data.getList("")));
    }
    @Test
    public void testGetItemById() {
        given()
                .pathParams("id", "5d7d1e49-4183-4489-8646-8711c113b672")
                .request(Method.GET, "/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("", is(data.getList("").get(0)))
                .body(matchesJsonSchemaInClasspath("book-schema.json"));
    }
    @Test
    public void testGetItemByOutOfRangeId() {
        var id = "test_id";
        given()
                .pathParams("id", id)
                .request(Method.GET, "/{id}")
                .then()
                .log().all()
                .contentType("application/json")
                .statusCode(404)
                .body("error", is("book with id: " + id +" is not found"));
    }

}
