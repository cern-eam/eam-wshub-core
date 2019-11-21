package ch.cern.eam.wshub.core.services.userdefinedscreens.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UDTRow implements Serializable {

    private Map<String, Date> dates = new HashMap<>();
    private Map<String, String> strings = new HashMap<>();
    private Map<String, BigInteger> integers = new HashMap<>();
    private Map<String, BigDecimal> decimals = new HashMap<>();

    public Map<String, Date> getDates() {
        return dates;
    }

    public void setDates(Map<String, Date> dates) {
        this.dates = dates;
    }

    public Map<String, String> getStrings() {
        return strings;
    }

    public void setStrings(Map<String, String> strings) {
        this.strings = strings;
    }

    public Map<String, BigInteger> getIntegers() {
        return integers;
    }

    public void setIntegers(Map<String, BigInteger> integers) {
        this.integers = integers;
    }

    public Map<String, BigDecimal> getDecimals() {
        return decimals;
    }

    public void setDecimals(Map<String, BigDecimal> decimals) {
        this.decimals = decimals;
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
