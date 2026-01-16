
package com.example.furfriends

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val password: String = ""
) {
    // No-argument constructor for Firebase
    constructor() : this("", "", "", "", "", "")
}