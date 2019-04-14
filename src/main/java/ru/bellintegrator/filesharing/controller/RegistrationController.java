package ru.bellintegrator.filesharing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.bellintegrator.filesharing.exception.ServiceException;
import ru.bellintegrator.filesharing.model.User;
import ru.bellintegrator.filesharing.service.RegistrationService;

import javax.validation.Valid;
import java.util.Map;

/**
 * Контроллер для регистрации пользователей
 */
@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Отобразить форму регистрации
     *
     * @return страница с формой рестрации
     */
    @GetMapping("/registration")
    public String showRegistrationForm() {
        return "registration";
    }

    /**
     * Добавление нового пользователя в систему
     *
     * @param user пользователь
     * @param bindingResult интерфейс для регистрации ошибок валидации
     * @param model модель
     * @return страница с формой рестрации
     */
    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        try {
            registrationService.addUser(user);
            model.addAttribute("info", "Activate your account through the link from your e-mail");
        } catch (ServiceException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "registration";
    }

    /**
     * Активация учётной записи
     *
     * @param model модель
     * @param code регистрационный код
     * @return страница с формой авторизации
     */
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        try {
            registrationService.activateUser(code);
            model.addAttribute("info", "User successfully activated");
        } catch (ServiceException e) {
            model.addAttribute("message", e.getMessage());
        }
        return "login";
    }
}
