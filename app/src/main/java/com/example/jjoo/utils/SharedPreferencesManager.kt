package com.example.jjoo.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.jjoo.data.User
import com.google.gson.Gson

object SharedPreferencesManager {

    private const val PREF_NAME = "user_prefs"
    private const val USER_KEY = "current_user"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    // Guardar el usuario
    fun saveUser(user: User) {
        val gson = Gson()
        val userJson = gson.toJson(user)
        editor.putString(USER_KEY, userJson)
        editor.apply()
    }

    // Obtener el usuario
    fun getUser(): User? {
        val gson = Gson()
        val userJson = sharedPreferences.getString(USER_KEY, null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    // Limpiar el usuario
    fun clearUser() {
        editor.remove(USER_KEY)
        editor.apply()
    }
}
