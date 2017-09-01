package dtmlibs.config.migration;

import dtmlibs.config.annotation.MigrateWith;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Migration {
    ;

    @NotNull
    private static final Map<Class<? extends MigrationHandler>, MigrationHandler> migrationHandlers
            = new ConcurrentHashMap<>();

    public static <T extends MigrationHandler> void registerMigrationHandler(
            @NotNull Class<T> clazz, @NotNull T instance) {
        migrationHandlers.put(clazz, instance);
    }

    @Nullable
    public static MigrationHandler getAnnotatedMigrationHandler(@NotNull Object someSerializableObject) {
        return getAnnotatedMigrationHandler(someSerializableObject.getClass());
    }

    @Nullable
    public static MigrationHandler getAnnotatedMigrationHandler(@NotNull Class someSerializableClass) {
        MigrateWith migrateWith = (MigrateWith) someSerializableClass.getAnnotation(MigrateWith.class);
        return migrateWith != null ? getMigrationHandler(migrateWith) : null;
    }

    @NotNull
    public static MigrationHandler getMigrationHandler(@NotNull MigrateWith migrateWith) {
        return getMigrationHandler(migrateWith.value());
    }

    @NotNull
    public static <T extends MigrationHandler> T getMigrationHandler(@NotNull Class<T> clazz) {
        T handler = (T) migrationHandlers.get(clazz);
        if (handler == null) {
            handler = createNewInstance(clazz);
        }
        return handler;
    }

    @NotNull
    private static <T extends MigrationHandler> T createNewInstance(@NotNull Class<T> clazz) {

    }
}
