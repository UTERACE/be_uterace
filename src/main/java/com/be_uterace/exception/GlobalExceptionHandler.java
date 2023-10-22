package com.be_uterace.exception;

import com.be_uterace.payload.ErrorDetails;
import com.be_uterace.utils.StatusCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // handle specific exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(false, StatusCode.NOT_FOUND, exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorDetails> handleAPIException(APIException exception,
                                                                        WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(false, StatusCode.UNAUTHORIZED, exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    // global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                               WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(false, StatusCode.INTERNAL_SERVER_ERROR, exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException exception,
                                                                    WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(false, StatusCode.NOT_FOUND, exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    ErrorDetails handleAuthenticationException(Exception ex) {
//        return new ErrorDetails(new Date(), ex.getMessage(), "username or password is incorrect.");
//    }
//
//    @ExceptionHandler(AccountStatusException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    ErrorDetails handleAccountStatusException(AccountStatusException ex) {
//        return new ErrorDetails(new Date(), ex.getMessage(), "User account is abnormal.");
//    }
//
////    @ExceptionHandler(InvalidBearerTokenException.class)
////    @ResponseStatus(HttpStatus.UNAUTHORIZED)
////    ErrorDetails handleInvalidBearerTokenException(InvalidBearerTokenException ex) {
////        return new ErrorDetails(false, ex.getMessage(), "The access token provided is expired, revoked, malformed, or invalid for other reasons.");
////    }
//
//
//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    ErrorDetails handleAccessDeniedException(AccessDeniedException ex) {
//        return new ErrorDetails(new Date(), ex.getMessage(), "No permission.");
//    }
//
//    @ExceptionHandler(NoHandlerFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    ErrorDetails handleAccessDeniedException(NoHandlerFoundException ex) {
//        return new ErrorDetails(new Date(), ex.getMessage(), "This API endpoint is not found.");
//    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    ErrorDetails handleOtherException(Exception ex) {
//        return new ErrorDetails(new Date(), ex.getMessage(), "A server internal error occurs.");
//    }



}
