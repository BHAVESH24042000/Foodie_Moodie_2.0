package com.example.utils

import android.app.Dialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.foodie_moodie_20.R

import com.google.android.material.snackbar.Snackbar

open class BaseFragment : Fragment() {
    private lateinit var mProgressDialog : Dialog


    fun showProgressDialog(){
        mProgressDialog = Dialog(requireContext(), R.style.myDialogStyle)
        mProgressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun hideProgressBar(){
        mProgressDialog.dismiss()
    }
}