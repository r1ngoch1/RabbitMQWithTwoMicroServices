package com.royal.receiverService.repository;

import com.royal.receiverService.dto.DeadLetterMessage;
import com.royal.receiverService.exception.DeadLetterPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class DeadLetterRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadLetterRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public DeadLetterRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(DeadLetterMessage deadLetterMessage) {
        String sql = "INSERT INTO dead_letter_messages (message_content, error_reason, timestamp) VALUES (?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    deadLetterMessage.getMessageBody(),
                    deadLetterMessage.getErrorReason(),
                    new Timestamp(System.currentTimeMillis()));

            LOGGER.info("Мертвое письмо сохранено в БД: {}", deadLetterMessage.getMessageBody());
        } catch (Exception e) {
            LOGGER.error("Ошибка сохранения мертвого письма в БД: {}", e.getMessage(), e);
            throw new DeadLetterPersistenceException("Ошибка сохранения мертвого письма в БД", e);
        }
    }
}
