/*
 * This file is part of dtmlibs.
 *
 * Copyright (c) 2017 Jeremy Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dtmlibs.config.examples

import dtmlibs.config.annotation.Comment
import dtmlibs.config.annotation.Description
import dtmlibs.config.annotation.HandlePropertyWith
import dtmlibs.config.annotation.Immutable
import dtmlibs.config.annotation.SerializableAs
import dtmlibs.config.annotation.SerializeWith
import dtmlibs.config.annotation.ValidateWith
import dtmlibs.config.field.FieldInstance
import dtmlibs.config.field.PropertyVetoException
import dtmlibs.config.field.Validator
import dtmlibs.config.field.VirtualField
import dtmlibs.config.properties.PropertiesWrapper
import dtmlibs.config.properties.PropertyAliases
import dtmlibs.config.properties.PropertyHandler
import dtmlibs.config.serializers.CustomSerializer2
import dtmlibs.config.util.ObjectStringifier

import java.math.BigDecimal
import java.math.BigInteger
import java.util.ArrayList
import java.util.HashMap
import java.util.Locale
import java.util.UUID
import java.util.concurrent.CopyOnWriteArrayList

@Comment("Test the header out", "\"It works,\" they say")
@SerializableAs("ComprehensiveTestClass")
class KComprehensive : PropertiesWrapper() {

    class NameValidator : Validator<String> {
        @Throws(PropertyVetoException::class)
        override fun validateChange(newValue: String?, oldValue: String?): String? {
            return if (newValue != null && newValue.length >= 4) {
                newValue
            } else {
                oldValue
            }
        }
    }

    class SimpleHandler : PropertyHandler<Any> {
        private fun convertToList(value: String): List<Simple> {
            val values = value.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val list = mutableListOf<Simple>()
            for (s in values) {
                list.add(Simple(s))
            }
            return list
        }

        @Throws(PropertyVetoException::class, UnsupportedOperationException::class)
        override fun set(field: FieldInstance, newValue: String) {
            field.value = convertToList(newValue)
        }

        @Throws(PropertyVetoException::class, UnsupportedOperationException::class)
        override fun add(field: FieldInstance, valueToAdd: String) {
            (field.value as MutableList<Any?>).addAll(convertToList(valueToAdd))
        }

        @Throws(PropertyVetoException::class, UnsupportedOperationException::class)
        override fun remove(field: FieldInstance, valueToRemove: String) {
            (field.value as MutableList<Any?>).removeAll(convertToList(valueToRemove))
        }

        @Throws(PropertyVetoException::class, UnsupportedOperationException::class)
        override fun clear(field: FieldInstance, valueToClear: String?) {
            (field.value as MutableList<Any?>).clear()
        }
    }

    @Description(A_INT_DESCRIPTION)
    @Comment(A_INT_COMMENT_1, A_INT_COMMENT_2)
    var aInt = A_INT

    var aLong = A_LONG
    var aDouble = A_DOUBLE
    var aFloat = A_FLOAT
    var aShort = A_SHORT
    var aByte = A_BYTE
    var aBoolean = A_BOOLEAN
    var aChar = A_CHAR

    var aBigInteger = A_BIG_INTEGER
    var aBigDecimal = A_BIG_DECIMAL

    var aUUID = A_UUID

    var doubleList = DOUBLE_LIST

    @Transient
    var tInt = T_INT
    @ValidateWith(NameValidator::class)
    var name = NAME
    var wordList: MutableList<String> = mutableListCopyOf(WORD_LIST)
    var wordList2: MutableList<String> = mutableListCopyOf(WORD_LIST_2)
    var listList: MutableList<List<String>> = mutableListCopyOf(LIST_LIST)
    var randomList: MutableList<Any> = mutableListCopyOf(RANDOM_LIST)
    var stringObjectMap: Map<String, Any> = HashMap(STRING_OBJECT_MAP)
    val custom = Custom(CUSTOM.name)
    @SerializeWith(CustomSerializer2::class)
    var custom2 = Custom(CUSTOM.name)
    @Immutable
    var immutableString = NAME
    val simple = Simple()
    val finalString = NAME
    val virtualEnum: VirtualField<Anum> = AnumField()

    internal class AnumField : VirtualField<Anum> {
        private var actual = Anum.A
        override fun get(): Anum {
            return actual
        }

        override fun set(newValue: Anum) {
            actual = newValue
        }

        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false
            val anumField = o as AnumField?
            return actual == anumField!!.actual
        }

        override fun hashCode(): Int {
            return actual.hashCode()
        }
    }

    var fakeEnum = FakeEnum.FAKE_2
    var locale = LOCALE

    private val testWildCardListVirtualProp: VirtualField<List<*>>? = null
    private val testWildCardVirtualProp: VirtualField<*>? = null
    private val testTypedVirtualProp: VirtualField<List<String>>? = null
    private val genericList = mutableListOf<Any?>()

    @HandlePropertyWith(SimpleHandler::class)
    var simpleList: MutableList<Simple> = mutableListOf()

    init {
        simpleList.add(Simple("test"))
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as KComprehensive?

        if (aInt != that!!.aInt) return false
        if (aLong != that.aLong) return false
        if (java.lang.Double.compare(that.aDouble, aDouble) != 0) return false
        if (java.lang.Float.compare(that.aFloat, aFloat) != 0) return false
        if (aShort != that.aShort) return false
        if (aByte != that.aByte) return false
        if (aBoolean != that.aBoolean) return false
        if (aChar != that.aChar) return false
        if (tInt != that.tInt) return false
        if (aBigInteger != that.aBigInteger) return false
        if (aBigDecimal != that.aBigDecimal) return false
        if (name != that.name) return false
        if (wordList != that.wordList) return false
        if (wordList2 != that.wordList2) return false
        if (listList != that.listList) return false
        if (randomList != that.randomList) return false
        if (stringObjectMap != that.stringObjectMap) return false
        if (custom != that.custom) return false
        if (custom2 != that.custom2) return false
        if (immutableString != that.immutableString) return false
        if (simple != that.simple) return false
        if (finalString != that.finalString) return false
        if (virtualEnum != that.virtualEnum) return false
        if (fakeEnum != that.fakeEnum) return false
        if (locale != that.locale) return false
        if (aUUID != that.aUUID) return false
        if (doubleList != that.doubleList) return false
        //if (!testWildCardListVirtualProp.equals(that.testWildCardListVirtualProp)) return false;
        //if (!testWildCardVirtualProp.equals(that.testWildCardVirtualProp)) return false;
        //if (!testTypedVirtualProp.equals(that.testTypedVirtualProp)) return false;
        return if (genericList != that.genericList) false else simpleList == that.simpleList

    }

    override fun hashCode(): Int {
        var result: Int
        val temp: Long
        result = aInt
        result = 31 * result + (aLong xor aLong.ushr(32)).toInt()
        temp = java.lang.Double.doubleToLongBits(aDouble)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        result = 31 * result + if (aFloat != +0.0f) java.lang.Float.floatToIntBits(aFloat) else 0
        result = 31 * result + aShort.toInt()
        result = 31 * result + aByte.toInt()
        result = 31 * result + if (aBoolean) 1 else 0
        result = 31 * result + aChar.toInt()
        result = 31 * result + aBigInteger.hashCode()
        result = 31 * result + aBigDecimal.hashCode()
        result = 31 * result + tInt
        result = 31 * result + name.hashCode()
        result = 31 * result + wordList.hashCode()
        result = 31 * result + wordList2.hashCode()
        result = 31 * result + listList.hashCode()
        result = 31 * result + randomList.hashCode()
        result = 31 * result + stringObjectMap.hashCode()
        result = 31 * result + custom.hashCode()
        result = 31 * result + custom2.hashCode()
        result = 31 * result + immutableString.hashCode()
        result = 31 * result + simple.hashCode()
        result = 31 * result + finalString.hashCode()
        result = 31 * result + virtualEnum.hashCode()
        result = 31 * result + fakeEnum.hashCode()
        result = 31 * result + locale.hashCode()
        result = 31 * result + genericList.hashCode()
        result = 31 * result + simpleList.hashCode()
        result = 31 * result + aUUID.hashCode()
        result = 31 * result + doubleList.hashCode()
        return result
    }

    override fun toString(): String {
        return ObjectStringifier.toString(this)
    }

    companion object {

        const val A_INT = 2123012310
        const val A_LONG = 1293750971209172093L
        const val A_DOUBLE = 304205905924.34925710270957
        const val A_FLOAT = 12305012.3451231f
        const val A_SHORT: Short = 12125
        const val A_BYTE: Byte = 124
        const val A_BOOLEAN = true
        const val A_CHAR = 'h'
        val A_BIG_INTEGER = BigInteger("12395357293415971941723985719273123")
        val A_BIG_DECIMAL = BigDecimal("123105810586823404825141235.112038105810831029301581028")
        val A_UUID = UUID.randomUUID()
        const val A_INT_DESCRIPTION = "Just some int"
        const val A_INT_COMMENT_1 = "Just some int"
        const val A_INT_COMMENT_2 = "Really."
        val A_INT_COMMENTS = arrayOf(A_INT_COMMENT_1, A_INT_COMMENT_2)
        val T_INT = 5
        const val NAME = "Comprehensive"
        val WORD_LIST: MutableList<String> = ArrayList()
        val WORD_LIST_2: MutableList<String> = CopyOnWriteArrayList()
        val LIST_LIST: MutableList<List<String>> = ArrayList()
        val CHILD = Child(true)
        val PARENT = Parent(CHILD)
        val RANDOM_LIST: MutableList<Any> = ArrayList()
        val STRING_OBJECT_MAP: MutableMap<String, Any> = HashMap()
        val CUSTOM = Custom("custom")
        val LOCALE = Locale.ENGLISH
        val DOUBLE_LIST: MutableList<Double> = ArrayList()

        init {
            WORD_LIST.add("test")
            WORD_LIST.add("lol")
            WORD_LIST_2.add("omg")
            WORD_LIST_2.add("words")
            LIST_LIST.add(WORD_LIST)
            LIST_LIST.add(WORD_LIST_2)
            RANDOM_LIST.add(PARENT)
            RANDOM_LIST.add(CHILD)
            RANDOM_LIST.add(false)
            STRING_OBJECT_MAP.put("parent", PARENT)
            STRING_OBJECT_MAP.put("child", CHILD)
            STRING_OBJECT_MAP.put("String", "String")
            STRING_OBJECT_MAP.put("list", WORD_LIST)
            STRING_OBJECT_MAP.put("custom1", CUSTOM)
            STRING_OBJECT_MAP.put("custom2", CUSTOM)
            RANDOM_LIST.add(STRING_OBJECT_MAP)
            DOUBLE_LIST.add(123151512615.0)
            DOUBLE_LIST.add(62342362.1231231251515)
            PropertyAliases.createAlias(KComprehensive::class.java, "cname", "custom", "name")
        }
    }

    private fun <T> mutableListCopyOf(list: List<T>): MutableList<T> {
        val newList = mutableListOf<T>()
        newList.addAll(list)
        return newList;
    }
}
