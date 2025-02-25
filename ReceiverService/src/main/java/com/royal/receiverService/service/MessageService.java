package com.royal.receiverService.service;

import com.royal.receiverService.dto.Message;
import com.royal.receiverService.exception.MessageAlreadyExistsException;
import com.royal.receiverService.exception.MessagePersistenceException;
import com.royal.receiverService.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с сообщениями.
 */
@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    /**
     * Конструктор сервиса сообщений.
     *
     * @param messageRepository Репозиторий для работы с сообщениями.
     */
    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Сохраняет сообщение в базу данных.
     *
     * @param message Сообщение для сохранения.
     * @throws MessageAlreadyExistsException если сообщение уже существует.
     * @throws MessagePersistenceException   если возникает ошибка при сохранении сообщения.
     */
    public void saveMessage(Message message) {
        try {
            LOGGER.info("Попытка сохранить сообщение с id: {}", message.getId());
            messageRepository.save(message);
            LOGGER.info("Сообщение успешно сохранено с id: {}", message.getId());
        } catch (MessageAlreadyExistsException e) {
            LOGGER.warn("Не удалось сохранить сообщение: {}", e.getMessage());
            throw e;
        } catch (MessagePersistenceException e) {
            LOGGER.error("Не удалось сохранить сообщение из-за ошибки базы данных: {}", e.getMessage());
            throw new MessagePersistenceException("Не удалось сохранить сообщение из-за ошибки базы данных", e);
        } catch (Exception e) {
            LOGGER.error("При сохранении сообщения произошла непредвиденная ошибка: {}", e.getMessage());
            throw new RuntimeException("При сохранении сообщения произошла непредвиденная ошибка", e);
        }
    }

    /**
     * Возвращает список всех сообщений из базы данных.
     *
     * @return Список сообщений.
     * @throws MessagePersistenceException если возникает ошибка при получении сообщений.
     */
    public List<Message> getAllMessages() throws MessagePersistenceException {
        try {
            LOGGER.info("Попытка получения всех сообщений из базы данных через сервис");
            List<Message> messages = messageRepository.findAll();
            LOGGER.info("Успешно получено {} сообщений через сервис", messages.size());
            return messages;
        } catch (MessagePersistenceException e) {
            LOGGER.error("Ошибка при получении сообщений через сервис", e);
            throw new MessagePersistenceException("Ошибка при получении сообщений через сервис", e);
        }
    }
}