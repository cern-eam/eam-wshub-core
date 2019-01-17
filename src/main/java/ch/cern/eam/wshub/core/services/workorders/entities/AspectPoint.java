package ch.cern.eam.wshub.core.services.workorders.entities;

import java.util.Arrays;

public class AspectPoint implements Comparable<AspectPoint>{

	private String aspectCode;
	private String pointCode;
	private String pointType;
	public String getAspectCode() {
		return aspectCode;
	}
	public void setAspectCode(String aspectCode) {
		this.aspectCode = aspectCode;
	}
	public String getPointCode() {
		return pointCode;
	}
	public void setPointCode(String pointCode) {
		this.pointCode = pointCode;
	}
	public String getPointType() {
		return pointType;
	}
	public void setPointType(String pointType) {
		this.pointType = pointType;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((aspectCode == null) ? 0 : aspectCode.hashCode());
		result = prime * result + ((pointCode == null) ? 0 : pointCode.hashCode());
		result = prime * result + ((pointType == null) ? 0 : pointType.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AspectPoint other = (AspectPoint) obj;
		if (aspectCode == null) {
			if (other.aspectCode != null)
				return false;
		} else if (!aspectCode.equals(other.aspectCode))
			return false;
		if (pointCode == null) {
			if (other.pointCode != null)
				return false;
		} else if (!pointCode.equals(other.pointCode))
			return false;
		if (pointType == null) {
			if (other.pointType != null)
				return false;
		} else if (!pointType.equals(other.pointType))
			return false;
		return true;
	}


    public int getOrder(String pointType1, String pointType2)
    {
        String[] pointTypes = { "VOLU", "SOLC", "MCP", "SOLT", "HUIS", "LAV", "WC", "URI", "DOU", "AUX", "EQFI", "TSH", "TSV", "SGS", "POUB", "BUR", "TAB", "TEL", "INT", "LAMP", "CEN" };
        try
        {
            int pt1 = Arrays.asList(pointTypes).indexOf(pointType1);
            int pt2 = Arrays.asList(pointTypes).indexOf(pointType2);

            if (pt1 == pt2)
            {
                return 0;
            }
            if (pt1 < pt2)
            {
                return -1;
            }
            if (pt1 > pt2)
            {
                return 1;
            }
            return 0;
        }
        catch (Exception e)
        {
            return 0;
        }


    }

	public int compareTo(AspectPoint ap2) {
        if (getOrder(this.pointType, ap2.getPointType()) < 0)
        {
            return -1;
        }
        if (getOrder(this.pointType, ap2.getPointType()) > 0)
        {
            return 1;
        }
        if (getOrder(this.pointType, ap2.getPointType()) == 0)
        {
            if (this.aspectCode.compareTo(ap2.getAspectCode()) < 0)
            {
                return -1;
            }
            if (this.aspectCode.compareTo(ap2.getAspectCode()) > 0)
            {
                return 1;
            }
            if (this.aspectCode.compareTo(ap2.getAspectCode()) == 0)
            {
                return 0;
            }
        }
        return 0;
	}

}
