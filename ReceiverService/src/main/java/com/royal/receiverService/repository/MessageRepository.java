package com.royal.receiverService.repository;

import com.royal.receiverService.dto.Message;
import com.royal.receiverService.exception.MessageAlreadyExistsException;
import com.royal.receiverService.exception.MessagePersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRepository.class);

    @Autowired
    public MessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsById(Long id) {
        try {
            String sql = "SELECT COUNT(*) FROM messages WHERE id = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            LOGGER.error("Не удалось проверить, существует ли сообщение в бд id: {}", id, e);
            throw new MessagePersistenceException("Не удалось проверить, существует ли сообщение в бд id: " + id, e);
        }
    }

    public void save(Message message) {
        try {
            if (existsById(message.getId())) {
                LOGGER.warn("Попытка сохранить дубликат сообщения id: {}", message.getId());
                throw new MessageAlreadyExistsException("Сообщение с id " + message.getId() + " уже существует");
            }
            String sql = "INSERT INTO messages (id, name, price, timestamp) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, message.getId(), message.getName(), message.getPrice(), message.getTimestamp());
            LOGGER.info("Сообщение было успешно в базе данных сохранено id: {}", message.getId());
        } catch (DataAccessException e) {
            LOGGER.error("Ошибка сохранения сообщения с id: {}", message.getId(), e);
            throw new MessagePersistenceException("Ошибка сохранения сообщения с id: " + message.getId(), e);
        }
    }


}
