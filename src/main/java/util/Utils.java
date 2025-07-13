package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String formatEntryDate(String date) {
        if (date == null) return "";
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, inputFormatter);
        return localDate.format(outputFormatter);
    }
    
    public static String formatIntToId(String prefix, int id) {
    	String formatedId = "";    	
    	if (id < 10000) {
			formatedId = String.format("%s%05d", prefix, id);
		} else {
			formatedId = prefix + Integer.toString(id);
		}    	
    	return formatedId;
    }
}
