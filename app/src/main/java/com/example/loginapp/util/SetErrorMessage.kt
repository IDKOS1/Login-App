package com.example.loginapp.util

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.loginapp.R

/**
 * 에러 메세지 설정 함수
 * @param view 에러 메세지가 표시될 뷰
 * @param isError 에러 여부
 * @param message 에러 메세지
 **/
fun TextView.setErrorMessage(isError: Boolean, message: String) {
    setTextColor(
        if (isError) ContextCompat.getColor(context, R.color.error)
        else ContextCompat.getColor(context, R.color.blue)
    )
    text = message
    visibility = View.VISIBLE
}