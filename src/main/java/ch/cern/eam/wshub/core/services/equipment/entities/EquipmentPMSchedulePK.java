package ch.cern.eam.wshub.core.services.equipment.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EquipmentPMSchedulePK implements Serializable {

	private String sequenceNumber;
	private String revision;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((revision == null) ? 0 : revision.hashCode());
		result = prime * result + ((sequenceNumber == null) ? 0 : sequenceNumber.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EquipmentPMSchedulePK other = (EquipmentPMSchedulePK) obj;
		if (revision == null) {
			if (other.revision != null)
				return false;
		} else if (!revision.equals(other.revision))
			return false;
		if (sequenceNumber == null) {
			if (other.sequenceNumber != null)
				return false;
		} else if (!sequenceNumber.equals(other.sequenceNumber))
			return false;
		return true;
	}
	
	
}
