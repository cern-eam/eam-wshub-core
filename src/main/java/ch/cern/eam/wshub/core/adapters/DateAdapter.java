package ch.cern.eam.wshub.core.adapters;

import ch.cern.eam.wshub.core.tools.ApplicationData;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateAdapter extends XmlAdapter<String, Date> {

    public static final String[] DATE_FORMAT_STRINGS = {
            // https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            //
            "dd-MMM-yyyy HH:mm:ss",
            "dd-MMM-yyyy HH:mm",
            "dd-MMM-yyyy",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd",
            "dd/MM/yyyy HH:mm:ss",
            "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy"};

    public static final String DATE_ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Override
    public String marshal(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (ApplicationData.localizeResults) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.ENGLISH);
            return simpleDateFormat.format(cal.getTime()).toUpperCase();
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_ISO_FORMAT);
            return simpleDateFormat.format(date);
        }
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

        // Check if the passed string is the number of milliseconds from epoch
        try {
            return new Date(Long.parseLong(date));
        } catch (NumberFormatException exception) {
            // Nothing wrong here, the passes
        }

        //
        if (date.trim().equalsIgnoreCase("SYSDATE")) {
            return Calendar.getInstance().getTime();
        }

        Exception exception = null;
        for (String formatString : DATE_FORMAT_STRINGS) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(formatString, Locale.ENGLISH);
                formatter.setLenient(false);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(formatter.parse(date));
                return calendar.getTime();
            } catch (Exception e) {
                exception = e;
            }

        }

        throw exception;
    }

}