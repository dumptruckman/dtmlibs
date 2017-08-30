package dtmlibs.config.examples;

import dtmlibs.config.util.ObjectStringifier;
import org.jetbrains.annotations.NotNull;
import dtmlibs.config.annotation.Comment;
import dtmlibs.config.annotation.Description;
import dtmlibs.config.annotation.HandlePropertyWith;
import dtmlibs.config.annotation.Immutable;
import dtmlibs.config.annotation.SerializableAs;
import dtmlibs.config.annotation.SerializeWith;
import dtmlibs.config.annotation.ValidateWith;
import dtmlibs.config.field.FieldInstance;
import dtmlibs.config.field.PropertyVetoException;
import dtmlibs.config.field.Validator;
import dtmlibs.config.field.VirtualField;
import dtmlibs.config.properties.PropertiesWrapper;
import dtmlibs.config.properties.PropertyAliases;
import dtmlibs.config.properties.PropertyHandler;
import dtmlibs.config.serializers.CustomSerializer2;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Comment({"Test the header out", "\"It works,\" they say"})
@SerializableAs("ComprehensiveTestClass")
public class Comprehensive extends PropertiesWrapper {

    public static final int A_INT = 2123012310;
    public static final long A_LONG = 1293750971209172093L;
    public static final double A_DOUBLE = 304205905924.34925710270957D;
    public static final float A_FLOAT = 12305012.3451231F;
    public static final short A_SHORT = 12125;
    public static final byte A_BYTE = 124;
    public static final boolean A_BOOLEAN = true;
    public static final char A_CHAR = 'h';
    public static final BigInteger A_BIG_INTEGER = new BigInteger("12395357293415971941723985719273123");
    public static final BigDecimal A_BIG_DECIMAL = new BigDecimal("123105810586823404825141235.112038105810831029301581028");
    public static final UUID A_UUID = UUID.randomUUID();
    public static final String A_INT_DESCRIPTION = "Just some int";
    public static final String A_INT_COMMENT_1 = "Just some int";
    public static final String A_INT_COMMENT_2 = "Really.";
    public static final String[] A_INT_COMMENTS = {A_INT_COMMENT_1, A_INT_COMMENT_2};
    public static final int T_INT = 5;
    public static final String NAME = "Comprehensive";
    public static final List<String> WORD_LIST = new ArrayList<>();
    public static final List<String> WORD_LIST_2 = new CopyOnWriteArrayList<>();
    public static final List<List<String>> LIST_LIST = new ArrayList<>();
    public static final Child CHILD = new Child(true);
    public static final Parent PARENT = new Parent(CHILD);
    public static final List<Object> RANDOM_LIST = new ArrayList<>();
    public static final Map<String, Object> STRING_OBJECT_MAP = new HashMap<>();
    public static final Custom CUSTOM = new Custom("custom");
    public static final Locale LOCALE = Locale.ENGLISH;
    public static final List<Double> DOUBLE_LIST = new ArrayList<>();

    static {
        WORD_LIST.add("test");
        WORD_LIST.add("lol");
        WORD_LIST_2.add("omg");
        WORD_LIST_2.add("words");
        LIST_LIST.add(WORD_LIST);
        LIST_LIST.add(WORD_LIST_2);
        RANDOM_LIST.add(PARENT);
        RANDOM_LIST.add(CHILD);
        RANDOM_LIST.add(false);
        STRING_OBJECT_MAP.put("parent", PARENT);
        STRING_OBJECT_MAP.put("child", CHILD);
        STRING_OBJECT_MAP.put("String", "String");
        STRING_OBJECT_MAP.put("list", WORD_LIST);
        STRING_OBJECT_MAP.put("custom1", CUSTOM);
        STRING_OBJECT_MAP.put("custom2", CUSTOM);
        RANDOM_LIST.add(STRING_OBJECT_MAP);
        DOUBLE_LIST.add(123151512615D);
        DOUBLE_LIST.add(62342362.1231231251515D);
        PropertyAliases.createAlias(Comprehensive.class, "cname", "custom", "name");
    }

    public Comprehensive() { }

    public static class NameValidator implements Validator<String> {
        @Nullable
        @Override
        public String validateChange(@Nullable String newValue, @Nullable String oldValue) throws PropertyVetoException {
            if (newValue != null && newValue.length() >= 4) {
                return newValue;
            } else {
                return oldValue;
            }
        }
    }

    public static class SimpleHandler implements PropertyHandler {
        private List<Simple> convertToList(String value) {
            String[] values = value.split(",");
            List<Simple> list = new ArrayList<Simple>(values.length);
            for (String s : values) {
                list.add(new Simple(s));
            }
            return list;
        }

        @Override
        public void set(@NotNull FieldInstance field, @NotNull String newValue) throws PropertyVetoException, UnsupportedOperationException {
            field.setValue(convertToList(newValue));
        }

        @Override
        public void add(@NotNull FieldInstance field, @NotNull String valueToAdd) throws PropertyVetoException, UnsupportedOperationException {
            ((List) field.getValue()).addAll(convertToList(valueToAdd));
        }

        @Override
        public void remove(@NotNull FieldInstance field, @NotNull String valueToRemove) throws PropertyVetoException, UnsupportedOperationException {
            ((List) field.getValue()).removeAll(convertToList(valueToRemove));
        }

        @Override
        public void clear(@NotNull FieldInstance field, @Nullable String valueToClear) throws PropertyVetoException, UnsupportedOperationException {
            ((List) field.getValue()).clear();
        }
    }

    @Description(A_INT_DESCRIPTION)
    @Comment({A_INT_COMMENT_1, A_INT_COMMENT_2})
    public int aInt = A_INT;

    public long aLong = A_LONG;
    public double aDouble = A_DOUBLE;
    public float aFloat = A_FLOAT;
    public short aShort = A_SHORT;
    public byte aByte = A_BYTE;
    public boolean aBoolean = A_BOOLEAN;
    public char aChar = A_CHAR;

    public BigInteger aBigInteger = A_BIG_INTEGER;
    public BigDecimal aBigDecimal = A_BIG_DECIMAL;

    public UUID aUUID = A_UUID;

    public List<Double> doubleList = DOUBLE_LIST;

    public transient int tInt = T_INT;
    @ValidateWith(NameValidator.class)
    public String name = NAME;
    public List<String> wordList = new ArrayList<>(WORD_LIST);
    public List<String> wordList2 = new ArrayList<>(WORD_LIST_2);
    public List<List<String>> listList = new ArrayList<>(LIST_LIST);
    public List<Object> randomList = new ArrayList<>(RANDOM_LIST);
    public Map<String, Object> stringObjectMap = new HashMap<>(STRING_OBJECT_MAP);
    public final Custom custom = new Custom(CUSTOM.name);
    @SerializeWith(CustomSerializer2.class)
    public Custom custom2 = new Custom(CUSTOM.name);
    @Immutable
    public String immutableString = NAME;
    public final Simple simple = new Simple();
    public final String finalString = NAME;
    public final VirtualField<Anum> virtualEnum = new AnumField();
    static class AnumField implements VirtualField<Anum> {
        private Anum actual = Anum.A;
        @Override
        public Anum get() {
            return actual;
        }
        @Override
        public void set(final Anum newValue) {
            actual = newValue;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AnumField anumField = (AnumField) o;
            return actual == anumField.actual;
        }
        @Override
        public int hashCode() {
            return actual.hashCode();
        }
    }

    public FakeEnum fakeEnum = FakeEnum.FAKE_2;
    public Locale locale = LOCALE;

    private VirtualField<List<?>> testWildCardListVirtualProp;
    private VirtualField<?> testWildCardVirtualProp;
    private VirtualField<List<String>> testTypedVirtualProp;
    private List<?> genericList = new ArrayList();

    @HandlePropertyWith(SimpleHandler.class)
    public List<Simple> simpleList = new ArrayList<Simple>();
    {
        simpleList.add(new Simple("test"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comprehensive that = (Comprehensive) o;

        if (aInt != that.aInt) return false;
        if (aLong != that.aLong) return false;
        if (Double.compare(that.aDouble, aDouble) != 0) return false;
        if (Float.compare(that.aFloat, aFloat) != 0) return false;
        if (aShort != that.aShort) return false;
        if (aByte != that.aByte) return false;
        if (aBoolean != that.aBoolean) return false;
        if (aChar != that.aChar) return false;
        if (tInt != that.tInt) return false;
        if (!aBigInteger.equals(that.aBigInteger)) return false;
        if (!aBigDecimal.equals(that.aBigDecimal)) return false;
        if (!name.equals(that.name)) return false;
        if (!wordList.equals(that.wordList)) return false;
        if (!wordList2.equals(that.wordList2)) return false;
        if (!listList.equals(that.listList)) return false;
        if (!randomList.equals(that.randomList)) return false;
        if (!stringObjectMap.equals(that.stringObjectMap)) return false;
        if (!custom.equals(that.custom)) return false;
        if (!custom2.equals(that.custom2)) return false;
        if (!immutableString.equals(that.immutableString)) return false;
        if (!simple.equals(that.simple)) return false;
        if (!finalString.equals(that.finalString)) return false;
        if (!virtualEnum.equals(that.virtualEnum)) return false;
        if (!fakeEnum.equals(that.fakeEnum)) return false;
        if (!locale.equals(that.locale)) return false;
        if (!aUUID.equals(that.aUUID)) return false;
        if (!doubleList.equals(that.doubleList)) return false;
        //if (!testWildCardListVirtualProp.equals(that.testWildCardListVirtualProp)) return false;
        //if (!testWildCardVirtualProp.equals(that.testWildCardVirtualProp)) return false;
        //if (!testTypedVirtualProp.equals(that.testTypedVirtualProp)) return false;
        if (!genericList.equals(that.genericList)) return false;
        return simpleList.equals(that.simpleList);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = aInt;
        result = 31 * result + (int) (aLong ^ (aLong >>> 32));
        temp = Double.doubleToLongBits(aDouble);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (aFloat != +0.0f ? Float.floatToIntBits(aFloat) : 0);
        result = 31 * result + (int) aShort;
        result = 31 * result + (int) aByte;
        result = 31 * result + (aBoolean ? 1 : 0);
        result = 31 * result + (int) aChar;
        result = 31 * result + aBigInteger.hashCode();
        result = 31 * result + aBigDecimal.hashCode();
        result = 31 * result + tInt;
        result = 31 * result + name.hashCode();
        result = 31 * result + wordList.hashCode();
        result = 31 * result + wordList2.hashCode();
        result = 31 * result + listList.hashCode();
        result = 31 * result + randomList.hashCode();
        result = 31 * result + stringObjectMap.hashCode();
        result = 31 * result + custom.hashCode();
        result = 31 * result + custom2.hashCode();
        result = 31 * result + immutableString.hashCode();
        result = 31 * result + simple.hashCode();
        result = 31 * result + finalString.hashCode();
        result = 31 * result + virtualEnum.hashCode();
        result = 31 * result + fakeEnum.hashCode();
        result = 31 * result + locale.hashCode();
        result = 31 * result + genericList.hashCode();
        result = 31 * result + simpleList.hashCode();
        result = 31 * result + aUUID.hashCode();
        result = 31 * result + doubleList.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return ObjectStringifier.toString(this);
    }
}
