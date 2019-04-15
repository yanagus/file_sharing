package ru.bellintegrator.filesharing.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тест контроллера авторизации
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration(value = "src/main/resources")
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController controller;

    /**
     * Проверка работы сервиса, если пользователь не авторизован
     *
     * @throws Exception
     */
    @Test
    public void loginTest() throws Exception {
        this.mockMvc.perform(get("/subscribers"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    /**
     * Успешная авторизация
     *
     * @throws Exception
     */
    @Test
    public void correctLoginTest() throws Exception {
        this.mockMvc.perform(formLogin().user("maria").password("maria"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    /**
     * Не пройденная регистрация
     *
     * @throws Exception
     */
    @Test
    public void badCredentials() throws Exception {
        this.mockMvc.perform(post("/registration").requestAttr("user", ""))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}
