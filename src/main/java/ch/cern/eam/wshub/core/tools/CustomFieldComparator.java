package ch.cern.eam.wshub.core.tools;

import net.datastream.schemas.mp_fields.CUSTOMFIELD;

import java.util.Comparator;


public class CustomFieldComparator implements Comparator<CUSTOMFIELD> {

	@Override
	public int compare(CUSTOMFIELD o1, CUSTOMFIELD o2) {
		return (int)(o1.getIndex() - o2.getIndex());
	}

}
