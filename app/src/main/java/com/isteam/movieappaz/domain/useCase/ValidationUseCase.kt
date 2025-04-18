package com.isteam.movieappaz.domain.useCase

import android.util.Patterns
import com.isteam.movieappaz.R
import com.isteam.movieappaz.common.utils.ValidationResult

class ValidationUseCase {

    fun executeEmail(email: String): ValidationResult {
        if (email.isEmpty()) {
            return ValidationResult(
                successful = false,
                errorMessage = R.string.empty_mail
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = R.string.invalid_mail
            )
        }
        return ValidationResult(successful = true)
    }

    fun executePassword(password: String): ValidationResult {
        if (password.isEmpty()) {
            return ValidationResult(
                successful = false,
                errorMessage = R.string.empty_password
            )
        }
        if (password.length < 6) {
            return ValidationResult(successful = false, errorMessage = R.string.short_password)
        }
        val containsLetterAndDigit = password.any { it.isDigit() } && password.any { it.isLetter() }

        if (!containsLetterAndDigit) {
            return ValidationResult(successful = false, errorMessage = R.string.letter_digit_pass)
        }
        return ValidationResult(successful = true)
    }

    fun executeName(name: String): ValidationResult {
        if (name.isEmpty()) {
            return ValidationResult(
                successful = false,
                errorMessage = R.string.empty_name
            )
        }
        return ValidationResult(successful = true)
    }

    fun executeNickname(name: String): ValidationResult {
        if (name.isEmpty()) {
            return ValidationResult(
                successful = false,
                errorMessage = R.string.empty_nickname
            )
        }
        return ValidationResult(successful = true)
    }

}