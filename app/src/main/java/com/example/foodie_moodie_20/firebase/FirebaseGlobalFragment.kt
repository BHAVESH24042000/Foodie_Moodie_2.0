package com.example.foodie_moodie_20.firebase

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_moodie_20.R
import com.example.foodie_moodie_20.databinding.FragmentFirebaseGlobalBinding
import com.example.foodie_moodie_20.firebase.adapters.GlobalMemoryAdapter
import com.example.foodie_moodie_20.firebase.firebaseDataModels.Memory
import com.example.foodie_moodie_20.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseGlobalFragment : Fragment() {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser!!.uid
    }

    private var mBinding : FragmentFirebaseGlobalBinding? = null
    private lateinit var mProgressDialog : Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding= FragmentFirebaseGlobalBinding.inflate(inflater,container,false)
        return mBinding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding=null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.personal_menu,menu)
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        //FireStoreClass().getAllMemoriesList(this)
        mFireStore.collection(Constants.MEMORIES)
            .whereEqualTo(Constants.PRIVACY,Constants.PUBLIC)
            .get()
            .addOnSuccessListener { list->
                val memoryList : ArrayList<Memory> = ArrayList()
                for(i in list.documents){
                    val memory =i.toObject(Memory::class.java)!!
                    memoryList.add(memory)
                }
                listReceivedSuccessfully(memoryList)

            }
            .addOnFailureListener {
                hideProgressBar()
            }
    }

    fun listReceivedSuccessfully(memoryList: ArrayList<Memory>) {
        hideProgressBar()
        if(memoryList.size>0){
            mBinding!!.rvGlobalMemoryList.layoutManager= LinearLayoutManager(requireContext())
            val rvAdapter= GlobalMemoryAdapter(requireContext(),memoryList)
            mBinding!!.rvGlobalMemoryList.adapter=rvAdapter
        }else{
            Toast.makeText(requireContext(), "Error! Please try after some time", Toast.LENGTH_SHORT).show()
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

}