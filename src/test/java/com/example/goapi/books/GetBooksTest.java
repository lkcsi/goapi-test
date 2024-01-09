package com.example.goapi.books;

import io.restassured.RestAssured;
import io.restassured.http.Method;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class GetBooksTest {

    @Test
    public void hasItems() {
        RestAssured.baseURI = "http://localhost:8080";
        given()
                .request(Method.GET, "/books")
                .then()
                .statusCode(200)
                .body("size()", equalTo(4))
                .body(matchesJsonSchemaInClasspath("books-schema.json"));
    }
    @Test
    public void testGetItemById() {
        RestAssured.baseURI = "http://localhost:8080";
        given()
                .pathParams("id", 2)
                .request(Method.GET, "/books/{id}")
                .then()
                .statusCode(200)
                .body("id", is(2))
                .body(matchesJsonSchemaInClasspath("book-schema.json"));
    }
    @Test
    public void testGetItemByOutOfRangeId() {
        RestAssured.baseURI = "http://localhost:8080";
        given()
                .pathParams("id", 5)
                .request(Method.GET, "/books/{id}")
                .then()
                .statusCode(404)
                .body("message", is("book not found"));
    }

}
