package ru.bellintegrator.filesharing.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Обработчик ошибок
 */
@ControllerAdvice
public class ApplicationExceptionHandler {

    /**
     * Обработчик ошибок ненайденных сущностей
     * @param e ошибка
     * @return страница с текстом ошибки
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleWeatherException(EntityNotFoundException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", e.getMessage());
        mav.setViewName("error");
        return mav;
    }
}
