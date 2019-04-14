package ru.bellintegrator.filesharing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.bellintegrator.filesharing.exception.ServiceException;
import ru.bellintegrator.filesharing.model.Role;
import ru.bellintegrator.filesharing.model.RoleName;
import ru.bellintegrator.filesharing.model.User;
import ru.bellintegrator.filesharing.repository.RoleRepository;
import ru.bellintegrator.filesharing.repository.UserRepository;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final MailSender mailSender;

    @Autowired
    public RegistrationServiceImpl(UserRepository userRepo, RoleRepository roleRepo, MailSender mailSender) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            throw new ServiceException("User exists!");
        }

        Role role = roleRepo.findByRoleName(RoleName.USER);
        user.setRoles(Collections.singleton(role));
        setActivationCodeAndRegDate(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            sendActivationCode(user);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void activateUser(String code) {
        if (code == null) {
            throw new ServiceException("Activation code has not found!");
        }

        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            throw new ServiceException("Activation code has not found!");
        }

        if (checkRegDate(user.getRegistrationDate())) {
            setActivationCodeAndRegDate(user);
            sendActivationCode(user);
            throw new ServiceException("Activation link has expired! The new one has been sent");
        }

        user.setActivationCode(null);
        user.setIsConfirmed(true);
        userRepo.save(user);
    }

    /**
     * Установить код активации и дату регистрации
     *
     * @param user пользователь
     */
    private void setActivationCodeAndRegDate(User user) {
        user.setActivationCode(UUID.randomUUID().toString());
        user.setRegistrationDate(new Date());
        userRepo.save(user);
    }

    /**
     * Послать на e-mail пользователю ссылку для подтверждения регистрации
     *
     * @param user пользователь
     */
    private void sendActivationCode(User user) {
        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to File Sharing! Please, visit next link to confirm your e-mail: http://localhost:8080/activate/%s",
                user.getUsername(),
                user.getActivationCode()
        );
        mailSender.send(user.getEmail(), "Activation code", message);
    }

    /**
     * Проверить зарегистрировался ли пользователь более суток назад
     *
     * 86400000 - это 24 часа в милисекундах
     *
     * @param regDate дата регистрации пользователя
     * @return true - если с момента регистрации прошло более 24 часов
     */
    private boolean checkRegDate(Date regDate) {
        return (System.currentTimeMillis() - regDate.getTime()) > 86400000;
    }
}
