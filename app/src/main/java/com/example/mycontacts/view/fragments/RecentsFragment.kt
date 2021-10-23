package com.example.mycontacts.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycontacts.application.MyContactApplication
import com.example.mycontacts.databinding.FragmentRecentsBinding
import com.example.mycontacts.model.entities.MyContact
import com.example.mycontacts.model.entities.RecentContact
import com.example.mycontacts.view.activities.MainActivity
import com.example.mycontacts.view.adapters.MyContactAdapter
import com.example.mycontacts.viewmodel.MyContactViewModel
import com.example.mycontacts.viewmodel.MyContactViewModelFactory

class RecentsFragment : Fragment() {

    private var mBinding: FragmentRecentsBinding? = null
    private val mMyContactsViewModel : MyContactViewModel by viewModels {
        MyContactViewModelFactory((requireActivity().application as MyContactApplication).repository)
    }

    private val myContactsAdapter = MyContactAdapter(this@RecentsFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentRecentsBinding.inflate(inflater, container, false)

        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding!!.rvRecentContactsList.layoutManager = GridLayoutManager(requireActivity(),1)
        mBinding!!.rvRecentContactsList.adapter = myContactsAdapter

        mMyContactsViewModel.allRecentContactsList.observe(viewLifecycleOwner){
                contacts->
            contacts.let {
                if (it.isNotEmpty()){
                    mBinding!!.rvRecentContactsList.visibility = View.VISIBLE
                    mBinding!!.tvNoRecentContactsAvailable.visibility = View.GONE
                    myContactsAdapter.contactsList(it)

                    val search = mBinding?.searchViewRecents
                    search?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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
                }else{
                    mBinding!!.rvRecentContactsList.visibility = View.GONE
                    mBinding!!.tvNoRecentContactsAvailable.visibility = View.VISIBLE
                    mBinding!!.floatingActionButton.visibility =View.GONE
                }
            }
        }

        mBinding!!.floatingActionButton.setOnClickListener{
            mMyContactsViewModel.deleteRecent()
        }
    }

    fun contactDetails(myContact : MyContact){

        findNavController().navigate(RecentsFragmentDirections.actionNavigationRecentsToNavigationContactDetails(myContact))
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }
    fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        mMyContactsViewModel.searchRecentDatabase(searchQuery).observe(viewLifecycleOwner, { list ->
            list.let {
                myContactsAdapter.contactsList(it)
            }
        })
    }
    fun insertRecentContact(recent : RecentContact){
        Log.i("recents ","insertrecent")
        mMyContactsViewModel.insertRecent(recent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}