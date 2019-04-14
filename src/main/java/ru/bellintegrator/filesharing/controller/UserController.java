package ru.bellintegrator.filesharing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bellintegrator.filesharing.model.RoleName;
import ru.bellintegrator.filesharing.model.User;
import ru.bellintegrator.filesharing.service.UserService;

import java.util.Map;

/**
 * Контроллер пользователей
 */
@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Отобразить список всех пользователей
     *
     * @param model модель
     * @return страница со списком пользователей
     */
    @GetMapping("user")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    /**
     * Отобразить форму редактирования пользователя
     * Доступно только администратору
     *
     * @param user пользователь
     * @param model модель
     * @return страница с формой редактирования пользователя
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("user/{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", RoleName.values());

        return "userEdit";
    }

    /**
     * Изменение данных пользователя
     * Доступно только администратору
     *
     * @param username имя пользователя
     * @param form форма
     * @param user пользователь
     * @return страница со списком пользователей
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("user")
    public String editUser(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {
        userService.updateUser(username, form, user);

        return "redirect:/user";
    }

    /**
     * Отобразить страницу со статистикой
     * Доступно только аналитику
     *
     * @param model модель
     * @return страница со статистикой
     */
    @PreAuthorize("hasAuthority('ANALYST')")
    @GetMapping("/statistic")
    public String showStatistic(Map<String, Object> model) {
        model.put("usersCount", userService.userCount());
        return "statistic";
    }
}