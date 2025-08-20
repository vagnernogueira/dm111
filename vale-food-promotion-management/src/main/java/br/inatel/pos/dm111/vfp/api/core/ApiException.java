package br.inatel.pos.dm111.vfp.api.core;

import java.util.List;

import org.springframework.http.HttpStatus;

public class ApiException extends Exception {

	private final HttpStatus status;
	private final List<AppError> errors;

	public ApiException(HttpStatus status, List<AppError> errors) {
		this.status = status;
		this.errors = errors;
	}

	public ApiException(AppErrorCode error) {
		this.status = HttpStatus.valueOf(error.getStatus());
		this.errors = List.of(new AppError(error.getCode(), error.getMessage()));
	}

	public HttpStatus getStatus() {
		return status;
	}

	public List<AppError> getErrors() {
		return errors;
	}
}
