package com.hackudc.poustfit_server;

import com.hackudc.poustfit_server.config.DatabaseLoader;
import com.hackudc.poustfit_server.exceptions.register.UserCorreoExistsException;
import com.hackudc.poustfit_server.exceptions.register.UserUsernameExistsException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
public class PoustfitServerApplication {
	private final Logger logger = LoggerFactory.getLogger(PoustfitServerApplication.class);
	@Autowired
	@Lazy
	private DatabaseLoader databaseLoader;

	public static void main(String[] args) {
		SpringApplication.run(PoustfitServerApplication.class, args);
	}


	@PostConstruct
	public void init() throws InterruptedException {
		try {
			databaseLoader.loadData();
		} catch (UserUsernameExistsException | UserCorreoExistsException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
