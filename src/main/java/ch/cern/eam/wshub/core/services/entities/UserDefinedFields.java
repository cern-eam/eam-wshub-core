package ch.cern.eam.wshub.core.services.entities;

import ch.cern.eam.wshub.core.adapters.DateAdapter;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

@Embeddable
public class UserDefinedFields implements Serializable, Cloneable {

	private static final long serialVersionUID = 8838910762758979180L;
	
	private String udfchar01;
	private String udfchar02;
	private String udfchar03;
	private String udfchar04;
	private String udfchar05;
	private String udfchar06;
	private String udfchar07;
	private String udfchar08;
	private String udfchar09;
	private String udfchar10;
	private String udfchar11;
	private String udfchar12;
	private String udfchar13;
	private String udfchar14;
	private String udfchar15;
	private String udfchar16;
	private String udfchar17;
	private String udfchar18;
	private String udfchar19;
	private String udfchar20;
	private String udfchar21;
	private String udfchar22;
	private String udfchar23;
	private String udfchar24;
	private String udfchar25;
	private String udfchar26;
	private String udfchar27;
	private String udfchar28;
	private String udfchar29;
	private String udfchar30;
	private String udfchar31;
	private String udfchar32;
	private String udfchar33;
	private String udfchar34;
	private String udfchar35;
	private String udfchar36;
	private String udfchar37;
	private String udfchar38;
	private String udfchar39;
	private String udfchar40;
	private String udfchar41;
	private String udfchar42;
	private String udfchar43;
	private String udfchar44;
	private String udfchar45;

	private String udfchar01Desc;
	private String udfchar02Desc;
	private String udfchar03Desc;
	private String udfchar04Desc;
	private String udfchar05Desc;
	private String udfchar06Desc;
	private String udfchar07Desc;
	private String udfchar08Desc;
	private String udfchar09Desc;
	private String udfchar10Desc;
	private String udfchar11Desc;
	private String udfchar12Desc;
	private String udfchar13Desc;
	private String udfchar14Desc;
	private String udfchar15Desc;
	private String udfchar16Desc;
	private String udfchar17Desc;
	private String udfchar18Desc;
	private String udfchar19Desc;
	private String udfchar20Desc;
	private String udfchar21Desc;
	private String udfchar22Desc;
	private String udfchar23Desc;
	private String udfchar24Desc;
	private String udfchar25Desc;
	private String udfchar26Desc;
	private String udfchar27Desc;
	private String udfchar28Desc;
	private String udfchar29Desc;
	private String udfchar30Desc;
	private String udfchar31Desc;
	private String udfchar32Desc;
	private String udfchar33Desc;
	private String udfchar34Desc;
	private String udfchar35Desc;
	private String udfchar36Desc;
	private String udfchar37Desc;
	private String udfchar38Desc;
	private String udfchar39Desc;
	private String udfchar40Desc;
	private String udfchar41Desc;
	private String udfchar42Desc;
	private String udfchar43Desc;
	private String udfchar44Desc;
	private String udfchar45Desc;

	private String udfchkbox01;
	private String udfchkbox02;
	private String udfchkbox03;
	private String udfchkbox04;
	private String udfchkbox05;
	private String udfchkbox06;
	private String udfchkbox07;
	private String udfchkbox08;
	private String udfchkbox09;
	private String udfchkbox10;

	private String udfnum01;
	private String udfnum02;
	private String udfnum03;
	private String udfnum04;
	private String udfnum05;
	private String udfnum06;
	private String udfnum07;
	private String udfnum08;
	private String udfnum09;
	private String udfnum10;

	private Date udfdate01;
	private Date udfdate02;
	private Date udfdate03;
	private Date udfdate04;
	private Date udfdate05;
	private Date udfdate06;
	private Date udfdate07;
	private Date udfdate08;
	private Date udfdate09;
	private Date udfdate10;
	
	public UserDefinedFields copy() {
		try {
			return (UserDefinedFields) this.clone();
		} catch (CloneNotSupportedException e) {
//			tools.log(Level.SEVERE,"Error during cloning ", e);
			return null;
		}
	}

	public String getUdfchar01() {
		return udfchar01;
	}

	public void setUdfchar01(String udfchar01) {
		this.udfchar01 = udfchar01;
	}

	public String getUdfchar02() {
		return udfchar02;
	}

	public void setUdfchar02(String udfchar02) {
		this.udfchar02 = udfchar02;
	}

	public String getUdfchar03() {
		return udfchar03;
	}

	public void setUdfchar03(String udfchar03) {
		this.udfchar03 = udfchar03;
	}

	public String getUdfchar04() {
		return udfchar04;
	}

	public void setUdfchar04(String udfchar04) {
		this.udfchar04 = udfchar04;
	}

	public String getUdfchar05() {
		return udfchar05;
	}

	public void setUdfchar05(String udfchar05) {
		this.udfchar05 = udfchar05;
	}

	public String getUdfchar06() {
		return udfchar06;
	}

	public void setUdfchar06(String udfchar06) {
		this.udfchar06 = udfchar06;
	}

	public String getUdfchar07() {
		return udfchar07;
	}

	public void setUdfchar07(String udfchar07) {
		this.udfchar07 = udfchar07;
	}

	public String getUdfchar08() {
		return udfchar08;
	}

	public void setUdfchar08(String udfchar08) {
		this.udfchar08 = udfchar08;
	}

	public String getUdfchar09() {
		return udfchar09;
	}

	public void setUdfchar09(String udfchar09) {
		this.udfchar09 = udfchar09;
	}

	public String getUdfchar10() {
		return udfchar10;
	}

	public void setUdfchar10(String udfchar10) {
		this.udfchar10 = udfchar10;
	}

	public String getUdfchar11() {
		return udfchar11;
	}

	public void setUdfchar11(String udfchar11) {
		this.udfchar11 = udfchar11;
	}

	public String getUdfchar12() {
		return udfchar12;
	}

	public void setUdfchar12(String udfchar12) {
		this.udfchar12 = udfchar12;
	}

	public String getUdfchar13() {
		return udfchar13;
	}

	public void setUdfchar13(String udfchar13) {
		this.udfchar13 = udfchar13;
	}

	public String getUdfchar14() {
		return udfchar14;
	}

	public void setUdfchar14(String udfchar14) {
		this.udfchar14 = udfchar14;
	}

	public String getUdfchar15() {
		return udfchar15;
	}

	public void setUdfchar15(String udfchar15) {
		this.udfchar15 = udfchar15;
	}

	public String getUdfchar16() {
		return udfchar16;
	}

	public void setUdfchar16(String udfchar16) {
		this.udfchar16 = udfchar16;
	}

	public String getUdfchar17() {
		return udfchar17;
	}

	public void setUdfchar17(String udfchar17) {
		this.udfchar17 = udfchar17;
	}

	public String getUdfchar18() {
		return udfchar18;
	}

	public void setUdfchar18(String udfchar18) {
		this.udfchar18 = udfchar18;
	}

	public String getUdfchar19() {
		return udfchar19;
	}

	public void setUdfchar19(String udfchar19) {
		this.udfchar19 = udfchar19;
	}

	public String getUdfchar20() {
		return udfchar20;
	}

	public void setUdfchar20(String udfchar20) {
		this.udfchar20 = udfchar20;
	}

	public String getUdfchar21() {
		return udfchar21;
	}

	public void setUdfchar21(String udfchar21) {
		this.udfchar21 = udfchar21;
	}

	public String getUdfchar22() {
		return udfchar22;
	}

	public void setUdfchar22(String udfchar22) {
		this.udfchar22 = udfchar22;
	}

	public String getUdfchar23() {
		return udfchar23;
	}

	public void setUdfchar23(String udfchar23) {
		this.udfchar23 = udfchar23;
	}

	public String getUdfchar24() {
		return udfchar24;
	}

	public void setUdfchar24(String udfchar24) {
		this.udfchar24 = udfchar24;
	}

	public String getUdfchar25() {
		return udfchar25;
	}

	public void setUdfchar25(String udfchar25) {
		this.udfchar25 = udfchar25;
	}

	public String getUdfchar26() {
		return udfchar26;
	}

	public void setUdfchar26(String udfchar26) {
		this.udfchar26 = udfchar26;
	}

	public String getUdfchar27() {
		return udfchar27;
	}

	public void setUdfchar27(String udfchar27) {
		this.udfchar27 = udfchar27;
	}

	public String getUdfchar28() {
		return udfchar28;
	}

	public void setUdfchar28(String udfchar28) {
		this.udfchar28 = udfchar28;
	}

	public String getUdfchar29() {
		return udfchar29;
	}

	public void setUdfchar29(String udfchar29) {
		this.udfchar29 = udfchar29;
	}

	public String getUdfchar30() {
		return udfchar30;
	}

	public void setUdfchar30(String udfchar30) {
		this.udfchar30 = udfchar30;
	}

	public String getUdfchkbox01() {
		return udfchkbox01;
	}

	public void setUdfchkbox01(String udfchkbox01) {
		this.udfchkbox01 = udfchkbox01;
	}

	public String getUdfchkbox02() {
		return udfchkbox02;
	}

	public void setUdfchkbox02(String udfchkbox02) {
		this.udfchkbox02 = udfchkbox02;
	}

	public String getUdfchkbox03() {
		return udfchkbox03;
	}

	public void setUdfchkbox03(String udfchkbox03) {
		this.udfchkbox03 = udfchkbox03;
	}

	public String getUdfchkbox04() {
		return udfchkbox04;
	}

	public void setUdfchkbox04(String udfchkbox04) {
		this.udfchkbox04 = udfchkbox04;
	}

	public String getUdfchkbox05() {
		return udfchkbox05;
	}

	public void setUdfchkbox05(String udfchkbox05) {
		this.udfchkbox05 = udfchkbox05;
	}

	public String getUdfnum01() {
		return udfnum01;
	}

	public void setUdfnum01(String udfnum01) {
		this.udfnum01 = udfnum01;
	}

	public String getUdfnum02() {
		return udfnum02;
	}

	public void setUdfnum02(String udfnum02) {
		this.udfnum02 = udfnum02;
	}

	public String getUdfnum03() {
		return udfnum03;
	}

	public void setUdfnum03(String udfnum03) {
		this.udfnum03 = udfnum03;
	}

	public String getUdfnum04() {
		return udfnum04;
	}

	public void setUdfnum04(String udfnum04) {
		this.udfnum04 = udfnum04;
	}

	public String getUdfnum05() {
		return udfnum05;
	}

	public void setUdfnum05(String udfnum05) {
		this.udfnum05 = udfnum05;
	}

	public String getUdfchkbox06() {
		return udfchkbox06;
	}

	public void setUdfchkbox06(String udfchkbox06) {
		this.udfchkbox06 = udfchkbox06;
	}

	public String getUdfchkbox07() {
		return udfchkbox07;
	}

	public void setUdfchkbox07(String udfchkbox07) {
		this.udfchkbox07 = udfchkbox07;
	}

	public String getUdfchkbox08() {
		return udfchkbox08;
	}

	public void setUdfchkbox08(String udfchkbox08) {
		this.udfchkbox08 = udfchkbox08;
	}

	public String getUdfchkbox09() {
		return udfchkbox09;
	}

	public void setUdfchkbox09(String udfchkbox09) {
		this.udfchkbox09 = udfchkbox09;
	}

	public String getUdfchkbox10() {
		return udfchkbox10;
	}

	public void setUdfchkbox10(String udfchkbox10) {
		this.udfchkbox10 = udfchkbox10;
	}

	public String getUdfnum06() {
		return udfnum06;
	}

	public void setUdfnum06(String udfnum06) {
		this.udfnum06 = udfnum06;
	}

	public String getUdfnum07() {
		return udfnum07;
	}

	public void setUdfnum07(String udfnum07) {
		this.udfnum07 = udfnum07;
	}

	public String getUdfnum08() {
		return udfnum08;
	}

	public void setUdfnum08(String udfnum08) {
		this.udfnum08 = udfnum08;
	}

	public String getUdfnum09() {
		return udfnum09;
	}

	public void setUdfnum09(String udfnum09) {
		this.udfnum09 = udfnum09;
	}

	public String getUdfnum10() {
		return udfnum10;
	}

	public void setUdfnum10(String udfnum10) {
		this.udfnum10 = udfnum10;
	}

	public String getUdfchar31() {
		return udfchar31;
	}

	public void setUdfchar31(String udfchar31) {
		this.udfchar31 = udfchar31;
	}

	public String getUdfchar32() {
		return udfchar32;
	}

	public void setUdfchar32(String udfchar32) {
		this.udfchar32 = udfchar32;
	}

	public String getUdfchar33() {
		return udfchar33;
	}

	public void setUdfchar33(String udfchar33) {
		this.udfchar33 = udfchar33;
	}

	public String getUdfchar34() {
		return udfchar34;
	}

	public void setUdfchar34(String udfchar34) {
		this.udfchar34 = udfchar34;
	}

	public String getUdfchar35() {
		return udfchar35;
	}

	public void setUdfchar35(String udfchar35) {
		this.udfchar35 = udfchar35;
	}

	public String getUdfchar36() {
		return udfchar36;
	}

	public void setUdfchar36(String udfchar36) {
		this.udfchar36 = udfchar36;
	}

	public String getUdfchar37() {
		return udfchar37;
	}

	public void setUdfchar37(String udfchar37) {
		this.udfchar37 = udfchar37;
	}

	public String getUdfchar38() {
		return udfchar38;
	}

	public void setUdfchar38(String udfchar38) {
		this.udfchar38 = udfchar38;
	}

	public String getUdfchar39() {
		return udfchar39;
	}

	public void setUdfchar39(String udfchar39) {
		this.udfchar39 = udfchar39;
	}

	public String getUdfchar40() {
		return udfchar40;
	}

	public void setUdfchar40(String udfchar40) {
		this.udfchar40 = udfchar40;
	}

	public String getUdfchar41() {
		return udfchar41;
	}

	public void setUdfchar41(String udfchar41) {
		this.udfchar41 = udfchar41;
	}

	public String getUdfchar42() {
		return udfchar42;
	}

	public void setUdfchar42(String udfchar42) {
		this.udfchar42 = udfchar42;
	}

	public String getUdfchar43() {
		return udfchar43;
	}

	public void setUdfchar43(String udfchar43) {
		this.udfchar43 = udfchar43;
	}

	public String getUdfchar44() {
		return udfchar44;
	}

	public void setUdfchar44(String udfchar44) {
		this.udfchar44 = udfchar44;
	}

	public String getUdfchar45() {
		return udfchar45;
	}

	public void setUdfchar45(String udfchar45) {
		this.udfchar45 = udfchar45;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUdfdate01() {
		return udfdate01;
	}

	public void setUdfdate01(Date udfdate01) {
		this.udfdate01 = udfdate01;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUdfdate02() {
		return udfdate02;
	}

	public void setUdfdate02(Date udfdate02) {
		this.udfdate02 = udfdate02;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUdfdate03() {
		return udfdate03;
	}

	public void setUdfdate03(Date udfdate03) {
		this.udfdate03 = udfdate03;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUdfdate04() {
		return udfdate04;
	}

	public void setUdfdate04(Date udfdate04) {
		this.udfdate04 = udfdate04;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUdfdate05() {
		return udfdate05;
	}

	public void setUdfdate05(Date udfdate05) {
		this.udfdate05 = udfdate05;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUdfdate06() {
		return udfdate06;
	}

	public void setUdfdate06(Date udfdate06) {
		this.udfdate06 = udfdate06;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUdfdate07() {
		return udfdate07;
	}

	public void setUdfdate07(Date udfdate07) {
		this.udfdate07 = udfdate07;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUdfdate08() {
		return udfdate08;
	}

	public void setUdfdate08(Date udfdate08) {
		this.udfdate08 = udfdate08;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUdfdate09() {
		return udfdate09;
	}

	public void setUdfdate09(Date udfdate09) {
		this.udfdate09 = udfdate09;
	}

	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUdfdate10() {
		return udfdate10;
	}

	public void setUdfdate10(Date udfdate10) {
		this.udfdate10 = udfdate10;
	}

	/**
	 * @return the udfchar01Desc
	 */
	public String getUdfchar01Desc() {
		return udfchar01Desc;
	}

	/**
	 * @param udfchar01Desc
	 *            the udfchar01Desc to set
	 */
	public void setUdfchar01Desc(String udfchar01Desc) {
		this.udfchar01Desc = udfchar01Desc;
	}

	/**
	 * @return the udfchar02Desc
	 */
	public String getUdfchar02Desc() {
		return udfchar02Desc;
	}

	/**
	 * @param udfchar02Desc
	 *            the udfchar02Desc to set
	 */
	public void setUdfchar02Desc(String udfchar02Desc) {
		this.udfchar02Desc = udfchar02Desc;
	}

	/**
	 * @return the udfchar03Desc
	 */
	public String getUdfchar03Desc() {
		return udfchar03Desc;
	}

	/**
	 * @param udfchar03Desc
	 *            the udfchar03Desc to set
	 */
	public void setUdfchar03Desc(String udfchar03Desc) {
		this.udfchar03Desc = udfchar03Desc;
	}

	/**
	 * @return the udfchar04Desc
	 */
	public String getUdfchar04Desc() {
		return udfchar04Desc;
	}

	/**
	 * @param udfchar04Desc
	 *            the udfchar04Desc to set
	 */
	public void setUdfchar04Desc(String udfchar04Desc) {
		this.udfchar04Desc = udfchar04Desc;
	}

	/**
	 * @return the udfchar05Desc
	 */
	public String getUdfchar05Desc() {
		return udfchar05Desc;
	}

	/**
	 * @param udfchar05Desc
	 *            the udfchar05Desc to set
	 */
	public void setUdfchar05Desc(String udfchar05Desc) {
		this.udfchar05Desc = udfchar05Desc;
	}

	/**
	 * @return the udfchar06Desc
	 */
	public String getUdfchar06Desc() {
		return udfchar06Desc;
	}

	/**
	 * @param udfchar06Desc
	 *            the udfchar06Desc to set
	 */
	public void setUdfchar06Desc(String udfchar06Desc) {
		this.udfchar06Desc = udfchar06Desc;
	}

	/**
	 * @return the udfchar07Desc
	 */
	public String getUdfchar07Desc() {
		return udfchar07Desc;
	}

	/**
	 * @param udfchar07Desc
	 *            the udfchar07Desc to set
	 */
	public void setUdfchar07Desc(String udfchar07Desc) {
		this.udfchar07Desc = udfchar07Desc;
	}

	/**
	 * @return the udfchar08Desc
	 */
	public String getUdfchar08Desc() {
		return udfchar08Desc;
	}

	/**
	 * @param udfchar08Desc
	 *            the udfchar08Desc to set
	 */
	public void setUdfchar08Desc(String udfchar08Desc) {
		this.udfchar08Desc = udfchar08Desc;
	}

	/**
	 * @return the udfchar09Desc
	 */
	public String getUdfchar09Desc() {
		return udfchar09Desc;
	}

	/**
	 * @param udfchar09Desc
	 *            the udfchar09Desc to set
	 */
	public void setUdfchar09Desc(String udfchar09Desc) {
		this.udfchar09Desc = udfchar09Desc;
	}

	/**
	 * @return the udfchar10Desc
	 */
	public String getUdfchar10Desc() {
		return udfchar10Desc;
	}

	/**
	 * @param udfchar10Desc
	 *            the udfchar10Desc to set
	 */
	public void setUdfchar10Desc(String udfchar10Desc) {
		this.udfchar10Desc = udfchar10Desc;
	}

	/**
	 * @return the udfchar11Desc
	 */
	public String getUdfchar11Desc() {
		return udfchar11Desc;
	}

	/**
	 * @param udfchar11Desc
	 *            the udfchar11Desc to set
	 */
	public void setUdfchar11Desc(String udfchar11Desc) {
		this.udfchar11Desc = udfchar11Desc;
	}

	/**
	 * @return the udfchar12Desc
	 */
	public String getUdfchar12Desc() {
		return udfchar12Desc;
	}

	/**
	 * @param udfchar12Desc
	 *            the udfchar12Desc to set
	 */
	public void setUdfchar12Desc(String udfchar12Desc) {
		this.udfchar12Desc = udfchar12Desc;
	}

	/**
	 * @return the udfchar13Desc
	 */
	public String getUdfchar13Desc() {
		return udfchar13Desc;
	}

	/**
	 * @param udfchar13Desc
	 *            the udfchar13Desc to set
	 */
	public void setUdfchar13Desc(String udfchar13Desc) {
		this.udfchar13Desc = udfchar13Desc;
	}

	/**
	 * @return the udfchar14Desc
	 */
	public String getUdfchar14Desc() {
		return udfchar14Desc;
	}

	/**
	 * @param udfchar14Desc
	 *            the udfchar14Desc to set
	 */
	public void setUdfchar14Desc(String udfchar14Desc) {
		this.udfchar14Desc = udfchar14Desc;
	}

	/**
	 * @return the udfchar15Desc
	 */
	public String getUdfchar15Desc() {
		return udfchar15Desc;
	}

	/**
	 * @param udfchar15Desc
	 *            the udfchar15Desc to set
	 */
	public void setUdfchar15Desc(String udfchar15Desc) {
		this.udfchar15Desc = udfchar15Desc;
	}

	/**
	 * @return the udfchar16Desc
	 */
	public String getUdfchar16Desc() {
		return udfchar16Desc;
	}

	/**
	 * @param udfchar16Desc
	 *            the udfchar16Desc to set
	 */
	public void setUdfchar16Desc(String udfchar16Desc) {
		this.udfchar16Desc = udfchar16Desc;
	}

	/**
	 * @return the udfchar17Desc
	 */
	public String getUdfchar17Desc() {
		return udfchar17Desc;
	}

	/**
	 * @param udfchar17Desc
	 *            the udfchar17Desc to set
	 */
	public void setUdfchar17Desc(String udfchar17Desc) {
		this.udfchar17Desc = udfchar17Desc;
	}

	/**
	 * @return the udfchar18Desc
	 */
	public String getUdfchar18Desc() {
		return udfchar18Desc;
	}

	/**
	 * @param udfchar18Desc
	 *            the udfchar18Desc to set
	 */
	public void setUdfchar18Desc(String udfchar18Desc) {
		this.udfchar18Desc = udfchar18Desc;
	}

	/**
	 * @return the udfchar19Desc
	 */
	public String getUdfchar19Desc() {
		return udfchar19Desc;
	}

	/**
	 * @param udfchar19Desc
	 *            the udfchar19Desc to set
	 */
	public void setUdfchar19Desc(String udfchar19Desc) {
		this.udfchar19Desc = udfchar19Desc;
	}

	/**
	 * @return the udfchar20Desc
	 */
	public String getUdfchar20Desc() {
		return udfchar20Desc;
	}

	/**
	 * @param udfchar20Desc
	 *            the udfchar20Desc to set
	 */
	public void setUdfchar20Desc(String udfchar20Desc) {
		this.udfchar20Desc = udfchar20Desc;
	}

	/**
	 * @return the udfchar21Desc
	 */
	public String getUdfchar21Desc() {
		return udfchar21Desc;
	}

	/**
	 * @param udfchar21Desc
	 *            the udfchar21Desc to set
	 */
	public void setUdfchar21Desc(String udfchar21Desc) {
		this.udfchar21Desc = udfchar21Desc;
	}

	/**
	 * @return the udfchar22Desc
	 */
	public String getUdfchar22Desc() {
		return udfchar22Desc;
	}

	/**
	 * @param udfchar22Desc
	 *            the udfchar22Desc to set
	 */
	public void setUdfchar22Desc(String udfchar22Desc) {
		this.udfchar22Desc = udfchar22Desc;
	}

	/**
	 * @return the udfchar23Desc
	 */
	public String getUdfchar23Desc() {
		return udfchar23Desc;
	}

	/**
	 * @param udfchar23Desc
	 *            the udfchar23Desc to set
	 */
	public void setUdfchar23Desc(String udfchar23Desc) {
		this.udfchar23Desc = udfchar23Desc;
	}

	/**
	 * @return the udfchar24Desc
	 */
	public String getUdfchar24Desc() {
		return udfchar24Desc;
	}

	/**
	 * @param udfchar24Desc
	 *            the udfchar24Desc to set
	 */
	public void setUdfchar24Desc(String udfchar24Desc) {
		this.udfchar24Desc = udfchar24Desc;
	}

	/**
	 * @return the udfchar25Desc
	 */
	public String getUdfchar25Desc() {
		return udfchar25Desc;
	}

	/**
	 * @param udfchar25Desc
	 *            the udfchar25Desc to set
	 */
	public void setUdfchar25Desc(String udfchar25Desc) {
		this.udfchar25Desc = udfchar25Desc;
	}

	/**
	 * @return the udfchar26Desc
	 */
	public String getUdfchar26Desc() {
		return udfchar26Desc;
	}

	/**
	 * @param udfchar26Desc
	 *            the udfchar26Desc to set
	 */
	public void setUdfchar26Desc(String udfchar26Desc) {
		this.udfchar26Desc = udfchar26Desc;
	}

	/**
	 * @return the udfchar27Desc
	 */
	public String getUdfchar27Desc() {
		return udfchar27Desc;
	}

	/**
	 * @param udfchar27Desc
	 *            the udfchar27Desc to set
	 */
	public void setUdfchar27Desc(String udfchar27Desc) {
		this.udfchar27Desc = udfchar27Desc;
	}

	/**
	 * @return the udfchar28Desc
	 */
	public String getUdfchar28Desc() {
		return udfchar28Desc;
	}

	/**
	 * @param udfchar28Desc
	 *            the udfchar28Desc to set
	 */
	public void setUdfchar28Desc(String udfchar28Desc) {
		this.udfchar28Desc = udfchar28Desc;
	}

	/**
	 * @return the udfchar29Desc
	 */
	public String getUdfchar29Desc() {
		return udfchar29Desc;
	}

	/**
	 * @param udfchar29Desc
	 *            the udfchar29Desc to set
	 */
	public void setUdfchar29Desc(String udfchar29Desc) {
		this.udfchar29Desc = udfchar29Desc;
	}

	/**
	 * @return the udfchar30Desc
	 */
	public String getUdfchar30Desc() {
		return udfchar30Desc;
	}

	/**
	 * @param udfchar30Desc
	 *            the udfchar30Desc to set
	 */
	public void setUdfchar30Desc(String udfchar30Desc) {
		this.udfchar30Desc = udfchar30Desc;
	}

	/**
	 * @return the udfchar31Desc
	 */
	public String getUdfchar31Desc() {
		return udfchar31Desc;
	}

	/**
	 * @param udfchar31Desc
	 *            the udfchar31Desc to set
	 */
	public void setUdfchar31Desc(String udfchar31Desc) {
		this.udfchar31Desc = udfchar31Desc;
	}

	/**
	 * @return the udfchar32Desc
	 */
	public String getUdfchar32Desc() {
		return udfchar32Desc;
	}

	/**
	 * @param udfchar32Desc
	 *            the udfchar32Desc to set
	 */
	public void setUdfchar32Desc(String udfchar32Desc) {
		this.udfchar32Desc = udfchar32Desc;
	}

	/**
	 * @return the udfchar33Desc
	 */
	public String getUdfchar33Desc() {
		return udfchar33Desc;
	}

	/**
	 * @param udfchar33Desc
	 *            the udfchar33Desc to set
	 */
	public void setUdfchar33Desc(String udfchar33Desc) {
		this.udfchar33Desc = udfchar33Desc;
	}

	/**
	 * @return the udfchar34Desc
	 */
	public String getUdfchar34Desc() {
		return udfchar34Desc;
	}

	/**
	 * @param udfchar34Desc
	 *            the udfchar34Desc to set
	 */
	public void setUdfchar34Desc(String udfchar34Desc) {
		this.udfchar34Desc = udfchar34Desc;
	}

	/**
	 * @return the udfchar35Desc
	 */
	public String getUdfchar35Desc() {
		return udfchar35Desc;
	}

	/**
	 * @param udfchar35Desc
	 *            the udfchar35Desc to set
	 */
	public void setUdfchar35Desc(String udfchar35Desc) {
		this.udfchar35Desc = udfchar35Desc;
	}

	/**
	 * @return the udfchar36Desc
	 */
	public String getUdfchar36Desc() {
		return udfchar36Desc;
	}

	/**
	 * @param udfchar36Desc
	 *            the udfchar36Desc to set
	 */
	public void setUdfchar36Desc(String udfchar36Desc) {
		this.udfchar36Desc = udfchar36Desc;
	}

	/**
	 * @return the udfchar37Desc
	 */
	public String getUdfchar37Desc() {
		return udfchar37Desc;
	}

	/**
	 * @param udfchar37Desc
	 *            the udfchar37Desc to set
	 */
	public void setUdfchar37Desc(String udfchar37Desc) {
		this.udfchar37Desc = udfchar37Desc;
	}

	/**
	 * @return the udfchar38Desc
	 */
	public String getUdfchar38Desc() {
		return udfchar38Desc;
	}

	/**
	 * @param udfchar38Desc
	 *            the udfchar38Desc to set
	 */
	public void setUdfchar38Desc(String udfchar38Desc) {
		this.udfchar38Desc = udfchar38Desc;
	}

	/**
	 * @return the udfchar39Desc
	 */
	public String getUdfchar39Desc() {
		return udfchar39Desc;
	}

	/**
	 * @param udfchar39Desc
	 *            the udfchar39Desc to set
	 */
	public void setUdfchar39Desc(String udfchar39Desc) {
		this.udfchar39Desc = udfchar39Desc;
	}

	/**
	 * @return the udfchar40Desc
	 */
	public String getUdfchar40Desc() {
		return udfchar40Desc;
	}

	/**
	 * @param udfchar40Desc
	 *            the udfchar40Desc to set
	 */
	public void setUdfchar40Desc(String udfchar40Desc) {
		this.udfchar40Desc = udfchar40Desc;
	}

	/**
	 * @return the udfchar41Desc
	 */
	public String getUdfchar41Desc() {
		return udfchar41Desc;
	}

	/**
	 * @param udfchar41Desc
	 *            the udfchar41Desc to set
	 */
	public void setUdfchar41Desc(String udfchar41Desc) {
		this.udfchar41Desc = udfchar41Desc;
	}

	/**
	 * @return the udfchar42Desc
	 */
	public String getUdfchar42Desc() {
		return udfchar42Desc;
	}

	/**
	 * @param udfchar42Desc
	 *            the udfchar42Desc to set
	 */
	public void setUdfchar42Desc(String udfchar42Desc) {
		this.udfchar42Desc = udfchar42Desc;
	}

	/**
	 * @return the udfchar43Desc
	 */
	public String getUdfchar43Desc() {
		return udfchar43Desc;
	}

	/**
	 * @param udfchar43Desc
	 *            the udfchar43Desc to set
	 */
	public void setUdfchar43Desc(String udfchar43Desc) {
		this.udfchar43Desc = udfchar43Desc;
	}

	/**
	 * @return the udfchar44Desc
	 */
	public String getUdfchar44Desc() {
		return udfchar44Desc;
	}

	/**
	 * @param udfchar44Desc
	 *            the udfchar44Desc to set
	 */
	public void setUdfchar44Desc(String udfchar44Desc) {
		this.udfchar44Desc = udfchar44Desc;
	}

	/**
	 * @return the udfchar45Desc
	 */
	public String getUdfchar45Desc() {
		return udfchar45Desc;
	}

	/**
	 * @param udfchar45Desc
	 *            the udfchar45Desc to set
	 */
	public void setUdfchar45Desc(String udfchar45Desc) {
		this.udfchar45Desc = udfchar45Desc;
	}

	@Override
	public String toString() {
		return "UserDefinedFields [" + (udfchar01 != null ? "udfchar01=" + udfchar01 + ", " : "")
				+ (udfchar02 != null ? "udfchar02=" + udfchar02 + ", " : "")
				+ (udfchar03 != null ? "udfchar03=" + udfchar03 + ", " : "")
				+ (udfchar04 != null ? "udfchar04=" + udfchar04 + ", " : "")
				+ (udfchar05 != null ? "udfchar05=" + udfchar05 + ", " : "")
				+ (udfchar06 != null ? "udfchar06=" + udfchar06 + ", " : "")
				+ (udfchar07 != null ? "udfchar07=" + udfchar07 + ", " : "")
				+ (udfchar08 != null ? "udfchar08=" + udfchar08 + ", " : "")
				+ (udfchar09 != null ? "udfchar09=" + udfchar09 + ", " : "")
				+ (udfchar10 != null ? "udfchar10=" + udfchar10 + ", " : "")
				+ (udfchar11 != null ? "udfchar11=" + udfchar11 + ", " : "")
				+ (udfchar12 != null ? "udfchar12=" + udfchar12 + ", " : "")
				+ (udfchar13 != null ? "udfchar13=" + udfchar13 + ", " : "")
				+ (udfchar14 != null ? "udfchar14=" + udfchar14 + ", " : "")
				+ (udfchar15 != null ? "udfchar15=" + udfchar15 + ", " : "")
				+ (udfchar16 != null ? "udfchar16=" + udfchar16 + ", " : "")
				+ (udfchar17 != null ? "udfchar17=" + udfchar17 + ", " : "")
				+ (udfchar18 != null ? "udfchar18=" + udfchar18 + ", " : "")
				+ (udfchar19 != null ? "udfchar19=" + udfchar19 + ", " : "")
				+ (udfchar20 != null ? "udfchar20=" + udfchar20 + ", " : "")
				+ (udfchar21 != null ? "udfchar21=" + udfchar21 + ", " : "")
				+ (udfchar22 != null ? "udfchar22=" + udfchar22 + ", " : "")
				+ (udfchar23 != null ? "udfchar23=" + udfchar23 + ", " : "")
				+ (udfchar24 != null ? "udfchar24=" + udfchar24 + ", " : "")
				+ (udfchar25 != null ? "udfchar25=" + udfchar25 + ", " : "")
				+ (udfchar26 != null ? "udfchar26=" + udfchar26 + ", " : "")
				+ (udfchar27 != null ? "udfchar27=" + udfchar27 + ", " : "")
				+ (udfchar28 != null ? "udfchar28=" + udfchar28 + ", " : "")
				+ (udfchar29 != null ? "udfchar29=" + udfchar29 + ", " : "")
				+ (udfchar30 != null ? "udfchar30=" + udfchar30 + ", " : "")
				+ (udfchar31 != null ? "udfchar31=" + udfchar31 + ", " : "")
				+ (udfchar32 != null ? "udfchar32=" + udfchar32 + ", " : "")
				+ (udfchar33 != null ? "udfchar33=" + udfchar33 + ", " : "")
				+ (udfchar34 != null ? "udfchar34=" + udfchar34 + ", " : "")
				+ (udfchar35 != null ? "udfchar35=" + udfchar35 + ", " : "")
				+ (udfchar36 != null ? "udfchar36=" + udfchar36 + ", " : "")
				+ (udfchar37 != null ? "udfchar37=" + udfchar37 + ", " : "")
				+ (udfchar38 != null ? "udfchar38=" + udfchar38 + ", " : "")
				+ (udfchar39 != null ? "udfchar39=" + udfchar39 + ", " : "")
				+ (udfchar40 != null ? "udfchar40=" + udfchar40 + ", " : "")
				+ (udfchar41 != null ? "udfchar41=" + udfchar41 + ", " : "")
				+ (udfchar42 != null ? "udfchar42=" + udfchar42 + ", " : "")
				+ (udfchar43 != null ? "udfchar43=" + udfchar43 + ", " : "")
				+ (udfchar44 != null ? "udfchar44=" + udfchar44 + ", " : "")
				+ (udfchar45 != null ? "udfchar45=" + udfchar45 + ", " : "")
				+ (udfchar01Desc != null ? "udfchar01Desc=" + udfchar01Desc + ", " : "")
				+ (udfchar02Desc != null ? "udfchar02Desc=" + udfchar02Desc + ", " : "")
				+ (udfchar03Desc != null ? "udfchar03Desc=" + udfchar03Desc + ", " : "")
				+ (udfchar04Desc != null ? "udfchar04Desc=" + udfchar04Desc + ", " : "")
				+ (udfchar05Desc != null ? "udfchar05Desc=" + udfchar05Desc + ", " : "")
				+ (udfchar06Desc != null ? "udfchar06Desc=" + udfchar06Desc + ", " : "")
				+ (udfchar07Desc != null ? "udfchar07Desc=" + udfchar07Desc + ", " : "")
				+ (udfchar08Desc != null ? "udfchar08Desc=" + udfchar08Desc + ", " : "")
				+ (udfchar09Desc != null ? "udfchar09Desc=" + udfchar09Desc + ", " : "")
				+ (udfchar10Desc != null ? "udfchar10Desc=" + udfchar10Desc + ", " : "")
				+ (udfchar11Desc != null ? "udfchar11Desc=" + udfchar11Desc + ", " : "")
				+ (udfchar12Desc != null ? "udfchar12Desc=" + udfchar12Desc + ", " : "")
				+ (udfchar13Desc != null ? "udfchar13Desc=" + udfchar13Desc + ", " : "")
				+ (udfchar14Desc != null ? "udfchar14Desc=" + udfchar14Desc + ", " : "")
				+ (udfchar15Desc != null ? "udfchar15Desc=" + udfchar15Desc + ", " : "")
				+ (udfchar16Desc != null ? "udfchar16Desc=" + udfchar16Desc + ", " : "")
				+ (udfchar17Desc != null ? "udfchar17Desc=" + udfchar17Desc + ", " : "")
				+ (udfchar18Desc != null ? "udfchar18Desc=" + udfchar18Desc + ", " : "")
				+ (udfchar19Desc != null ? "udfchar19Desc=" + udfchar19Desc + ", " : "")
				+ (udfchar20Desc != null ? "udfchar20Desc=" + udfchar20Desc + ", " : "")
				+ (udfchar21Desc != null ? "udfchar21Desc=" + udfchar21Desc + ", " : "")
				+ (udfchar22Desc != null ? "udfchar22Desc=" + udfchar22Desc + ", " : "")
				+ (udfchar23Desc != null ? "udfchar23Desc=" + udfchar23Desc + ", " : "")
				+ (udfchar24Desc != null ? "udfchar24Desc=" + udfchar24Desc + ", " : "")
				+ (udfchar25Desc != null ? "udfchar25Desc=" + udfchar25Desc + ", " : "")
				+ (udfchar26Desc != null ? "udfchar26Desc=" + udfchar26Desc + ", " : "")
				+ (udfchar27Desc != null ? "udfchar27Desc=" + udfchar27Desc + ", " : "")
				+ (udfchar28Desc != null ? "udfchar28Desc=" + udfchar28Desc + ", " : "")
				+ (udfchar29Desc != null ? "udfchar29Desc=" + udfchar29Desc + ", " : "")
				+ (udfchar30Desc != null ? "udfchar30Desc=" + udfchar30Desc + ", " : "")
				+ (udfchar31Desc != null ? "udfchar31Desc=" + udfchar31Desc + ", " : "")
				+ (udfchar32Desc != null ? "udfchar32Desc=" + udfchar32Desc + ", " : "")
				+ (udfchar33Desc != null ? "udfchar33Desc=" + udfchar33Desc + ", " : "")
				+ (udfchar34Desc != null ? "udfchar34Desc=" + udfchar34Desc + ", " : "")
				+ (udfchar35Desc != null ? "udfchar35Desc=" + udfchar35Desc + ", " : "")
				+ (udfchar36Desc != null ? "udfchar36Desc=" + udfchar36Desc + ", " : "")
				+ (udfchar37Desc != null ? "udfchar37Desc=" + udfchar37Desc + ", " : "")
				+ (udfchar38Desc != null ? "udfchar38Desc=" + udfchar38Desc + ", " : "")
				+ (udfchar39Desc != null ? "udfchar39Desc=" + udfchar39Desc + ", " : "")
				+ (udfchar40Desc != null ? "udfchar40Desc=" + udfchar40Desc + ", " : "")
				+ (udfchar41Desc != null ? "udfchar41Desc=" + udfchar41Desc + ", " : "")
				+ (udfchar42Desc != null ? "udfchar42Desc=" + udfchar42Desc + ", " : "")
				+ (udfchar43Desc != null ? "udfchar43Desc=" + udfchar43Desc + ", " : "")
				+ (udfchar44Desc != null ? "udfchar44Desc=" + udfchar44Desc + ", " : "")
				+ (udfchar45Desc != null ? "udfchar45Desc=" + udfchar45Desc + ", " : "")
				+ (udfchkbox01 != null ? "udfchkbox01=" + udfchkbox01 + ", " : "")
				+ (udfchkbox02 != null ? "udfchkbox02=" + udfchkbox02 + ", " : "")
				+ (udfchkbox03 != null ? "udfchkbox03=" + udfchkbox03 + ", " : "")
				+ (udfchkbox04 != null ? "udfchkbox04=" + udfchkbox04 + ", " : "")
				+ (udfchkbox05 != null ? "udfchkbox05=" + udfchkbox05 + ", " : "")
				+ (udfchkbox06 != null ? "udfchkbox06=" + udfchkbox06 + ", " : "")
				+ (udfchkbox07 != null ? "udfchkbox07=" + udfchkbox07 + ", " : "")
				+ (udfchkbox08 != null ? "udfchkbox08=" + udfchkbox08 + ", " : "")
				+ (udfchkbox09 != null ? "udfchkbox09=" + udfchkbox09 + ", " : "")
				+ (udfchkbox10 != null ? "udfchkbox10=" + udfchkbox10 + ", " : "")
				+ (udfnum01 != null ? "udfnum01=" + udfnum01 + ", " : "")
				+ (udfnum02 != null ? "udfnum02=" + udfnum02 + ", " : "")
				+ (udfnum03 != null ? "udfnum03=" + udfnum03 + ", " : "")
				+ (udfnum04 != null ? "udfnum04=" + udfnum04 + ", " : "")
				+ (udfnum05 != null ? "udfnum05=" + udfnum05 + ", " : "")
				+ (udfnum06 != null ? "udfnum06=" + udfnum06 + ", " : "")
				+ (udfnum07 != null ? "udfnum07=" + udfnum07 + ", " : "")
				+ (udfnum08 != null ? "udfnum08=" + udfnum08 + ", " : "")
				+ (udfnum09 != null ? "udfnum09=" + udfnum09 + ", " : "")
				+ (udfnum10 != null ? "udfnum10=" + udfnum10 + ", " : "")
				+ (udfdate01 != null ? "udfdate01=" + udfdate01 + ", " : "")
				+ (udfdate02 != null ? "udfdate02=" + udfdate02 + ", " : "")
				+ (udfdate03 != null ? "udfdate03=" + udfdate03 + ", " : "")
				+ (udfdate04 != null ? "udfdate04=" + udfdate04 + ", " : "")
				+ (udfdate05 != null ? "udfdate05=" + udfdate05 + ", " : "")
				+ (udfdate06 != null ? "udfdate06=" + udfdate06 + ", " : "")
				+ (udfdate07 != null ? "udfdate07=" + udfdate07 + ", " : "")
				+ (udfdate08 != null ? "udfdate08=" + udfdate08 + ", " : "")
				+ (udfdate09 != null ? "udfdate09=" + udfdate09 + ", " : "")
				+ (udfdate10 != null ? "udfdate10=" + udfdate10 : "") + "]";
	}


}
