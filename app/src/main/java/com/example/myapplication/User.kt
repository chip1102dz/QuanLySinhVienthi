package com.example.myapplication

import java.io.Serializable

data class User(
    var id: Int = 0,
    var name: String = "",
    var date: String = "",
    var image: Int = 0
) : Serializable {
}