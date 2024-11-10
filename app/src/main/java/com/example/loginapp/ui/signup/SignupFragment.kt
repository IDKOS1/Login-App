package com.example.loginapp.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.loginapp.databinding.FragmentSignupBinding
import com.example.loginapp.model.User
import com.example.loginapp.ui.MainActivity
import com.example.loginapp.ui.login.LoginFragment
import com.example.loginapp.util.setErrorMessage
import com.example.loginapp.util.setFocusAndScroll
import com.example.loginapp.util.toastMessage
import com.example.loginapp.viewmodel.SignupViewModel

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val signupViewModel: SignupViewModel by viewModels()

    private var isEmailValid = false
    private var isPasswordValid = false
    private var isPasswordMatch = false

    private var isEmailChecked = false
    private var isNicknameChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
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
        setInputValidation()
        setCheckDuplicate()

        // EditText 포커스시 스크롤 이동
        binding.run {
            setFocusAndScroll(svSignup, etUserName, etEmail, etPassword, etPasswordCheck, etNickname)
        }

        setSignup()
    }

    private fun setSignup() {
        binding.run {
            btnSignup.setOnClickListener {
                when {
                    etUserName.text.isNullOrEmpty() -> {
                        toastMessage(requireContext(), "이름을 입력해주세요.")
                        etUserName.requestFocus()
                    }

                    etEmail.text.isNullOrEmpty() -> {
                        toastMessage(requireContext(), "이메일을 입력해주세요.")
                        etEmail.requestFocus()
                    }

                    !isEmailValid -> {
                        toastMessage(requireContext(), "이메일 형식이 올바르지 않습니다.")
                        etEmail.requestFocus()
                    }

                    !isEmailChecked -> {
                        toastMessage(requireContext(), "이메일 중복검사를 해주세요.")
                        etEmail.requestFocus()
                    }

                    etPassword.text.isNullOrEmpty() -> {
                        toastMessage(requireContext(), "비밀번호를 입력해주세요.")
                        etPassword.requestFocus()
                    }

                    !isPasswordValid -> {
                        toastMessage(requireContext(), "비밀번호를 확인해주세요.")
                        etPassword.requestFocus()
                    }

                    etPasswordCheck.text.isNullOrEmpty() -> {
                        toastMessage(requireContext(), "비밀번호 확인을 입력해주세요.")
                        etPasswordCheck.requestFocus()
                    }

                    !isPasswordMatch -> {
                        toastMessage(requireContext(), "비밀번호가 일치하지 않습니다.")
                        etPasswordCheck.requestFocus()
                    }

                    etNickname.text.isNullOrEmpty() -> {
                        toastMessage(requireContext(), "닉네임을 입력해주세요.")
                        etNickname.requestFocus()
                    }

                    !isNicknameChecked -> {
                        toastMessage(requireContext(), "닉네임 중복검사를 해주세요.")
                        etNickname.requestFocus()
                    }

                    else -> {
                        // UserRepository에 사용자 정보 추가
                        val user = User(
                            binding.etUserName.text.toString(),
                            binding.etEmail.text.toString(),
                            binding.etPassword.text.toString(),
                            binding.etNickname.text.toString()
                        )
                        signupViewModel.addUser(user)

                        // LoginFragment로 화면 전환 및 백스택 제거
                        (activity as? MainActivity)?.switchFragment(LoginFragment())
                        toastMessage(requireContext(), "회원가입 성공")
                    }
                }
            }

            tvNavigateToLogin.setOnClickListener {
                (activity as? MainActivity)?.switchFragment(LoginFragment())
            }
        }
    }

    private fun setInputValidation() {
        binding.run {
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
                        tvEmailError.setErrorMessage(true, "이메일 형식이 올바르지 않습니다.")
                        isEmailValid = false
                        isEmailChecked = false
                    } else {
                        tvEmailError.visibility = View.GONE
                        isEmailValid = true
                        isEmailChecked = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

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
                            true,
                            "영문, 숫자, 특수문자 조합으로 8~15자이여야 합니다."
                        )
                        isPasswordValid = false
                    } else {
                        // 비밀번호가 유효할 때 에러 메시지 숨김
                        tvPasswordError.setErrorMessage(false, "올바른 형식의 비밀번호입니다.")
                        isPasswordValid = true
                    }

                }

                override fun afterTextChanged(s: Editable?) {
                    // 비밀번호 입력이 변경될 때마다 비밀번호 확인도 다시 검사
                    val passwordCheckText = binding.etPasswordCheck.text.toString()

                    if (passwordCheckText.isEmpty()) {
                        // 비밀번호 확인이 비어 있는 경우 에러 메시지를 숨김
                        tvPasswordCheckError.visibility = View.GONE
                        isPasswordMatch = false
                    } else if (passwordCheckText != binding.etPassword.text.toString()) {
                        // 비밀번호 확인이 입력되어 있는 경우에만 일치 여부 검사
                        tvPasswordCheckError.setErrorMessage(true, "비밀번호가 일치하지 않습니다.")
                        isPasswordMatch = false
                    } else {
                        tvPasswordCheckError.setErrorMessage(false, "비밀번호가 일치합니다.")
                        isPasswordMatch = true
                    }


                }
            })

            etPasswordCheck.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        // 비밀번호 확인이 비어 있으면 에러 메시지 숨기기
                        if (s.isNullOrEmpty()) {
                            tvPasswordCheckError.visibility = View.GONE
                            isPasswordMatch = false
                        } else if (s.toString() != binding.etPassword.text.toString()) {
                            // 비밀번호와 일치하지 않으면 에러 메시지 표시
                            tvPasswordCheckError.setErrorMessage(true, "비밀번호가 일치하지 않습니다.")
                            isPasswordMatch = false
                        } else {
                            // 비밀번호와 일치하면 에러 메시지 숨김
                            tvPasswordCheckError.setErrorMessage(false, "비밀번호가 일치합니다..")
                            isPasswordMatch = true
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                    }
                })

            etNickname.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        // 닉네임 필드가 비어져 있을때 에러 메세지 숨김
                        if (s.isNullOrEmpty()) {
                            tvNicknameError.visibility = View.INVISIBLE
                        } else if (s.length > 10) {
                            // 닉네임이 10자 이상일 때 입력 제한 및 에러 메세지 표시
                            etNickname.setText(s.substring(0, 10))
                            etNickname.setSelection(10)
                            tvNicknameError.setErrorMessage(true, "닉네임은 10자 이하로 입력해주세요.")
                            isNicknameChecked = false
                        } else {
                            // 해당 조건이 없을때 에러 메세지 숨김
                            tvNicknameError.visibility = View.INVISIBLE
                            isNicknameChecked = false
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                    }
                })

        }
    }

    private fun setCheckDuplicate() {
        binding.run {
            tvEmailDuplicateCheck.setOnClickListener {
                validationEmail(etEmail.text.toString())
                isEmailChecked = true
            }

            tvNicknameDuplicateCheck.setOnClickListener {
                validationNickname(etNickname.text.toString())
                isNicknameChecked = true
            }
        }
    }

    private fun validationEmail(email: String) {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"

        binding.run {
            when {
                email.isEmpty() -> {
                    tvEmailError.setErrorMessage(true, "이메일을 입력해주세요.")
                }

                !email.matches(emailPattern.toRegex()) -> {
                    tvEmailError.setErrorMessage(true, "이메일 형식이 올바르지 않습니다.")
                }

                signupViewModel.checkEmailDuplicate(email) -> {
                    tvEmailError.setErrorMessage(true, "이미 사용 중인 이메일입니다.")
                }

                else -> {
                    tvEmailError.setErrorMessage(false, "사용 가능한 이메일입니다.")
                }
            }
        }
    }

    private fun validationNickname(nickname: String) {
        binding.run {
            when {
                nickname.isEmpty() -> {
                    tvNicknameError.setErrorMessage(true, "닉네임을 입력해주세요.")
                }

                signupViewModel.isNicknameDuplicate(nickname) -> {
                    tvNicknameError.setErrorMessage(true, "이미 사용 중인 닉네임입니다.")
                }

                else -> {
                    tvNicknameError.setErrorMessage(false, "사용 가능한 닉네임입니다.")
                }
            }
        }
    }

}