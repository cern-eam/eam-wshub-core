package ch.cern.eam.wshub.core.services.grids.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@NamedNativeQueries({
		@NamedNativeQuery(name = GridField.GETDDSPYFIELDS, query = "select dqf_ddspyid, gfd_fieldid, gfd_tagname, "
				+ " NVL((SELECT TRA_TEXT FROM U5TRANSLATIONS WHERE TRA_LANGUAGE = :language and TRA_PAGENAME = bot_function and UPPER(TRA_ELEMENTID) = UPPER(gfd_tagname)),BOT_TEXT) bot_text, "
				+ " dqf_columnwidth, ddf_datatype , PLD_CASE, dqf_columnorder " + " from r5gridfield "
				+ "     join r5queryfield on gfd_fieldid = dqf_fieldid "
				+ "     join r5boilertexts on gfd_botnumber = bot_number and gfd_botfunction = bot_function "
				+ "     join r5ddfield on gfd_fieldid = ddf_fieldid "
				+ "     join r5functions on gfd_botfunction = fun_code "
				+ "		left join R5DEFAULTPAGELAYOUT on (fun_code = pld_pagename or fun_application = pld_pagename) AND PLD_ELEMENTID = GFD_TAGNAME "
				+ " where gfd_gridid = :gridid and dqf_ddspyid = :ddspyid and dqf_viewtype = :viewtype "
				+ "  and dqf_columnorder > 0 ", resultClass = GridField.class),
		@NamedNativeQuery(name = GridField.GETGRIDFIELDS, query = "select distinct null as dqf_ddspyid, gfd_fieldid, gfd_tagname, "
				+ " NVL((SELECT TRA_TEXT FROM U5TRANSLATIONS WHERE TRA_LANGUAGE = :language and TRA_PAGENAME = bot_function and UPPER(TRA_ELEMENTID) = UPPER(gfd_tagname)),BOT_TEXT) bot_text, "
				+ " null as dqf_columnwidth, ddf_datatype, null as PLD_CASE, null as dqf_columnorder "
				+ " from r5gridfield  " + "     join r5queryfield on gfd_fieldid = dqf_fieldid  "
				+ "		join r5boilertexts on gfd_botnumber = bot_number and gfd_botfunction = bot_function  "
				+ "     join r5ddfield on gfd_fieldid = ddf_fieldid  "
				+ "     join r5functions on gfd_botfunction = fun_code   " + " where gfd_gridid = :gridid   "
				+ " and dqf_viewtype = :viewtype  " + " order by bot_text  ", resultClass = GridField.class) })
@Table(name = "r5gridfield")
public class GridField implements Serializable {

	private static final long serialVersionUID = 769608262540390595L;
	public static final String GETDDSPYFIELDS = "GridField.GETDDSPYFIELDS";
	public static final String GETGRIDFIELDS = "GridField.GETGRIDFIELDS";

	@Id
	@Column(name = "GFD_FIELDID")
	private String id;
	@Column(name = "GFD_TAGNAME")
	private String name;
	@Column(name = "BOT_TEXT")
	private String label;
	@Column(name = "DQF_COLUMNWIDTH")
	private String width;
	@Column(name = "DDF_DATATYPE")
	private String dataType;
	@Column(name = "PLD_CASE")
	private String pldCase;
	@Column(name = "DQF_DDSPYID")
	private String ddSpyId;
	@Column(name = "dqf_columnorder")
	private Integer order;

	@Transient
	private Boolean visible;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getPldCase() {
		return pldCase;
	}

	public void setPldCase(String pldCase) {
		this.pldCase = pldCase;
	}

	public String getDdSpyId() {
		return ddSpyId;
	}

	public void setDdSpyId(String ddSpyId) {
		this.ddSpyId = ddSpyId;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GridField [" + (id != null ? "id=" + id + ", " : "") + (name != null ? "name=" + name + ", " : "")
				+ (label != null ? "label=" + label + ", " : "") + (width != null ? "width=" + width + ", " : "")
				+ (dataType != null ? "dataType=" + dataType + ", " : "")
				+ (pldCase != null ? "pldCase=" + pldCase + ", " : "")
				+ (ddSpyId != null ? "ddSpyId=" + ddSpyId + ", " : "") + (order != null ? "order=" + order : "") + "]";
	}

}
