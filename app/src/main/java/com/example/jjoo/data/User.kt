package com.example.jjoo.data

data class User(
    val id: Long = 0,
    val nickName: String = "",
    val password: String = "",
    val email : String = "",
    val name: String = "",
    val surname: String = "",
    var money: Double = 500000.0,
    val createdDate: String = "",
    val rol: String = "user"
)