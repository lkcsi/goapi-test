package com.example.goapi.books;

import io.restassured.RestAssured;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.hamcrest.Matchers.is;

public class CreateBookTest {

    private static JSONObject testObject = new JSONObject();

    @BeforeAll
    public static void init() {
        RestAssured.baseURI = "http://localhost:8080/books";
    }

    @BeforeEach
    public void before() {
        testObject = new JSONObject();
        testObject.put("title", "lotr");
        testObject.put("author", "idk");
        testObject.put("quantity", 5);
    }

    @Test
    public void testCreateBook() {
        var response = given()
                .body(testObject.toString())
                .contentType("application/json")
        .when()
                .post();

        response.then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .body("title", is(testObject.get("title")))
                .body("author", is(testObject.get("author")))
                .body("quantity", is(testObject.get("quantity")))
                .body(matchesJsonSchemaInClasspath("book-schema.json"));
    }

    @Test
    public void testCreateWithMissingField() {
        testObject.remove("author");
        given()
                .body(testObject.toString())
                .contentType("application/json")
        .when()
                .post()
        .then()
                .log().all()
                .assertThat()
                .body("error", is("Author field is mandatory"))
                .statusCode(400);
    }

    @Test
    public void testCreateWithQuantityIsZero() {
        testObject.put("quantity", 3);
        given()
                .body(testObject.toString())
                .contentType("application/json")
        .when()
                .post()
        .then()
                .log().all()
                .assertThat()
                .body("error", is("Quantity must be greater than or equals 5"))
                .statusCode(400);
    }
}
