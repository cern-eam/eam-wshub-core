package ch.cern.eam.wshub.core.services.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Entity implements Serializable {

    private String code;
    private String desc;
    private String organization;

    public Entity() {

    }

    public Entity(String code, String desc, String organization) {
        this.code = code;
        this.desc = desc;
        this.organization = organization;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public static Map<String, String> generateGridEntityMap(String code, String desc, String organization) {
        Map<String, String> map = new HashMap<>();
        map.put(code, "code");
        map.put(desc, "desc");
        map.put(organization, "organization");
        return map;
    }
}

