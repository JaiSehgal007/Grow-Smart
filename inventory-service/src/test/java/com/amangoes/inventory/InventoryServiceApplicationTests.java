package com.amangoes.inventory;

import com.amangoes.inventory.stub.InventoryStubs;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

import static org.hamcrest.MatcherAssert.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class InventoryServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");
	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mySQLContainer.start();
	}

	@Test
	void checkInventory() {
		InventoryStubs.stubInventoryCall("tomato_enhancer", 1);
		var responseBody = RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/inventory?skuCode=tomato_enhancer&quantity=1")
				.then()
				.log().all()
				.statusCode(200)
				.extract()
				.body().as(Boolean.class);

		assertThat(responseBody, Matchers.is(true));
	}
}

// q1 -> what are these stubs for
// q2 -> what is the role of this wiremock, why do we have to set it and its port?
