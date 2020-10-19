package ch.cern.eam.wshub.core.services.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Pair implements Serializable {

	private String code;
	private String desc;

	public Pair() {}

	public Pair(String code, String desc) {
		super();
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		if (desc == null) {
			return code;
		}
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "Pair [" + (code != null ? "code=" + code + ", " : "") + (desc != null ? "desc=" + desc : "") + "]";
	}

	public static Map<String, String> generateGridPairMap(String code, String desc) {
		Map<String, String> map = new HashMap<>();
		map.put(code, "code");
		map.put(desc, "desc");
		return map;
	}

	@Override
	public boolean equals(Object other){
		if(other instanceof Pair){
			Pair otherPair = (Pair) other;
			boolean codeEq = otherPair.getCode() == code ||
					otherPair.getCode() != null && code != null && otherPair.getCode().equals(code);
			boolean descEq = otherPair.getDesc() == desc ||
					otherPair.getDesc()  != null && desc != null && otherPair.getDesc().equals(desc);
			return codeEq && descEq;
		}
		return false;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + code.hashCode();
		result = prime * result + desc.hashCode();
		return result;
	}
}
