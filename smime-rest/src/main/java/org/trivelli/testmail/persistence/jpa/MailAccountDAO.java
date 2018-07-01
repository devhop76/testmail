package org.trivelli.testmail.persistence.jpa;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface MailAccountDAO extends JpaRepository<MailAccount, Long> {
    List<MailAccount> findByBoxId(Long boxId);
}
