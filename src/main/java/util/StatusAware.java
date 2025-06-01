package util;

import java.util.function.Consumer;

public interface StatusAware {
	void setStatusCallback(Consumer<StatusMessage> callback);
}
