package com.example.loginapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginapp.databinding.FragmentLoginBinding
import com.example.loginapp.ui.MainActivity
import com.example.loginapp.ui.signup.SignupFragment

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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

        binding.btnNavigateSignup.setOnClickListener {
            (activity as? MainActivity)?.switchFragment(SignupFragment())
        }

    }
}