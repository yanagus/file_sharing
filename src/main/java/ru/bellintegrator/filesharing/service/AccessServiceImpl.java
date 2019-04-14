package ru.bellintegrator.filesharing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bellintegrator.filesharing.exception.EntityNotFoundException;
import ru.bellintegrator.filesharing.model.Access;
import ru.bellintegrator.filesharing.model.User;
import ru.bellintegrator.filesharing.repository.AccessRepository;
import ru.bellintegrator.filesharing.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
public class AccessServiceImpl implements AccessService {

    private final AccessRepository accessRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccessServiceImpl(AccessRepository accessRepository, UserRepository userRepository) {
        this.accessRepository = accessRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void saveRequestToRead(User user, User subscriber) {
        checkUserAndSubscriber(user, subscriber);
        Access access = getAccessByUserAndSubscriber(user, subscriber);
        access.setReadAccess(true);
        access.setRequest(true);
        accessRepository.save(access);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void saveRequestToDownload(User user, User subscriber) {
        checkUserAndSubscriber(user, subscriber);
        Access access = getAccessByUserAndSubscriber(user, subscriber);
        access.setDownloadAccess(true);
        access.setRequest(true);
        accessRepository.save(access);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Set<Access> getRequestingAccesses(User currentUser) {
        if (currentUser == null) {
            throw new EntityNotFoundException("No user");
        }
        return accessRepository.findByUser(currentUser)
                .stream()
                .filter(access -> access.getRequest())
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void allowReadOrDownload(User currentUser, User subscriber) {
        checkUserAndSubscriber(currentUser, subscriber);
        Access access = accessRepository.findByUserAndSubscriber(currentUser, subscriber);
        access.setRequest(false);
        accessRepository.save(access);
    }

    @Override
    public Access findByUserAndSubscriber(User user, User subscriber) {
        Access access = accessRepository.findByUserAndSubscriber(user, subscriber);
        if (access == null) {
            throw new EntityNotFoundException("User not found!");
        }
        return access;
    }

    /**
     * Проверка пользователя и подписчика на пустоту
     *
     * @param user пользователь
     * @param subscriber подписчик
     */
    private void checkUserAndSubscriber(User user, User subscriber) {
        if (user == null || subscriber == null) {
            throw new EntityNotFoundException("No user or subscriber");
        }
    }

    /**
     * Получить доступ по пользователю и подписчику из базы данных или создать новый
     *
     * @param user пользователь
     * @param subscriber подписчик
     * @return доступ
     */
    private Access getAccessByUserAndSubscriber(User user, User subscriber) {
        Access access = accessRepository.findByUserAndSubscriber(user, subscriber);
        if (access == null) {
            access = new Access(userRepository.getOne(user.getId()), userRepository.getOne(subscriber.getId()));
        }
        return access;
    }
}
