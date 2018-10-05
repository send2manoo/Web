/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.travels.service;

import com.qlc.fieldsense.utils.FieldSenseValidationErrorDTO;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author anuja
 */
@ControllerAdvice
public class TravelsErrorHandler {

    /**
     *
     */
    public static final Logger log4jLog = Logger.getLogger("UserKeyErrorHandler");
    private MessageSource messageSource;

    /**
     *
     * @param messageSource
     */
    @Autowired
    public TravelsErrorHandler(MessageSource messageSource) {
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
