package org.pitest.util;

import java.security.Permission;

public class SystemUtil {

    public static void runWithExitForbidden(Runnable runnable) {
        forbidExit();
        try {
            runnable.run();
        } finally {
            allowExit();
        }
    }

    private static void forbidExit() {
        final SecurityManager securityManager = new SecurityManager() {
            public void checkPermission( Permission permission ) {
                String name = permission.getName();
                if (name != null && name.startsWith("exitVM.")) {
                    throw new ExitTrappedException();
                }
            }
        };
        System.setSecurityManager(securityManager);
    }

    private static void allowExit() {
        System.setSecurityManager(null);
    }

    private static class ExitTrappedException extends SecurityException {
    }
}
