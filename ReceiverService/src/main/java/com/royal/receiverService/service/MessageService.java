package com.royal.receiverService.service;

import com.royal.receiverService.dto.Message;
import com.royal.receiverService.exception.MessageAlreadyExistsException;
import com.royal.receiverService.exception.MessagePersistenceException;
import com.royal.receiverService.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void save(Message message) {
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
}