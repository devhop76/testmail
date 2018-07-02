package org.trivelli.testmail.persistence.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MailAccountExec {
	
	private static final Logger log = LoggerFactory.getLogger(MailAccountExec.class);

	public static void main(String[] args) {
		SpringApplication.run(MailAccountExec.class);
	}

	@Bean
	public CommandLineRunner demo(MailAccountDAO repo1, MailDAO repo2) {
		return (args) -> {
			Long boxId = 1000L;
			MailAccount mailAccount = repo1.getOne(boxId);
			log.info("----------$$$--------------");
			log.info("Found for BoxId="+boxId+" a mail account named:"+mailAccount.getDescription());
			log.info("----------$$$--------------");
		};
	}

}