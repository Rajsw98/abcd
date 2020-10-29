package com.example.registrationlogin

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.user_registration.*

class MainActivity : AppCompatActivity() {
    lateinit var handeler:Database
    //permission constants
    private val CAMARA_REQUEST_CODE=100
    private val STORAGE_REQUEST_CODE=101
    //image pic constants
    private val IMAGE_PIC_CAMARA_CODE=102
    private val IMAGE_PIC_GALLARY_CODE=103
    //arrays of permisssion
    private lateinit var camara_permission:Array<String>
    private lateinit var storage_permission:Array<String>
    private var uname:String?=""
    private var uemail:String?=""
    private var upassword:String?=""
   // private var uimage:String?=""
    //actionbar
    //private var actionBar:ActionBar?= null
    var image_uri:Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        camara_permission= arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storage_permission= arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //click image view to pic image
        profile_iv.setOnClickListener{
            imagePicDialoge()
        }
        //click save button
        save.setOnClickListener{
        inputData()
        }



        handeler=Database(this)
        showLogin()
        showHome()
        registration.setOnClickListener{
            showRegistration()
        }
        Login.setOnClickListener{
            showLogin()
        }


        login_button.setOnClickListener{
            if (handeler.userPresent(loginemail.text.toString(), loginpassword.text.toString())) {
                val lname=loginemail.text.toString()
                val lpassword=loginpassword.text.toString()

                val intent = Intent(this@MainActivity, welcomeActivity::class.java)
                    intent.putExtra("M", lname)
                    intent.putExtra("P", lpassword)
                    startActivity(intent)
                    Toast.makeText(this, "login successfully", Toast.LENGTH_LONG).show()

            }
            else {
                Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_LONG).show()
            }

showLogin()

        }


    }

    private fun inputData() {
        uname=""+name.text.toString()
        upassword=""+password.text.toString()
        uemail=""+email.text.toString()


        showHome()


        val id=handeler.insertUserData(
            name = ""+name.getText().toString(),
            image = ""+image_uri,
            email = ""+email.getText().toString(),
            password = ""+password.getText().toString()



        )
        Toast.makeText(this,"Record Added Against TimeStampID $id",Toast.LENGTH_SHORT).show()
    }

    private fun imagePicDialoge() {
      //option to display in dialoge
        val options= arrayOf("Camara","Gallrry")
        //dialog
        var builder=AlertDialog.Builder(this)
        //Tittle
        builder.setTitle("Pic Image from")
        //set items options
        builder.setItems(options){dialog, which ->
            if (which==0){
                //camera clicked
                if (!checkCamaraPermission()){
                    requestCameraPerission()
                }else{
                    PicFromCamera()
                }
            }else{
                if (!checkStoragePermission()){
                    requestStoragePerission()
                }else{
                    PicFromCGallery()
                }
            }
        }
        builder.show()
    }

    private fun PicFromCGallery() {
        val galleryintent=Intent(Intent.ACTION_PICK)
        galleryintent.type="image/*"
        startActivityForResult(galleryintent,IMAGE_PIC_GALLARY_CODE)
    }

    private fun requestStoragePerission() {
//check storage permission is enable or not
        ActivityCompat.requestPermissions(this,storage_permission , STORAGE_REQUEST_CODE)
    }

    private fun checkStoragePermission(): Boolean {
return ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
    }

    private fun PicFromCamera() {
        val values=ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera")
        image_uri=contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        //intent
        val camaraIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri)
        startActivityForResult(camaraIntent,IMAGE_PIC_CAMARA_CODE)
    }

    private fun requestCameraPerission() {
        ActivityCompat.requestPermissions(this,camara_permission , CAMARA_REQUEST_CODE)
    }

    private fun checkCamaraPermission(): Boolean {
val result=ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED
        val result1=ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    companion object{
        private val IMAGE_PICK_BUTTON=1000
        private val PERMISSION_CODE=1001
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMARA_REQUEST_CODE->{
                if (grantResults.isNotEmpty()){
                    val cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED
                    val StorageAccepetd=grantResults[1]==PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && StorageAccepetd){
                        PicFromCamera()
                    }else{
                        Toast.makeText(this,"camaera and storage permission are required",Toast.LENGTH_SHORT).show()
                    }

                }

            }
            STORAGE_REQUEST_CODE->{
                if (grantResults.isNotEmpty()){
                    val StorageAccepetd=grantResults[1]==PackageManager.PERMISSION_GRANTED
                    if ( StorageAccepetd){
                        PicFromCGallery()
                    }else{
                        Toast.makeText(this," storage permission are required",Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode==Activity.RESULT_OK) {
            if (requestCode == IMAGE_PIC_GALLARY_CODE) {
                CropImage.activity(data!!.data)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this)
            } else if (requestCode == IMAGE_PIC_CAMARA_CODE) {
                CropImage.activity(image_uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this)
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
         val result=CropImage.getActivityResult(data)
                if (resultCode==Activity.RESULT_OK){
                   val resulturi=result.uri
                    image_uri=resulturi
                    profile_iv.setImageURI(resulturi)
                }
                else if (resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    val error=result.error
                    Toast.makeText(this,""+error,Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showRegistration(){
        registration_layout.visibility=View.VISIBLE
        login_layout.visibility=View.GONE
        home.visibility=View.GONE
    }
   private fun showLogin(){
        registration_layout.visibility=View.GONE
        login_layout.visibility=View.VISIBLE
        home.visibility=View.GONE
    }
   private fun showHome(){
        registration_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        home.visibility=View.VISIBLE
    }
}