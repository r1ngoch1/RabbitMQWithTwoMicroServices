package com.royal.receiverService.service;

import com.royal.receiverService.dto.DeadLetterMessage;
import com.royal.receiverService.exception.DeadLetterPersistenceException;
import com.royal.receiverService.repository.DeadLetterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Сервис для работы с мертвыми (необработанными) сообщениями.
 */
@Service
public class DeadLetterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadLetterService.class);

    private final DeadLetterRepository deadLetterRepository;

    /**
     * Конструктор сервиса мертвых сообщений.
     *
     * @param deadLetterRepository Репозиторий для работы с мертвыми сообщениями.
     */
    @Autowired
    public DeadLetterService(DeadLetterRepository deadLetterRepository) {
        this.deadLetterRepository = deadLetterRepository;
    }

    /**
     * Сохраняет мертвое сообщение в базу данных.
     *
     * @param messageBody Тело сообщения.
     * @param errorReason Причина ошибки.
     * @throws DeadLetterPersistenceException если возникает ошибка при сохранении сообщения.
     */
    public void saveDeadLetter(String messageBody, String errorReason) {
        DeadLetterMessage deadLetter = new DeadLetterMessage(messageBody, errorReason, new Timestamp(System.currentTimeMillis()));
        try {
            LOGGER.info("Попытка сохранения мертвого сообщения: messageBody={}, errorReason={}", messageBody, errorReason);
            deadLetterRepository.save(deadLetter);
            LOGGER.info("Мертвое сообщение успешно сохранено: messageBody={}, errorReason={}", messageBody, errorReason);
        } catch (DeadLetterPersistenceException e) {
            LOGGER.error("Ошибка при сохранении мертвого сообщения: messageBody={}, errorReason={}", messageBody, errorReason, e);
            throw e;
        }
    }

    /**
     * Возвращает список всех мертвых сообщений из базы данных.
     *
     * @return Список мертвых сообщений.
     * @throws DeadLetterPersistenceException если возникает ошибка при получении сообщений.
     */
    public List<DeadLetterMessage> getAllDeadLetters() {
        try {
            LOGGER.info("Попытка получения всех мертвых сообщений из базы данных");
            List<DeadLetterMessage> deadLetters = deadLetterRepository.findAll();
            LOGGER.info("Успешно получено {} мертвых сообщений", deadLetters.size());
            return deadLetters;
        } catch (DeadLetterPersistenceException e) {
            LOGGER.error("Ошибка при получении мертвых сообщений из базы данных", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Произошла непредвиденная ошибка при получении мертвых сообщений", e);
            throw new DeadLetterPersistenceException("Произошла непредвиденная ошибка", e);
        }
    }
}
