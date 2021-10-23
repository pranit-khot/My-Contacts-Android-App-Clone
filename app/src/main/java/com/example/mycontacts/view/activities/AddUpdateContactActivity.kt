package com.example.mycontacts.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mycontacts.R
import com.example.mycontacts.application.MyContactApplication
import com.example.mycontacts.databinding.ActivityAddUpdateContactBinding
import com.example.mycontacts.databinding.DialogCustomImageSelectionBinding
import com.example.mycontacts.model.entities.MyContact
import com.example.mycontacts.viewmodel.MyContactViewModel
import com.example.mycontacts.viewmodel.MyContactViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateContactActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityAddUpdateContactBinding
    private var mImagePath : String = ""

    private var mMyContactDetails :MyContact? = null

    private val mMyContactViewModel : MyContactViewModel by viewModels{
        MyContactViewModelFactory((application as MyContactApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateContactBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        if(intent.hasExtra("ContactDetails")){
            mMyContactDetails = intent.getParcelableExtra("ContactDetails")
        }

        setupActionBar()

        mMyContactDetails?.let {
            if(it.id !=0){
                mImagePath = it.image
                Glide.with(this@AddUpdateContactActivity)
                    .load(mImagePath)
                    .centerCrop()
                    .into(mBinding.ivContactImage)

                mBinding.etFirstName.setText(it.firstName)
                mBinding.etLastName.setText(it.lastName)
                mBinding.etMiddleName.setText(it.middleName)
                mBinding.etHomeNum.setText(it.homeNumber)
                mBinding.etWorkNum.setText(it.workNumber)
                mBinding.etOtherNum.setText(it.otherNumber)
                mBinding.etEmail.setText(it.email)
                mBinding.etCompany.setText(it.company)

                mBinding.btnAddContact.text = resources.getString(R.string.lbl_update_contact)
            }
        }

        mBinding.ivAddContactImage.setOnClickListener(this@AddUpdateContactActivity)
        mBinding.btnAddContact.setOnClickListener(this@AddUpdateContactActivity)
    }

    private fun setupActionBar(){
        setSupportActionBar(mBinding.toolbarAddContactActivity)

        if(mMyContactDetails != null && mMyContactDetails!!.id !=0){
            supportActionBar?.let {
                it.title =resources.getString(R.string.title_edit_dish)
            }
        }else{
            supportActionBar?.let {
                it.title =resources.getString(R.string.title_add_dish)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddContactActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.iv_add_contact_image ->{
                    customImageSelectionDialog()
                    return
                }
                R.id.btn_add_contact ->{
                    val firstName = mBinding.etFirstName.text.toString().trim{it <= ' '}
                    val middleName = mBinding.etMiddleName.text.toString().trim{it <= ' '}
                    val lastName = mBinding.etLastName.text.toString().trim{it <= ' '}
                    val workNumber = mBinding.etWorkNum.text.toString().trim{it <= ' '}
                    val homeNumber = mBinding.etHomeNum.text.toString().trim{it <= ' '}
                    val otherNumber = mBinding.etOtherNum.text.toString().trim{it <= ' '}
                    val email = mBinding.etEmail.text.toString().trim{it <= ' '}
                    val company = mBinding.etCompany.text.toString().trim{it <= ' '}

                    when{
                        TextUtils.isEmpty(mImagePath) -> {
                            Toast.makeText(
                                this@AddUpdateContactActivity,
                                resources.getString(R.string.err_msg_enter_contact_image),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(firstName) -> {
                            Toast.makeText(
                                this@AddUpdateContactActivity,
                                resources.getString(R.string.err_msg_enter_first_name),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(middleName) -> {
                            Toast.makeText(
                                this@AddUpdateContactActivity,
                                resources.getString(R.string.err_msg_enter_middle_name),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(lastName) -> {
                            Toast.makeText(
                                this@AddUpdateContactActivity,
                                resources.getString(R.string.err_msg_enter_last_name),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(workNumber) -> {
                            Toast.makeText(
                                this@AddUpdateContactActivity,
                                resources.getString(R.string.err_msg_enter_work_number),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(homeNumber) -> {
                            Toast.makeText(
                                this@AddUpdateContactActivity,
                                resources.getString(R.string.err_msg_enter_home_number),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(otherNumber) -> {
                            Toast.makeText(
                                this@AddUpdateContactActivity,
                                resources.getString(R.string.err_msg_enter_other_number),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(email) -> {
                            Toast.makeText(
                                this@AddUpdateContactActivity,
                                resources.getString(R.string.err_msg_enter_email),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        TextUtils.isEmpty(company) -> {
                            Toast.makeText(
                                this@AddUpdateContactActivity,
                                resources.getString(R.string.err_msg_enter_company),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {

                            var contactID = 0
                            var isFavourite = false

                            mMyContactDetails?.let {
                                if(it.id !=0){
                                    contactID = it.id
                                    isFavourite  = it.isFavourite
                                }
                            }

                            val myContactDetails = MyContact(
                                mImagePath,
                                firstName,
                                middleName,
                                lastName,
                                workNumber,
                                homeNumber,
                                otherNumber,
                                email,
                                company,
                                isFavourite,
                                contactID
                            )

                            if(contactID == 0){
                                mMyContactViewModel.insert(myContactDetails)
                                Toast.makeText(
                                    this@AddUpdateContactActivity,"Contact Record Created Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                mMyContactViewModel.update(myContactDetails)
                                Toast.makeText(
                                    this@AddUpdateContactActivity,"Contact Record updated Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun customImageSelectionDialog(){
        val dialog = Dialog(this)
        val binding : DialogCustomImageSelectionBinding = DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {

            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener( object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if(report.areAllPermissionsGranted()){
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, CAMERA)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token : PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }

            }
            ).onSameThread().check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this@AddUpdateContactActivity)
                .withPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(galleryIntent, GALLERY)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(this@AddUpdateContactActivity, "Gallery Permissions Denied",
                            Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }

                }).onSameThread()
                .check()
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA){
                data?.extras?.let {
                    val thumbnail : Bitmap = data.extras!!.get("data") as Bitmap
                    //mBinding.ivContactImage.setImageBitmap(thumbnail)
                    Glide.with(this)
                        .load(thumbnail)
                        .centerCrop()
                        .into(mBinding.ivContactImage)

                    mImagePath = saveImageToInternalStorage(thumbnail)
                    Log.i("ImagePath", mImagePath)

                    mBinding.ivAddContactImage.setImageDrawable(ContextCompat.getDrawable(this@AddUpdateContactActivity, R.drawable.ic_vector_edit))
                }
            }
            else if (requestCode == GALLERY){
                data?.let {

                    val selectedPhotoUri = data.data

                    //mBinding.ivContactImage.setImageURI(selectedPhotoUri)

                    Glide.with(this)
                        .load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable>{
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("TAG", "Error Loading Image", e)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    val bitmap : Bitmap = resource.toBitmap()
                                    mImagePath = saveImageToInternalStorage(bitmap)
                                    Log.i("ImagePath", mImagePath)
                                }
                                return false
                            }

                        })
                        .into(mBinding.ivContactImage)

                    mBinding.ivAddContactImage.setImageDrawable(ContextCompat.getDrawable(this@AddUpdateContactActivity, R.drawable.ic_vector_edit))
                }
            }
        }else if (resultCode == Activity.RESULT_CANCELED){
            Log.e("Cancelled", "Cancelled")
        }
    }

    private fun showRationalDialogForPermissions(){
        AlertDialog.Builder(this).setMessage("It Looks like you have turned off permissions required for this feature. " +
                "It can be enabled under Application Settings")
            .setPositiveButton("GO TO SETTINGS")
            {_,_ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }

            }
            .setNegativeButton("CANCEL"){dialog,_ ->
                dialog.dismiss()
            }.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String{
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream : OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return file.absolutePath
    }

    companion object{
        private const val CAMERA =1
        private const val GALLERY =2
        private const val IMAGE_DIRECTORY = "ContactsImages"
    }
}