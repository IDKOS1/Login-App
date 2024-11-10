package com.example.loginapp.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.loginapp.R
import com.example.loginapp.databinding.FragmentLoginBinding
import com.example.loginapp.ui.MainActivity
import com.example.loginapp.ui.MainFragment
import com.example.loginapp.ui.signup.SignupFragment
import com.example.loginapp.util.setErrorMessage
import com.example.loginapp.util.setFocusAndScroll
import com.example.loginapp.util.toastMessage
import com.example.loginapp.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    private var isPasswordVisible = false

    private var isEmailValid = false
    private var isPasswordValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // 키보드가 올라왔을 때 하단 인셋을 적용하여 축소 효과
            v.setPadding(
                systemBarsInsets.left,
                imeInsets.top - systemBarsInsets.top,
                systemBarsInsets.right,
                imeInsets.bottom - systemBarsInsets.bottom
            )
            insets
        }
        initialSetting()
        setInputValidation()
        setLoginBtn()


    }

    private fun setLoginBtn() {
        binding.run {
            btnLogin.setOnClickListener {
                if (it.isEnabled) {
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()

                    val isEmailExists = loginViewModel.checkEmail(email) // 이메일로 유저 조회

                    if (isEmailExists) {
                        if (loginViewModel.isLoginPossible(email, password)) {
                            // 비밀번호가 일치할 때
                            val user = loginViewModel.returnUser(email, password)
                            val mainFragment = MainFragment()
                            val bundle = Bundle()

                            bundle.putParcelable("user", user)
                            mainFragment.arguments = bundle

                            (activity as? MainActivity)?.switchFragment(mainFragment)
                            toastMessage(requireContext(), "로그인 되었습니다.")
                        } else {
                            // 비밀번호가 일치하지 않을 때
                            toastMessage(requireContext(), "비밀번호가 일치하지 않습니다.")
                        }
                    } else {
                        toastMessage(requireContext(), "등록되지 않은 이메일입니다.")
                    }
                }
            }
        }
    }

    private fun setInputValidation() {
        binding.run {
            // 이메일 유효성 검사
            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"

                    if (s.isNullOrEmpty()) {
                        tvEmailError.visibility = View.GONE
                    } else if (!s.matches(emailPattern.toRegex())) {
                        tvEmailError.setErrorMessage(
                            true, "이메일 형식이 올바르지 않습니다."
                        )
                        isEmailValid = false
                        checkValidCredentials()
                    } else {
                        tvEmailError.visibility = View.GONE
                        isEmailValid = true
                        checkValidCredentials()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            // 비밀번호 유효성 검사
            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 최소 8자 이상, 특수문자와 숫자가 최소 1번씩 포함된 패턴
                    val passwordPattern = "^(?=.*[0-9])(?=.*[!@#\$%^&*])(?=\\S+$).{8,}$"

                    if (s.isNullOrEmpty()) {
                        // 비밀번호가 비어 있을 때 에러 메시지 숨김
                        tvPasswordError.visibility = View.GONE
                        isPasswordValid = false
                    } else if (!s.matches(passwordPattern.toRegex())) {
                        // 비밀번호 조건이 만족되지 않을 경우 에러 메시지 표시
                        tvPasswordError.setErrorMessage(
                            true, "영문, 숫자, 특수문자 조합으로 8~15자이여야 합니다."
                        )
                        isPasswordValid = false
                        checkValidCredentials()
                    } else {
                        // 비밀번호가 유효할 때 에러 메시지 숨김
                        tvPasswordError.visibility = View.GONE
                        isPasswordValid = true
                        checkValidCredentials()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
    }


    private fun initialSetting() {
        binding.run {
            // 비밀번호 표시 전환
            ivShowPassword.setOnClickListener {
                etPassword.inputType = if (isPasswordVisible) {
                    isPasswordVisible = false
                    ivShowPassword.setImageResource(R.drawable.ic_visibility_off)
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD // 비밀번호 숨김

                } else {
                    isPasswordVisible = true
                    ivShowPassword.setImageResource(R.drawable.ic_visibility)
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD // 비밀번호 표시
                }

            }

            // 회원가입 버튼 클릭 시 회원가입 화면으로 전환
            btnNavigateSignup.setOnClickListener {
                (activity as? MainActivity)?.switchFragment(SignupFragment())
            }

            // EditText 포커스시 스크롤 이동
            setFocusAndScroll(svLogin, etEmail, etPassword)
        }
    }

    private fun checkValidCredentials() {
        binding.run {
            if (isEmailValid && isPasswordValid) {
                btnLogin.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.main)

                btnLogin.isEnabled = true
                Log.d("LoginFragment", "로그인 버튼 활성화")
            } else {
                btnLogin.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.gray)

                btnLogin.isEnabled = false
                Log.d("LoginFragment", "로그인 버튼 비활성화")
            }
        }
    }
}