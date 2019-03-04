package ch.cern.eam.wshub.core.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateAdapter extends XmlAdapter<String, Date> {

    @Override
    public String marshal(Date date) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.ENGLISH);
		return formatter.format(cal.getTime()).toUpperCase();
    }

    @Override
    public Date unmarshal(String date) throws Exception {
    	//
    	if (date == null) {
    		return null;
    	}
    	// 
    	if (date.trim().equals("")) {
    		return new Date(0L);
    	}
    	//
		if (date.trim().equalsIgnoreCase("SYSDATE")) {
			return Calendar.getInstance().getTime();
		}
		String[] formatStrings = {"dd-MMM-yyyy HH:mm", "dd-MMM-yyyy", "yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm", "yyyy-MM-dd", "dd/MM/yyyy HH:mm:ss","dd/MM/yyyy"};

		Exception exception = null;
		for (String formatString : formatStrings)
		{
			try
			{
				SimpleDateFormat formatter = new SimpleDateFormat(formatString, Locale.ENGLISH);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(formatter.parse(date));
				return calendar.getTime();
			}
			catch (Exception e) {
				exception = e;
			}
			
		}
		
		throw exception;
    }


    
}