package com.be_uterace.exception;

import com.be_uterace.payload.ErrorDetails;
import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.utils.StatusCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
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

//    @ExceptionHandler(value = {UserServiceException.class})
//    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {
//        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI().toString();
//        ExceptionMessage exceptionMessage = new ExceptionMessage(ex.getMessage(), requestUri);
//        return new ResponseEntity<>(exceptionMessage, new HttpHeaders(), ex.getStatus());
//    }
//    @ExceptionHandler(value = {ExpiredJwtException.class})
//    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
//        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI().toString();
//        ExceptionMessage exceptionMessage = new ExceptionMessage(ex.getMessage(), requestUri);
//        return new ResponseEntity<>(exceptionMessage, new HttpHeaders(), HttpStatus.FORBIDDEN);
//    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseObject> handleResourceNotFoundException(AuthenticationException exception,
                                                                          WebRequest webRequest){
        ResponseObject responseObject = new ResponseObject(StatusCode.UNAUTHORIZED, exception.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseObject> handleMalformedJwtException(MalformedJwtException ex) {
        ResponseObject responseObject = new ResponseObject(StatusCode.UNAUTHORIZED, "Invalid JWT token: " + ex.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expired JWT token: " + ex.getMessage());
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported JWT token: " + ex.getMessage());
    }




}
