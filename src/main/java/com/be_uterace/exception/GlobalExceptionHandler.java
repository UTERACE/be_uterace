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
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<ResponseObject> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        WebRequest webRequest){
        ResponseObject responseObject = new ResponseObject(StatusCode.NOT_FOUND, exception.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ResponseObject> handleAPIException(APIException exception,
                                                                        WebRequest webRequest){
        ResponseObject responseObject = new ResponseObject(StatusCode.INVALID_ARGUMENT, exception.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseObject> handleBadRequestException(BadRequestException exception){
        ResponseObject responseObject = new ResponseObject(StatusCode.INVALID_ARGUMENT, exception.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }
    // global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObject> handleGlobalException(Exception exception,
                                                               WebRequest webRequest){
        ResponseObject responseObject = new ResponseObject(StatusCode.INTERNAL_SERVER_ERROR, exception.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseObject> handleAccessDeniedException(AccessDeniedException exception,
                                                                    WebRequest webRequest){
        ResponseObject responseObject = new ResponseObject(StatusCode.UNAUTHORIZED, exception.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
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

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ResponseObject> handleResourceConflictException(ResourceConflictException exception){
        ResponseObject responseObject = new ResponseObject(StatusCode.CONFLICT, exception.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseObject> handleResourceNotFoundException(AuthenticationException exception,
                                                                          WebRequest webRequest){
        ResponseObject responseObject = new ResponseObject(StatusCode.UNAUTHORIZED, exception.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseObject> badCredentialsException(AuthenticationException exception,
                                                                          WebRequest webRequest){
        ResponseObject responseObject = new ResponseObject(StatusCode.UNAUTHORIZED, exception.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
    }





    @ExceptionHandler(JWTException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseObject> JwtException(JWTException ex) {
        ErrorHolder.clearErrorMessage();
        ResponseObject responseObject = new ResponseObject(StatusCode.UNAUTHORIZED, ex.getMessage());
        return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
    }




}
