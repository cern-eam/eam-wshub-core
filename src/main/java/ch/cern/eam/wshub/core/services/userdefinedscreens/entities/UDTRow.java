package ch.cern.eam.wshub.core.services.userdefinedscreens.entities;

import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap.DateHashMapAdapter;
import ch.cern.eam.wshub.core.services.userdefinedscreens.entities.xmlhashmap.StringHashMapAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class UDTRow implements Serializable {

    private HashMap<String, Date> dates = new LinkedHashMap<>();
    private HashMap<String, String> strings = new LinkedHashMap<>();
    private HashMap<String, BigInteger> integers = new LinkedHashMap<>();
    private HashMap<String, BigDecimal> decimals = new LinkedHashMap<>();

    @XmlJavaTypeAdapter(DateHashMapAdapter.class)
    public HashMap<String, Date> getDates() {
        return dates;
    }

    public void setDates(HashMap<String, Date> dates) {
        this.dates = dates;
    }

    @XmlJavaTypeAdapter(StringHashMapAdapter.class)
    public HashMap<String, String> getStrings() {
        return strings;
    }

    public void setStrings(HashMap<String, String> strings) {
        this.strings = strings;
    }

   // @XmlJavaTypeAdapter(XmlHashMapAdapter.class)
    public HashMap<String, BigInteger> getIntegers() {
        return integers;
    }

    public void setIntegers(HashMap<String, BigInteger> integers) {
        this.integers = integers;
    }

    //@XmlJavaTypeAdapter(XmlHashMapAdapter.class)
    public HashMap<String, BigDecimal> getDecimals() {
        return decimals;
    }

    public void setDecimals(HashMap<String, BigDecimal> decimals) {
        this.decimals = decimals;
    }

    public void addDate(String columnName, Date date) {
        dates.put(columnName, date);
    }

    public void addString(String columnName, String string) {
        strings.put(columnName, string);
    }

    public void addInteger(String columnName, BigInteger integer) {
        integers.put(columnName, integer);
    }

    public void addDecimal(String columnName, BigDecimal decimal) {
        decimals.put(columnName, decimal);
    }

    public List<String> getAllKeys() {
        List<String> collect = Stream.of(
                dates,
                strings,
                integers,
                decimals
        )
                .map(UDTRow::getSafeKeySet)
                .map(ArrayList::new)
                .flatMap(ArrayList::stream)
                .collect(Collectors.toList());
        return collect;
    }

    public static <K, V> Set<K> getSafeKeySet(Map<K, V> variable) {
        if (variable == null) return new HashSet<>();
        return variable.keySet();
    }
}
