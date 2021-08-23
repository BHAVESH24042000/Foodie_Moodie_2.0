package com.example.foodie_moodie_20.authScreens

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodie_moodie_20.R
import com.example.foodie_moodie_20.databinding.FragmentLoginBinding
import com.example.foodie_moodie_20.firebase.FirebaseUserActivity
import com.example.foodie_moodie_20.firebase.firebaseDataModels.Firebaseuser
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginFragment : Fragment() {

    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var  mProgressDialog : Dialog
    private var mBinding: FragmentLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProgressDialog = Dialog(requireContext(),R.style.myDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentLoginBinding.inflate(inflater, container, false)


        mBinding?.loginGuest?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
        }
        mBinding?.btnLogin?.setOnClickListener {
            loginRegisteredUser()
        }
        return mBinding?.root


    }


    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser!!.uid
    }

    private fun loginRegisteredUser() {
        if(validateUser()){
           showProgressDialog()
            val email=mBinding?.etEmail?.text.toString().trim {it<=' ' }
            val password=mBinding?.etPassword?.text.toString().trim {it<=' ' }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        //FireBaseClass().getUserDetails()

                        mFireStore.collection("users")
                            .document(getCurrentUserID())
                            .get()
                            .addOnSuccessListener { userFromFirestore->
                                val user=userFromFirestore.toObject(Firebaseuser::class.java)!!
                                userLoggedInSuccess(user)
                                //fragment.userLoggedInSuccess(user)
                            }
                            .addOnFailureListener {
                                //fragment.hideProgressBar()
                                Log.e("Login Error","Error while getting user details")
                            }


                    }else{
                        hideProgressBar()
                        showErrorSnackBar(task.exception!!.message.toString())
                    }
                }
        }
    }

    private fun validateUser(): Boolean {
        return when{
            TextUtils.isEmpty(mBinding?.etEmail?.text.toString().trim {it<=' ' }) ->{
                showErrorSnackBar("Please Enter Your Email ID")
                false
            }
            TextUtils.isEmpty(mBinding?.etPassword?.text.toString().trim {it<=' ' }) ->{
                showErrorSnackBar("Please Enter Your Password")
                false
            }
            else ->{
                true
            }
        }
    }

    fun userLoggedInSuccess(user: Firebaseuser) {
        Log.i("login", "userLoggedInSuccess: ${user.email}")
        Toast.makeText(requireContext(), "Login Done Successfully", Toast.LENGTH_LONG).show()
        hideProgressBar()
       //findNavController().navigate(R.id.action_loginFragment_to_mobile_navigation)
        val intent = Intent(context, FirebaseUserActivity::class.java)
        startActivity(intent)

    }

    fun showProgressDialog(){

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
        val snackBar : Snackbar = Snackbar.make(requireActivity().findViewById(android.R.id.content),message,
            Snackbar.LENGTH_SHORT)
        snackBar.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_color))
        snackBar.show()
    }

}