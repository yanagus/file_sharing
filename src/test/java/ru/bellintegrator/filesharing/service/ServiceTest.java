package ru.bellintegrator.filesharing.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.bellintegrator.filesharing.Application;
import ru.bellintegrator.filesharing.exception.ServiceException;
import ru.bellintegrator.filesharing.model.Access;
import ru.bellintegrator.filesharing.model.User;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Тест сервисов
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration(value = "src/main/resources")
@Transactional
public class ServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessService accessService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private MailSender mailSender;

    /**
     * Тест общего количества пользователей
     */
    @Test
    public void testUserCount() {
        Assert.assertEquals(new Long(9L), userService.userCount());
    }

    /**
     * Тест метода, который находит список пользователей
     */
    @Test
    public void testFindAll() {
        List<User> userList = userService.findAll();
        User maria = new User(1, "maria", "maria",
                "tuwipis@businesssource.net", null, true);
        Assert.assertEquals(maria, userList.get(0));
    }

    /**
     * Тест нахождения объекта доступа связи между двумя пользователями
     */
    @Test
    public void testFindByUserAndSubscriber() {
        List<User> userList = userService.findAll();
        User maria = userList.get(0);
        User mia = userList.get(5);
        Access access = accessService.findByUserAndSubscriber(maria, mia);
        Assert.assertTrue(access.getDownloadAccess());
        Assert.assertFalse(access.getRequest());
    }

    /**
     * Тест добавления пользователя, который уже имеет такое же имя.
     * При добавлении выбрасывается ошибка ServiceException
     */
    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("maria");
        user.setPassword("user");
        user.setEmail("tuwipis@businesssource.net");
        try {
            registrationService.addUser(user);
        } catch (ServiceException e) {
            assertEquals("User exists!", e.getMessage());
        }
    }

}
