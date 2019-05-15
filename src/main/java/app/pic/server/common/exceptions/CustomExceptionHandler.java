package app.pic.server.common.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@SuppressWarnings({ "unchecked", "rawtypes" })
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{

    private final String INCORRECT_REQUEST = "INCORRECT_REQUEST";
    private final String BAD_REQUEST = "BAD_REQUEST";
    private final String ValidationFailed = "Validation Failed";
    private final String ServerError = "Server Error";

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions( Exception ex, WebRequest request ){
        List<String> details = new ArrayList<>();
        details.add( ex.getLocalizedMessage() );
        ErrorResponse error = new ErrorResponse( ServerError, details );
        return new ResponseEntity( error, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException( RecordNotFoundException ex, WebRequest request ){
        List<String> details = new ArrayList<>();
        details.add( ex.getLocalizedMessage() );
        ErrorResponse error = new ErrorResponse( BAD_REQUEST, details );
        return new ResponseEntity(error, HttpStatus.NOT_FOUND );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid( MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request ){
        List<String> details = new ArrayList<>();
        for( ObjectError error : ex.getBindingResult().getAllErrors() ){
            details.add( error.getDefaultMessage() );
        }
        ErrorResponse error = new ErrorResponse( ValidationFailed, details );
        return new ResponseEntity( error, HttpStatus.BAD_REQUEST );
    }
}
