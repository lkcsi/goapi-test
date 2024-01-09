package com.example.goapi.books;

import io.restassured.RestAssured;
import io.restassured.http.Method;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class GetBooksTest {
    @BeforeAll
    public static void init() {
        RestAssured.baseURI = "http://localhost:8080/books";
    }

    @Test
    public void hasItems() {

        given()
                .request(Method.GET)
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", equalTo(4))
                .body(matchesJsonSchemaInClasspath("books-schema.json"));
    }
    @Test
    public void testGetItemById() {
        given()
                .pathParams("id", 2)
                .request(Method.GET, "/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is("2"))
                .body(matchesJsonSchemaInClasspath("book-schema.json"));
    }
    @Test
    public void testGetItemByOutOfRangeId() {
        var id = 5;
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
