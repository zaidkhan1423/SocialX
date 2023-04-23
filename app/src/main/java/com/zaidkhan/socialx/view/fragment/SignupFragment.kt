package com.zaidkhan.socialx.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zaidkhan.socialx.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private var callback: LoginFragment.MainActivityCallback? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as? LoginFragment.MainActivityCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize it in your sign-up activity's onCreate method
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val userData = hashMapOf(
                                    "name" to name,
                                    "email" to email,
                                    "phoneNumber" to phoneNo
                                )
                                db.collection("users").document(user!!.uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "User Register Successfully",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        val action =
                                            SignupFragmentDirections.actionSignupFragmentToLoginFragment()
                                        Navigation.findNavController(view).navigate(action)
                                        callback?.signupToLogin()
                                    }
                                    .addOnFailureListener {
                                        // Registration failed
                                        Toast.makeText(
                                            requireContext(),
                                            it.suppressedExceptions.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                // Registration failed
                                Toast.makeText(
                                    requireContext(),
                                    task.exception.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Empty Fields Are not allowed !!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else{
                Toast.makeText(requireContext(), "Check The Term & Conditions", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}