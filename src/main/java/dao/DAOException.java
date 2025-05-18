package dao;

public class DAOException extends RuntimeException {

	DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	DAOException(String message) {
		super(message);
	}
	
}
