package com.example.loginapp.data

import com.example.loginapp.model.User

object UserRepository {
    // 유저 목록
    private val users = mutableListOf<User>(
        User("이강진", "IDKOS", "kss3736@naver.com", "password!!1"),
        User("공명선", "msBall", "kms0209@naver.com", "kmsjjang!!1"),
        User("김현지","HingGgu", "khj0923@gmail.com", "password!!1"),
    )

    // 새로운 유저 추가
    fun addUser(user: User) {
        users.add(user)
    }

    // 특정 유저 삭제
    fun removeUser(user: User) {
        users.remove(user)
    }

    // 모든 유저 데이터 삭제 (초기화)
    fun clearUsers() {
        users.clear()
    }

    // 이메일 중복성 검사
    fun isEmailDuplicate(email: String): Boolean {
        return users.any { it.email == email }
    }

    // 닉네임 중복성 검사
    fun isNicknameDuplicate(nickname: String): Boolean {
        return users.any { it.nickname == nickname }
    }
}