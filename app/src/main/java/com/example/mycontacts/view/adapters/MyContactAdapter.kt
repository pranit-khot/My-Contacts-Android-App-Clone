package com.example.mycontacts.view.adapters

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mycontacts.R
import com.example.mycontacts.databinding.ItemContactLayoutBinding
import com.example.mycontacts.model.entities.MyContact
import com.example.mycontacts.model.entities.RecentContact
import com.example.mycontacts.view.activities.AddUpdateContactActivity
import com.example.mycontacts.view.fragments.ContactsFragment
import com.example.mycontacts.view.fragments.FavoritesFragment
import com.example.mycontacts.view.fragments.RecentsFragment

class MyContactAdapter (private val fragment : Fragment): RecyclerView.Adapter<MyContactAdapter.ViewHolder>()  {

    private var contacts : List<MyContact> = listOf()

    class ViewHolder(view: ItemContactLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        // Holds the TextView that will add each item to
        val ivContactImage = view.ivContactImage
        val tvContactName = view.tvContactName
        val tvContactNumber = view.tvContactNumber
        val tvContactLastname = view.tvContactLastName
        val ibMore = view.ibMore
        val ibMakeCall = view.ibMakeCall
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding : ItemContactLayoutBinding = ItemContactLayoutBinding.inflate(
           LayoutInflater.from(fragment.context), parent, false )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val contact = contacts[position]
        Glide.with(fragment)
            .load(contact.image)
            .transform(RoundedCorners(20))
            .into(holder.ivContactImage)
        holder.tvContactName.text = contact.firstName
        holder.tvContactNumber.text = contact.homeNumber
        holder.tvContactLastname.text =contact.lastName

        holder.itemView.setOnClickListener{
            if(fragment is ContactsFragment){
                fragment.contactDetails(contact)
            }
            if(fragment is FavoritesFragment){
                fragment.contactDetails(contact)
            }
            if(fragment is RecentsFragment){
                fragment.contactDetails(contact)
            }

        }

        holder.ibMore.setOnClickListener {
            val popupMenu = PopupMenu(fragment.context, holder.ibMore)
            popupMenu.menuInflater.inflate(R.menu.menu_adapter, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                if(it.itemId == R.id.action_edit_contact){
                    val intent = Intent(fragment.requireContext(), AddUpdateContactActivity::class.java)
                    intent.putExtra("ContactDetails", contact)
                    fragment.requireActivity().startActivity(intent)
                }else if(it.itemId == R.id.action_delete_contact){
                    if (fragment is ContactsFragment){
                        fragment.deleteContact(contact)
                    }
                }
                true
            }
            popupMenu.show()
        }

        holder.ibMakeCall.setOnClickListener {
            Log.i("selected contact","${contact.firstName}")
            val recentContact = RecentContact(contact.id)
            val intent = Intent(Intent.ACTION_CALL)
            if (fragment is ContactsFragment){
                fragment.insertRecentContact(recentContact)
                intent.data= Uri.parse("tel:${contact.homeNumber}")
                fragment.requireActivity().startActivity(intent)
            }
            if (fragment is RecentsFragment){
                fragment.insertRecentContact(recentContact)
                intent.data= Uri.parse("tel:${contact.homeNumber}")
                fragment.requireActivity().startActivity(intent)
            }
            if (fragment is FavoritesFragment){
                fragment.insertRecentContact(recentContact)
                intent.data= Uri.parse("tel:${contact.homeNumber}")
                fragment.requireActivity().startActivity(intent)
            }
        }

        if(fragment is ContactsFragment){
            holder.ibMore.visibility = View.VISIBLE
        }else if (fragment is RecentsFragment){
            holder.ibMore.visibility = View.GONE
        }else{
            holder.ibMore.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
       return contacts.size
    }

    fun contactsList(list : List<MyContact>){
        contacts = list
        notifyDataSetChanged()
    }

}