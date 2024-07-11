package ch.cern.eam.wshub.core;

import ch.cern.eam.wshub.core.client.EAMClient;
import ch.cern.eam.wshub.core.client.EAMContext;
import ch.cern.eam.wshub.core.services.entities.Credentials;
import ch.cern.eam.wshub.core.tools.Tools;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class GlobalContext {
    public static EAMClient eamClient;
    public static EAMContext context;
    public static Tools tools;
    public static String username;
    public static Random random;

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public enum TypeCode {
        L
    }
    public static String getCode(TypeCode type) {
        return "__T3ST__" + type.name() + "-" + getRandomString(16);
    }

    public static String DEPARTMENT = "HXMF";
    public static String COST_CODE = "H#96231";
    public static String ASSET_CODE = "PA-A-001";

    static {
        int cores = Runtime.getRuntime().availableProcessors();
        int threadPoolSize = cores * 2;

        eamClient = new EAMClient.Builder("https://cmmsx-test.cern.ch/axis/services/EWSConnector")
                .withDefaultTenant("infor")
                .withDefaultOrganizationCode("*")
                .withLogger(Logger.getLogger("wshublogger"))
                .withExecutorService(Executors.newFixedThreadPool(threadPoolSize))
                .build();

        context = new EAMContext(new Credentials(System.getenv("TESTS_USERNAME"), System.getenv("TESTS_PASSPHRASE")));
        username = context.getCredentials().getUsername().toUpperCase();

        tools = eamClient.getTools();

        random = new Random();
    }

    final private static String RANDOM_CHAR_SET =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static char getRandomCharacter() {
        return RANDOM_CHAR_SET.charAt(random.nextInt(RANDOM_CHAR_SET.length()));
    }

    private static String getRandomString(int length) {
        char[] charArr = new char[length];
        for(int i = 0; i < length; ++i) charArr[i] = getRandomCharacter();
        return new String(charArr);
    }
}
