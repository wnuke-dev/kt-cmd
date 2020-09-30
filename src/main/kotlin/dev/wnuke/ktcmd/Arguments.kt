package dev.wnuke.ktcmd

import kotlin.reflect.KClass

class SyntaxError(problem: String) : RuntimeException(problem)

abstract class Argument<T, U : Call>(
    name: String,
    val description: String,
    val runs: U.(T) -> Unit,
    val shortName: String,
    val type: KClass<*>
) {
    val prefix = "--$name"
    val shortPrefix = "-$shortName"

    @Throws(SyntaxError::class)
    fun parse(string: String): T {
        if (string.startsWith("$prefix=")) return parseValue(string.removePrefix("$prefix="))
        if (shortName.isNotEmpty() && string.startsWith("$shortPrefix=")) return parseValue(string.removePrefix("$shortPrefix="))
        return parseValue(string)
    }

    fun matches(string: String): Boolean {
        if (string.startsWith(prefix)) return true
        if (shortName.isNotEmpty() && string.startsWith(shortPrefix)) return true
        return false
    }

    fun prefixOnly(string: String) = string == prefix || string == shortPrefix

    @Throws(SyntaxError::class)
    abstract fun parseValue(string: String): T
}

class StringArgument<T : Call>(name: String, description: String = "", runs: T.(String) -> Unit, shortName: String) :
    Argument<String, T>(name, description, runs, shortName, String::class) {
    override fun parseValue(string: String) = string
}

class IntArgument<T : Call>(name: String, description: String = "", runs: T.(Int) -> Unit, shortName: String) :
    Argument<Int, T>(name, description, runs, shortName, Int::class) {
    override fun parseValue(string: String): Int {
        try {
            return string.toInt()
        } catch (_: NumberFormatException) {
            throw SyntaxError("$string is not a valid Integer.")
        }
    }
}

class LongArgument<T : Call>(name: String, description: String = "", runs: T.(Long) -> Unit, shortName: String) :
    Argument<Long, T>(name, description, runs, shortName, Long::class) {
    override fun parseValue(string: String): Long {
        try {
            return string.toLong()
        } catch (_: NumberFormatException) {
            throw SyntaxError("$string is not a valid Long.")
        }
    }
}

class FloatArgument<T : Call>(name: String, description: String = "", runs: T.(Float) -> Unit, shortName: String) :
    Argument<Float, T>(name, description, runs, shortName, Float::class) {
    override fun parseValue(string: String): Float {
        try {
            return string.toFloat()
        } catch (_: NumberFormatException) {
            throw SyntaxError("$string is not a valid Float.")
        }
    }
}

class DoubleArgument<T : Call>(name: String, description: String = "", runs: T.(Double) -> Unit, shortName: String) :
    Argument<Double, T>(name, description, runs, shortName, Double::class) {
    override fun parseValue(string: String): Double {
        try {
            return string.toDouble()
        } catch (_: NumberFormatException) {
            throw SyntaxError("$string is not a valid Double.")
        }
    }
}
