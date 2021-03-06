package org.trivelli.testmail.persistence.jpa;


import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface MailAccountDAO extends JpaRepository<MailAccount, Long> {
	
}
