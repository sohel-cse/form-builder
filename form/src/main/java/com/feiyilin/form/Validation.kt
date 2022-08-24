package com.feiyilin.form

import android.util.Patterns

interface ValueValidationRule {
    val enforceOnHidden: Boolean
    fun validate(item: FormItem): Boolean
    fun failureMessage(): String
}

class RequiredRule : ValueValidationRule {
    override val enforceOnHidden = false
    override fun validate(item: FormItem) = item.getValueString().isNotEmpty()
    override fun failureMessage(): String = "No Value Provided"
}

class EmailRule : ValueValidationRule {
    override val enforceOnHidden = false
    override fun validate(item: FormItem): Boolean = item.getValueString().run { !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches() }
    override fun failureMessage() = "Invalid Email"
}

class LengthRule(private val length: Int) : ValueValidationRule {
    override val enforceOnHidden: Boolean
        get() = false

    override fun validate(item: FormItem): Boolean = item.getValueString().length == length
    override fun failureMessage() = "Text length is not equal to $length"
}

class LengthRangeRule(private val min: Int, private val max: Int) : ValueValidationRule {
    override val enforceOnHidden: Boolean
        get() = false

    override fun validate(item: FormItem): Boolean = item.getValueString().length in min..max
    override fun failureMessage() = "Text length is not between $min, $max"
}

class ValueCeilingRule(private val max: Double) : ValueValidationRule {
    override val enforceOnHidden: Boolean
        get() = false

    override fun validate(item: FormItem): Boolean = item.getValueString().toDouble() <= max
    override fun failureMessage() = "Value is not less than $max"
}

class LengthCeilingRule(private val max: Int) : ValueValidationRule {
    override val enforceOnHidden: Boolean
        get() = false

    override fun validate(item: FormItem): Boolean = item.getValueString().length <= max
    override fun failureMessage() = "Text length is not less than $max"
}

class ValueFloorRule(private val min: Double) : ValueValidationRule {
    override val enforceOnHidden: Boolean
        get() = false

    override fun validate(item: FormItem): Boolean = item.getValueString().toDouble() >= min
    override fun failureMessage() = "Value is not greater than $min"
}

class LengthFloorRule(private val min: Int) : ValueValidationRule {
    override val enforceOnHidden: Boolean
        get() = false

    override fun validate(item: FormItem): Boolean = item.getValueString().length >= min
    override fun failureMessage() = "Text length is not greater than $min"
}

class ValueRangeRule(private val min: Double, private val max: Double) : ValueValidationRule {
    override val enforceOnHidden: Boolean
        get() = false

    override fun validate(item: FormItem): Boolean = item.getValueString().toDouble() in min..max
    override fun failureMessage() = "Value is not between $min, $max"
}

class ValueMatchRule(private val value: Double) : ValueValidationRule {
    override val enforceOnHidden: Boolean
        get() = false

    override fun validate(item: FormItem): Boolean = item.getValueString().toDouble() == value
    override fun failureMessage() = "Value is not equal to $value"
}

