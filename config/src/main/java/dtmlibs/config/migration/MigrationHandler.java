package dtmlibs.config.migration;

import ninja.leaping.configurate.ConfigurationNode;

public interface MigrationHandler {

    ConfigurationNode migrate(ConfigurationNode node);
}
