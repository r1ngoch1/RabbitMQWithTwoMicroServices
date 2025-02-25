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

import java.util.List;

/**
 * Репозиторий для работы с таблицей сообщений в базе данных.
 * Позволяет проверять существование сообщений, сохранять их и получать список всех сообщений.
 */
@Repository
public class MessageRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRepository.class);

    /**
     * Конструктор с внедрением зависимости JdbcTemplate.
     *
     * @param jdbcTemplate объект для взаимодействия с базой данных
     */
    @Autowired
    public MessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Проверяет, существует ли сообщение в базе данных по его идентификатору.
     *
     * @param id идентификатор сообщения
     * @return true, если сообщение существует, иначе false
     */
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

    /**
     * Сохраняет сообщение в базе данных. Если сообщение с таким ID уже существует, выбрасывается исключение.
     *
     * @param message объект сообщения для сохранения
     */
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

    /**
     * Получает список всех сообщений из базы данных.
     *
     * @return список сообщений
     */
    public List<Message> findAll() {
        try {
            LOGGER.info("Попытка получения всех сообщений из базы данных");
            String sql = "SELECT * FROM messages";
            List<Message> messages = jdbcTemplate.query(sql, (rs, rowNum) -> {
                Message message = new Message();
                message.setId(rs.getLong("id"));
                message.setName(rs.getString("name"));
                message.setPrice(rs.getDouble("price"));
                message.setTimestamp(rs.getTimestamp("timestamp"));
                return message;
            });
            LOGGER.info("Успешно получено {} сообщений", messages.size());
            return messages;
        } catch (DataAccessException e) {
            LOGGER.error("Ошибка при получении всех сообщений из базы данных", e);
            throw new MessagePersistenceException("Ошибка при получении всех сообщений из базы данных", e);
        }
    }
}
