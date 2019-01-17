package ch.cern.eam.wshub.core.tools;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanTFConverter implements AttributeConverter<Boolean, String>{
    @Override
    public String convertToDatabaseColumn(Boolean value) {
        if (Boolean.TRUE.equals(value)) {
            return "+";
        } else {
            return "-";
        }
    }
    @Override
    public Boolean convertToEntityAttribute(String value) {
        return "+".equals(value);
    }
}