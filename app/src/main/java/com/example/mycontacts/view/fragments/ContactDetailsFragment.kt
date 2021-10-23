package com.example.mycontacts.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mycontacts.R
import com.example.mycontacts.application.MyContactApplication
import com.example.mycontacts.databinding.FragmentContactDetailsBinding
import com.example.mycontacts.viewmodel.MyContactViewModel
import com.example.mycontacts.viewmodel.MyContactViewModelFactory
import java.io.IOException

class ContactDetailsFragment : Fragment() {

    private var mBinding : FragmentContactDetailsBinding? = null

    private val mMyContactViewModel : MyContactViewModel by viewModels {
        MyContactViewModelFactory(((requireActivity().application)as MyContactApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentContactDetailsBinding.inflate(inflater, container, false)

        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ContactDetailsFragmentArgs by navArgs()
        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.contactDetails.image)
                    .centerCrop()
                    .into(mBinding!!.ivContactImage)
            }catch (e:IOException){
                e.printStackTrace()
            }

            mBinding!!.tvFirstName.text = it.contactDetails.firstName
            mBinding!!.tvMiddleName.text = it.contactDetails.middleName
            mBinding!!.tvLastName.text = it.contactDetails.lastName
            mBinding!!.tvHomeNumber.text = it.contactDetails.homeNumber
            mBinding!!.tvOfficeNumber.text = it.contactDetails.workNumber
            mBinding!!.tvOtherNumber.text = it.contactDetails.otherNumber
            mBinding!!.tvEmail.text = it.contactDetails.email
            mBinding!!.tvCompany.text = it.contactDetails.company

            if (args.contactDetails.isFavourite){
                mBinding!!.ivFavoriteContact.setImageDrawable(ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_favourite_selected1))
            }else{
                mBinding!!.ivFavoriteContact.setImageDrawable(ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_favourite_unselected1))
            }
        }

        mBinding!!.ivFavoriteContact.setOnClickListener{
            args.contactDetails.isFavourite = !args.contactDetails.isFavourite
            mMyContactViewModel.update(args.contactDetails)

            if (args.contactDetails.isFavourite){
                mBinding!!.ivFavoriteContact.setImageDrawable(ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_favourite_selected1
                ))
                Toast.makeText(
                    requireActivity(),
                    "Contact added to Favourite",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                mBinding!!.ivFavoriteContact.setImageDrawable(ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_favourite_unselected1))
                Toast.makeText(
                    requireActivity(),
                    "Contact removed from Favourite",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}