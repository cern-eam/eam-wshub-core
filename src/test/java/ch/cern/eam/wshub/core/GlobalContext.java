package ch.cern.eam.wshub.core;

import ch.cern.eam.wshub.core.client.InforClient;
import ch.cern.eam.wshub.core.client.InforContext;
import ch.cern.eam.wshub.core.services.entities.Credentials;
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

    static {
        inforClient = new InforClient.Builder("https://cmmsx-test.cern.ch/axis/services/EWSConnector")
                .withDefaultTenant("infor")
                .withDefaultOrganizationCode("*")
                .withLogger(Logger.getLogger("wshublogger"))
                .build();

        context = new InforContext(new Credentials("plavarin", "***REMOVED***"));
        username = context.getCredentials().getUsername().toUpperCase();

        tools = inforClient.getTools();

        random = new Random();
    }

    public static String getCode(String type) {
        return "7LO7WD" + type + username + "-" + random.nextInt(Integer.MAX_VALUE);
    }
}
