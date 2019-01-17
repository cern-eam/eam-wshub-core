package ch.cern.eam.wshub.core.tools;


import ch.cern.eam.wshub.core.services.entities.UserDefinedFields;
import net.datastream.schemas.mp_fields.StandardUserDefinedFields;



public class UserDefinedFieldsTools {

	private Tools tools;

	public UserDefinedFieldsTools(Tools tools) {
		this.tools = tools;
	}

	//
	// USER DEFINED AREA
	//
	public UserDefinedFields readInforUserDefinedFields(
			net.datastream.schemas.mp_entities.workorder_001.UserDefinedFields inforUserDefinedFields) {
		UserDefinedFields udfs = new UserDefinedFields();
		if (inforUserDefinedFields == null) {
			return null;
		}

		udfs.setUdfchar01(inforUserDefinedFields.getUDFCHAR01());
		udfs.setUdfchar02(inforUserDefinedFields.getUDFCHAR02());
		udfs.setUdfchar03(inforUserDefinedFields.getUDFCHAR03());
		udfs.setUdfchar04(inforUserDefinedFields.getUDFCHAR04());
		udfs.setUdfchar05(inforUserDefinedFields.getUDFCHAR05());
		udfs.setUdfchar06(inforUserDefinedFields.getUDFCHAR06());
		udfs.setUdfchar07(inforUserDefinedFields.getUDFCHAR07());
		udfs.setUdfchar08(inforUserDefinedFields.getUDFCHAR08());
		udfs.setUdfchar09(inforUserDefinedFields.getUDFCHAR09());
		udfs.setUdfchar10(inforUserDefinedFields.getUDFCHAR10());
		udfs.setUdfchar11(inforUserDefinedFields.getUDFCHAR11());
		udfs.setUdfchar12(inforUserDefinedFields.getUDFCHAR12());
		udfs.setUdfchar13(inforUserDefinedFields.getUDFCHAR13());
		udfs.setUdfchar14(inforUserDefinedFields.getUDFCHAR14());
		udfs.setUdfchar15(inforUserDefinedFields.getUDFCHAR15());
		udfs.setUdfchar16(inforUserDefinedFields.getUDFCHAR16());
		udfs.setUdfchar17(inforUserDefinedFields.getUDFCHAR17());
		udfs.setUdfchar18(inforUserDefinedFields.getUDFCHAR18());
		udfs.setUdfchar19(inforUserDefinedFields.getUDFCHAR19());
		udfs.setUdfchar20(inforUserDefinedFields.getUDFCHAR20());
		udfs.setUdfchar21(inforUserDefinedFields.getUDFCHAR21());
		udfs.setUdfchar22(inforUserDefinedFields.getUDFCHAR22());
		udfs.setUdfchar23(inforUserDefinedFields.getUDFCHAR23());
		udfs.setUdfchar24(inforUserDefinedFields.getUDFCHAR24());
		udfs.setUdfchar25(inforUserDefinedFields.getUDFCHAR25());
		udfs.setUdfchar26(inforUserDefinedFields.getUDFCHAR26());
		udfs.setUdfchar27(inforUserDefinedFields.getUDFCHAR27());
		udfs.setUdfchar28(inforUserDefinedFields.getUDFCHAR28());
		udfs.setUdfchar29(inforUserDefinedFields.getUDFCHAR29());
		udfs.setUdfchar30(inforUserDefinedFields.getUDFCHAR30());

		udfs.setUdfchkbox01(inforUserDefinedFields.getUDFCHKBOX01());
		udfs.setUdfchkbox02(inforUserDefinedFields.getUDFCHKBOX02());
		udfs.setUdfchkbox03(inforUserDefinedFields.getUDFCHKBOX03());
		udfs.setUdfchkbox04(inforUserDefinedFields.getUDFCHKBOX04());
		udfs.setUdfchkbox05(inforUserDefinedFields.getUDFCHKBOX05());

		udfs.setUdfnum01(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM01()));
		udfs.setUdfnum02(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM02()));
		udfs.setUdfnum03(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM03()));
		udfs.setUdfnum04(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM04()));
		udfs.setUdfnum05(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM05()));

		udfs.setUdfdate01(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE01()));
		udfs.setUdfdate02(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE02()));
		udfs.setUdfdate03(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE03()));
		udfs.setUdfdate04(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE04()));
		udfs.setUdfdate05(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE05()));
		// Complete descriptions for RENT fields
		tools.getFieldDescriptionsTools().readUDFRENTDescriptions(udfs, "EVNT");
		return udfs;
	}

	public void updateInforUserDefinedFields(
			net.datastream.schemas.mp_entities.workorder_001.UserDefinedFields inforUserDefinedFields,
			UserDefinedFields userDefinedFields) throws InforException {
		if (userDefinedFields == null) {
			return;
		}

		if (inforUserDefinedFields == null) {
			inforUserDefinedFields = new net.datastream.schemas.mp_entities.workorder_001.UserDefinedFields();
		}

		if (userDefinedFields.getUdfchar01() != null) {
			inforUserDefinedFields.setUDFCHAR01(userDefinedFields.getUdfchar01());
		}

		if (userDefinedFields.getUdfchar02() != null) {
			inforUserDefinedFields.setUDFCHAR02(userDefinedFields.getUdfchar02());
		}

		if (userDefinedFields.getUdfchar03() != null) {
			inforUserDefinedFields.setUDFCHAR03(userDefinedFields.getUdfchar03());
		}

		if (userDefinedFields.getUdfchar04() != null) {
			inforUserDefinedFields.setUDFCHAR04(userDefinedFields.getUdfchar04());
		}

		if (userDefinedFields.getUdfchar05() != null) {
			inforUserDefinedFields.setUDFCHAR05(userDefinedFields.getUdfchar05());
		}

		if (userDefinedFields.getUdfchar06() != null) {
			inforUserDefinedFields.setUDFCHAR06(userDefinedFields.getUdfchar06());
		}

		if (userDefinedFields.getUdfchar07() != null) {
			inforUserDefinedFields.setUDFCHAR07(userDefinedFields.getUdfchar07());
		}

		if (userDefinedFields.getUdfchar08() != null) {
			inforUserDefinedFields.setUDFCHAR08(userDefinedFields.getUdfchar08());
		}

		if (userDefinedFields.getUdfchar09() != null) {
			inforUserDefinedFields.setUDFCHAR09(userDefinedFields.getUdfchar09());
		}

		if (userDefinedFields.getUdfchar10() != null) {
			inforUserDefinedFields.setUDFCHAR10(userDefinedFields.getUdfchar10());
		}

		if (userDefinedFields.getUdfchar11() != null) {
			inforUserDefinedFields.setUDFCHAR11(userDefinedFields.getUdfchar11());
		}

		if (userDefinedFields.getUdfchar12() != null) {
			inforUserDefinedFields.setUDFCHAR12(userDefinedFields.getUdfchar12());
		}

		if (userDefinedFields.getUdfchar13() != null) {
			inforUserDefinedFields.setUDFCHAR13(userDefinedFields.getUdfchar13());
		}

		if (userDefinedFields.getUdfchar14() != null) {
			inforUserDefinedFields.setUDFCHAR14(userDefinedFields.getUdfchar14());
		}

		if (userDefinedFields.getUdfchar15() != null) {
			inforUserDefinedFields.setUDFCHAR15(userDefinedFields.getUdfchar15());
		}

		if (userDefinedFields.getUdfchar16() != null) {
			inforUserDefinedFields.setUDFCHAR16(userDefinedFields.getUdfchar16());
		}

		if (userDefinedFields.getUdfchar17() != null) {
			inforUserDefinedFields.setUDFCHAR17(userDefinedFields.getUdfchar17());
		}

		if (userDefinedFields.getUdfchar18() != null) {
			inforUserDefinedFields.setUDFCHAR18(userDefinedFields.getUdfchar18());
		}

		if (userDefinedFields.getUdfchar19() != null) {
			inforUserDefinedFields.setUDFCHAR19(userDefinedFields.getUdfchar19());
		}

		if (userDefinedFields.getUdfchar20() != null) {
			inforUserDefinedFields.setUDFCHAR20(userDefinedFields.getUdfchar20());
		}

		if (userDefinedFields.getUdfchar21() != null) {
			inforUserDefinedFields.setUDFCHAR21(userDefinedFields.getUdfchar21());
		}

		if (userDefinedFields.getUdfchar22() != null) {
			inforUserDefinedFields.setUDFCHAR22(userDefinedFields.getUdfchar22());
		}

		if (userDefinedFields.getUdfchar23() != null) {
			inforUserDefinedFields.setUDFCHAR23(userDefinedFields.getUdfchar23());
		}

		if (userDefinedFields.getUdfchar24() != null) {
			inforUserDefinedFields.setUDFCHAR24(userDefinedFields.getUdfchar24());
		}

		if (userDefinedFields.getUdfchar25() != null) {
			inforUserDefinedFields.setUDFCHAR25(userDefinedFields.getUdfchar25());
		}

		if (userDefinedFields.getUdfchar26() != null) {
			inforUserDefinedFields.setUDFCHAR26(userDefinedFields.getUdfchar26());
		}

		if (userDefinedFields.getUdfchar27() != null) {
			inforUserDefinedFields.setUDFCHAR27(userDefinedFields.getUdfchar27());
		}

		if (userDefinedFields.getUdfchar28() != null) {
			inforUserDefinedFields.setUDFCHAR28(userDefinedFields.getUdfchar28());
		}

		if (userDefinedFields.getUdfchar29() != null) {
			inforUserDefinedFields.setUDFCHAR29(userDefinedFields.getUdfchar29());
		}

		if (userDefinedFields.getUdfchar30() != null) {
			inforUserDefinedFields.setUDFCHAR30(userDefinedFields.getUdfchar30());
		}

		//
		// DATES
		//

		if (userDefinedFields.getUdfdate01() != null) {
			inforUserDefinedFields.setUDFDATE01(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate01(), "UDFDATE01"));
		}

		if (userDefinedFields.getUdfdate02() != null) {
			inforUserDefinedFields.setUDFDATE02(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate02(), "UDFDATE02"));
		}

		if (userDefinedFields.getUdfdate03() != null) {
			inforUserDefinedFields.setUDFDATE03(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate03(), "UDFDATE03"));
		}

		if (userDefinedFields.getUdfdate04() != null) {
			inforUserDefinedFields.setUDFDATE04(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate04(), "UDFDATE04"));
		}

		if (userDefinedFields.getUdfdate05() != null) {
			inforUserDefinedFields.setUDFDATE05(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate05(), "UDFDATE05"));
		}

		// NUMBERS

		if (userDefinedFields.getUdfnum01() != null) {
			inforUserDefinedFields.setUDFNUM01(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum01(), "UDFDATE01"));
		}

		if (userDefinedFields.getUdfnum02() != null) {
			inforUserDefinedFields.setUDFNUM02(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum02(), "UDFDATE02"));
		}

		if (userDefinedFields.getUdfnum03() != null) {
			inforUserDefinedFields.setUDFNUM03(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum03(), "UDFDATE03"));
		}

		if (userDefinedFields.getUdfnum04() != null) {
			inforUserDefinedFields.setUDFNUM04(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum04(), "UDFDATE04"));
		}

		if (userDefinedFields.getUdfnum05() != null) {
			inforUserDefinedFields.setUDFNUM05(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum05(), "UDFDATE05"));
		}
		//
		// CHECK BOXES
		//
		if (userDefinedFields.getUdfchkbox01() != null) {
			inforUserDefinedFields.setUDFCHKBOX01(userDefinedFields.getUdfchkbox01());
		}

		if (userDefinedFields.getUdfchkbox02() != null) {
			inforUserDefinedFields.setUDFCHKBOX02(userDefinedFields.getUdfchkbox02());
		}

		if (userDefinedFields.getUdfchkbox03() != null) {
			inforUserDefinedFields.setUDFCHKBOX03(userDefinedFields.getUdfchkbox03());
		}

		if (userDefinedFields.getUdfchkbox04() != null) {
			inforUserDefinedFields.setUDFCHKBOX04(userDefinedFields.getUdfchkbox04());
		}

		if (userDefinedFields.getUdfchkbox05() != null) {
			inforUserDefinedFields.setUDFCHKBOX05(userDefinedFields.getUdfchkbox05());
		}
		//
		//
		//

	}

	//
	// STANDARD USER DEFIED FIELDS
	//
	public void updateInforUserDefinedFields(
			StandardUserDefinedFields inforUserDefinedFields,
			UserDefinedFields userDefinedFields) throws InforException {
		if (userDefinedFields == null) {
			return;
		}

		if (inforUserDefinedFields == null) {
			inforUserDefinedFields = new StandardUserDefinedFields();
		}

		if (userDefinedFields.getUdfchar01() != null) {
			inforUserDefinedFields.setUDFCHAR01(userDefinedFields.getUdfchar01());
		}

		if (userDefinedFields.getUdfchar02() != null) {
			inforUserDefinedFields.setUDFCHAR02(userDefinedFields.getUdfchar02());
		}

		if (userDefinedFields.getUdfchar03() != null) {
			inforUserDefinedFields.setUDFCHAR03(userDefinedFields.getUdfchar03());
		}

		if (userDefinedFields.getUdfchar04() != null) {
			inforUserDefinedFields.setUDFCHAR04(userDefinedFields.getUdfchar04());
		}

		if (userDefinedFields.getUdfchar05() != null) {
			inforUserDefinedFields.setUDFCHAR05(userDefinedFields.getUdfchar05());
		}

		if (userDefinedFields.getUdfchar06() != null) {
			inforUserDefinedFields.setUDFCHAR06(userDefinedFields.getUdfchar06());
		}

		if (userDefinedFields.getUdfchar07() != null) {
			inforUserDefinedFields.setUDFCHAR07(userDefinedFields.getUdfchar07());
		}

		if (userDefinedFields.getUdfchar08() != null) {
			inforUserDefinedFields.setUDFCHAR08(userDefinedFields.getUdfchar08());
		}

		if (userDefinedFields.getUdfchar09() != null) {
			inforUserDefinedFields.setUDFCHAR09(userDefinedFields.getUdfchar09());
		}

		if (userDefinedFields.getUdfchar10() != null) {
			inforUserDefinedFields.setUDFCHAR10(userDefinedFields.getUdfchar10());
		}

		if (userDefinedFields.getUdfchar11() != null) {
			inforUserDefinedFields.setUDFCHAR11(userDefinedFields.getUdfchar11());
		}

		if (userDefinedFields.getUdfchar12() != null) {
			inforUserDefinedFields.setUDFCHAR12(userDefinedFields.getUdfchar12());
		}

		if (userDefinedFields.getUdfchar13() != null) {
			inforUserDefinedFields.setUDFCHAR13(userDefinedFields.getUdfchar13());
		}

		if (userDefinedFields.getUdfchar14() != null) {
			inforUserDefinedFields.setUDFCHAR14(userDefinedFields.getUdfchar14());
		}

		if (userDefinedFields.getUdfchar15() != null) {
			inforUserDefinedFields.setUDFCHAR15(userDefinedFields.getUdfchar15());
		}

		if (userDefinedFields.getUdfchar16() != null) {
			inforUserDefinedFields.setUDFCHAR16(userDefinedFields.getUdfchar16());
		}

		if (userDefinedFields.getUdfchar17() != null) {
			inforUserDefinedFields.setUDFCHAR17(userDefinedFields.getUdfchar17());
		}

		if (userDefinedFields.getUdfchar18() != null) {
			inforUserDefinedFields.setUDFCHAR18(userDefinedFields.getUdfchar18());
		}

		if (userDefinedFields.getUdfchar19() != null) {
			inforUserDefinedFields.setUDFCHAR19(userDefinedFields.getUdfchar19());
		}

		if (userDefinedFields.getUdfchar20() != null) {
			inforUserDefinedFields.setUDFCHAR20(userDefinedFields.getUdfchar20());
		}

		if (userDefinedFields.getUdfchar21() != null) {
			inforUserDefinedFields.setUDFCHAR21(userDefinedFields.getUdfchar21());
		}

		if (userDefinedFields.getUdfchar22() != null) {
			inforUserDefinedFields.setUDFCHAR22(userDefinedFields.getUdfchar22());
		}

		if (userDefinedFields.getUdfchar23() != null) {
			inforUserDefinedFields.setUDFCHAR23(userDefinedFields.getUdfchar23());
		}

		if (userDefinedFields.getUdfchar24() != null) {
			inforUserDefinedFields.setUDFCHAR24(userDefinedFields.getUdfchar24());
		}

		if (userDefinedFields.getUdfchar25() != null) {
			inforUserDefinedFields.setUDFCHAR25(userDefinedFields.getUdfchar25());
		}

		if (userDefinedFields.getUdfchar26() != null) {
			inforUserDefinedFields.setUDFCHAR26(userDefinedFields.getUdfchar26());
		}

		if (userDefinedFields.getUdfchar27() != null) {
			inforUserDefinedFields.setUDFCHAR27(userDefinedFields.getUdfchar27());
		}

		if (userDefinedFields.getUdfchar28() != null) {
			inforUserDefinedFields.setUDFCHAR28(userDefinedFields.getUdfchar28());
		}

		if (userDefinedFields.getUdfchar29() != null) {
			inforUserDefinedFields.setUDFCHAR29(userDefinedFields.getUdfchar29());
		}

		if (userDefinedFields.getUdfchar30() != null) {
			inforUserDefinedFields.setUDFCHAR30(userDefinedFields.getUdfchar30());
		}

		//
		// DATES
		//

		if (userDefinedFields.getUdfdate01() != null) {
			inforUserDefinedFields.setUDFDATE01(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate01(), "UDFDATE01"));
		}

		if (userDefinedFields.getUdfdate02() != null) {
			inforUserDefinedFields.setUDFDATE02(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate02(), "UDFDATE02"));
		}

		if (userDefinedFields.getUdfdate03() != null) {
			inforUserDefinedFields.setUDFDATE03(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate03(), "UDFDATE03"));
		}

		if (userDefinedFields.getUdfdate04() != null) {
			inforUserDefinedFields.setUDFDATE04(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate04(), "UDFDATE04"));
		}

		if (userDefinedFields.getUdfdate05() != null) {
			inforUserDefinedFields.setUDFDATE05(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate05(), "UDFDATE05"));
		}

		// NUMBERS

		if (userDefinedFields.getUdfnum01() != null) {
			inforUserDefinedFields.setUDFNUM01(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum01(), "UDFDATE01"));
		}

		if (userDefinedFields.getUdfnum02() != null) {
			inforUserDefinedFields.setUDFNUM02(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum02(), "UDFDATE02"));
		}

		if (userDefinedFields.getUdfnum03() != null) {
			inforUserDefinedFields.setUDFNUM03(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum03(), "UDFDATE03"));
		}

		if (userDefinedFields.getUdfnum04() != null) {
			inforUserDefinedFields.setUDFNUM04(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum04(), "UDFDATE04"));
		}

		if (userDefinedFields.getUdfnum05() != null) {
			inforUserDefinedFields.setUDFNUM05(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum05(), "UDFDATE05"));
		}

		//
		// CHECK BOXES
		//

		if (userDefinedFields.getUdfchkbox01() != null) {
			inforUserDefinedFields.setUDFCHKBOX01(userDefinedFields.getUdfchkbox01());
		}

		if (userDefinedFields.getUdfchkbox02() != null) {
			inforUserDefinedFields.setUDFCHKBOX02(userDefinedFields.getUdfchkbox02());
		}

		if (userDefinedFields.getUdfchkbox03() != null) {
			inforUserDefinedFields.setUDFCHKBOX03(userDefinedFields.getUdfchkbox03());
		}

		if (userDefinedFields.getUdfchkbox04() != null) {
			inforUserDefinedFields.setUDFCHKBOX04(userDefinedFields.getUdfchkbox04());
		}

		if (userDefinedFields.getUdfchkbox05() != null) {
			inforUserDefinedFields.setUDFCHKBOX05(userDefinedFields.getUdfchkbox05());
		}

	}

	public UserDefinedFields readInforUserDefinedFields(
			StandardUserDefinedFields inforUserDefinedFields) {
		UserDefinedFields udfs = new UserDefinedFields();
		if (inforUserDefinedFields == null) {
			return null;
		}

		udfs.setUdfchar01(inforUserDefinedFields.getUDFCHAR01());
		udfs.setUdfchar02(inforUserDefinedFields.getUDFCHAR02());
		udfs.setUdfchar03(inforUserDefinedFields.getUDFCHAR03());
		udfs.setUdfchar04(inforUserDefinedFields.getUDFCHAR04());
		udfs.setUdfchar05(inforUserDefinedFields.getUDFCHAR05());
		udfs.setUdfchar06(inforUserDefinedFields.getUDFCHAR06());
		udfs.setUdfchar07(inforUserDefinedFields.getUDFCHAR07());
		udfs.setUdfchar08(inforUserDefinedFields.getUDFCHAR08());
		udfs.setUdfchar09(inforUserDefinedFields.getUDFCHAR09());
		udfs.setUdfchar10(inforUserDefinedFields.getUDFCHAR10());
		udfs.setUdfchar11(inforUserDefinedFields.getUDFCHAR11());
		udfs.setUdfchar12(inforUserDefinedFields.getUDFCHAR12());
		udfs.setUdfchar13(inforUserDefinedFields.getUDFCHAR13());
		udfs.setUdfchar14(inforUserDefinedFields.getUDFCHAR14());
		udfs.setUdfchar15(inforUserDefinedFields.getUDFCHAR15());
		udfs.setUdfchar16(inforUserDefinedFields.getUDFCHAR16());
		udfs.setUdfchar17(inforUserDefinedFields.getUDFCHAR17());
		udfs.setUdfchar18(inforUserDefinedFields.getUDFCHAR18());
		udfs.setUdfchar19(inforUserDefinedFields.getUDFCHAR19());
		udfs.setUdfchar20(inforUserDefinedFields.getUDFCHAR20());
		udfs.setUdfchar21(inforUserDefinedFields.getUDFCHAR21());
		udfs.setUdfchar22(inforUserDefinedFields.getUDFCHAR22());
		udfs.setUdfchar23(inforUserDefinedFields.getUDFCHAR23());
		udfs.setUdfchar24(inforUserDefinedFields.getUDFCHAR24());
		udfs.setUdfchar25(inforUserDefinedFields.getUDFCHAR25());
		udfs.setUdfchar26(inforUserDefinedFields.getUDFCHAR26());
		udfs.setUdfchar27(inforUserDefinedFields.getUDFCHAR27());
		udfs.setUdfchar28(inforUserDefinedFields.getUDFCHAR28());
		udfs.setUdfchar29(inforUserDefinedFields.getUDFCHAR29());
		udfs.setUdfchar30(inforUserDefinedFields.getUDFCHAR30());

		udfs.setUdfchkbox01(inforUserDefinedFields.getUDFCHKBOX01());
		udfs.setUdfchkbox02(inforUserDefinedFields.getUDFCHKBOX02());
		udfs.setUdfchkbox03(inforUserDefinedFields.getUDFCHKBOX03());
		udfs.setUdfchkbox04(inforUserDefinedFields.getUDFCHKBOX04());
		udfs.setUdfchkbox05(inforUserDefinedFields.getUDFCHKBOX05());

		udfs.setUdfnum01(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM01()));
		udfs.setUdfnum02(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM02()));
		udfs.setUdfnum03(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM03()));
		udfs.setUdfnum04(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM04()));
		udfs.setUdfnum05(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM05()));

		udfs.setUdfdate01(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE01()));
		udfs.setUdfdate02(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE02()));
		udfs.setUdfdate03(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE03()));
		udfs.setUdfdate04(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE04()));
		udfs.setUdfdate05(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE05()));

		return udfs;
	}

	//
	// USER DEFIED FIELDS FOR PARTS
	//
	public void updateInforUserDefinedFields(
			net.datastream.schemas.mp_entities.part_001.UserDefinedFields inforUserDefinedFields,
			UserDefinedFields userDefinedFields) throws InforException {
		if (userDefinedFields == null) {
			return;
		}

		if (inforUserDefinedFields == null) {
			inforUserDefinedFields = new net.datastream.schemas.mp_entities.part_001.UserDefinedFields();
		}

		if (userDefinedFields.getUdfchar01() != null) {
			inforUserDefinedFields.setUDFCHAR01(userDefinedFields.getUdfchar01());
		}

		if (userDefinedFields.getUdfchar02() != null) {
			inforUserDefinedFields.setUDFCHAR02(userDefinedFields.getUdfchar02());
		}

		if (userDefinedFields.getUdfchar03() != null) {
			inforUserDefinedFields.setUDFCHAR03(userDefinedFields.getUdfchar03());
		}

		if (userDefinedFields.getUdfchar04() != null) {
			inforUserDefinedFields.setUDFCHAR04(userDefinedFields.getUdfchar04());
		}

		if (userDefinedFields.getUdfchar05() != null) {
			inforUserDefinedFields.setUDFCHAR05(userDefinedFields.getUdfchar05());
		}

		if (userDefinedFields.getUdfchar06() != null) {
			inforUserDefinedFields.setUDFCHAR06(userDefinedFields.getUdfchar06());
		}

		if (userDefinedFields.getUdfchar07() != null) {
			inforUserDefinedFields.setUDFCHAR07(userDefinedFields.getUdfchar07());
		}

		if (userDefinedFields.getUdfchar08() != null) {
			inforUserDefinedFields.setUDFCHAR08(userDefinedFields.getUdfchar08());
		}

		if (userDefinedFields.getUdfchar09() != null) {
			inforUserDefinedFields.setUDFCHAR09(userDefinedFields.getUdfchar09());
		}

		if (userDefinedFields.getUdfchar10() != null) {
			inforUserDefinedFields.setUDFCHAR10(userDefinedFields.getUdfchar10());
		}

		if (userDefinedFields.getUdfchar11() != null) {
			inforUserDefinedFields.setUDFCHAR11(userDefinedFields.getUdfchar11());
		}

		if (userDefinedFields.getUdfchar12() != null) {
			inforUserDefinedFields.setUDFCHAR12(userDefinedFields.getUdfchar12());
		}

		if (userDefinedFields.getUdfchar13() != null) {
			inforUserDefinedFields.setUDFCHAR13(userDefinedFields.getUdfchar13());
		}

		if (userDefinedFields.getUdfchar14() != null) {
			inforUserDefinedFields.setUDFCHAR14(userDefinedFields.getUdfchar14());
		}

		if (userDefinedFields.getUdfchar15() != null) {
			inforUserDefinedFields.setUDFCHAR15(userDefinedFields.getUdfchar15());
		}

		if (userDefinedFields.getUdfchar16() != null) {
			inforUserDefinedFields.setUDFCHAR16(userDefinedFields.getUdfchar16());
		}

		if (userDefinedFields.getUdfchar17() != null) {
			inforUserDefinedFields.setUDFCHAR17(userDefinedFields.getUdfchar17());
		}

		if (userDefinedFields.getUdfchar18() != null) {
			inforUserDefinedFields.setUDFCHAR18(userDefinedFields.getUdfchar18());
		}

		if (userDefinedFields.getUdfchar19() != null) {
			inforUserDefinedFields.setUDFCHAR19(userDefinedFields.getUdfchar19());
		}

		if (userDefinedFields.getUdfchar20() != null) {
			inforUserDefinedFields.setUDFCHAR20(userDefinedFields.getUdfchar20());
		}

		if (userDefinedFields.getUdfchar21() != null) {
			inforUserDefinedFields.setUDFCHAR21(userDefinedFields.getUdfchar21());
		}

		if (userDefinedFields.getUdfchar22() != null) {
			inforUserDefinedFields.setUDFCHAR22(userDefinedFields.getUdfchar22());
		}

		if (userDefinedFields.getUdfchar23() != null) {
			inforUserDefinedFields.setUDFCHAR23(userDefinedFields.getUdfchar23());
		}

		if (userDefinedFields.getUdfchar24() != null) {
			inforUserDefinedFields.setUDFCHAR24(userDefinedFields.getUdfchar24());
		}

		if (userDefinedFields.getUdfchar25() != null) {
			inforUserDefinedFields.setUDFCHAR25(userDefinedFields.getUdfchar25());
		}

		if (userDefinedFields.getUdfchar26() != null) {
			inforUserDefinedFields.setUDFCHAR26(userDefinedFields.getUdfchar26());
		}

		if (userDefinedFields.getUdfchar27() != null) {
			inforUserDefinedFields.setUDFCHAR27(userDefinedFields.getUdfchar27());
		}

		if (userDefinedFields.getUdfchar28() != null) {
			inforUserDefinedFields.setUDFCHAR28(userDefinedFields.getUdfchar28());
		}

		if (userDefinedFields.getUdfchar29() != null) {
			inforUserDefinedFields.setUDFCHAR29(userDefinedFields.getUdfchar29());
		}

		if (userDefinedFields.getUdfchar30() != null) {
			inforUserDefinedFields.setUDFCHAR30(userDefinedFields.getUdfchar30());
		}

		//
		// DATES
		//

		if (userDefinedFields.getUdfdate01() != null) {
			inforUserDefinedFields.setUDFDATE01(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate01(), "UDFDATE01"));
		}

		if (userDefinedFields.getUdfdate02() != null) {
			inforUserDefinedFields.setUDFDATE02(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate02(), "UDFDATE02"));
		}

		if (userDefinedFields.getUdfdate03() != null) {
			inforUserDefinedFields.setUDFDATE03(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate03(), "UDFDATE03"));
		}

		if (userDefinedFields.getUdfdate04() != null) {
			inforUserDefinedFields.setUDFDATE04(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate04(), "UDFDATE04"));
		}

		if (userDefinedFields.getUdfdate05() != null) {
			inforUserDefinedFields.setUDFDATE05(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate05(), "UDFDATE05"));
		}

		// NUMBERS

		if (userDefinedFields.getUdfnum01() != null) {
			inforUserDefinedFields.setUDFNUM01(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum01(), "UDFDATE01"));
		}

		if (userDefinedFields.getUdfnum02() != null) {
			inforUserDefinedFields.setUDFNUM02(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum02(), "UDFDATE02"));
		}

		if (userDefinedFields.getUdfnum03() != null) {
			inforUserDefinedFields.setUDFNUM03(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum03(), "UDFDATE03"));
		}

		if (userDefinedFields.getUdfnum04() != null) {
			inforUserDefinedFields.setUDFNUM04(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum04(), "UDFDATE04"));
		}

		if (userDefinedFields.getUdfnum05() != null) {
			inforUserDefinedFields.setUDFNUM05(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum05(), "UDFDATE05"));
		}

		//
		// CHECK BOXES
		//

		if (userDefinedFields.getUdfchkbox01() != null) {
			inforUserDefinedFields.setUDFCHKBOX01(userDefinedFields.getUdfchkbox01());
		}

		if (userDefinedFields.getUdfchkbox02() != null) {
			inforUserDefinedFields.setUDFCHKBOX02(userDefinedFields.getUdfchkbox02());
		}

		if (userDefinedFields.getUdfchkbox03() != null) {
			inforUserDefinedFields.setUDFCHKBOX03(userDefinedFields.getUdfchkbox03());
		}

		if (userDefinedFields.getUdfchkbox04() != null) {
			inforUserDefinedFields.setUDFCHKBOX04(userDefinedFields.getUdfchkbox04());
		}

		if (userDefinedFields.getUdfchkbox05() != null) {
			inforUserDefinedFields.setUDFCHKBOX05(userDefinedFields.getUdfchkbox05());
		}

	}

	public UserDefinedFields readInforUserDefinedFields(
			net.datastream.schemas.mp_entities.part_001.UserDefinedFields inforUserDefinedFields) {
		UserDefinedFields udfs = new UserDefinedFields();
		if (inforUserDefinedFields == null) {
			return null;
		}

		udfs.setUdfchar01(inforUserDefinedFields.getUDFCHAR01());
		udfs.setUdfchar02(inforUserDefinedFields.getUDFCHAR02());
		udfs.setUdfchar03(inforUserDefinedFields.getUDFCHAR03());
		udfs.setUdfchar04(inforUserDefinedFields.getUDFCHAR04());
		udfs.setUdfchar05(inforUserDefinedFields.getUDFCHAR05());
		udfs.setUdfchar06(inforUserDefinedFields.getUDFCHAR06());
		udfs.setUdfchar07(inforUserDefinedFields.getUDFCHAR07());
		udfs.setUdfchar08(inforUserDefinedFields.getUDFCHAR08());
		udfs.setUdfchar09(inforUserDefinedFields.getUDFCHAR09());
		udfs.setUdfchar10(inforUserDefinedFields.getUDFCHAR10());
		udfs.setUdfchar11(inforUserDefinedFields.getUDFCHAR11());
		udfs.setUdfchar12(inforUserDefinedFields.getUDFCHAR12());
		udfs.setUdfchar13(inforUserDefinedFields.getUDFCHAR13());
		udfs.setUdfchar14(inforUserDefinedFields.getUDFCHAR14());
		udfs.setUdfchar15(inforUserDefinedFields.getUDFCHAR15());
		udfs.setUdfchar16(inforUserDefinedFields.getUDFCHAR16());
		udfs.setUdfchar17(inforUserDefinedFields.getUDFCHAR17());
		udfs.setUdfchar18(inforUserDefinedFields.getUDFCHAR18());
		udfs.setUdfchar19(inforUserDefinedFields.getUDFCHAR19());
		udfs.setUdfchar20(inforUserDefinedFields.getUDFCHAR20());
		udfs.setUdfchar21(inforUserDefinedFields.getUDFCHAR21());
		udfs.setUdfchar22(inforUserDefinedFields.getUDFCHAR22());
		udfs.setUdfchar23(inforUserDefinedFields.getUDFCHAR23());
		udfs.setUdfchar24(inforUserDefinedFields.getUDFCHAR24());
		udfs.setUdfchar25(inforUserDefinedFields.getUDFCHAR25());
		udfs.setUdfchar26(inforUserDefinedFields.getUDFCHAR26());
		udfs.setUdfchar27(inforUserDefinedFields.getUDFCHAR27());
		udfs.setUdfchar28(inforUserDefinedFields.getUDFCHAR28());
		udfs.setUdfchar29(inforUserDefinedFields.getUDFCHAR29());
		udfs.setUdfchar30(inforUserDefinedFields.getUDFCHAR30());

		udfs.setUdfchkbox01(inforUserDefinedFields.getUDFCHKBOX01());
		udfs.setUdfchkbox02(inforUserDefinedFields.getUDFCHKBOX02());
		udfs.setUdfchkbox03(inforUserDefinedFields.getUDFCHKBOX03());
		udfs.setUdfchkbox04(inforUserDefinedFields.getUDFCHKBOX04());
		udfs.setUdfchkbox05(inforUserDefinedFields.getUDFCHKBOX05());

		udfs.setUdfnum01(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM01()));
		udfs.setUdfnum02(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM02()));
		udfs.setUdfnum03(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM03()));
		udfs.setUdfnum04(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM04()));
		udfs.setUdfnum05(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM05()));

		udfs.setUdfdate01(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE01()));
		udfs.setUdfdate02(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE02()));
		udfs.setUdfdate03(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE03()));
		udfs.setUdfdate04(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE04()));
		udfs.setUdfdate05(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE05()));
		// Complete descriptions for RENT fields
		tools.getFieldDescriptionsTools().readUDFRENTDescriptions(udfs, "PART");
		return udfs;
	}

	//
	// USER DEFINED FIELDS FOR ASSETS
	//
	public void updateInforUserDefinedFields(
			net.datastream.schemas.mp_entities.assetequipment_001.UserDefinedFields inforUserDefinedFields,
			UserDefinedFields userDefinedFields) throws InforException {
		if (userDefinedFields == null) {
			return;
		}

		if (inforUserDefinedFields == null) {
			inforUserDefinedFields = new net.datastream.schemas.mp_entities.assetequipment_001.UserDefinedFields();
		}

		if (userDefinedFields.getUdfchar01() != null) {
			inforUserDefinedFields.setUDFCHAR01(userDefinedFields.getUdfchar01());
		}

		if (userDefinedFields.getUdfchar02() != null) {
			inforUserDefinedFields.setUDFCHAR02(userDefinedFields.getUdfchar02());
		}

		if (userDefinedFields.getUdfchar03() != null) {
			inforUserDefinedFields.setUDFCHAR03(userDefinedFields.getUdfchar03());
		}

		if (userDefinedFields.getUdfchar04() != null) {
			inforUserDefinedFields.setUDFCHAR04(userDefinedFields.getUdfchar04());
		}

		if (userDefinedFields.getUdfchar05() != null) {
			inforUserDefinedFields.setUDFCHAR05(userDefinedFields.getUdfchar05());
		}

		if (userDefinedFields.getUdfchar06() != null) {
			inforUserDefinedFields.setUDFCHAR06(userDefinedFields.getUdfchar06());
		}

		if (userDefinedFields.getUdfchar07() != null) {
			inforUserDefinedFields.setUDFCHAR07(userDefinedFields.getUdfchar07());
		}

		if (userDefinedFields.getUdfchar08() != null) {
			inforUserDefinedFields.setUDFCHAR08(userDefinedFields.getUdfchar08());
		}

		if (userDefinedFields.getUdfchar09() != null) {
			inforUserDefinedFields.setUDFCHAR09(userDefinedFields.getUdfchar09());
		}

		if (userDefinedFields.getUdfchar10() != null) {
			inforUserDefinedFields.setUDFCHAR10(userDefinedFields.getUdfchar10());
		}

		if (userDefinedFields.getUdfchar11() != null) {
			inforUserDefinedFields.setUDFCHAR11(userDefinedFields.getUdfchar11());
		}

		if (userDefinedFields.getUdfchar12() != null) {
			inforUserDefinedFields.setUDFCHAR12(userDefinedFields.getUdfchar12());
		}

		if (userDefinedFields.getUdfchar13() != null) {
			inforUserDefinedFields.setUDFCHAR13(userDefinedFields.getUdfchar13());
		}

		if (userDefinedFields.getUdfchar14() != null) {
			inforUserDefinedFields.setUDFCHAR14(userDefinedFields.getUdfchar14());
		}

		if (userDefinedFields.getUdfchar15() != null) {
			inforUserDefinedFields.setUDFCHAR15(userDefinedFields.getUdfchar15());
		}

		if (userDefinedFields.getUdfchar16() != null) {
			inforUserDefinedFields.setUDFCHAR16(userDefinedFields.getUdfchar16());
		}

		if (userDefinedFields.getUdfchar17() != null) {
			inforUserDefinedFields.setUDFCHAR17(userDefinedFields.getUdfchar17());
		}

		if (userDefinedFields.getUdfchar18() != null) {
			inforUserDefinedFields.setUDFCHAR18(userDefinedFields.getUdfchar18());
		}

		if (userDefinedFields.getUdfchar19() != null) {
			inforUserDefinedFields.setUDFCHAR19(userDefinedFields.getUdfchar19());
		}

		if (userDefinedFields.getUdfchar20() != null) {
			inforUserDefinedFields.setUDFCHAR20(userDefinedFields.getUdfchar20());
		}

		if (userDefinedFields.getUdfchar21() != null) {
			inforUserDefinedFields.setUDFCHAR21(userDefinedFields.getUdfchar21());
		}

		if (userDefinedFields.getUdfchar22() != null) {
			inforUserDefinedFields.setUDFCHAR22(userDefinedFields.getUdfchar22());
		}

		if (userDefinedFields.getUdfchar23() != null) {
			inforUserDefinedFields.setUDFCHAR23(userDefinedFields.getUdfchar23());
		}

		if (userDefinedFields.getUdfchar24() != null) {
			inforUserDefinedFields.setUDFCHAR24(userDefinedFields.getUdfchar24());
		}

		if (userDefinedFields.getUdfchar25() != null) {
			inforUserDefinedFields.setUDFCHAR25(userDefinedFields.getUdfchar25());
		}

		if (userDefinedFields.getUdfchar26() != null) {
			inforUserDefinedFields.setUDFCHAR26(userDefinedFields.getUdfchar26());
		}

		if (userDefinedFields.getUdfchar27() != null) {
			inforUserDefinedFields.setUDFCHAR27(userDefinedFields.getUdfchar27());
		}

		if (userDefinedFields.getUdfchar28() != null) {
			inforUserDefinedFields.setUDFCHAR28(userDefinedFields.getUdfchar28());
		}

		if (userDefinedFields.getUdfchar29() != null) {
			inforUserDefinedFields.setUDFCHAR29(userDefinedFields.getUdfchar29());
		}

		if (userDefinedFields.getUdfchar30() != null) {
			inforUserDefinedFields.setUDFCHAR30(userDefinedFields.getUdfchar30());
		}

		if (userDefinedFields.getUdfchar31() != null) {
			inforUserDefinedFields.setUDFCHAR31(userDefinedFields.getUdfchar31());
		}

		if (userDefinedFields.getUdfchar32() != null) {
			inforUserDefinedFields.setUDFCHAR32(userDefinedFields.getUdfchar32());
		}

		if (userDefinedFields.getUdfchar33() != null) {
			inforUserDefinedFields.setUDFCHAR33(userDefinedFields.getUdfchar33());
		}

		if (userDefinedFields.getUdfchar34() != null) {
			inforUserDefinedFields.setUDFCHAR34(userDefinedFields.getUdfchar34());
		}

		if (userDefinedFields.getUdfchar35() != null) {
			inforUserDefinedFields.setUDFCHAR35(userDefinedFields.getUdfchar35());
		}

		if (userDefinedFields.getUdfchar36() != null) {
			inforUserDefinedFields.setUDFCHAR36(userDefinedFields.getUdfchar36());
		}

		if (userDefinedFields.getUdfchar37() != null) {
			inforUserDefinedFields.setUDFCHAR37(userDefinedFields.getUdfchar37());
		}

		if (userDefinedFields.getUdfchar38() != null) {
			inforUserDefinedFields.setUDFCHAR38(userDefinedFields.getUdfchar38());
		}

		if (userDefinedFields.getUdfchar39() != null) {
			inforUserDefinedFields.setUDFCHAR39(userDefinedFields.getUdfchar39());
		}

		if (userDefinedFields.getUdfchar40() != null) {
			inforUserDefinedFields.setUDFCHAR40(userDefinedFields.getUdfchar40());
		}

		if (userDefinedFields.getUdfchar30() != null) {
			inforUserDefinedFields.setUDFCHAR30(userDefinedFields.getUdfchar30());
		}

		if (userDefinedFields.getUdfchar41() != null) {
			inforUserDefinedFields.setUDFCHAR41(userDefinedFields.getUdfchar41());
		}

		if (userDefinedFields.getUdfchar42() != null) {
			inforUserDefinedFields.setUDFCHAR42(userDefinedFields.getUdfchar42());
		}

		if (userDefinedFields.getUdfchar43() != null) {
			inforUserDefinedFields.setUDFCHAR43(userDefinedFields.getUdfchar43());
		}

		if (userDefinedFields.getUdfchar44() != null) {
			inforUserDefinedFields.setUDFCHAR44(userDefinedFields.getUdfchar44());
		}

		if (userDefinedFields.getUdfchar45() != null) {
			inforUserDefinedFields.setUDFCHAR45(userDefinedFields.getUdfchar45());
		}

		//
		// DATES
		//

		if (userDefinedFields.getUdfdate01() != null) {
			inforUserDefinedFields.setUDFDATE01(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate01(), "UDFDATE01"));
		}

		if (userDefinedFields.getUdfdate02() != null) {
			inforUserDefinedFields.setUDFDATE02(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate02(), "UDFDATE02"));
		}

		if (userDefinedFields.getUdfdate03() != null) {
			inforUserDefinedFields.setUDFDATE03(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate03(), "UDFDATE03"));
		}

		if (userDefinedFields.getUdfdate04() != null) {
			inforUserDefinedFields.setUDFDATE04(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate04(), "UDFDATE04"));
		}

		if (userDefinedFields.getUdfdate05() != null) {
			inforUserDefinedFields.setUDFDATE05(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate05(), "UDFDATE05"));
		}

		if (userDefinedFields.getUdfdate06() != null) {
			inforUserDefinedFields.setUDFDATE06(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate06(), "UDFDATE06"));
		}

		if (userDefinedFields.getUdfdate07() != null) {
			inforUserDefinedFields.setUDFDATE07(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate07(), "UDFDATE07"));
		}

		if (userDefinedFields.getUdfdate08() != null) {
			inforUserDefinedFields.setUDFDATE08(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate08(), "UDFDATE08"));
		}

		if (userDefinedFields.getUdfdate09() != null) {
			inforUserDefinedFields.setUDFDATE09(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate09(), "UDFDATE09"));
		}

		if (userDefinedFields.getUdfdate10() != null) {
			inforUserDefinedFields.setUDFDATE10(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate10(), "UDFDATE10"));
		}

		// NUMBERS

		if (userDefinedFields.getUdfnum01() != null) {
			inforUserDefinedFields.setUDFNUM01(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum01(), "UDFDATE01"));
		}

		if (userDefinedFields.getUdfnum02() != null) {
			inforUserDefinedFields.setUDFNUM02(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum02(), "UDFDATE02"));
		}

		if (userDefinedFields.getUdfnum03() != null) {
			inforUserDefinedFields.setUDFNUM03(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum03(), "UDFDATE03"));
		}

		if (userDefinedFields.getUdfnum04() != null) {
			inforUserDefinedFields.setUDFNUM04(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum04(), "UDFDATE04"));
		}

		if (userDefinedFields.getUdfnum05() != null) {
			inforUserDefinedFields.setUDFNUM05(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum05(), "UDFDATE05"));
		}

		if (userDefinedFields.getUdfnum06() != null) {
			inforUserDefinedFields.setUDFNUM06(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum06(), "UDFDATE06"));
		}

		if (userDefinedFields.getUdfnum07() != null) {
			inforUserDefinedFields.setUDFNUM07(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum07(), "UDFDATE07"));
		}

		if (userDefinedFields.getUdfnum08() != null) {
			inforUserDefinedFields.setUDFNUM08(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum08(), "UDFDATE08"));
		}

		if (userDefinedFields.getUdfnum09() != null) {
			inforUserDefinedFields.setUDFNUM09(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum09(), "UDFDATE09"));
		}

		if (userDefinedFields.getUdfnum10() != null) {
			inforUserDefinedFields.setUDFNUM10(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum10(), "UDFDATE10"));
		}

		//
		// CHECK BOXES
		//

		if (userDefinedFields.getUdfchkbox01() != null) {
			inforUserDefinedFields.setUDFCHKBOX01(userDefinedFields.getUdfchkbox01());
		}

		if (userDefinedFields.getUdfchkbox02() != null) {
			inforUserDefinedFields.setUDFCHKBOX02(userDefinedFields.getUdfchkbox02());
		}

		if (userDefinedFields.getUdfchkbox03() != null) {
			inforUserDefinedFields.setUDFCHKBOX03(userDefinedFields.getUdfchkbox03());
		}

		if (userDefinedFields.getUdfchkbox04() != null) {
			inforUserDefinedFields.setUDFCHKBOX04(userDefinedFields.getUdfchkbox04());
		}

		if (userDefinedFields.getUdfchkbox05() != null) {
			inforUserDefinedFields.setUDFCHKBOX05(userDefinedFields.getUdfchkbox05());
		}

		if (userDefinedFields.getUdfchkbox06() != null) {
			inforUserDefinedFields.setUDFCHKBOX06(userDefinedFields.getUdfchkbox06());
		}

		if (userDefinedFields.getUdfchkbox07() != null) {
			inforUserDefinedFields.setUDFCHKBOX07(userDefinedFields.getUdfchkbox07());
		}

		if (userDefinedFields.getUdfchkbox08() != null) {
			inforUserDefinedFields.setUDFCHKBOX08(userDefinedFields.getUdfchkbox08());
		}

		if (userDefinedFields.getUdfchkbox09() != null) {
			inforUserDefinedFields.setUDFCHKBOX09(userDefinedFields.getUdfchkbox09());
		}

		if (userDefinedFields.getUdfchkbox10() != null) {
			inforUserDefinedFields.setUDFCHKBOX10(userDefinedFields.getUdfchkbox10());
		}

	}

	public void updateInforUserDefinedFields(
			net.datastream.schemas.mp_entities.positionequipment_001.UserDefinedFields inforUserDefinedFields,
			UserDefinedFields userDefinedFields) throws InforException {
		if (userDefinedFields == null) {
			return;
		}

		if (inforUserDefinedFields == null) {
			inforUserDefinedFields = new net.datastream.schemas.mp_entities.positionequipment_001.UserDefinedFields();
		}

		if (userDefinedFields.getUdfchar01() != null) {
			inforUserDefinedFields.setUDFCHAR01(userDefinedFields.getUdfchar01());
		}

		if (userDefinedFields.getUdfchar02() != null) {
			inforUserDefinedFields.setUDFCHAR02(userDefinedFields.getUdfchar02());
		}

		if (userDefinedFields.getUdfchar03() != null) {
			inforUserDefinedFields.setUDFCHAR03(userDefinedFields.getUdfchar03());
		}

		if (userDefinedFields.getUdfchar04() != null) {
			inforUserDefinedFields.setUDFCHAR04(userDefinedFields.getUdfchar04());
		}

		if (userDefinedFields.getUdfchar05() != null) {
			inforUserDefinedFields.setUDFCHAR05(userDefinedFields.getUdfchar05());
		}

		if (userDefinedFields.getUdfchar06() != null) {
			inforUserDefinedFields.setUDFCHAR06(userDefinedFields.getUdfchar06());
		}

		if (userDefinedFields.getUdfchar07() != null) {
			inforUserDefinedFields.setUDFCHAR07(userDefinedFields.getUdfchar07());
		}

		if (userDefinedFields.getUdfchar08() != null) {
			inforUserDefinedFields.setUDFCHAR08(userDefinedFields.getUdfchar08());
		}

		if (userDefinedFields.getUdfchar09() != null) {
			inforUserDefinedFields.setUDFCHAR09(userDefinedFields.getUdfchar09());
		}

		if (userDefinedFields.getUdfchar10() != null) {
			inforUserDefinedFields.setUDFCHAR10(userDefinedFields.getUdfchar10());
		}

		if (userDefinedFields.getUdfchar11() != null) {
			inforUserDefinedFields.setUDFCHAR11(userDefinedFields.getUdfchar11());
		}

		if (userDefinedFields.getUdfchar12() != null) {
			inforUserDefinedFields.setUDFCHAR12(userDefinedFields.getUdfchar12());
		}

		if (userDefinedFields.getUdfchar13() != null) {
			inforUserDefinedFields.setUDFCHAR13(userDefinedFields.getUdfchar13());
		}

		if (userDefinedFields.getUdfchar14() != null) {
			inforUserDefinedFields.setUDFCHAR14(userDefinedFields.getUdfchar14());
		}

		if (userDefinedFields.getUdfchar15() != null) {
			inforUserDefinedFields.setUDFCHAR15(userDefinedFields.getUdfchar15());
		}

		if (userDefinedFields.getUdfchar16() != null) {
			inforUserDefinedFields.setUDFCHAR16(userDefinedFields.getUdfchar16());
		}

		if (userDefinedFields.getUdfchar17() != null) {
			inforUserDefinedFields.setUDFCHAR17(userDefinedFields.getUdfchar17());
		}

		if (userDefinedFields.getUdfchar18() != null) {
			inforUserDefinedFields.setUDFCHAR18(userDefinedFields.getUdfchar18());
		}

		if (userDefinedFields.getUdfchar19() != null) {
			inforUserDefinedFields.setUDFCHAR19(userDefinedFields.getUdfchar19());
		}

		if (userDefinedFields.getUdfchar20() != null) {
			inforUserDefinedFields.setUDFCHAR20(userDefinedFields.getUdfchar20());
		}

		if (userDefinedFields.getUdfchar21() != null) {
			inforUserDefinedFields.setUDFCHAR21(userDefinedFields.getUdfchar21());
		}

		if (userDefinedFields.getUdfchar22() != null) {
			inforUserDefinedFields.setUDFCHAR22(userDefinedFields.getUdfchar22());
		}

		if (userDefinedFields.getUdfchar23() != null) {
			inforUserDefinedFields.setUDFCHAR23(userDefinedFields.getUdfchar23());
		}

		if (userDefinedFields.getUdfchar24() != null) {
			inforUserDefinedFields.setUDFCHAR24(userDefinedFields.getUdfchar24());
		}

		if (userDefinedFields.getUdfchar25() != null) {
			inforUserDefinedFields.setUDFCHAR25(userDefinedFields.getUdfchar25());
		}

		if (userDefinedFields.getUdfchar26() != null) {
			inforUserDefinedFields.setUDFCHAR26(userDefinedFields.getUdfchar26());
		}

		if (userDefinedFields.getUdfchar27() != null) {
			inforUserDefinedFields.setUDFCHAR27(userDefinedFields.getUdfchar27());
		}

		if (userDefinedFields.getUdfchar28() != null) {
			inforUserDefinedFields.setUDFCHAR28(userDefinedFields.getUdfchar28());
		}

		if (userDefinedFields.getUdfchar29() != null) {
			inforUserDefinedFields.setUDFCHAR29(userDefinedFields.getUdfchar29());
		}

		if (userDefinedFields.getUdfchar30() != null) {
			inforUserDefinedFields.setUDFCHAR30(userDefinedFields.getUdfchar30());
		}

		if (userDefinedFields.getUdfchar31() != null) {
			inforUserDefinedFields.setUDFCHAR31(userDefinedFields.getUdfchar31());
		}

		if (userDefinedFields.getUdfchar32() != null) {
			inforUserDefinedFields.setUDFCHAR32(userDefinedFields.getUdfchar32());
		}

		if (userDefinedFields.getUdfchar33() != null) {
			inforUserDefinedFields.setUDFCHAR33(userDefinedFields.getUdfchar33());
		}

		if (userDefinedFields.getUdfchar34() != null) {
			inforUserDefinedFields.setUDFCHAR34(userDefinedFields.getUdfchar34());
		}

		if (userDefinedFields.getUdfchar35() != null) {
			inforUserDefinedFields.setUDFCHAR35(userDefinedFields.getUdfchar35());
		}

		if (userDefinedFields.getUdfchar36() != null) {
			inforUserDefinedFields.setUDFCHAR36(userDefinedFields.getUdfchar36());
		}

		if (userDefinedFields.getUdfchar37() != null) {
			inforUserDefinedFields.setUDFCHAR37(userDefinedFields.getUdfchar37());
		}

		if (userDefinedFields.getUdfchar38() != null) {
			inforUserDefinedFields.setUDFCHAR38(userDefinedFields.getUdfchar38());
		}

		if (userDefinedFields.getUdfchar39() != null) {
			inforUserDefinedFields.setUDFCHAR39(userDefinedFields.getUdfchar39());
		}

		if (userDefinedFields.getUdfchar40() != null) {
			inforUserDefinedFields.setUDFCHAR40(userDefinedFields.getUdfchar40());
		}

		if (userDefinedFields.getUdfchar30() != null) {
			inforUserDefinedFields.setUDFCHAR30(userDefinedFields.getUdfchar30());
		}

		if (userDefinedFields.getUdfchar41() != null) {
			inforUserDefinedFields.setUDFCHAR41(userDefinedFields.getUdfchar41());
		}

		if (userDefinedFields.getUdfchar42() != null) {
			inforUserDefinedFields.setUDFCHAR42(userDefinedFields.getUdfchar42());
		}

		if (userDefinedFields.getUdfchar43() != null) {
			inforUserDefinedFields.setUDFCHAR43(userDefinedFields.getUdfchar43());
		}

		if (userDefinedFields.getUdfchar44() != null) {
			inforUserDefinedFields.setUDFCHAR44(userDefinedFields.getUdfchar44());
		}

		if (userDefinedFields.getUdfchar45() != null) {
			inforUserDefinedFields.setUDFCHAR45(userDefinedFields.getUdfchar45());
		}

		//
		// DATES
		//

		if (userDefinedFields.getUdfdate01() != null) {
			inforUserDefinedFields.setUDFDATE01(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate01(), "UDFDATE01"));
		}

		if (userDefinedFields.getUdfdate02() != null) {
			inforUserDefinedFields.setUDFDATE02(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate02(), "UDFDATE02"));
		}

		if (userDefinedFields.getUdfdate03() != null) {
			inforUserDefinedFields.setUDFDATE03(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate03(), "UDFDATE03"));
		}

		if (userDefinedFields.getUdfdate04() != null) {
			inforUserDefinedFields.setUDFDATE04(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate04(), "UDFDATE04"));
		}

		if (userDefinedFields.getUdfdate05() != null) {
			inforUserDefinedFields.setUDFDATE05(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate05(), "UDFDATE05"));
		}

		if (userDefinedFields.getUdfdate06() != null) {
			inforUserDefinedFields.setUDFDATE06(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate06(), "UDFDATE06"));
		}

		if (userDefinedFields.getUdfdate07() != null) {
			inforUserDefinedFields.setUDFDATE07(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate07(), "UDFDATE07"));
		}

		if (userDefinedFields.getUdfdate08() != null) {
			inforUserDefinedFields.setUDFDATE08(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate08(), "UDFDATE08"));
		}

		if (userDefinedFields.getUdfdate09() != null) {
			inforUserDefinedFields.setUDFDATE09(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate09(), "UDFDATE09"));
		}

		if (userDefinedFields.getUdfdate10() != null) {
			inforUserDefinedFields.setUDFDATE10(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate10(), "UDFDATE10"));
		}

		// NUMBERS

		if (userDefinedFields.getUdfnum01() != null) {
			inforUserDefinedFields.setUDFNUM01(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum01(), "UDFDATE01"));
		}

		if (userDefinedFields.getUdfnum02() != null) {
			inforUserDefinedFields.setUDFNUM02(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum02(), "UDFDATE02"));
		}

		if (userDefinedFields.getUdfnum03() != null) {
			inforUserDefinedFields.setUDFNUM03(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum03(), "UDFDATE03"));
		}

		if (userDefinedFields.getUdfnum04() != null) {
			inforUserDefinedFields.setUDFNUM04(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum04(), "UDFDATE04"));
		}

		if (userDefinedFields.getUdfnum05() != null) {
			inforUserDefinedFields.setUDFNUM05(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum05(), "UDFDATE05"));
		}

		if (userDefinedFields.getUdfnum06() != null) {
			inforUserDefinedFields.setUDFNUM06(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum06(), "UDFDATE06"));
		}

		if (userDefinedFields.getUdfnum07() != null) {
			inforUserDefinedFields.setUDFNUM07(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum07(), "UDFDATE07"));
		}

		if (userDefinedFields.getUdfnum08() != null) {
			inforUserDefinedFields.setUDFNUM08(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum08(), "UDFDATE08"));
		}

		if (userDefinedFields.getUdfnum09() != null) {
			inforUserDefinedFields.setUDFNUM09(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum09(), "UDFDATE09"));
		}

		if (userDefinedFields.getUdfnum10() != null) {
			inforUserDefinedFields.setUDFNUM10(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum10(), "UDFDATE10"));
		}

		//
		// CHECK BOXES
		//

		if (userDefinedFields.getUdfchkbox01() != null) {
			inforUserDefinedFields.setUDFCHKBOX01(userDefinedFields.getUdfchkbox01());
		}

		if (userDefinedFields.getUdfchkbox02() != null) {
			inforUserDefinedFields.setUDFCHKBOX02(userDefinedFields.getUdfchkbox02());
		}

		if (userDefinedFields.getUdfchkbox03() != null) {
			inforUserDefinedFields.setUDFCHKBOX03(userDefinedFields.getUdfchkbox03());
		}

		if (userDefinedFields.getUdfchkbox04() != null) {
			inforUserDefinedFields.setUDFCHKBOX04(userDefinedFields.getUdfchkbox04());
		}

		if (userDefinedFields.getUdfchkbox05() != null) {
			inforUserDefinedFields.setUDFCHKBOX05(userDefinedFields.getUdfchkbox05());
		}

		if (userDefinedFields.getUdfchkbox06() != null) {
			inforUserDefinedFields.setUDFCHKBOX06(userDefinedFields.getUdfchkbox06());
		}

		if (userDefinedFields.getUdfchkbox07() != null) {
			inforUserDefinedFields.setUDFCHKBOX07(userDefinedFields.getUdfchkbox07());
		}

		if (userDefinedFields.getUdfchkbox08() != null) {
			inforUserDefinedFields.setUDFCHKBOX08(userDefinedFields.getUdfchkbox08());
		}

		if (userDefinedFields.getUdfchkbox09() != null) {
			inforUserDefinedFields.setUDFCHKBOX09(userDefinedFields.getUdfchkbox09());
		}

		if (userDefinedFields.getUdfchkbox10() != null) {
			inforUserDefinedFields.setUDFCHKBOX10(userDefinedFields.getUdfchkbox10());
		}
	}

	public void updateInforUserDefinedFields(
			net.datastream.schemas.mp_entities.systemequipment_001.UserDefinedFields inforUserDefinedFields,
			UserDefinedFields userDefinedFields) throws InforException {
		if (userDefinedFields == null) {
			return;
		}

		if (inforUserDefinedFields == null) {
			inforUserDefinedFields = new net.datastream.schemas.mp_entities.systemequipment_001.UserDefinedFields();
		}

		if (userDefinedFields.getUdfchar01() != null) {
			inforUserDefinedFields.setUDFCHAR01(userDefinedFields.getUdfchar01());
		}

		if (userDefinedFields.getUdfchar02() != null) {
			inforUserDefinedFields.setUDFCHAR02(userDefinedFields.getUdfchar02());
		}

		if (userDefinedFields.getUdfchar03() != null) {
			inforUserDefinedFields.setUDFCHAR03(userDefinedFields.getUdfchar03());
		}

		if (userDefinedFields.getUdfchar04() != null) {
			inforUserDefinedFields.setUDFCHAR04(userDefinedFields.getUdfchar04());
		}

		if (userDefinedFields.getUdfchar05() != null) {
			inforUserDefinedFields.setUDFCHAR05(userDefinedFields.getUdfchar05());
		}

		if (userDefinedFields.getUdfchar06() != null) {
			inforUserDefinedFields.setUDFCHAR06(userDefinedFields.getUdfchar06());
		}

		if (userDefinedFields.getUdfchar07() != null) {
			inforUserDefinedFields.setUDFCHAR07(userDefinedFields.getUdfchar07());
		}

		if (userDefinedFields.getUdfchar08() != null) {
			inforUserDefinedFields.setUDFCHAR08(userDefinedFields.getUdfchar08());
		}

		if (userDefinedFields.getUdfchar09() != null) {
			inforUserDefinedFields.setUDFCHAR09(userDefinedFields.getUdfchar09());
		}

		if (userDefinedFields.getUdfchar10() != null) {
			inforUserDefinedFields.setUDFCHAR10(userDefinedFields.getUdfchar10());
		}

		if (userDefinedFields.getUdfchar11() != null) {
			inforUserDefinedFields.setUDFCHAR11(userDefinedFields.getUdfchar11());
		}

		if (userDefinedFields.getUdfchar12() != null) {
			inforUserDefinedFields.setUDFCHAR12(userDefinedFields.getUdfchar12());
		}

		if (userDefinedFields.getUdfchar13() != null) {
			inforUserDefinedFields.setUDFCHAR13(userDefinedFields.getUdfchar13());
		}

		if (userDefinedFields.getUdfchar14() != null) {
			inforUserDefinedFields.setUDFCHAR14(userDefinedFields.getUdfchar14());
		}

		if (userDefinedFields.getUdfchar15() != null) {
			inforUserDefinedFields.setUDFCHAR15(userDefinedFields.getUdfchar15());
		}

		if (userDefinedFields.getUdfchar16() != null) {
			inforUserDefinedFields.setUDFCHAR16(userDefinedFields.getUdfchar16());
		}

		if (userDefinedFields.getUdfchar17() != null) {
			inforUserDefinedFields.setUDFCHAR17(userDefinedFields.getUdfchar17());
		}

		if (userDefinedFields.getUdfchar18() != null) {
			inforUserDefinedFields.setUDFCHAR18(userDefinedFields.getUdfchar18());
		}

		if (userDefinedFields.getUdfchar19() != null) {
			inforUserDefinedFields.setUDFCHAR19(userDefinedFields.getUdfchar19());
		}

		if (userDefinedFields.getUdfchar20() != null) {
			inforUserDefinedFields.setUDFCHAR20(userDefinedFields.getUdfchar20());
		}

		if (userDefinedFields.getUdfchar21() != null) {
			inforUserDefinedFields.setUDFCHAR21(userDefinedFields.getUdfchar21());
		}

		if (userDefinedFields.getUdfchar22() != null) {
			inforUserDefinedFields.setUDFCHAR22(userDefinedFields.getUdfchar22());
		}

		if (userDefinedFields.getUdfchar23() != null) {
			inforUserDefinedFields.setUDFCHAR23(userDefinedFields.getUdfchar23());
		}

		if (userDefinedFields.getUdfchar24() != null) {
			inforUserDefinedFields.setUDFCHAR24(userDefinedFields.getUdfchar24());
		}

		if (userDefinedFields.getUdfchar25() != null) {
			inforUserDefinedFields.setUDFCHAR25(userDefinedFields.getUdfchar25());
		}

		if (userDefinedFields.getUdfchar26() != null) {
			inforUserDefinedFields.setUDFCHAR26(userDefinedFields.getUdfchar26());
		}

		if (userDefinedFields.getUdfchar27() != null) {
			inforUserDefinedFields.setUDFCHAR27(userDefinedFields.getUdfchar27());
		}

		if (userDefinedFields.getUdfchar28() != null) {
			inforUserDefinedFields.setUDFCHAR28(userDefinedFields.getUdfchar28());
		}

		if (userDefinedFields.getUdfchar29() != null) {
			inforUserDefinedFields.setUDFCHAR29(userDefinedFields.getUdfchar29());
		}

		if (userDefinedFields.getUdfchar30() != null) {
			inforUserDefinedFields.setUDFCHAR30(userDefinedFields.getUdfchar30());
		}

		if (userDefinedFields.getUdfchar31() != null) {
			inforUserDefinedFields.setUDFCHAR31(userDefinedFields.getUdfchar31());
		}

		if (userDefinedFields.getUdfchar32() != null) {
			inforUserDefinedFields.setUDFCHAR32(userDefinedFields.getUdfchar32());
		}

		if (userDefinedFields.getUdfchar33() != null) {
			inforUserDefinedFields.setUDFCHAR33(userDefinedFields.getUdfchar33());
		}

		if (userDefinedFields.getUdfchar34() != null) {
			inforUserDefinedFields.setUDFCHAR34(userDefinedFields.getUdfchar34());
		}

		if (userDefinedFields.getUdfchar35() != null) {
			inforUserDefinedFields.setUDFCHAR35(userDefinedFields.getUdfchar35());
		}

		if (userDefinedFields.getUdfchar36() != null) {
			inforUserDefinedFields.setUDFCHAR36(userDefinedFields.getUdfchar36());
		}

		if (userDefinedFields.getUdfchar37() != null) {
			inforUserDefinedFields.setUDFCHAR37(userDefinedFields.getUdfchar37());
		}

		if (userDefinedFields.getUdfchar38() != null) {
			inforUserDefinedFields.setUDFCHAR38(userDefinedFields.getUdfchar38());
		}

		if (userDefinedFields.getUdfchar39() != null) {
			inforUserDefinedFields.setUDFCHAR39(userDefinedFields.getUdfchar39());
		}

		if (userDefinedFields.getUdfchar40() != null) {
			inforUserDefinedFields.setUDFCHAR40(userDefinedFields.getUdfchar40());
		}

		if (userDefinedFields.getUdfchar30() != null) {
			inforUserDefinedFields.setUDFCHAR30(userDefinedFields.getUdfchar30());
		}

		if (userDefinedFields.getUdfchar41() != null) {
			inforUserDefinedFields.setUDFCHAR41(userDefinedFields.getUdfchar41());
		}

		if (userDefinedFields.getUdfchar42() != null) {
			inforUserDefinedFields.setUDFCHAR42(userDefinedFields.getUdfchar42());
		}

		if (userDefinedFields.getUdfchar43() != null) {
			inforUserDefinedFields.setUDFCHAR43(userDefinedFields.getUdfchar43());
		}

		if (userDefinedFields.getUdfchar44() != null) {
			inforUserDefinedFields.setUDFCHAR44(userDefinedFields.getUdfchar44());
		}

		if (userDefinedFields.getUdfchar45() != null) {
			inforUserDefinedFields.setUDFCHAR45(userDefinedFields.getUdfchar45());
		}

		//
		// DATES
		//

		if (userDefinedFields.getUdfdate01() != null) {
			inforUserDefinedFields.setUDFDATE01(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate01(), "UDFDATE01"));
		}

		if (userDefinedFields.getUdfdate02() != null) {
			inforUserDefinedFields.setUDFDATE02(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate02(), "UDFDATE02"));
		}

		if (userDefinedFields.getUdfdate03() != null) {
			inforUserDefinedFields.setUDFDATE03(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate03(), "UDFDATE03"));
		}

		if (userDefinedFields.getUdfdate04() != null) {
			inforUserDefinedFields.setUDFDATE04(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate04(), "UDFDATE04"));
		}

		if (userDefinedFields.getUdfdate05() != null) {
			inforUserDefinedFields.setUDFDATE05(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate05(), "UDFDATE05"));
		}

		if (userDefinedFields.getUdfdate06() != null) {
			inforUserDefinedFields.setUDFDATE06(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate06(), "UDFDATE06"));
		}

		if (userDefinedFields.getUdfdate07() != null) {
			inforUserDefinedFields.setUDFDATE07(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate07(), "UDFDATE07"));
		}

		if (userDefinedFields.getUdfdate08() != null) {
			inforUserDefinedFields.setUDFDATE08(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate08(), "UDFDATE08"));
		}

		if (userDefinedFields.getUdfdate09() != null) {
			inforUserDefinedFields.setUDFDATE09(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate09(), "UDFDATE09"));
		}

		if (userDefinedFields.getUdfdate10() != null) {
			inforUserDefinedFields.setUDFDATE10(tools.getDataTypeTools().encodeInforDate(userDefinedFields.getUdfdate10(), "UDFDATE10"));
		}

		// TODO UDFDATE -> UDFNUM!

		// NUMBERS

		if (userDefinedFields.getUdfnum01() != null) {
			inforUserDefinedFields.setUDFNUM01(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum01(), "UDFNUM01"));
		}

		if (userDefinedFields.getUdfnum02() != null) {
			inforUserDefinedFields.setUDFNUM02(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum02(), "UDFNUM02"));
		}

		if (userDefinedFields.getUdfnum03() != null) {
			inforUserDefinedFields.setUDFNUM03(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum03(), "UDFNUM03"));
		}

		if (userDefinedFields.getUdfnum04() != null) {
			inforUserDefinedFields.setUDFNUM04(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum04(), "UDFNUM04"));
		}

		if (userDefinedFields.getUdfnum05() != null) {
			inforUserDefinedFields.setUDFNUM05(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum05(), "UDFNUM05"));
		}

		if (userDefinedFields.getUdfnum06() != null) {
			inforUserDefinedFields.setUDFNUM06(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum06(), "UDFNUM06"));
		}

		if (userDefinedFields.getUdfnum07() != null) {
			inforUserDefinedFields.setUDFNUM07(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum07(), "UDFNUM07"));
		}

		if (userDefinedFields.getUdfnum08() != null) {
			inforUserDefinedFields.setUDFNUM08(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum08(), "UDFNUM08"));
		}

		if (userDefinedFields.getUdfnum09() != null) {
			inforUserDefinedFields.setUDFNUM09(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum09(), "UDFNUM09"));
		}

		if (userDefinedFields.getUdfnum10() != null) {
			inforUserDefinedFields.setUDFNUM10(tools.getDataTypeTools().encodeQuantity(userDefinedFields.getUdfnum10(), "UDFNUM10"));
		}

		//
		// CHECK BOXES
		//

		if (userDefinedFields.getUdfchkbox01() != null) {
			inforUserDefinedFields.setUDFCHKBOX01(userDefinedFields.getUdfchkbox01());
		}

		if (userDefinedFields.getUdfchkbox02() != null) {
			inforUserDefinedFields.setUDFCHKBOX02(userDefinedFields.getUdfchkbox02());
		}

		if (userDefinedFields.getUdfchkbox03() != null) {
			inforUserDefinedFields.setUDFCHKBOX03(userDefinedFields.getUdfchkbox03());
		}

		if (userDefinedFields.getUdfchkbox04() != null) {
			inforUserDefinedFields.setUDFCHKBOX04(userDefinedFields.getUdfchkbox04());
		}

		if (userDefinedFields.getUdfchkbox05() != null) {
			inforUserDefinedFields.setUDFCHKBOX05(userDefinedFields.getUdfchkbox05());
		}

		if (userDefinedFields.getUdfchkbox06() != null) {
			inforUserDefinedFields.setUDFCHKBOX06(userDefinedFields.getUdfchkbox06());
		}

		if (userDefinedFields.getUdfchkbox07() != null) {
			inforUserDefinedFields.setUDFCHKBOX07(userDefinedFields.getUdfchkbox07());
		}

		if (userDefinedFields.getUdfchkbox08() != null) {
			inforUserDefinedFields.setUDFCHKBOX08(userDefinedFields.getUdfchkbox08());
		}

		if (userDefinedFields.getUdfchkbox09() != null) {
			inforUserDefinedFields.setUDFCHKBOX09(userDefinedFields.getUdfchkbox09());
		}

		if (userDefinedFields.getUdfchkbox10() != null) {
			inforUserDefinedFields.setUDFCHKBOX10(userDefinedFields.getUdfchkbox10());
		}
	}

	public UserDefinedFields readInforUserDefinedFields(
			net.datastream.schemas.mp_entities.standardworkorder_001.UserDefinedFields inforUserDefinedFields) {
		UserDefinedFields udfs = new UserDefinedFields();
		if (inforUserDefinedFields == null) {
			return null;
		}

		udfs.setUdfchar01(inforUserDefinedFields.getUDFCHAR01());
		udfs.setUdfchar02(inforUserDefinedFields.getUDFCHAR02());
		udfs.setUdfchar03(inforUserDefinedFields.getUDFCHAR03());
		udfs.setUdfchar04(inforUserDefinedFields.getUDFCHAR04());
		udfs.setUdfchar05(inforUserDefinedFields.getUDFCHAR05());
		udfs.setUdfchar06(inforUserDefinedFields.getUDFCHAR06());
		udfs.setUdfchar07(inforUserDefinedFields.getUDFCHAR07());
		udfs.setUdfchar08(inforUserDefinedFields.getUDFCHAR08());
		udfs.setUdfchar09(inforUserDefinedFields.getUDFCHAR09());
		udfs.setUdfchar10(inforUserDefinedFields.getUDFCHAR10());
		udfs.setUdfchar11(inforUserDefinedFields.getUDFCHAR11());
		udfs.setUdfchar12(inforUserDefinedFields.getUDFCHAR12());
		udfs.setUdfchar13(inforUserDefinedFields.getUDFCHAR13());
		udfs.setUdfchar14(inforUserDefinedFields.getUDFCHAR14());
		udfs.setUdfchar15(inforUserDefinedFields.getUDFCHAR15());
		udfs.setUdfchar16(inforUserDefinedFields.getUDFCHAR16());
		udfs.setUdfchar17(inforUserDefinedFields.getUDFCHAR17());
		udfs.setUdfchar18(inforUserDefinedFields.getUDFCHAR18());
		udfs.setUdfchar19(inforUserDefinedFields.getUDFCHAR19());
		udfs.setUdfchar20(inforUserDefinedFields.getUDFCHAR20());
		udfs.setUdfchar21(inforUserDefinedFields.getUDFCHAR21());
		udfs.setUdfchar22(inforUserDefinedFields.getUDFCHAR22());
		udfs.setUdfchar23(inforUserDefinedFields.getUDFCHAR23());
		udfs.setUdfchar24(inforUserDefinedFields.getUDFCHAR24());
		udfs.setUdfchar25(inforUserDefinedFields.getUDFCHAR25());
		udfs.setUdfchar26(inforUserDefinedFields.getUDFCHAR26());
		udfs.setUdfchar27(inforUserDefinedFields.getUDFCHAR27());
		udfs.setUdfchar28(inforUserDefinedFields.getUDFCHAR28());
		udfs.setUdfchar29(inforUserDefinedFields.getUDFCHAR29());
		udfs.setUdfchar30(inforUserDefinedFields.getUDFCHAR30());

		udfs.setUdfchkbox01(inforUserDefinedFields.getUDFCHKBOX01());
		udfs.setUdfchkbox02(inforUserDefinedFields.getUDFCHKBOX02());
		udfs.setUdfchkbox03(inforUserDefinedFields.getUDFCHKBOX03());
		udfs.setUdfchkbox04(inforUserDefinedFields.getUDFCHKBOX04());
		udfs.setUdfchkbox05(inforUserDefinedFields.getUDFCHKBOX05());

		udfs.setUdfnum01(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM01()));
		udfs.setUdfnum02(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM02()));
		udfs.setUdfnum03(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM03()));
		udfs.setUdfnum04(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM04()));
		udfs.setUdfnum05(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM05()));

		udfs.setUdfdate01(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE01()));
		udfs.setUdfdate02(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE02()));
		udfs.setUdfdate03(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE03()));
		udfs.setUdfdate04(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE04()));
		udfs.setUdfdate05(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE05()));
		return udfs;
	}

	public UserDefinedFields readInforUserDefinedFields(
			net.datastream.schemas.mp_entities.positionequipment_001.UserDefinedFields inforUserDefinedFields) {
		UserDefinedFields udfs = new UserDefinedFields();
		if (inforUserDefinedFields == null) {
			return null;
		}
		udfs.setUdfchar01(inforUserDefinedFields.getUDFCHAR01());
		udfs.setUdfchar02(inforUserDefinedFields.getUDFCHAR02());
		udfs.setUdfchar03(inforUserDefinedFields.getUDFCHAR03());
		udfs.setUdfchar04(inforUserDefinedFields.getUDFCHAR04());
		udfs.setUdfchar05(inforUserDefinedFields.getUDFCHAR05());
		udfs.setUdfchar06(inforUserDefinedFields.getUDFCHAR06());
		udfs.setUdfchar07(inforUserDefinedFields.getUDFCHAR07());
		udfs.setUdfchar08(inforUserDefinedFields.getUDFCHAR08());
		udfs.setUdfchar09(inforUserDefinedFields.getUDFCHAR09());
		udfs.setUdfchar10(inforUserDefinedFields.getUDFCHAR10());
		udfs.setUdfchar11(inforUserDefinedFields.getUDFCHAR11());
		udfs.setUdfchar12(inforUserDefinedFields.getUDFCHAR12());
		udfs.setUdfchar13(inforUserDefinedFields.getUDFCHAR13());
		udfs.setUdfchar14(inforUserDefinedFields.getUDFCHAR14());
		udfs.setUdfchar15(inforUserDefinedFields.getUDFCHAR15());
		udfs.setUdfchar16(inforUserDefinedFields.getUDFCHAR16());
		udfs.setUdfchar17(inforUserDefinedFields.getUDFCHAR17());
		udfs.setUdfchar18(inforUserDefinedFields.getUDFCHAR18());
		udfs.setUdfchar19(inforUserDefinedFields.getUDFCHAR19());
		udfs.setUdfchar20(inforUserDefinedFields.getUDFCHAR20());
		udfs.setUdfchar21(inforUserDefinedFields.getUDFCHAR21());
		udfs.setUdfchar22(inforUserDefinedFields.getUDFCHAR22());
		udfs.setUdfchar23(inforUserDefinedFields.getUDFCHAR23());
		udfs.setUdfchar24(inforUserDefinedFields.getUDFCHAR24());
		udfs.setUdfchar25(inforUserDefinedFields.getUDFCHAR25());
		udfs.setUdfchar26(inforUserDefinedFields.getUDFCHAR26());
		udfs.setUdfchar27(inforUserDefinedFields.getUDFCHAR27());
		udfs.setUdfchar28(inforUserDefinedFields.getUDFCHAR28());
		udfs.setUdfchar29(inforUserDefinedFields.getUDFCHAR29());
		udfs.setUdfchar30(inforUserDefinedFields.getUDFCHAR30());
		udfs.setUdfchar31(inforUserDefinedFields.getUDFCHAR31());
		udfs.setUdfchar32(inforUserDefinedFields.getUDFCHAR32());
		udfs.setUdfchar33(inforUserDefinedFields.getUDFCHAR33());
		udfs.setUdfchar34(inforUserDefinedFields.getUDFCHAR34());
		udfs.setUdfchar35(inforUserDefinedFields.getUDFCHAR35());
		udfs.setUdfchar36(inforUserDefinedFields.getUDFCHAR36());
		udfs.setUdfchar37(inforUserDefinedFields.getUDFCHAR37());
		udfs.setUdfchar38(inforUserDefinedFields.getUDFCHAR38());
		udfs.setUdfchar39(inforUserDefinedFields.getUDFCHAR39());
		udfs.setUdfchar40(inforUserDefinedFields.getUDFCHAR40());
		udfs.setUdfchar41(inforUserDefinedFields.getUDFCHAR41());
		udfs.setUdfchar42(inforUserDefinedFields.getUDFCHAR42());
		udfs.setUdfchar43(inforUserDefinedFields.getUDFCHAR43());
		udfs.setUdfchar44(inforUserDefinedFields.getUDFCHAR44());
		udfs.setUdfchar45(inforUserDefinedFields.getUDFCHAR45());

		udfs.setUdfchkbox01(inforUserDefinedFields.getUDFCHKBOX01());
		udfs.setUdfchkbox02(inforUserDefinedFields.getUDFCHKBOX02());
		udfs.setUdfchkbox03(inforUserDefinedFields.getUDFCHKBOX03());
		udfs.setUdfchkbox04(inforUserDefinedFields.getUDFCHKBOX04());
		udfs.setUdfchkbox05(inforUserDefinedFields.getUDFCHKBOX05());
		udfs.setUdfchkbox06(inforUserDefinedFields.getUDFCHKBOX06());
		udfs.setUdfchkbox07(inforUserDefinedFields.getUDFCHKBOX07());
		udfs.setUdfchkbox08(inforUserDefinedFields.getUDFCHKBOX08());
		udfs.setUdfchkbox09(inforUserDefinedFields.getUDFCHKBOX09());
		udfs.setUdfchkbox10(inforUserDefinedFields.getUDFCHKBOX10());

		udfs.setUdfnum01(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM01()));
		udfs.setUdfnum02(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM02()));
		udfs.setUdfnum03(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM03()));
		udfs.setUdfnum04(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM04()));
		udfs.setUdfnum05(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM05()));
		udfs.setUdfnum06(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM06()));
		udfs.setUdfnum07(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM07()));
		udfs.setUdfnum08(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM08()));
		udfs.setUdfnum09(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM09()));
		udfs.setUdfnum10(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM10()));

		udfs.setUdfdate01(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE01()));
		udfs.setUdfdate02(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE02()));
		udfs.setUdfdate03(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE03()));
		udfs.setUdfdate04(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE04()));
		udfs.setUdfdate05(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE05()));
		udfs.setUdfdate06(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE06()));
		udfs.setUdfdate07(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE07()));
		udfs.setUdfdate08(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE08()));
		udfs.setUdfdate09(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE09()));
		udfs.setUdfdate10(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE10()));

		// Complete descriptions for RENT fields
		tools.getFieldDescriptionsTools().readUDFRENTDescriptions(udfs, "OBJ");
		return udfs;
	}

	public UserDefinedFields readInforUserDefinedFields(
			net.datastream.schemas.mp_entities.assetequipment_001.UserDefinedFields inforUserDefinedFields) {
		UserDefinedFields udfs = new UserDefinedFields();
		if (inforUserDefinedFields == null) {
			return null;
		}

		udfs.setUdfchar01(inforUserDefinedFields.getUDFCHAR01());
		udfs.setUdfchar02(inforUserDefinedFields.getUDFCHAR02());
		udfs.setUdfchar03(inforUserDefinedFields.getUDFCHAR03());
		udfs.setUdfchar04(inforUserDefinedFields.getUDFCHAR04());
		udfs.setUdfchar05(inforUserDefinedFields.getUDFCHAR05());
		udfs.setUdfchar06(inforUserDefinedFields.getUDFCHAR06());
		udfs.setUdfchar07(inforUserDefinedFields.getUDFCHAR07());
		udfs.setUdfchar08(inforUserDefinedFields.getUDFCHAR08());
		udfs.setUdfchar09(inforUserDefinedFields.getUDFCHAR09());
		udfs.setUdfchar10(inforUserDefinedFields.getUDFCHAR10());
		udfs.setUdfchar11(inforUserDefinedFields.getUDFCHAR11());
		udfs.setUdfchar12(inforUserDefinedFields.getUDFCHAR12());
		udfs.setUdfchar13(inforUserDefinedFields.getUDFCHAR13());
		udfs.setUdfchar14(inforUserDefinedFields.getUDFCHAR14());
		udfs.setUdfchar15(inforUserDefinedFields.getUDFCHAR15());
		udfs.setUdfchar16(inforUserDefinedFields.getUDFCHAR16());
		udfs.setUdfchar17(inforUserDefinedFields.getUDFCHAR17());
		udfs.setUdfchar18(inforUserDefinedFields.getUDFCHAR18());
		udfs.setUdfchar19(inforUserDefinedFields.getUDFCHAR19());
		udfs.setUdfchar20(inforUserDefinedFields.getUDFCHAR20());
		udfs.setUdfchar21(inforUserDefinedFields.getUDFCHAR21());
		udfs.setUdfchar22(inforUserDefinedFields.getUDFCHAR22());
		udfs.setUdfchar23(inforUserDefinedFields.getUDFCHAR23());
		udfs.setUdfchar24(inforUserDefinedFields.getUDFCHAR24());
		udfs.setUdfchar25(inforUserDefinedFields.getUDFCHAR25());
		udfs.setUdfchar26(inforUserDefinedFields.getUDFCHAR26());
		udfs.setUdfchar27(inforUserDefinedFields.getUDFCHAR27());
		udfs.setUdfchar28(inforUserDefinedFields.getUDFCHAR28());
		udfs.setUdfchar29(inforUserDefinedFields.getUDFCHAR29());
		udfs.setUdfchar30(inforUserDefinedFields.getUDFCHAR30());
		udfs.setUdfchar31(inforUserDefinedFields.getUDFCHAR31());
		udfs.setUdfchar32(inforUserDefinedFields.getUDFCHAR32());
		udfs.setUdfchar33(inforUserDefinedFields.getUDFCHAR33());
		udfs.setUdfchar34(inforUserDefinedFields.getUDFCHAR34());
		udfs.setUdfchar35(inforUserDefinedFields.getUDFCHAR35());
		udfs.setUdfchar36(inforUserDefinedFields.getUDFCHAR36());
		udfs.setUdfchar37(inforUserDefinedFields.getUDFCHAR37());
		udfs.setUdfchar38(inforUserDefinedFields.getUDFCHAR38());
		udfs.setUdfchar39(inforUserDefinedFields.getUDFCHAR39());
		udfs.setUdfchar40(inforUserDefinedFields.getUDFCHAR40());
		udfs.setUdfchar41(inforUserDefinedFields.getUDFCHAR41());
		udfs.setUdfchar42(inforUserDefinedFields.getUDFCHAR42());
		udfs.setUdfchar43(inforUserDefinedFields.getUDFCHAR43());
		udfs.setUdfchar44(inforUserDefinedFields.getUDFCHAR44());
		udfs.setUdfchar45(inforUserDefinedFields.getUDFCHAR45());

		udfs.setUdfchkbox01(inforUserDefinedFields.getUDFCHKBOX01());
		udfs.setUdfchkbox02(inforUserDefinedFields.getUDFCHKBOX02());
		udfs.setUdfchkbox03(inforUserDefinedFields.getUDFCHKBOX03());
		udfs.setUdfchkbox04(inforUserDefinedFields.getUDFCHKBOX04());
		udfs.setUdfchkbox05(inforUserDefinedFields.getUDFCHKBOX05());
		udfs.setUdfchkbox06(inforUserDefinedFields.getUDFCHKBOX06());
		udfs.setUdfchkbox07(inforUserDefinedFields.getUDFCHKBOX07());
		udfs.setUdfchkbox08(inforUserDefinedFields.getUDFCHKBOX08());
		udfs.setUdfchkbox09(inforUserDefinedFields.getUDFCHKBOX09());
		udfs.setUdfchkbox10(inforUserDefinedFields.getUDFCHKBOX10());

		udfs.setUdfnum01(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM01()));
		udfs.setUdfnum02(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM02()));
		udfs.setUdfnum03(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM03()));
		udfs.setUdfnum04(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM04()));
		udfs.setUdfnum05(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM05()));
		udfs.setUdfnum06(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM06()));
		udfs.setUdfnum07(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM07()));
		udfs.setUdfnum08(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM08()));
		udfs.setUdfnum09(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM09()));
		udfs.setUdfnum10(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM10()));

		udfs.setUdfdate01(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE01()));
		udfs.setUdfdate02(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE02()));
		udfs.setUdfdate03(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE03()));
		udfs.setUdfdate04(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE04()));
		udfs.setUdfdate05(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE05()));
		udfs.setUdfdate06(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE06()));
		udfs.setUdfdate07(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE07()));
		udfs.setUdfdate08(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE08()));
		udfs.setUdfdate09(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE09()));
		udfs.setUdfdate10(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE10()));

		// Complete descriptions for RENT fields
		tools.getFieldDescriptionsTools().readUDFRENTDescriptions(udfs, "OBJ");
		return udfs;
	}

	public UserDefinedFields readInforUserDefinedFields(
			net.datastream.schemas.mp_entities.systemequipment_001.UserDefinedFields inforUserDefinedFields) {
		UserDefinedFields udfs = new UserDefinedFields();
		if (inforUserDefinedFields == null) {
			return null;
		}

		udfs.setUdfchar01(inforUserDefinedFields.getUDFCHAR01());
		udfs.setUdfchar02(inforUserDefinedFields.getUDFCHAR02());
		udfs.setUdfchar03(inforUserDefinedFields.getUDFCHAR03());
		udfs.setUdfchar04(inforUserDefinedFields.getUDFCHAR04());
		udfs.setUdfchar05(inforUserDefinedFields.getUDFCHAR05());
		udfs.setUdfchar06(inforUserDefinedFields.getUDFCHAR06());
		udfs.setUdfchar07(inforUserDefinedFields.getUDFCHAR07());
		udfs.setUdfchar08(inforUserDefinedFields.getUDFCHAR08());
		udfs.setUdfchar09(inforUserDefinedFields.getUDFCHAR09());
		udfs.setUdfchar10(inforUserDefinedFields.getUDFCHAR10());
		udfs.setUdfchar11(inforUserDefinedFields.getUDFCHAR11());
		udfs.setUdfchar12(inforUserDefinedFields.getUDFCHAR12());
		udfs.setUdfchar13(inforUserDefinedFields.getUDFCHAR13());
		udfs.setUdfchar14(inforUserDefinedFields.getUDFCHAR14());
		udfs.setUdfchar15(inforUserDefinedFields.getUDFCHAR15());
		udfs.setUdfchar16(inforUserDefinedFields.getUDFCHAR16());
		udfs.setUdfchar17(inforUserDefinedFields.getUDFCHAR17());
		udfs.setUdfchar18(inforUserDefinedFields.getUDFCHAR18());
		udfs.setUdfchar19(inforUserDefinedFields.getUDFCHAR19());
		udfs.setUdfchar20(inforUserDefinedFields.getUDFCHAR20());
		udfs.setUdfchar21(inforUserDefinedFields.getUDFCHAR21());
		udfs.setUdfchar22(inforUserDefinedFields.getUDFCHAR22());
		udfs.setUdfchar23(inforUserDefinedFields.getUDFCHAR23());
		udfs.setUdfchar24(inforUserDefinedFields.getUDFCHAR24());
		udfs.setUdfchar25(inforUserDefinedFields.getUDFCHAR25());
		udfs.setUdfchar26(inforUserDefinedFields.getUDFCHAR26());
		udfs.setUdfchar27(inforUserDefinedFields.getUDFCHAR27());
		udfs.setUdfchar28(inforUserDefinedFields.getUDFCHAR28());
		udfs.setUdfchar29(inforUserDefinedFields.getUDFCHAR29());
		udfs.setUdfchar30(inforUserDefinedFields.getUDFCHAR30());
		udfs.setUdfchar31(inforUserDefinedFields.getUDFCHAR31());
		udfs.setUdfchar32(inforUserDefinedFields.getUDFCHAR32());
		udfs.setUdfchar33(inforUserDefinedFields.getUDFCHAR33());
		udfs.setUdfchar34(inforUserDefinedFields.getUDFCHAR34());
		udfs.setUdfchar35(inforUserDefinedFields.getUDFCHAR35());
		udfs.setUdfchar36(inforUserDefinedFields.getUDFCHAR36());
		udfs.setUdfchar37(inforUserDefinedFields.getUDFCHAR37());
		udfs.setUdfchar38(inforUserDefinedFields.getUDFCHAR38());
		udfs.setUdfchar39(inforUserDefinedFields.getUDFCHAR39());
		udfs.setUdfchar40(inforUserDefinedFields.getUDFCHAR40());
		udfs.setUdfchar41(inforUserDefinedFields.getUDFCHAR41());
		udfs.setUdfchar42(inforUserDefinedFields.getUDFCHAR42());
		udfs.setUdfchar43(inforUserDefinedFields.getUDFCHAR43());
		udfs.setUdfchar44(inforUserDefinedFields.getUDFCHAR44());
		udfs.setUdfchar45(inforUserDefinedFields.getUDFCHAR45());

		udfs.setUdfchkbox01(inforUserDefinedFields.getUDFCHKBOX01());
		udfs.setUdfchkbox02(inforUserDefinedFields.getUDFCHKBOX02());
		udfs.setUdfchkbox03(inforUserDefinedFields.getUDFCHKBOX03());
		udfs.setUdfchkbox04(inforUserDefinedFields.getUDFCHKBOX04());
		udfs.setUdfchkbox05(inforUserDefinedFields.getUDFCHKBOX05());
		udfs.setUdfchkbox06(inforUserDefinedFields.getUDFCHKBOX06());
		udfs.setUdfchkbox07(inforUserDefinedFields.getUDFCHKBOX07());
		udfs.setUdfchkbox08(inforUserDefinedFields.getUDFCHKBOX08());
		udfs.setUdfchkbox09(inforUserDefinedFields.getUDFCHKBOX09());
		udfs.setUdfchkbox10(inforUserDefinedFields.getUDFCHKBOX10());

		udfs.setUdfnum01(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM01()));
		udfs.setUdfnum02(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM02()));
		udfs.setUdfnum03(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM03()));
		udfs.setUdfnum04(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM04()));
		udfs.setUdfnum05(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM05()));
		udfs.setUdfnum06(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM06()));
		udfs.setUdfnum07(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM07()));
		udfs.setUdfnum08(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM08()));
		udfs.setUdfnum09(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM09()));
		udfs.setUdfnum10(tools.getDataTypeTools().decodeQuantity(inforUserDefinedFields.getUDFNUM10()));

		udfs.setUdfdate01(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE01()));
		udfs.setUdfdate02(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE02()));
		udfs.setUdfdate03(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE03()));
		udfs.setUdfdate04(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE04()));
		udfs.setUdfdate05(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE05()));
		udfs.setUdfdate06(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE06()));
		udfs.setUdfdate07(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE07()));
		udfs.setUdfdate08(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE08()));
		udfs.setUdfdate09(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE09()));
		udfs.setUdfdate10(tools.getDataTypeTools().decodeInforDate(inforUserDefinedFields.getUDFDATE10()));

		// Complete descriptions for RENT fields
		tools.getFieldDescriptionsTools().readUDFRENTDescriptions(udfs, "OBJ");
		return udfs;
	}

}
