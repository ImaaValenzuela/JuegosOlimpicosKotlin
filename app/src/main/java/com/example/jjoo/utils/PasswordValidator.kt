package com.example.jjoo.utils

import org.passay.CharacterRule
import org.passay.EnglishCharacterData
import org.passay.PasswordData
import org.passay.PasswordValidator
import org.passay.RuleResult
import org.passay.WhitespaceRule

object PasswordValidator {

    fun validate(password: String): Boolean {
        val validator = PasswordValidator(
            listOf(
                CharacterRule(EnglishCharacterData.UpperCase, 1), // Al menos 1 mayúscula
                CharacterRule(EnglishCharacterData.LowerCase, 1), // Al menos 1 minúscula
                CharacterRule(EnglishCharacterData.Digit, 1),     // Al menos 1 número
                org.passay.LengthRule(8, 20),                    // Longitud entre 8 y 20 caracteres
                WhitespaceRule()                                 // Sin espacios en blanco
            )
        )

        val result: RuleResult = validator.validate(PasswordData(password))
        return result.isValid
    }
}