package com.example.goapi.books;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

public class CheckoutBookTest {

    @BeforeAll
    public static void init() {
        RestAssured.baseURI = "http://localhost:8080/books";
    }

    @Test
    public void testCheckoutBook() {
        given()
                .pathParams("id", 1)
                .contentType("application/json")
        .when()
                .patch("/{id}/checkout")
        .then()
                .log().all()
                .assertThat()
                .statusCode(202)
                .body("quantity", is(4))
                .body(matchesJsonSchemaInClasspath("book-schema.json"));
    }

    @Test
    public void testOutOfOrder() {
        given()
                .pathParams("id", 2)
                .contentType("application/json")
        .when()
                .patch("/{id}/checkout")
        .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .body("error", is("book with id: " + 2 + " is out of order"))
                .body(matchesJsonSchemaInClasspath("book-schema.json"));
    }
}
