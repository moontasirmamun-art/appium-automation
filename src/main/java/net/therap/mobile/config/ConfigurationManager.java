package net.therap.mobile.config;

import org.aeonbits.owner.ConfigCache;

public final class ConfigurationManager {

    private ConfigurationManager() {}

    public static ConfigurationProps getConfigurationProps() {
        return ConfigCache.getOrCreate(ConfigurationProps.class);
    }
}
