package ch.cern.eam.wshub.core.tools;

import ch.cern.eam.wshub.core.annotations.BooleanType;
import org.openapplications.oagis_segments.AMOUNT;
import org.openapplications.oagis_segments.DATETIME;
import org.openapplications.oagis_segments.DATETIMEqual;
import org.openapplications.oagis_segments.QUANTITY;
import static ch.cern.eam.wshub.core.tools.Tools.generateFault;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataTypeTools {

    private Tools tools;
    private static String[] formatStrings = { "dd-MMM-yyyy HH:mm", "dd-MMM-yyyy", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy" };

    public static final Integer NULLIFY_VALUE =  Integer.MAX_VALUE;

    public DataTypeTools(Tools tools) {
        this.tools = tools;
    }

    //
    // DATES
    //
    public static Calendar convertStringToCalendar(String date) {
        if (date == null || date.trim().equals("")) {
            return null;
        }

        if (date.trim().toUpperCase().equals("SYSDATE")) {
            return Calendar.getInstance();
        }

        for (String formatString : formatStrings) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(formatString, Locale.ENGLISH);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(formatter.parse(date));
                return calendar;
            } catch (Exception e) {
            }
        }

        return null;
    }

    public static Date convertStringToDate(String date) {
        Calendar calendar = convertStringToCalendar(date);
        if (calendar != null) {
            return calendar.getTime();
        } else {
            return null;
        }
    }

    public static DATETIME encodeInforDate(Date dateValue, String dateLabel) throws InforException {
        if (dateValue.getTime() == 0L) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateValue);

        DATETIME inforDateTime = new DATETIME();
        inforDateTime.setSUBSECOND(BigInteger.valueOf(0));
        inforDateTime.setSECOND(BigInteger.valueOf(calendar.get(Calendar.SECOND)));
        inforDateTime.setMINUTE(BigInteger.valueOf(calendar.get(Calendar.MINUTE)));
        inforDateTime.setHOUR(BigInteger.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        inforDateTime.setDAY(BigInteger.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        inforDateTime.setMONTH(BigInteger.valueOf(calendar.get(Calendar.MONTH) + 1));

        try {
            DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
            XMLGregorianCalendar yearCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            yearCalendar.setYear(calendar.get(Calendar.YEAR));
            inforDateTime.setYEAR(yearCalendar);
        } catch (Exception e) {

        }

        inforDateTime.setQualifier(DATETIMEqual.OTHER);
        inforDateTime.setTIMEZONE("UTC");
        return inforDateTime;
    }

    public static DATETIME formatDate(String dateValue, String dateLabel) throws InforException {
        if (dateValue == null || dateValue.trim().equals("")) {
            return null;
        }

        Calendar calendar = convertStringToCalendar(dateValue);

        if (calendar == null) {
            throw Tools.generateFault((dateLabel + " has invalid format. Please change it to dd-MMM-yyyy [HH:mm]"));
        }

        DATETIME inforDateTime = new DATETIME();
        inforDateTime.setSUBSECOND(BigInteger.valueOf(0));
        inforDateTime.setSECOND(BigInteger.valueOf(calendar.get(Calendar.SECOND)));
        inforDateTime.setMINUTE(BigInteger.valueOf(calendar.get(Calendar.MINUTE)));
        inforDateTime.setHOUR(BigInteger.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        inforDateTime.setDAY(BigInteger.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        inforDateTime.setMONTH(BigInteger.valueOf(calendar.get(Calendar.MONTH) + 1));

        try {
            DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
            XMLGregorianCalendar yearCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            yearCalendar.setYear(calendar.get(Calendar.YEAR));
            inforDateTime.setYEAR(yearCalendar);
        } catch (Exception e) {

        }

        inforDateTime.setQualifier(DATETIMEqual.OTHER);
        inforDateTime.setTIMEZONE("UTC");
        return inforDateTime;
    }

    public String retrieveDate(DATETIME inforDateTime, String dateFormat) {
        Calendar cal = Calendar.getInstance();
        cal.set(inforDateTime.getYEAR().getYear(), inforDateTime.getMONTH().intValue() - 1,
                inforDateTime.getDAY().intValue(), inforDateTime.getHOUR().intValue(),
                inforDateTime.getMINUTE().intValue(), inforDateTime.getSECOND().intValue());
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        return formatter.format(cal.getTime()).toUpperCase();
    }

    public String retrieveDate(DATETIME inforDateTime) {
        return retrieveDate(inforDateTime, "dd-MMM-yyyy HH:mm");
    }

    public static Date decodeInforDate(DATETIME inforDateTime) {
        if (inforDateTime == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(inforDateTime.getYEAR().getYear(), inforDateTime.getMONTH().intValue() - 1,
                inforDateTime.getDAY().intValue(), inforDateTime.getHOUR().intValue(),
                inforDateTime.getMINUTE().intValue(), inforDateTime.getSECOND().intValue());
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //
    // QUANTITY
    //
    public static QUANTITY encodeQuantity(BigDecimal numberValue, String numberLabel) throws InforException {
        if (numberValue == null ) {
            return null;
        }

        numberValue = numberValue.stripTrailingZeros();
        int numberOfDec = Math.max(0, numberValue.scale());
        numberValue = numberValue.scaleByPowerOfTen(numberOfDec);
        numberValue = numberValue.setScale(0);

        QUANTITY quantity = new QUANTITY();
        try {
            quantity.setSIGN(numberValue.signum() < 0 ? "-" : "+");
            quantity.setNUMOFDEC(BigInteger.valueOf(numberOfDec));
            quantity.setVALUE(numberValue.abs());
            quantity.setQualifier("OTHER");
            quantity.setUOM("default");
        } catch (NumberFormatException e) {
            throw generateFault(numberLabel + " couldn't be parsed");
        }
        return quantity;
    }

    public static BigDecimal decodeQuantity(QUANTITY inforNumber) {
        if (inforNumber != null) {
            BigDecimal bc = inforNumber.getVALUE().divide(
                    new BigDecimal(Math.pow(10, inforNumber.getNUMOFDEC().intValue())), 6, RoundingMode.HALF_UP);

            if (inforNumber.getSIGN() != null && inforNumber.getSIGN().equals("-")) {
                bc = bc.negate();
            }
            return bc.stripTrailingZeros();
        } else {
            return null;
        }
    }

    //
    // AMOUNT
    //
    public static AMOUNT encodeAmount(BigDecimal numberValue, String numberLabel) throws InforException {
        if (numberValue == null) {
            return null;
        }

        numberValue = numberValue.stripTrailingZeros();
        int numberOfDec = Math.max(0, numberValue.scale());
        numberValue = numberValue.scaleByPowerOfTen(numberOfDec);
        numberValue = numberValue.setScale(0);

        AMOUNT amount = new AMOUNT();
        try {
            amount.setSIGN(numberValue.signum() < 0 ? "-" : "+");
            amount.setNUMOFDEC(BigInteger.valueOf(numberOfDec));
            amount.setVALUE(numberValue.abs());
            amount.setCURRENCY("default");
            amount.setDRCR("C");
            amount.setQualifier("OTHER");
        } catch (NumberFormatException e) {
            throw generateFault(numberLabel + " couldn't be parsed.");
        }
        return amount;
    }

    public static BigDecimal decodeAmount(AMOUNT inforNumber) {
        if (inforNumber != null) {
            BigDecimal bc = inforNumber.getVALUE().divide(
                    new BigDecimal(Math.pow(10, inforNumber.getNUMOFDEC().intValue())), 6, RoundingMode.HALF_UP);
            if (inforNumber.getSIGN() != null && inforNumber.getSIGN().equals("-")) {
                bc = bc.negate();
            }
            return bc;
        } else {
            return null;
        }
    }

    //
    // LONG
    //
    public long encodeLong(String longValue, String longLabel) throws InforException {
        long value;
        try {
            value = Long.valueOf(longValue);
        } catch (NumberFormatException e) {
            throw tools.generateFault(longLabel + " couldn't be parsed.");
        }
        return value;
    }

    public double encodeDouble(String doubleValue, String longLabel) throws InforException {
        double value;
        try {
            value = Double.valueOf(doubleValue);
        } catch (NumberFormatException e) {
            throw tools.generateFault(longLabel + " couldn't be parsed.");
        }
        return value;
    }

    //
    // BIG DECIMAL
    //
    public static BigDecimal encodeBigDecimal(String stringValue, String valueLabel) throws InforException {
        if (stringValue == null || stringValue.trim().equals("")) {
            return null;
        }
        // Remove commas used as the thousands separator
        stringValue = stringValue.replace(",", "");
        BigDecimal bigDecimalValue = null;
        try {
            bigDecimalValue = new BigDecimal(stringValue);
        } catch (NumberFormatException e) {
            throw Tools.generateFault(valueLabel + " couldn't be parsed.");
        }
        return bigDecimalValue;
    }

    public static String decodeBigDecimal(BigDecimal bigDecimalValue) {
        return bigDecimalValue == null ? null : bigDecimalValue.toPlainString();
    }

    //
    // BIG INTEGER
    //
    public static BigInteger encodeBigInteger(String stringValue, String valueLabel) throws InforException {
        if (stringValue == null || stringValue.trim().equals("")) {
            return null;
        }
        // Remove commas used as the thousands separator
        stringValue = stringValue.replace(",", "");
        BigInteger bigIntegerValue = null;
        try {
            bigIntegerValue = new BigInteger(stringValue);
        } catch (NumberFormatException e) {
            throw Tools.generateFault(valueLabel + " couldn't be parsed.");
        }
        return bigIntegerValue;
    }

    public static String decodeBigInteger(BigInteger bigIntegerValue) {
        return String.valueOf(bigIntegerValue);
    }

    /**
     * Encodes the boolean according to the type received
     *
     * @param value
     *            Boolean received
     * @param returnType
     *            Type of return that should be used
     * @return String with the type of return received representing the boolean
     */
    public static String encodeBoolean(Boolean value, BooleanType returnType) {
        // Return boolean
        String resultStr = null;
        // If value is null, return "false"
        if (value == null)
            value = false;
        // Assign the result according to the type
        switch (returnType) {
            case TRUE_FALSE:
                resultStr = value ? "true" : "false";
                break;
            case YES_NO:
                resultStr = value ? "yes" : "no";
                break;
            case ONE_ZERO:
                resultStr = value ? "one" : "zero";
                break;
            case PLUS_MINUS:
                resultStr = value ? "+" : "-";
                break;
        }
        // Return the result
        return resultStr;
    }

    /**
     * Decode a boolean
     *
     * @param value
     *            Value to be converted as a boolean
     * @return Will return the boolean in the string form
     */
    public static Boolean decodeBoolean(String value) {
        return "true".equalsIgnoreCase(value) ||
                "yes".equalsIgnoreCase(value) ||
                "1".equalsIgnoreCase(value) ||
                "+".equalsIgnoreCase(value) ||
                "-1".equalsIgnoreCase(value);
    }

    public static boolean isTrueValue(String value) {
        return value != null && value.trim().toUpperCase().equals("TRUE");
    }

    public static boolean isFalseValue(String value) {
        return value == null || value.trim().toUpperCase().equals("FALSE");
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().equals("");
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public String decodeBoolean(Boolean value) {
        if (value == null) {
            return null;
        }
        if (value) {
            return "true";
        } else {
            return "false";
        }
    }
}
