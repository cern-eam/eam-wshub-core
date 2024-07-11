package ch.cern.eam.wshub.core.services.grids.entities;

import ch.cern.eam.wshub.core.tools.BooleanTFConverter;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@NamedNativeQueries({
	@NamedNativeQuery(name=GridDataspy.GETGRIDDDSFILTER_AND_SORT_STRXML,
		query= 		
		"SELECT secfil.dds_filterstrxml, secfil.dds_sortstrxml "+  
		"from r5dddataspy mainddd, r5dddataspy secfil, r5permissions, r5users "+
		"where secfil.dds_gridid = mainddd.dds_gridid and secfil.dds_securitydataspy = '+' "+
		"and prm_securityddspyid = secfil.dds_ddspyid and prm_group = usr_group and mainddd.dds_ddspyid = :dataspyid and usr_code = :userid "+
		"and prm_function = :userfunction "+
		"UNION "+
		"SELECT dds_filterstrxml, dds_sortstrxml " +
		"FROM r5dddataspy " +
		"WHERE dds_ddspyid = :dataspyid"
	),
	@NamedNativeQuery(name=GridDataspy.GETGRIDDATASPIES, 
		query=
		"SELECT nvl(bot_text, dds_ddspyname) as dds_ddspyname, dds_ddspyid, dds_autorun, " +
		"CASE " +
		"WHEN usd_dataspyid IS NOT NULL THEN '+' " +
		"WHEN usd_dataspyid IS NULL AND DDS_DEFAULTFLAG = '+' AND NOT EXISTS (SELECT * FROM r5usegridsysdefault usd WHERE usd.USD_gridid = :gridid and usd.USD_userid = :userid) THEN '+' " +
		"ELSE '-' " +
		"END AS default_ds " +
		"FROM r5dddataspy LEFT OUTER JOIN r5usegridsysdefault  " +
		"ON r5usegridsysdefault.usd_dataspyid = r5dddataspy.dds_ddspyid AND usd_userid = :userid  " +
		"JOIN R5GRID ON dds_gridid = grd_gridid " +
		"LEFT OUTER JOIN R5BOILERTEXTS ON bot_fld1 = DDS_BOTNAME and BOT_FUNCTION = GRD_GRIDNAME " +
		"WHERE dds_gridid = :gridid AND (dds_globaldataspy = '+' OR dds_owner = :userid OR usd_gridid is not null) " +
		"  AND dds_securitydataspy = '-' " +
		"  AND dds_mekey like 'Y%' " +
		"order by dds_ddspyname ", 
		resultClass=GridDataspy.class
	),
	@NamedNativeQuery(name=GridDataspy.GETDEFAULTDATASPY, 
	query=
		" SELECT dds_ddspyname, dds_ddspyid, dds_autorun, '+' as default_ds " +
		" FROM r5dddataspy LEFT OUTER JOIN r5usegridsysdefault   " +
		"  ON r5usegridsysdefault.usd_dataspyid = r5dddataspy.dds_ddspyid AND usd_userid = :userid   " +
		"  WHERE dds_gridid = :gridid AND (dds_globaldataspy = '+' OR dds_owner = :userid)  " +
		"  AND dds_securitydataspy = '-' " +
		"  AND (usd_dataspyid IS NOT NULL OR (DDS_DEFAULTFLAG = '+' AND NOT EXISTS (SELECT * FROM r5usegridsysdefault usd WHERE usd.USD_gridid = :gridid and usd.USD_userid = :userid)))", 
		resultClass=GridDataspy.class
	),
  	@NamedNativeQuery(name=GridDataspy.GETGRIDDATASPYSELECTSTATEMENT, 
  		query = 
  		"WITH "+
  		" S1 AS ("+
  		" select DDF_SOURCENAME || ' AS A' || ROWNUM as select_statement "+
  		" from R5QUERYFIELD "+
  		"   join R5GRIDFIELD on DQF_FIELDID = GFD_FIELDID and gfd_GRIDID = :gridID "+
  		"   left join R5BOILERTEXTS on GFD_BOTNUMBER = BOT_NUMBER and bot_function = GFD_BOTFUNCTION "+
  		"   join R5GRID on GRD_GRIDID = GFD_GRIDID "+
  		"   join R5DDFIELD on DDF_FIELDID = DQF_FIELDID "+
  		" where dqf_DDSPYID = :dataSpyID "+
  		"  and dqf_viewtype = :requestType "+ 
//  		"  and GFD_FIELDID  in ( "+
//  		"   select regexp_substr(GRD_DISPLAYABLELIST,'[^,]+', 1, level) from dual connect by regexp_substr(GRD_DISPLAYABLELIST, '[^,]+', 1, level) is not null "+
//  		"  ) "+
  		" order by DQF_COLUMNORDER, DQF_FIELDID "+
  		" ), "+
  		" S2 AS ( "+
  		" select grd_hints as indexHint from R5GRID where GRD_GRIDID=:gridID "+
  		" ) "+
  		"select indexHint || select_statement as dataspy_select "+
  		" from s1, s2 "
  	),
  	@NamedNativeQuery(name=GridDataspy.GETGRIDDATASPYQUERY, 
		query = 
		"WITH "+
		" S1 AS ( "+
		" select grd_basequery as basequery from R5GRID where GRD_GRIDID=:gridID "+
		" ), "+
		" S2 AS ( "+
		" select case when dds_userfilter is null then '' else ' AND (' || dds_userfilter || ') ' end as userfilter from R5DDDATASPY where dds_ddspyid = :dataSpyID and dds_gridid = :gridID "+
		" ) "+
		"select basequery || userfilter as dataspy_query "+
		" from s1, s2 "
	),
  	@NamedNativeQuery(name = GridDataspy.GETGRIDFIELDSANDSTATEMENT,
  		query =
  		"select GFD_TAGNAME, DDF_SOURCENAME, DDF_DATATYPE, " +
  		"CASE WHEN USF_UPPERCASE = '+' THEN 'uppercase' ELSE CASE WHEN USF_UPPERCASE = '-' THEN 'mixed' ELSE nvl(PLD_CASE, CASE WHEN DDF_DATATYPE = 'VARCHAR' THEN 'uppercase' ELSE CASE WHEN DDF_DATATYPE = 'mixed' THEN 'uppercase' ELSE 'mixed' END END ) END END AS PLD_CASE " +
        "from r5gridfield " +
  		"join R5DDFIELD on DDF_FIELDID = GFD_FIELDID " +
  		"join r5functions on gfd_botfunction = fun_code " +
  		"left join R5DEFAULTPAGELAYOUT on (:userfunction = pld_pagename or fun_code = pld_pagename or fun_application = pld_pagename) AND PLD_ELEMENTID = GFD_TAGNAME " +
  		"left join R5UDFSCREENFIELDS on (:userfunction = usf_screenname or fun_code = usf_screenname or fun_application = usf_screenname) AND usf_fieldname = DDF_SOURCENAME " +
  		"where gfd_gridid = :gridID "
	),
  	@NamedNativeQuery(name=GridDataspy.GETGRIDDATASPYQUERYSELECTFIELDS, 
		query = 
		" select LISTAGG(GFD_TAGNAME || '#' || DDF_SOURCENAME,';') WITHIN GROUP (ORDER BY DQF_COLUMNORDER, DQF_FIELDID) as select_statement "+
		" from R5QUERYFIELD "+
		"   join R5GRIDFIELD on DQF_FIELDID = GFD_FIELDID and gfd_GRIDID = :gridID "+
		"   left join R5BOILERTEXTS on GFD_BOTNUMBER = BOT_NUMBER and bot_function = :botFunction "+
		"   join R5GRID on GRD_GRIDID = GFD_GRIDID "+
		"   join R5DDFIELD on DDF_FIELDID = DQF_FIELDID "+
		" where dqf_DDSPYID = :dataSpyID "+
		"   and dqf_viewtype = :requestType "+  
//		"   and GFD_FIELDID  in ( "+
//		"    select regexp_substr(GRD_DISPLAYABLELIST,'[^,]+', 1, level) from dual connect by regexp_substr(GRD_DISPLAYABLELIST, '[^,]+', 1, level) is not null "+
//		"   ) "+
		" order by DQF_COLUMNORDER, DQF_FIELDID "
	)
})
@Table(name="r5dddataspy")
public class GridDataspy implements Serializable {
	
	private static final long serialVersionUID = -3798374167695279787L;
	public static final String GETGRIDDDSFILTER_AND_SORT_STRXML = "GridDataspy.GETGRIDDDSFILTER_AND_SORT_STRXML";
	public static final String GETGRIDDATASPIES = "GridDataspy.GETGRIDDATASPIESS";
	public static final String GETGRIDDATASPYQUERY = "GridDataspy.GETGRIDDATASPYQUERY";
	public static final String GETGRIDDATASPYSELECTSTATEMENT = "GridDataspy.GETGRIDDATASPYSELECTSTATEMENT";
	public static final String GETGRIDDATASPYQUERYSELECTFIELDS = "GridDataspy.GETGRIDDATASPYQUERYSELECTFIELDS";
	public static final String GETGRIDFIELDSANDSTATEMENT = "GridDataspy.GETGRIDFIELDSANDSTATEMENT";
	public static final String GETDEFAULTDATASPY = "GridDataspy.GETDEFAULTDATASPY";
	
	@Id
	@Column(name="dds_ddspyid")
	private String code;
	@Column(name="dds_ddspyname")
	private String label;
	@Column(name="default_ds")
	@Convert(converter= BooleanTFConverter.class)
	private boolean defaultDataspy;
	@Column(name="dds_autorun")
	@Convert(converter=BooleanTFConverter.class)
	private boolean autorun;

	public GridDataspy() {

	}

	public GridDataspy(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public GridDataspy(String code, String label, boolean defaultDataspy) {
		this.code = code;
		this.label = label;
		this.defaultDataspy = defaultDataspy;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isDefaultDataspy() {
		return defaultDataspy;
	}
	public void setDefaultDataspy(boolean defaultDataspy) {
		this.defaultDataspy = defaultDataspy;
	}
	public boolean isAutorun() {
		return autorun;
	}
	public void setAutorun(boolean autorun) {
		this.autorun = autorun;
	}
	
}
