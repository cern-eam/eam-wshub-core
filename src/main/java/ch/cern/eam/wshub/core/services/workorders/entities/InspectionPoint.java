package ch.cern.eam.wshub.core.services.workorders.entities;


public class InspectionPoint {

	public int sumYes;
	public int sumNo;
	public int sumTotal;
	public int threshold;
	@Override
	public String toString() {
		return "InspectionPoint [sumYes=" + sumYes + ", sumNo=" + sumNo
				+ ", sumTotal=" + sumTotal + ", threshold=" + threshold + "]";
	}
	


	
		
}
