package com.kpmg.cacm.api.exceptions.handler;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import com.kpmg.cacm.api.dto.response.ApiErrorResponse;
import com.kpmg.cacm.api.dto.response.model.ApiErrorDTO;
import com.kpmg.cacm.api.exceptions.BadRequestException;
import com.kpmg.cacm.api.exceptions.NotAuthenticatedException;
import com.kpmg.cacm.api.exceptions.NotAuthorizedException;
import com.kpmg.cacm.api.exceptions.PreConditionFailedException;
import com.kpmg.cacm.api.exceptions.SqlGrammarException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        final List<ApiErrorDTO> errors = exception.getBindingResult().getFieldErrors()
            .stream()
            .map(error -> ApiErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(error.getField() + " : " + error.getDefaultMessage())
                .build()
            ).collect(Collectors.toList());
        exception.getBindingResult().getGlobalErrors()
            .stream()
            .map(error -> ApiErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(error.getObjectName() + " : " + error.getDefaultMessage())
                .build()
            ).forEach(errors::add);
        final ApiErrorResponse response = ApiErrorResponse.builder().errors(errors).build();
        return this.handleExceptionInternal(exception, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException exception,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        return this.handleGeneralException(HttpStatus.BAD_REQUEST, exception.getParameterName() + " parameter is missing");
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException exception,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        final StringBuilder builder = new StringBuilder();
        builder.append(exception.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        exception.getSupportedHttpMethods().forEach(t -> builder.append(t).append(' '));
        return this.handleGeneralException(HttpStatus.METHOD_NOT_ALLOWED, builder.toString());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            final HttpMediaTypeNotSupportedException exception,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        final StringBuilder builder = new StringBuilder();
        builder.append(exception.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        exception.getSupportedMediaTypes().forEach(t -> builder.append(t).append(' '));
        return this.handleGeneralException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.toString());
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    protected ResponseEntity<Object> handleConstraintViolation(
            final ConstraintViolationException exception,
            final WebRequest request) {
        final List<ApiErrorDTO> errors = exception.getConstraintViolations()
            .stream()
            .map(violation -> ApiErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(violation.getRootBeanClass().getName() + ' ' +
                    violation.getPropertyPath() + ": " +
                    violation.getMessage())
                .build()
            ).collect(Collectors.toList());
        final ApiErrorResponse response = ApiErrorResponse.builder().errors(errors).build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            final MethodArgumentTypeMismatchException exception,
            final WebRequest request) {
        final ApiErrorResponse response = ApiErrorResponse.builder()
            .errors(Collections.singletonList(ApiErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getName() + " should be of type " + exception.getRequiredType().getName())
                .build()
            )).build();
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<Object> handleBadRequest(final Exception exception, final WebRequest request) {
        return this.handleGeneralException(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
    }

    @ExceptionHandler({ PreConditionFailedException.class })
    public ResponseEntity<Object> handlePreconditionFailed(final Exception exception, final WebRequest request) {
        return this.handleGeneralException(HttpStatus.PRECONDITION_FAILED, exception.getLocalizedMessage());
    }

    @ExceptionHandler({ NotAuthorizedException.class })
    public ResponseEntity<Object> handleNotAuthorized(final Exception exception, final WebRequest request) {
        return this.handleGeneralException(HttpStatus.UNAUTHORIZED, exception.getLocalizedMessage());
    }

    @ExceptionHandler({ NotAuthenticatedException.class })
    public ResponseEntity<Object> handleNotAuthenticated(final Exception exception, final WebRequest request) {
        return this.handleGeneralException(HttpStatus.UNAUTHORIZED, exception.getLocalizedMessage());
    }

    @ExceptionHandler({ SqlGrammarException.class })
    public ResponseEntity<Object> handleSqlGrammar(final Exception exception, final WebRequest request) {
        return this.handleGeneralException(HttpStatus.PRECONDITION_FAILED, exception.getLocalizedMessage());
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDenied(final Exception exception, final WebRequest request) {
        return this.handleGeneralException(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
    }

    @ExceptionHandler({ FileNotFoundException.class })
    public ResponseEntity<Object> handleFileNotFound(final Exception exception, final WebRequest request) {
        RestExceptionHandler.log.error(exception.getLocalizedMessage(), exception);
        return this.handleGeneralException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception exception, final WebRequest request) {
        RestExceptionHandler.log.error(exception.getLocalizedMessage(), exception);
        return this.handleGeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error. Please contact support");
    }

    private ResponseEntity<Object> handleGeneralException(final HttpStatus httpStatus, final String localizedMessage) {
        final ApiErrorResponse response = ApiErrorResponse.builder()
            .errors(Collections.singletonList(ApiErrorDTO.builder()
                .code(httpStatus.value())
                .message(localizedMessage)
                .build()
            )).build();
        return new ResponseEntity<>(response, new HttpHeaders(), httpStatus);
    }
}
