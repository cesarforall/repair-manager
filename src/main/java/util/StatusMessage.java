package util;

public class StatusMessage {
	public enum Type { INFO, ERROR }
	
	private final Type type;
	private final String message;
	
	public StatusMessage(Type type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public Type getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
}
