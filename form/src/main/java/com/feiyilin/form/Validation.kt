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