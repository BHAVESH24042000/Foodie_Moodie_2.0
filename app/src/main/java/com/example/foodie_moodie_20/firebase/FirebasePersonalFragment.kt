package com.example.foodie_moodie_20.firebase

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_moodie_20.R
import com.example.foodie_moodie_20.authScreens.CheckUserActivity
import com.example.foodie_moodie_20.databinding.FragmentFirebasePersonalBinding
import com.example.foodie_moodie_20.firebase.adapters.PersonalMemoryAdapter
import com.example.foodie_moodie_20.firebase.firebaseDataModels.Memory
import com.example.shopperista.utils.SwipeToDeleteCallback
import com.example.shopperista.utils.SwipeToEditCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirebasePersonalFragment : Fragment() {

    private var mBinding : FragmentFirebasePersonalBinding? = null
    private lateinit var mProgressDialog : Dialog
    private val mFireStore = FirebaseFirestore.getInstance()

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser!!.uid
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding= FragmentFirebasePersonalBinding.inflate(inflater,container,false)
        return mBinding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.personal_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add ->{
                val intent= Intent(requireContext(),FirebaseAddEditMemory::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_logout->{
                FirebaseAuth.getInstance().signOut()
                val intent= Intent(requireContext(), CheckUserActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
                Toast.makeText(requireContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getListFromFireStore()
    }

    private fun getListFromFireStore(){
        showProgressBar()
        //FireStoreClass().getMemoriesList(this)
        mFireStore.collection("memories")
            .whereEqualTo("userID",getCurrentUserID())
            .get()
            .addOnSuccessListener { list->
                val memoryList : ArrayList<Memory> = ArrayList()
                for(i in list.documents){
                    val memory =i.toObject(Memory::class.java)!!

                    val memoryHashmap = HashMap<String,Any>()
                    memoryHashmap["memoryID"]=i.id
                    mFireStore.collection("memories")
                        .document(i.id)
                        .update(memoryHashmap)

                    memoryList.add(memory)
                }

                listReceivedSuccessfully(memoryList)

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error in Fetching Personal Feed", Toast.LENGTH_SHORT).show()
                hideProgressBar()
            }
    }

    fun listReceivedSuccessfully(memoryList: ArrayList<Memory>) {
        hideProgressBar()
        if(memoryList.size>0){
            mBinding!!.rvPersonal.visibility=View.VISIBLE
            mBinding!!.tvPersonal.visibility=View.GONE

            mBinding!!.rvPersonal.layoutManager= LinearLayoutManager(requireContext())
            val rvAdapter= PersonalMemoryAdapter(requireContext(),memoryList)
            mBinding!!.rvPersonal.adapter=rvAdapter



            val deleteSwipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = mBinding!!.rvPersonal.adapter as PersonalMemoryAdapter
                    adapter.notifyDeleteItem(this@FirebasePersonalFragment,viewHolder.adapterPosition)
                }
            }

            val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
            deleteItemTouchHelper.attachToRecyclerView(mBinding!!.rvPersonal)

            val editSwipeHandler = object : SwipeToEditCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = mBinding!!.rvPersonal.adapter as PersonalMemoryAdapter
                    adapter.notifyEditItem(this@FirebasePersonalFragment, viewHolder.adapterPosition)
                }
            }

            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(mBinding!!.rvPersonal)

        }else{
            mBinding!!.rvPersonal.visibility=View.GONE
            mBinding!!.tvPersonal.visibility=View.VISIBLE
        }

    }

    fun deleteSuccessful() {
        hideProgressBar()
        Toast.makeText(requireContext(), "Deleted successfully", Toast.LENGTH_SHORT).show()
        getListFromFireStore()
    }


    fun showProgressBar(){
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