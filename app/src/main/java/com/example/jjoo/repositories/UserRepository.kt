package com.example.jjoo.repositories

import com.example.jjoo.data.User
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object UserRepository {

    private val database = Firebase.database
    private val userRef = database.getReference("users") // Nodo de usuarios en Firebase

    // Función para agregar un usuario a Firebase
    fun add(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        // Crear una clave única para el usuario usando 'push()' para evitar sobrescribir datos existentes
        val userId = userRef.push().key

        if (userId != null) {
            // Guardamos el usuario bajo esta clave única
            userRef.child(userId).setValue(user)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure(it) }
        } else {
            onFailure(Exception("Error al generar ID para el usuario"))
        }
    }

    // Función para eliminar un usuario de Firebase
    fun remove(id: Long, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        userRef.child(id.toString())
            .removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Función para encontrar un usuario por su ID
    fun findById(id: Long, onSuccess: (User?) -> Unit, onFailure: (Exception) -> Unit) {
        userRef.child(id.toString())
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                onSuccess(user)
            }
            .addOnFailureListener { onFailure(it) }
    }

    // Función para encontrar un usuario por su nickname
    fun findByNickname(nickname: String, onSuccess: (User?) -> Unit, onFailure: (Exception) -> Unit) {
        userRef.orderByChild("nickName")
            .equalTo(nickname)
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.children.firstOrNull()?.getValue(User::class.java)
                onSuccess(user)
            }
            .addOnFailureListener { onFailure(it) }
    }
}
