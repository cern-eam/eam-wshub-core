package ch.cern.eam.wshub.core.services.material.entities;

import java.io.Serializable;

public class PartSubstitute implements Serializable {

	private String partA;
	private String partB;
	private String condition;
	private String fullyCompatible;
	
	public String getPartA() {
		return partA;
	}
	public void setPartA(String partA) {
		this.partA = partA;
	}
	public String getPartB() {
		return partB;
	}
	public void setPartB(String partB) {
		this.partB = partB;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getFullyCompatible() {
		return fullyCompatible;
	}
	public void setFullyCompatible(String fullyCompatible) {
		this.fullyCompatible = fullyCompatible;
	}
	
	@Override
	public String toString() {
		return "PartSubstitute ["
				+ (partA != null ? "partA=" + partA + ", " : "")
				+ (partB != null ? "partB=" + partB + ", " : "")
				+ (condition != null ? "condition=" + condition + ", " : "")
				+ (fullyCompatible != null ? "fullyCompatible="
						+ fullyCompatible : "") + "]";
	}
	
}
