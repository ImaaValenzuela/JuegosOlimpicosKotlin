package com.example.jjoo.utils

import android.content.Context
import com.example.jjoo.data.User

object CurrentUser {

    var user: User? = null

    // Cargar el usuario desde SharedPreferences
    fun loadUser(context: Context) {
        SharedPreferencesManager.initialize(context)
        user = SharedPreferencesManager.getUser()
    }

    // Guardar el usuario en SharedPreferences
    fun setUser(context: Context, user: User) {
        SharedPreferencesManager.initialize(context)
        SharedPreferencesManager.saveUser(user)
    }

    // Limpiar el usuario de SharedPreferences
    fun clearUser(context: Context) {
        SharedPreferencesManager.initialize(context)
        SharedPreferencesManager.clearUser()
    }
}