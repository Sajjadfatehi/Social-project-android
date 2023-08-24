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
import com.example.nattramn.core.utils.snackMaker
import com.example.nattramn.databinding.FragmentRegisterBinding
import com.example.nattramn.features.user.ui.viewmodels.RegisterViewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding = FragmentRegisterBinding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = registerViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onRegisterClick()

        setOnClicks()

    }

    private fun onRegisterClick() {
        binding.btnMembership.setOnClickListener {

            val username = binding.registerUsername.text.toString()
            val email = binding.registerUsernameConfirm.text.toString()
            val password = binding.registerPassword.text.toString()
            val passwordConfirmation = binding.registerPasswordConfirm.text.toString()

            if (password != passwordConfirmation) {
                binding.registerPassword.requestFocus()
                binding.registerPassword.error = getString(R.string.errorPasswordConfirmation)
            } else if (email.isValidEmail() && username.isValidUsername()) {
                registerViewModel.registerUser(username, email, password)
            }
        }

        registerViewModel.registerResult.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    snackMaker(requireView(), "ثبت نام با موفقیت انجام شد")
                    Navigation.findNavController(requireView())
                        .navigate(RegisterFragmentDirections.actionRegisterFragmentToHomerFragment())
                }
                Status.LOADING -> {
                    snackMaker(requireView(), getString(R.string.messagePleaseWait))
                }
                else -> {
                    if (it.message == "422") {
                        snackMaker(requireView(), getString(R.string.errorUsernameTaken))
                    } else {
                        snackMaker(requireView(), getString(R.string.messageServerConnectionError))
                    }
                }
            }
        })
    }

    private fun setOnClicks() {

        binding.tvEnter.setOnClickListener { view ->
            Navigation.findNavController(view)
                .navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }

        /*binding.btnMembership.setOnClickListener { view ->
            if (binding.registerUsername.text.isValidUsername() && binding.registerUsernameConfirm.text.isValidEmail()) {

                Navigation.findNavController(view)
                    .navigate(RegisterFragmentDirections.actionRegisterFragmentToHomerFragment())

            }
        }*/

    }

    private fun CharSequence?.isValidUsername(): Boolean {

        if (this == null) {

            binding.registerUsername.requestFocus()
            binding.registerUsername.error = getString(R.string.errorEnterUsername)
            return false

        }

        if (isNullOrEmpty()) {

            binding.registerUsername.requestFocus()
            binding.registerUsername.error = getString(R.string.errorEnterUsername)
            return false

        } else if (binding.registerUsername.text!!.length > LoginFragment.minUsernameLength
            && !Patterns.EMAIL_ADDRESS.matcher(this).matches()
        ) {

            return true

        } else if (!Patterns.EMAIL_ADDRESS.matcher(this).matches()) {

            binding.registerUsername.requestFocus()
            binding.registerUsername.error = getString(R.string.errorEmailOrUsernameFormat)
            return false

        }

        binding.registerUsername.error = null
        return true
    }

    private fun CharSequence?.isValidEmail(): Boolean {

        if (isNullOrEmpty()) {
            binding.registerUsernameConfirm.requestFocus()
            binding.registerUsernameConfirm.error = getString(R.string.errorEnterEmail)
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(this!!).matches()) {
            binding.registerUsernameConfirm.requestFocus()
            binding.registerUsernameConfirm.error = getString(R.string.errorEmailFormat)
            return false
        }

        return true
    }

}