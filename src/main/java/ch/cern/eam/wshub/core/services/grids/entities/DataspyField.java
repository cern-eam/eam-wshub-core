package ch.cern.eam.wshub.core.services.grids.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="r5queryfield")
@NamedNativeQueries({
	@NamedNativeQuery(name = DataspyField.GETDATASPYFIELDS,  
					 query = "select n.DQF_FIELDID, n.DQF_DDSPYID, n.DQF_COLUMNORDER, ddf_datatype, gfd_tagname  "
							+" from  r5queryfield n  "
							+" join  r5dddataspy on dqf_ddspyid = dds_ddspyid  "
							+" join  r5grid on dds_gridid = grd_gridid  "
							+" join  r5gridfield on n.dqf_fieldid = gfd_fieldid and dds_gridid = gfd_gridid  "
							+" join  r5ddfield on ddf_fieldid = gfd_fieldid "
							+" where dqf_ddspyid = :dataspyid and dqf_viewtype = :requestType  "
							,
				resultClass = DataspyField.class),
	@NamedNativeQuery(name = DataspyField.GETDATASPYVISIBLEFIELDS, 
					 query = "select n.*, gfd_controltype "
							+" from  r5queryfield n "
							+" join  r5dddataspy on dqf_ddspyid = dds_ddspyid "
							+" join  r5grid on dds_gridid = grd_gridid " 
							+" join  r5gridfield on n.dqf_fieldid = gfd_fieldid and dds_gridid = gfd_gridid "
							+" join  r5ddfield on ddf_fieldid = gfd_fieldid "
							+" where dqf_ddspyid = :dataspyid and dqf_viewtype = :requestType " 
							+"  and dqf_fieldid in ( "
							+"       select regexp_substr(grd_displayablelist,'[^,]+', 1, level) from dual connect by regexp_substr(grd_displayablelist, '[^,]+', 1, level) is not null "
							+"      )"
							+" order by DQF_COLUMNORDER, DQF_FIELDID "
							, 
				resultClass = DataspyField.class) 
})
public class DataspyField implements Serializable {

	public static final String GETDATASPYFIELDS = "GridField.GETDATASPYFIELDS";
	public static final String GETDATASPYVISIBLEFIELDS = "GridField.GETDATASPYVISIBLEFIELDS";
	
	@Id
	@Column(name="DQF_FIELDID")
	private String id;
	@Column(name="DQF_DDSPYID")
	private String dataspy;
	@Column(name="DQF_COLUMNORDER")
	private int order;
	@Column(name="DDF_DATATYPE")
	private String dataType;
	@Column(name="GFD_TAGNAME")
	private String tagName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDataspy() {
		return dataspy;
	}
	public void setDataspy(String dataspy) {
		this.dataspy = dataspy;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	@Override
	public String toString() {
		return "DataspyField [" + (id != null ? "id=" + id + ", " : "") + (dataspy != null ? "dataspy=" + dataspy + ", " : "") + "order=" + order + (tagName != null ? "tagName=" + tagName + ", " : "") + "]";
	}
	
}
