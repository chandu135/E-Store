package com.company.e_store;

import org.springframework.boot.SpringApplication;

public class TestEStoreApplication {

	public static void main(String[] args) {
		SpringApplication.from(EStoreApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
