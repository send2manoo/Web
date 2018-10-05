/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.qlc.fieldsense.user.service;
import com.qlc.fieldsense.utils.FieldSenseValidationErrorDTO;
import java.util.Locale;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Ramesh .
 * @date 19-02-2014 .
 * @purpouse To handle user related validations .
 */
@ControllerAdvice
public class UserErrorHandler {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("AccountErrorHandler");
    private MessageSource messageSource;

    /**
     *
     * @param messageSource
     */
    @Autowired
    public UserErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public FieldSenseValidationErrorDTO processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return processFieldErrors(fieldErrors);
    }

    private FieldSenseValidationErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
        FieldSenseValidationErrorDTO dto = new FieldSenseValidationErrorDTO();

        for (FieldError fieldError : fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        return dto;
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
        return localizedErrorMessage;
    }
}
