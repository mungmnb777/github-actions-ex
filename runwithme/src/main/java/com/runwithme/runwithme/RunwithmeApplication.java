package com.runwithme.runwithme;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {@Server(url = "https://server.runwithme.shop")})
//@OpenAPIDefinition(servers = {@Server(url = "http://localhost:8080")})
@SpringBootApplication
public class RunwithmeApplication {

	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	public static void main(String[] args) {
		SpringApplication.run(RunwithmeApplication.class, args);
	}

}
