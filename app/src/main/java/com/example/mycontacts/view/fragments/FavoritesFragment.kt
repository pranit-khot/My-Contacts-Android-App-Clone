package com.example.mycontacts.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycontacts.application.MyContactApplication
import com.example.mycontacts.databinding.FragmentFavoritesBinding
import com.example.mycontacts.model.entities.MyContact
import com.example.mycontacts.model.entities.RecentContact
import com.example.mycontacts.view.activities.MainActivity
import com.example.mycontacts.view.adapters.MyContactAdapter
import com.example.mycontacts.viewmodel.FavoritesViewModel
import com.example.mycontacts.viewmodel.MyContactViewModel
import com.example.mycontacts.viewmodel.MyContactViewModelFactory

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel

    private var mBinding: FragmentFavoritesBinding? = null

    private val mFavoritesViewModel: MyContactViewModel by viewModels {
        MyContactViewModelFactory((requireActivity().application as MyContactApplication).repository)
    }
    private val myContactsAdapter = MyContactAdapter(this@FavoritesFragment)

    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoritesViewModel =
            ViewModelProvider(this).get(FavoritesViewModel::class.java)

        mBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding!!.rvFavoriteContactsList.layoutManager =GridLayoutManager(requireActivity(),1)
        mBinding!!.rvFavoriteContactsList.adapter = myContactsAdapter

        mFavoritesViewModel.favouriteContacts.observe(viewLifecycleOwner){
            contacts->
            contacts.let {
                if (it.isNotEmpty()){
                    mBinding!!.rvFavoriteContactsList.visibility = View.VISIBLE
                    mBinding!!.tvNoFavoriteContactsAvailable.visibility = View.GONE
                    myContactsAdapter.contactsList(it)
                    val search = mBinding?.searchViewFavourites
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
                    mBinding!!.rvFavoriteContactsList.visibility = View.GONE
                    mBinding!!.tvNoFavoriteContactsAvailable.visibility = View.VISIBLE
                }
            }
        }
    }
    fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        mFavoritesViewModel.searchFavDatabase(searchQuery).observe(viewLifecycleOwner, { list ->
            list.let {
                myContactsAdapter.contactsList(it)
            }
        })
    }
    fun contactDetails(myContact :MyContact){

        findNavController().navigate(FavoritesFragmentDirections.actionNavigationFavouritesToNavigationContactDetails(myContact))
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

    fun insertRecentContact(recent : RecentContact){
        mFavoritesViewModel.insertRecent(recent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}