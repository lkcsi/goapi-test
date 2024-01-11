package com.example.goapi.books;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

public class CheckoutBookTest {

    private static JsonPath data;
    @BeforeAll
    public static void init() {
        RestAssured.baseURI = "http://localhost:8080/books";
        data = new JsonPath(GetBooksTest.class.getResourceAsStream("/initial-data.json"));
    }

    @Test
    public void testCheckoutBook() {
        given()
                .pathParams("id", "5d7d1e49-4183-4489-8646-8711c113b672")
                .contentType("application/json")
        .when()
                .patch("/{id}/checkout")
        .then()
                .log().all()
                .assertThat()
                .statusCode(202)
                .body("quantity", is(0))
                .body(matchesJsonSchemaInClasspath("book-schema.json"));
    }

    @Test
    public void testOutOfOrder() {
        var id = "bdecc1de-8b7d-4bf0-8154-f29d22b72be4";
        given()
                .pathParams("id", id)
                .contentType("application/json")
        .when()
                .patch("/{id}/checkout")
        .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .body("error", is("book with id: " + id + " is out of order"))
                .body(matchesJsonSchemaInClasspath("book-schema.json"));
    }
}
