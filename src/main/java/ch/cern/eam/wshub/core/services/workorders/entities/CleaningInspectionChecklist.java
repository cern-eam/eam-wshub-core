package ch.cern.eam.wshub.core.services.workorders.entities;

public class CleaningInspectionChecklist {
	
	public CleaningInspectionChecklist() {
		super();
	}
	public CleaningInspectionChecklist(int sequence, String description,
			boolean yesSelected, boolean noSelected) {
		super();
		this.sequence = sequence;
		this.description = description;
		this.yesSelected = yesSelected;
		this.noSelected = noSelected;
	}
	
	private int sequence;
	private String description;
	private boolean yesSelected;
	private boolean noSelected;
	
	
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isYesSelected() {
		return yesSelected;
	}
	public void setYesSelected(boolean yesSelected) {
		this.yesSelected = yesSelected;
	}
	public boolean isNoSelected() {
		return noSelected;
	}
	public void setNoSelected(boolean noSelected) {
		this.noSelected = noSelected;
	}

	
	
}
