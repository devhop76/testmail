package org.trivelli.testmail.persistence.jpa;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories
public class MailAccountLauncher {
	
	private static final Logger log = LoggerFactory.getLogger(MailAccountLauncher.class);

	public static void main(String[] args) {
		SpringApplication.run(MailAccountLauncher.class);
	}

	@Bean
	public CommandLineRunner demo(MailAccountDAO repo1) {
		return (args) -> {
			Long boxId = 1000L;
			repo1.findById(boxId)
				 .ifPresent(mailAccount -> {
					log.info("----------$$$--------------");
					log.info("Found for BoxId="+boxId+" a mail account named:"+mailAccount.getDescription());
					log.info("----------$$$--------------");
				 });

		};
	}

}