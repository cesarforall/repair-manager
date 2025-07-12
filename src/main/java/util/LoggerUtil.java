package util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {
	private static final Logger logger = Logger.getLogger(LoggerUtil.class.getName());
	private static FileHandler fh;
	
	static {
		try {
			if (logger.getHandlers().length == 0) {
				Logger rootLogger = Logger.getLogger("");
	            for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
	                if (handler.getClass().getName().contains("ConsoleHandler")) {
	                    rootLogger.removeHandler(handler);
	                }
	            }
	            
	            Path logDir = Paths.get("logs");
	            if (!Files.exists(logDir)) {
	            	Files.createDirectories(logDir);
	            }
	            
	            Path logFile = logDir.resolve("vulcano-lite.log");
				
			    fh = new FileHandler(logFile.toString(), true);
			    
			    fh.setFormatter(new SimpleFormatter());
			    logger.addHandler(fh);
			    logger.setLevel(Level.ALL);
			}
		} catch (Exception e) {
			System.err.println("Logger init error: " + e.getMessage());
		}
	}
	
	public static void logError(String mensaje, Exception e) {
        logger.log(Level.SEVERE, mensaje, e);
    }
	
	public static void close() {
		if (fh != null) {
			fh.close();
			logger.removeHandler(fh);
			fh = null;
		}
	}
}
