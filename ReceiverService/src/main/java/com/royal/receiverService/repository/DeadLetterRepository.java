package com.royal.receiverService.repository;

import com.royal.receiverService.dto.DeadLetterMessage;
import com.royal.receiverService.exception.DeadLetterPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Репозиторий для работы с таблицей dead_letter_messages в базе данных.
 * Предоставляет методы для сохранения и получения мертвых сообщений.
 */
@Repository
public class DeadLetterRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadLetterRepository.class);
    private final JdbcTemplate jdbcTemplate;

    /**
     * Конструктор с внедрением зависимости JdbcTemplate для работы с БД.
     *
     * @param jdbcTemplate объект JdbcTemplate
     */
    public DeadLetterRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Сохраняет мертвое сообщение в базе данных.
     *
     * @param deadLetterMessage объект мертвого сообщения
     * @throws DeadLetterPersistenceException если произошла ошибка при сохранении
     */
    public void save(DeadLetterMessage deadLetterMessage) {
        String sql = "INSERT INTO dead_letter_messages (message_content, error_reason, timestamp) VALUES (?, ?, ?)";

        try {
            jdbcTemplate.update(sql,
                    deadLetterMessage.getMessageBody(),
                    deadLetterMessage.getErrorReason(),
                    new Timestamp(System.currentTimeMillis()));

            LOGGER.info("Мертвое сообщение сохранено в БД: {}", deadLetterMessage.getMessageBody());
        } catch (Exception e) {
            LOGGER.error("Ошибка сохранения мертвого сообщения в БД: {}", e.getMessage(), e);
            throw new DeadLetterPersistenceException("Ошибка сохранения мертвого сообщения в БД", e);
        }
    }

    /**
     * Возвращает список всех мертвых сообщений из базы данных.
     *
     * @return список объектов DeadLetterMessage
     * @throws DeadLetterPersistenceException если произошла ошибка при получении данных
     */
    public List<DeadLetterMessage> findAll() {
        String sql = "SELECT * FROM dead_letter_messages";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                DeadLetterMessage message = new DeadLetterMessage(
                        rs.getString("message_content"),
                        rs.getString("error_reason"),
                        rs.getTimestamp("timestamp")
                );
                message.setId(rs.getLong("id"));
                return message;
            });
        } catch (DataAccessException e) {
            LOGGER.error("Ошибка при получении всех записей из таблицы dead_letter_messages", e);
            throw new DeadLetterPersistenceException("Ошибка при получении записей из таблицы dead_letter_messages", e);
        }
    }
}