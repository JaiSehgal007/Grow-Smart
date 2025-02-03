package com.amangoes.product_service;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.shaded.org.bouncycastle.pqc.crypto.rainbow.RainbowSigner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI="http://localhost";
		RestAssured.port=port;
	}

	static{
		mongoDBContainer.start();
	}

	@Test
	void shouldCreateProduct() {
		String requestBody= """
				{
				    "name":"d2d tomato fetrilizer",
				    "description":"fertilizer for tomatoes red leaves disease",
				    "price":"150"
				}
			""";

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("d2d tomato fetrilizer"))
				.body("description", Matchers.equalTo("fertilizer for tomatoes red leaves disease"))
				.body("price", Matchers.equalTo(150));
	}

}

// we will be using test containers library which will spin up docker containers for us
// this is for integration testing
// so instead of embedded mongodb, we will get to work with real mongodb with these containers
