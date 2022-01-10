package ch.cern.eam.wshub.core.services.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "R5LOCALE")
@NamedNativeQuery(name = LocaleInfo.FIND_LOCALE_INFO, query = "select * from R5LOCALE where LOC_CODE = :code", resultClass = LocaleInfo.class)
public class LocaleInfo implements Serializable {
    private static final long serialVersionUID = 1149632956701915140L;
    public final static String FIND_LOCALE_INFO = "Locale.FIND_LOCALE_INFO";

    @Id
    @Column(name = "LOC_CODE")
    private String code;

    @Column(name = "LOC_DECSYM")
    private String decimalSeparator;

    @Column(name = "LOC_GRPSYM")
    private String groupSeparator;

    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    public String getGroupSeparator() {
        return groupSeparator;
    }

    public void setGroupSeparator(String groupSeparator) {
        this.groupSeparator = groupSeparator;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
