package dtmlibs.config.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dtmlibs.config.field.PropertyVetoException;

public interface Properties {

    @NotNull
    String[] getAllPropertyNames();

    @Nullable
    Object getProperty(@NotNull String name) throws NoSuchFieldException;

    void setProperty(@NotNull String name, @NotNull String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException;

    void addProperty(@NotNull String name, @NotNull String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException;

    void removeProperty(@NotNull String name, @NotNull String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException;

    void clearProperty(@NotNull String name, @Nullable String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException;

    @Nullable
    Object getPropertyUnchecked(@NotNull String name);

    boolean setPropertyUnchecked(@NotNull String name, @NotNull String value);

    boolean addPropertyUnchecked(@NotNull String name, @NotNull String value);

    boolean removePropertyUnchecked(@NotNull String name, @NotNull String value);

    boolean clearPropertyUnchecked(@NotNull String name, @Nullable String value);
}
