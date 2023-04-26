package com.zaidkhan.socialx.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.zaidkhan.socialx.SocialXApplication
import com.zaidkhan.socialx.utils.Resource
import com.zaidkhan.socialx.databinding.FragmentLoginBinding
import com.zaidkhan.socialx.view.activity.NewsActivity
import com.zaidkhan.socialx.viewModel.AuthViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class LoginFragment : Fragment() {

    interface MainActivityCallback {
        fun loginToSignup()
        fun signupToLogin()
    }

    private lateinit var binding: FragmentLoginBinding
    private var callback: MainActivityCallback? = null
    @Inject
    lateinit var googleSignInClient: GoogleSignInClient
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    @Named("Auth")
    lateinit var authfactory: ViewModelProvider.Factory
    private val authViewModel: AuthViewModel by viewModels { authfactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as? MainActivityCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as SocialXApplication).appComponent.inject(this)

        if( authViewModel.currentUser != null){
            val intent = Intent(activity?.application, NewsActivity::class.java)
            startActivity(intent)
            activity?.finish()
            onDestroy()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegisterNow.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            Navigation.findNavController(it!!).navigate(action)
            callback?.loginToSignup()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                    logInUser(email,password)
            } else {
                Toast.makeText(requireContext(), "Incorrect Credentials", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGoogleLogin.setOnClickListener {
            signInGoogle()
        }

    }

    private fun logInUser(email: String, password: String) {
        authViewModel?.login(email, password)
        lifecycleScope.launch {
            authViewModel.loginFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(requireContext(), "Check Your Email And Password", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        Toast.makeText(requireContext(), "Login", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity?.application, NewsActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                        onDestroy()
                    }
                    Resource.Loading -> {
                        Toast.makeText(requireContext(), "Loading State", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Unexpected Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT)
                    .show()
                updateUI(account)
            }
        } else {
            Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                val intent = Intent(activity?.application, NewsActivity::class.java)
                startActivity(intent)
                activity?.finish()
                onDestroy()
            } else {
                Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}