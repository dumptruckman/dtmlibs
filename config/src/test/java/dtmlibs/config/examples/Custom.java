package dtmlibs.config.examples;

import dtmlibs.config.annotation.Comment;
import dtmlibs.config.annotation.SerializeWith;
import dtmlibs.config.serializers.CustomSerializer;
import dtmlibs.config.util.ObjectStringifier;

import java.util.Arrays;

@SerializeWith(CustomSerializer.class)
public class Custom {

    @Comment("A name")
    public String name;
    @Comment("Some data")
    public Data data = new Data();

    public Custom(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Custom)) return false;

        final Custom custom = (Custom) o;

        if (!name.equals(custom.name)) return false;
        return data.equals(custom.data);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return ObjectStringifier.toString(this, true);
    }

    public static class Data {

        public Object[] array = new Object[] {1, 2, 3};

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Data)) return false;

            final Data data = (Data) o;

            if (array == null && data.array == null) return true;
            if (array == null || data.array == null) return false;
            if (array.length != data.array.length) return false;

            for (int i = 0; i < array.length; i++) {
                if (!array[i].equals(data.array[i])) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(array);
        }

        @Override
        public String toString() {
            return ObjectStringifier.toString(this, true);
        }
    }
}
