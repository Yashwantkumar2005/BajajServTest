package com.example.BajajFinserv;

import com.example.BajajFinserv.Service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BajajFinservTestApplication implements CommandLineRunner {

	@Autowired
	private ApiService apiService;
	public static void main(String[] args) {
		SpringApplication.run(BajajFinservTestApplication.class, args);
	}

	@Override
	public void run(String... args) {
		apiService.execute();
	}
}
