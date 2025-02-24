package com.royal.receiverService.service;

import com.royal.receiverService.dto.DeadLetterMessage;
import com.royal.receiverService.exception.DeadLetterPersistenceException;
import com.royal.receiverService.repository.DeadLetterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class DeadLetterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadLetterService.class);


    private final DeadLetterRepository deadLetterRepository;

    @Autowired
    public DeadLetterService(DeadLetterRepository deadLetterRepository) {
        this.deadLetterRepository = deadLetterRepository;
    }

    public void saveDeadLetter(String messageBody, String errorReason) {
        DeadLetterMessage deadLetter = new DeadLetterMessage(messageBody, errorReason, new Timestamp(System.currentTimeMillis()));
        try {
            deadLetterRepository.save(deadLetter);
        } catch (DeadLetterPersistenceException e) {
            LOGGER.error("Ошибка при сохранении мертвого письма: {}", e.getMessage(), e);
        }
    }
}
