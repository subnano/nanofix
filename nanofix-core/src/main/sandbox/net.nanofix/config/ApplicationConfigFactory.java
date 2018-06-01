package net.nanofix.config;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 06:30
 */
public interface ApplicationConfigFactory {
    ApplicationConfig load(String filename);
}
