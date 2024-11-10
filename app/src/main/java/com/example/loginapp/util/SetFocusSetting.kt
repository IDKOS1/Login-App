package com.example.loginapp.util

import android.widget.EditText
import android.widget.ScrollView

/**
 * EditText에 포커스 시 ScrollView를 조정하는 함수
 * @param scrollView 조정할 ScrollView
 * @param editTexts 포커스를 조정할 EditText
 **/
fun setFocusAndScroll(scrollView: ScrollView, vararg editTexts: EditText) {
    for (editText in editTexts) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                scrollView.post {
                    scrollView.scrollBy(0, 50) // 필요에 따라 조정 (예: 50px)
                }
            }
        }
    }
}