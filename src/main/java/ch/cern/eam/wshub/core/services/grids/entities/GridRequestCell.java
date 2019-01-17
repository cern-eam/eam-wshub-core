package ch.cern.eam.wshub.core.services.grids.entities;


import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class GridRequestCell implements Serializable {

	public GridRequestCell(String col, String content, int order, String tagname) {
		super();
		this.col = col;
		this.content = content;
		this.order = order;
		this.tag = tagname;
	}
	
	public GridRequestCell(String col, String content, int order) {
		super();
		this.col = col;
		this.content = content;
		this.order = order;
	}
	
	public GridRequestCell(String col, String content) {
		super();
		this.col = col;
		this.content = content;
	}
	
	private static final long serialVersionUID = 7833250910481494524L;
	
	@XmlAttribute(name = "n")
	private String col;
	
	@XmlAttribute(name = "t")
	private String tag;
	
	@XmlAttribute(name = "order")
	private int order;
	
	@XmlValue
	private String content;
	
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "GridRequestCell [col=" + col + ", tag=" + tag + ", order=" + order + ", content=" + content + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((col == null) ? 0 : col.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + order;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
		GridRequestCell other = (GridRequestCell) obj;
		if (col == null) {
			if (other.col != null)
				return false;
		} else if (!col.equals(other.col))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (order != other.order)
			return false;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}
	
}
