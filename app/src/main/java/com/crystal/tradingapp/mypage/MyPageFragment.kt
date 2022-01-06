package com.crystal.tradingapp.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.crystal.tradingapp.R
import com.crystal.tradingapp.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyPageFragment : Fragment(R.layout.fragment_mypage) {
    private var binding: FragmentMypageBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMypageBinding = FragmentMypageBinding.bind(view)
        binding = fragmentMypageBinding

        fragmentMypageBinding.signInOutButton.setOnClickListener {
            binding?.let { binding ->
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                if (auth.currentUser == null) {
                    //로그인
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                successSignIn()
                            } else {
                                Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                            }
                        }

                } else {
                    //로그아웃
                    successSingOut()
                }
            }
        }
        fragmentMypageBinding.signUpButton.setOnClickListener {
            binding?.let { binding ->
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener (requireActivity()){task->
                        if(task.isSuccessful){
                            Toast.makeText(context, "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "회원가입에 실패했습니다. 이미 가입한 이메일일 수 있습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        fragmentMypageBinding.emailEditText.addTextChangedListener {
            binding?.let { binding ->
                val enable =
                    binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                binding.signInOutButton.isEnabled = enable
                binding.signUpButton.isEnabled = enable
            }
        }
        fragmentMypageBinding.passwordEditText.addTextChangedListener {
            binding?.let { binding ->
                val enable =
                    binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                binding.signInOutButton.isEnabled = enable
                binding.signUpButton.isEnabled = enable
            }
        }


    }

    private fun successSingOut() {
        auth.signOut()
        binding?.let { binding ->
            binding.emailEditText.text.clear()
            binding.emailEditText.isEnabled = true

            binding.passwordEditText.text.clear()
            binding.passwordEditText.isEnabled = true

            binding.signInOutButton.text = "Sign in"
            binding.signInOutButton.isEnabled = false
            binding.signUpButton.isEnabled = false
        }

    }

    private fun successSignIn() {
        if (auth.currentUser == null) {
            Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
            return
        }
        binding?.emailEditText?.isEnabled = false
        binding?.passwordEditText?.isEnabled = false
        binding?.signUpButton?.isEnabled = false
        binding?.signInOutButton?.text = "Sign out"
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            successSingOut()
        } else {
            binding?.let { binding ->
                binding.emailEditText.isEnabled = false
                binding.emailEditText.setText(auth.currentUser?.email)

                binding.passwordEditText.setText("******")
                binding.passwordEditText.isEnabled = false

                binding.signUpButton.isEnabled = false
                binding.signInOutButton.text = "Sign out"
            }
        }
    }
}