package com.example.nattramn.features.user.ui.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.nattramn.R
import com.example.nattramn.core.resource.Status
import com.example.nattramn.core.utils.setOnSingleClickListener
import com.example.nattramn.core.utils.snackMaker
import com.example.nattramn.databinding.FragmentLoginBinding
import com.example.nattramn.features.user.data.UserNetwork
import com.example.nattramn.features.user.data.models.AuthRequest
import com.example.nattramn.features.user.ui.viewmodels.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    companion object {
        const val minUsernameLength = 7
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showSystemUI()

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding = FragmentLoginBinding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = loginViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onEnterClick()

        onRegisterClick()

    }

    private fun onEnterClick() {

        binding.loginEnterButton.setOnClickListener {

            val email = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()

            if (email.isValidEmail()) {
                loginViewModel.loginUser(
                    AuthRequest(
                        UserNetwork(
                            email = email,
                            password = password
                        )
                    )
                )
            }
        }

        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Navigation.findNavController(requireView())
                        .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                }
                Status.LOADING -> {
                    snackMaker(requireView(), getString(R.string.messagePleaseWait))
                }
                Status.ERROR -> {
                    snackMaker(requireView(), getString(R.string.messageServerConnectionError))
                }
            }

        })

    }

    private fun onRegisterClick() {
        binding.loginRegisterButton.setOnSingleClickListener { view ->
            Navigation.findNavController(view)
                .navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun CharSequence?.isValidEmail(): Boolean {

        if (isNullOrEmpty()) {
            binding.loginUsername.requestFocus()
            binding.loginUsername.error = getString(R.string.errorEnterEmail)
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(this!!).matches()) {
            binding.loginUsername.requestFocus()
            binding.loginUsername.error = getString(R.string.errorEmailFormat)
            return false
        }

        return true
    }

    private fun CharSequence?.isValidUsername(): Boolean {

        if (this == null) {

            binding.loginUsername.requestFocus()
            binding.loginUsername.error = getString(R.string.errorEnterUsername)
            return false

        }

        if (isNullOrEmpty()) {

            binding.loginUsername.requestFocus()
            binding.loginUsername.error = getString(R.string.errorEnterUsername)
            return false

        } else if (binding.loginUsername.text!!.length > minUsernameLength
            && !Patterns.EMAIL_ADDRESS.matcher(this).matches()
        ) {

            return true

        } else if (!Patterns.EMAIL_ADDRESS.matcher(this).matches()) {

            binding.loginUsername.requestFocus()
            binding.loginUsername.error = getString(R.string.errorEmailOrUsernameFormat)
            return false

        }

        binding.loginUsername.error = null
        return true
    }

    @Suppress("DEPRECATION")
    private fun showSystemUI() {
        requireActivity().window.decorView.systemUiVisibility = 0
    }

}
