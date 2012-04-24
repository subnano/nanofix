package net.nanofix;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 24/03/12
 * Time: 12:57
 */
public class Version {
    
    public static void main(String[] args) {
        String version = getVersion();
        System.out.println("Version: " + (version == null ? "undefined" : version));
    }

    private static String getVersion() {
        return Version.class.getPackage().getImplementationVersion();
    }
}
