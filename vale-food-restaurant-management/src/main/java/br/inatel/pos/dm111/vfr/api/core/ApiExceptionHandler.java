package br.inatel.pos.dm111.vfr.api.core;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ApiException.class)
	protected ResponseEntity<List<AppError>> handleEntity(ApiException exception, WebRequest request) {
		return new ResponseEntity<>(exception.getErrors(), exception.getStatus());
	}

}
