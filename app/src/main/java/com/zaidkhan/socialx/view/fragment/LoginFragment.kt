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
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.zaidkhan.socialx.R
import com.zaidkhan.socialx.databinding.FragmentLoginBinding
import com.zaidkhan.socialx.view.activity.MainActivity
import com.zaidkhan.socialx.view.activity.NewsActivity


class LoginFragment : Fragment() {

    interface MainActivityCallback {
        fun loginToSignup()
        fun signupToLogin()
    }

    private lateinit var binding: FragmentLoginBinding
    private var callback: MainActivityCallback? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as? MainActivityCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
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
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(requireContext(), "Open", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            it.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Incorrect Credentials", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGoogleLogin.setOnClickListener {
            signInGoogle()
        }
    }

    val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(requireContext(), "launcher k under0", Toast.LENGTH_SHORT).show()
                val task =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
            else{
                Toast.makeText(requireContext(), "launcher k under", Toast.LENGTH_SHORT).show()
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
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
                Intent(activity?.application, NewsActivity::class.java)
            } else {
                Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInGoogle() {
        Toast.makeText(requireContext(), "Sign in k under", Toast.LENGTH_SHORT).show()
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}