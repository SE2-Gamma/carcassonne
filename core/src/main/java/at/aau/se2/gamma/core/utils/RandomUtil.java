package at.aau.se2.gamma.core.utils;

import java.util.UUID;

public class RandomUtil {
    public static String randomToken() {
        return UUID.randomUUID().toString();
    }
}
