package com.example.foodie_moodie_20.authScreens

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.foodie_moodie_20.R
import com.example.foodie_moodie_20.databinding.FragmentSignupBinding
import com.example.foodie_moodie_20.firebase.FirebaseUserActivity
import com.example.foodie_moodie_20.firebase.firebaseDataModels.Firebaseuser
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class SignupFragment : Fragment() {


    private lateinit var mProgressDialog : Dialog
    private lateinit var mBinding : FragmentSignupBinding
    private val mFireStore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding= FragmentSignupBinding.inflate(inflater, container, false)


        mBinding.btnRegister.setOnClickListener {
            registerUser()
        }
        mBinding?.SignupGuest?.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_mainActivity)
        }

        return mBinding?.root

    }


    private fun registerUser() {
        if(validateUser()){
            showProgressDialog()

            val email = mBinding.etEmail.text.toString().trim{it<=' '}
            val password = mBinding.etPassword.text.toString().trim{it<=' '}
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val firebaseUser : FirebaseUser =  task.result!!.user!!
                        val user = Firebaseuser(
                            firebaseUser.uid,
                            mBinding.etFirstName.text.toString().trim{it<=' '},
                            mBinding.etLastName.text.toString().trim{it<=' '},
                            mBinding.etEmail.text.toString().trim{it<=' '},
                            mBinding.etPassword.text.toString().trim{it<=' '}
                        )
                        //FireBaseClass().registerUser(user)

                        mFireStore.collection("users")
                            .document(user.userID)
                            .set(user, SetOptions.merge())
                            .addOnSuccessListener {
                                //activity.userRegistrationSuccess()
                                userRegistrationSuccess()
                            }
                            .addOnFailureListener {
                                //fragment.hideProgressBar()
                                Log.e("SignUp Error","Error while registering the user")
                            }

                    }else{
                        hideProgressBar()
                        showErrorSnackBar(task.exception!!.message.toString())
                    }
                }
        }
    }

    fun userRegistrationSuccess() {
        hideProgressBar()
        Toast.makeText(requireContext(), "User Registered Successfully", Toast.LENGTH_LONG).show()
        //findNavController().navigate(R.id.action_signupFragment_to_mobile_navigation)
        val intent = Intent(context, FirebaseUserActivity::class.java)
        startActivity(intent)

        // val intent = Intent(this, LoginActivity::class.java)
        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //startActivity(intent)

    }

    private fun validateUser(): Boolean {
        return when{
            TextUtils.isEmpty(mBinding.etFirstName.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Please Enter Your First Name")
                false
            }
            TextUtils.isEmpty(mBinding.etLastName.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Please Enter Your Last Name")
                false
            }
            TextUtils.isEmpty(mBinding.etEmail.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Please Enter Your Email")
                false
            }
            TextUtils.isEmpty(mBinding.etPassword.text.toString().trim{it<=' '}) ->{
              showErrorSnackBar("Please Enter Your Password")
                false
            }
            TextUtils.isEmpty(mBinding.etConfirmPassword.text.toString().trim{it<=' '}) ->{
              showErrorSnackBar("Please Confirm Your Password")
                false
            }
            mBinding.etPassword.text.toString().trim{it<=' '}!=mBinding.etConfirmPassword.text.toString().trim{it<=' '} -> {
            showErrorSnackBar("Passwords Do Not Match")
                false
            }
            else -> {
                true
            }
        }

    }

    fun showProgressDialog(){
        mProgressDialog = Dialog(requireContext(),R.style.myDialogStyle)
        mProgressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun hideProgressBar(){
        mProgressDialog.dismiss()
    }

    fun showErrorSnackBar(message:String){
        val snackBar : Snackbar = Snackbar.make(requireActivity().findViewById(android.R.id.content),message,Snackbar.LENGTH_SHORT)
        snackBar.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_color))
        snackBar.show()
    }
}