package com.netservex.caf.core

import android.text.TextUtils
import android.util.Patterns
import android.widget.CheckBox
import android.widget.EditText

fun EditText.isValidEditText(message: String = "Invalid field"): Boolean {
    if (!text.toString().isBlank()) {
        error = null
        return true
    } else {
        error = message
        return false
    }
}


fun EditText.isConfirmPasswordValid(password: String): Boolean {

    return if (text.isNullOrBlank() || text.toString() != password) {
        error = "Password not match"
        false
    } else {
        error = null
        true
    }
}


fun EditText.isEditTextValid(vararg editTexts: EditText, message: String = ""): Boolean {
    var isValid = true
    for (editText in editTexts) {
        if (editText.text.toString().isBlank()) {
            editText.error = message
            isValid = false
        } else {
            editText.error = null
        }
    }
    return isValid
}

fun EditText.isValidPhone(): Boolean {
    if (textValue().isBlank()) {
        error = "Phone is required"
        return false
    } else if (textValue().length != 10) {
        error = "It should be 10 numbers"
        return false
    } else {
        error = null
        return true
    }
}

fun EditText.isValidCreditNumber(): Boolean {
    if (textValue().isBlank()) {
        error = "Card number is required"
        return false
    } else if (textValue().length != 19) {
        error = "Card number should be 16 numbers"
        return false
    } else {
        error = null
        return true
    }
}

fun EditText.isValidExpiryDate(): Boolean {
    if (textValue().isBlank()) {
        error = "Expiry date is required"
        return false
    } else {
        error = null
        return true
    }
}

fun EditText.isValidCCV(): Boolean {
    if (textValue().isBlank()) {
        error = "CCV number is required"
        return false
    } else if (textValue().length != 3) {
        error = "CCV number should be 3 numbers"
        return false
    } else {
        error = null
        return true
    }
}

fun EditText.isValidAmount(): Boolean {
    if (textValue().isBlank()) {
        error = "Amount is required"
        return false
    } else if (textValue().toInt() == 0) {
        error = "It should be a valid number"
        return false
    } else {
        error = null
        return true
    }
}

fun EditText.isValidWithData(data: Any?, message: String = "Champs requis"): Boolean {
    var isValid = true
    if (text.toString().isBlank() || data == null || (data is Collection<*> && data.isEmpty())) {
        error = message
        isValid = false
    } else {
        error = null
    }
    return isValid
}

fun EditText.isPasswordValid(): Boolean {

    return if (text.isNullOrBlank() || text.toString().length < 6) {
        error = "Password need to be 8 characters at least"
        false
    } else {
        error = null
        true
    }
}

fun EditText.isValidEmail(message: String = "Invalid email"): Boolean {
    if (!TextUtils.isEmpty(text.toString()) && Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
        error = null
        return true
    } else {
        error = message
        return false
    }
}

fun EditText.isValidEmailWithData(data: Any?, message: String = "Invalid email"): Boolean {
    if (!TextUtils.isEmpty(text.toString()) && Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches() || data == null) {
        error = null
        return true
    } else {
        error = message
        return false
    }
}

fun EditText.replaceText(text: String) {
    val originalText = text.replace(text.toRegex(), "")
    setText(originalText)
}

fun CheckBox.isValid(message: String = "Champs requis"): Boolean {
    return if (!isChecked) {
        error = message
        false
    } else {
        error = null
        true
    }
}

fun EditText.textValue() = text.toString()