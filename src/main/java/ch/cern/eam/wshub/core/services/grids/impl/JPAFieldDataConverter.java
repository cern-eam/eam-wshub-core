package ch.cern.eam.wshub.core.services.grids.impl;

import ch.cern.eam.wshub.core.services.grids.entities.DataspyField;

import java.sql.Clob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class JPAFieldDataConverter {

	public static String getAsString(DataspyField dsf, Object value) {
		
		if(value == null)
			return null;
		
		String result = "";

		switch (dsf.getDataType()) {
			case "VARCHAR":
			case "MIXVARCHAR":
				result = value.toString();
				break;
			case "DECIMAL":
				result = DecimalFormat.getInstance().format(value);
				break;
			case "DATE":
				result =  new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(value).toUpperCase();
				break;
			case "CHKBOOLEAN":
				if("-".equals(value) || "false".equals(value) || "No".equals(value) || "0".equals(value))
					result = "false";
				else
					result = "true";
				break;
			case "CLOB":
			try {
				Clob clob = ((Clob)value);
				result = clob.getSubString(1, (int) clob.length());
			} catch (SQLException e) {
				result = e.getMessage();
			}
				break;
			default:
				result = value.toString();
				break;
		}
		
		return result;
	}
	
}
