package com.zaidkhan.socialx.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.zaidkhan.socialx.SocialXApplication
import com.zaidkhan.socialx.utils.Resource
import com.zaidkhan.socialx.databinding.FragmentSignupBinding
import com.zaidkhan.socialx.viewModel.AuthViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private var callback: LoginFragment.MainActivityCallback? = null
    @Inject
    @Named("Auth")
    lateinit var authfactory: ViewModelProvider.Factory
    private val authViewModel: AuthViewModel by viewModels { authfactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as? LoginFragment.MainActivityCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as SocialXApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignIn.setOnClickListener {
            val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
            Navigation.findNavController(it!!).navigate(action)
            callback?.signupToLogin()
        }
        binding.btnRegister.setOnClickListener {
            if (binding.checkBox.isChecked) {
                val email = binding.etEmail.text.toString().trim()
                val name = binding.etName.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()
                val phoneNo = binding.etPhone.text.toString().trim()
                if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && phoneNo.isNotEmpty()) {
                    authViewModel?.signup(name, email, password, phoneNo)
                    lifecycleScope.launch {
                        authViewModel.signupFlow.collect {
                            when (it) {
                                is Resource.Success -> {
                                    Toast.makeText(requireContext(), "User Register Successfully", Toast.LENGTH_LONG).show()
                                    val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
                                    Navigation.findNavController(view).navigate(action)
                                    authViewModel.logout()
                                    callback?.signupToLogin()
                                }
                                is Resource.Failure -> {
                                    Toast.makeText(requireContext(), "Registration is Fail", Toast.LENGTH_LONG).show()
                                }
                                is Resource.Loading -> {
                                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                                }
                                else -> {}
                            }
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Empty Fields Are not allowed !!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Check The Term & Conditions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}