package controller;

public class ControllerException extends RuntimeException {
	ControllerException(String message, Throwable cause) {
		super(message, cause);
	}

	ControllerException(String message) {
		super(message);
	}
}
