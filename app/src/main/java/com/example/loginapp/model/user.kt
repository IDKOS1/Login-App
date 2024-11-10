package com.example.loginapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val nickname: String,
    val email: String,
    val password: String
) : Parcelable