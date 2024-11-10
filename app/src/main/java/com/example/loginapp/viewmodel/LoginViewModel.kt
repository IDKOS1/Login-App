package com.example.loginapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.loginapp.data.UserRepository
import com.example.loginapp.model.User

class LoginViewModel : ViewModel() {
    // 이메일 존재 여부 검사
    fun checkEmail(email: String): Boolean {
        return UserRepository.isEmailDuplicate(email)
    }

    // 로그인 가능 여부 검사
    fun isLoginPossible(email: String, password: String): Boolean {
        return UserRepository.isLoginPossible(email, password)
    }

    fun returnUser(email: String, password: String): User? {
        return UserRepository.returnUser(email, password)
    }

}