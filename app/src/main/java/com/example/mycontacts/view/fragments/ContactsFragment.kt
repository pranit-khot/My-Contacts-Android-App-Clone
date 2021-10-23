package com.example.mycontacts.view.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycontacts.R
import com.example.mycontacts.application.MyContactApplication
import com.example.mycontacts.databinding.FragmentContactsBinding
import com.example.mycontacts.model.entities.MyContact
import com.example.mycontacts.model.entities.RecentContact
import com.example.mycontacts.view.activities.AddUpdateContactActivity
import com.example.mycontacts.view.activities.MainActivity
import com.example.mycontacts.view.adapters.MyContactAdapter
import com.example.mycontacts.viewmodel.MyContactViewModel
import com.example.mycontacts.viewmodel.MyContactViewModelFactory

class ContactsFragment : Fragment() {

    private lateinit var mBinding: FragmentContactsBinding

    private val mMyContactsViewModel : MyContactViewModel by viewModels {
        MyContactViewModelFactory((requireActivity().application as MyContactApplication).repository)
    }
    private val myContactsAdapter = MyContactAdapter(this@ContactsFragment)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvContactsList.layoutManager = GridLayoutManager(requireActivity(),1)

        mBinding.rvContactsList.adapter = myContactsAdapter

        val search = mBinding.searchView

        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null){
                    searchDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if(query != null){
                    searchDatabase(query)
                }
                return true
            }
        })

        mMyContactsViewModel.allContactsList.observe(viewLifecycleOwner){
            contacts ->
            contacts.let {
                if(it.isNotEmpty()){
                    mBinding.rvContactsList.visibility = View.VISIBLE
                    mBinding.tvNoContactsAddedYet.visibility = View.GONE
                    myContactsAdapter.contactsList(it)
                }else{
                    mBinding.rvContactsList.visibility = View.GONE
                    mBinding.tvNoContactsAddedYet.visibility = View.VISIBLE
                }
            }
        }

    }
    fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        mMyContactsViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, { list ->
            list.let {
                myContactsAdapter.contactsList(it)
            }
        })
    }


    fun contactDetails(myContact : MyContact){
        findNavController().navigate(ContactsFragmentDirections.actionNavigationContactsToNavigationContactDetails(
            myContact
        ))
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.showBottomNavigationView()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentContactsBinding.inflate(inflater, container, false)

        mBinding.button2.setOnClickListener {
            val intent = Intent (activity, AddUpdateContactActivity::class.java)
            activity?.startActivity(intent)
        }
        return mBinding.root
    }


    fun deleteContact(myContact: MyContact){
        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle(resources.getString(R.string.title_delete_contact))
        builder.setMessage(resources.getString(R.string.msg_delete_contact_dialog, myContact.firstName))
        builder.setIcon(resources.getDrawable(R.drawable.ic_baseline_warning))

        builder.setPositiveButton(resources.getString(R.string.lbl_yes)) { dialogInterface, _ ->
            mMyContactsViewModel.delete(myContact)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.lbl_no)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog? = builder.create()

        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    fun insertRecentContact(recent : RecentContact){
        mMyContactsViewModel.insertRecent(recent)
    }

}