package com.example.loginapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.loginapp.data.UserRepository
import com.example.loginapp.model.User

class SignupViewModel : ViewModel() {
    // 이메일 중복 검사
    fun checkEmailDuplicate(email: String): Boolean {
        return UserRepository.isEmailDuplicate(email)
    }

    // 닉네임 중복 검사
    fun isNicknameDuplicate(nickname: String): Boolean {
        return UserRepository.isNicknameDuplicate(nickname)
    }

    // 사용자 정보 추가
    fun addUser(user: User) {
        UserRepository.addUser(user)
    }
}