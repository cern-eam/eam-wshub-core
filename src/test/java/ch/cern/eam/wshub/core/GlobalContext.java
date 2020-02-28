package ch.cern.eam.wshub.core;

import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.services.equipment.EquipmentFacadeService;
import ch.cern.eam.wshub.core.services.equipment.LocationService;
import ch.cern.eam.wshub.core.tools.Tools;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Random;
import java.util.logging.Logger;

public class GlobalContext {
    public static InforClient inforClient;
    public static InforContext context;
    public static Tools tools;
    public static String username;
    public static Random random;

    public static LocationService locationService;
    public static EquipmentFacadeService equipmentFacadeService;

    final private static String RANDOM_CHAR_SET =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static {
        inforClient = new InforClient.Builder("https://cmmsx-test.cern.ch/axis/services/EWSConnector")
                .withDefaultTenant("infor")
                .withDefaultOrganizationCode("*")
                .withLogger(Logger.getLogger("wshublogger"))
                .build();

        context = new InforContext(new Credentials(System.getenv("TESTS_USERNAME"), System.getenv("TESTS_PASSPHRASE")));
        username = context.getCredentials().getUsername().toUpperCase();

        tools = inforClient.getTools();

        random = new Random();

        locationService = inforClient.getLocationService();
        equipmentFacadeService = inforClient.getEquipmentFacadeService();
    }

    public static String getRandomCharacter() {
        int index = random.nextInt(RANDOM_CHAR_SET.length());
        return RANDOM_CHAR_SET.substring(index, index + 1);
    }

    public static String getRandomString(int length) {
        if(length > 100)
            throw new IllegalArgumentException("This function has low performance on large strings");

        String returnString = "";
        for(int i = 0; i < length; ++i)
            returnString += getRandomCharacter();
        return returnString;
    }

    public static String getCode(String type) {
        return "7LO7WD" + type + username + "-" + getRandomString(10);
    }
}
