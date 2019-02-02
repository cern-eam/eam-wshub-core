package ch.cern.eam.wshub.core.tools;

import org.openapplications.oagis_segments.AMOUNT;
import org.openapplications.oagis_segments.DATETIME;
import org.openapplications.oagis_segments.DATETIMEqual;
import org.openapplications.oagis_segments.QUANTITY;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataTypeTools {

    private Tools tools;

    public DataTypeTools(Tools tools) {
        this.tools = tools;
    }
    //
    // DATES
    //

    private Calendar tryParse(String date) {
        if (date.trim().toUpperCase().equals("SYSDATE")) {
            return Calendar.getInstance();
        }
        String[] formatStrings = { "dd-MMM-yyyy HH:mm", "dd-MMM-yyyy", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
                "yyyy-MM-dd", "dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy" };

        for (String formatString : formatStrings) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(formatString);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(formatter.parse(date));
                return calendar;
            } catch (Exception e) {
            }
        }

        return null;
    }

    public DATETIME encodeInforDate(Date dateValue, String dateLabel) throws InforException {
        if (dateValue.getTime() == 0l) {
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

    public DATETIME formatDate(String dateValue, String dateLabel) throws InforException {
        if (dateValue == null || dateValue.trim().equals("")) {
            return null;
        }

        Calendar calendar = tryParse(dateValue);

        if (calendar == null) {
            throw tools.generateFault((dateLabel + " has invalid format. Please change it to dd-MMM-yyyy [HH:mm]"));
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
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(cal.getTime()).toUpperCase();
    }

    public String retrieveDate(DATETIME inforDateTime) {
        return retrieveDate(inforDateTime, "dd-MMM-yyyy HH:mm");
    }

    public Date decodeInforDate(DATETIME inforDateTime) {
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
    // NUMBERS:
    //
    public QUANTITY encodeQuantity(String numberValue, String numberLabel) throws InforException {
        if (numberValue == null || numberValue.trim().equals("")) {
            return null;
        }
        numberValue = numberValue.trim();
        QUANTITY quantity = new QUANTITY();
        try {
            if (numberValue.startsWith("-")) {
                quantity.setSIGN("-");
                numberValue = numberValue.substring(1);
            } else {
                quantity.setSIGN("+");
            }
            numberValue = numberValue.replace(",", "");
            int index = numberValue.indexOf(".");
            numberValue = numberValue.replace(".", "");

            quantity.setNUMOFDEC(BigInteger.valueOf(index < 0 ? 0 : numberValue.length() - index));
            quantity.setVALUE(new BigDecimal(numberValue));
            quantity.setUOM("default");
            quantity.setQualifier("OTHER");
        } catch (NumberFormatException e) {
            throw tools.generateFault(numberLabel + " couldn't be parsed");
        }
        return quantity;
    }

    public String decodeQuantity(QUANTITY inforNumber) {
        if (inforNumber != null) {
            BigDecimal bc = inforNumber.getVALUE().divide(
                    new BigDecimal(Math.pow(10, inforNumber.getNUMOFDEC().intValue())), 6, RoundingMode.HALF_UP);

            if (inforNumber.getSIGN() != null && inforNumber.getSIGN().equals("-")) {
                bc = bc.negate();
            }
            return bc.stripTrailingZeros().toPlainString();
        } else {
            return null;
        }
    }

    public AMOUNT encodeAmount(String numberValue, String numberLabel) throws InforException {
        if (numberValue == null || numberValue.trim().equals("")) {
            return null;
        }
        AMOUNT amount = new AMOUNT();
        try {
            if (numberValue.startsWith("-")) {
                amount.setSIGN("-");
                numberValue = numberValue.substring(1);
            } else {
                amount.setSIGN("+");
            }
            numberValue = numberValue.replace(",", "");
            int index = numberValue.indexOf(".");
            numberValue = numberValue.replace(".", "");

            amount.setNUMOFDEC(BigInteger.valueOf(index < 0 ? 0 : numberValue.length() - index));
            amount.setVALUE(new BigDecimal(numberValue));
            amount.setCURRENCY("default");
            amount.setDRCR("C");
            amount.setQualifier("OTHER");
        } catch (NumberFormatException e) {
            throw tools.generateFault(numberLabel + " couldn't be parsed.");
        }
        return amount;
    }

    public String decodeAmount(AMOUNT inforNumber) {
        if (inforNumber != null) {
            BigDecimal bc = inforNumber.getVALUE().divide(
                    new BigDecimal(Math.pow(10, inforNumber.getNUMOFDEC().intValue())), 6, RoundingMode.HALF_UP);
            if (inforNumber.getSIGN() != null && inforNumber.getSIGN().equals("-")) {
                bc = bc.negate();
            }
            return bc.stripTrailingZeros().toPlainString();
        } else {
            return null;
        }
    }

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

    public BigInteger encodeBigInteger(String value, String valueLabel) throws InforException {
        if (value == null || value.trim().equals("")) {
            return null;
        }
        BigInteger bigIntegerValue = null;
        try {
            bigIntegerValue = new BigInteger(value);
        } catch (NumberFormatException e) {
            throw tools.generateFault(valueLabel + " couldn't be parsed.");
        }
        return bigIntegerValue;
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
    public String encodeBoolean(String value, BooleanType returnType) {
        // Return boolean
        String resultStr = null;
        // If value is null, return "false"
        if (value == null)
            value = "-";
        // Check the value
        boolean result = value.equalsIgnoreCase("+")
                        || value.equalsIgnoreCase("1")
                        || value.equalsIgnoreCase("true")
                        || value.equalsIgnoreCase("yes");
        // Assign the result according to the type
        switch (returnType) {
            case TRUE_FALSE:
                resultStr = result ? "true" : "false";
                break;
            case YES_NO:
                resultStr = result ? "yes" : "no";
                break;
            case ONE_ZERO:
                resultStr = result ? "one" : "zero";
                break;
            case PLUS_MINUS:
                resultStr = result ? "+" : "-";
                break;
        }
        // Return the result
        return resultStr;
    }

    public boolean isTrueValue(String value) {
        return value != null && value.trim().toUpperCase().equals("TRUE");
    }

    public boolean isFalseValue(String value) {
        return value == null || value.trim().toUpperCase().equals("FALSE");
    }

    /**
     * Decode a boolean
     *
     * @param value
     *            Value to be converted as a boolean
     * @return Will return the boolean in the string form
     */
    public String decodeBoolean(String value) {
        return encodeBoolean(value, BooleanType.TRUE_FALSE);
    }

    public boolean isEmpty(String value) {
        return value == null || value.trim().equals("");
    }
}
