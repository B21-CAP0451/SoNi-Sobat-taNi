package com.evirus.sonisobattani

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.ByteArrayOutputStream

class PindaiTanaman : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        private val REQUEST_CODE = 7
        private lateinit var binding:ActivityPindaiTanamanBinding
        override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            binding=ActivityPindaiTanamanBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.btnTake.setOnClickListener {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_CODE)
                } else {
                    Toast.makeText(this, "Unable to use camera", Toast.LENGTH_SHORT).show()
                }

            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            binding=ActivityPindaiTanamanBinding.inflate(layoutInflater)
            setContentView(binding.root)
            if (requestCode== REQUEST_CODE && resultCode== Activity.RESULT_OK){
                val takenImage= data?.extras?.get("data") as Bitmap
                binding.imageView.setImageBitmap(takenImage)
                FirebaseStorageManager().uploadImage(this, getImageUriFromBitmap(this,takenImage))
            }else{ super.onActivityResult(requestCode, resultCode, data)}

        }
        fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
            return Uri.parse(path.toString())
        }
    }