package com.evirus.sonisobattani

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.evirus.sonisobattani.databinding.ActivityPindaiTanamanBinding
import com.google.android.gms.common.util.IOUtils.toByteArray
import java.io.ByteArrayOutputStream

class PindaiTanaman : AppCompatActivity() {
        private val REQUEST_CODE = 7
        private lateinit var binding:ActivityPindaiTanamanBinding
        override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            binding=ActivityPindaiTanamanBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.btnDown.isEnabled = false
            binding.btnUp.isEnabled = false
            binding.btnTake.setOnClickListener {

                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_CODE)
                } else {
                    Toast.makeText(this, "Tidak Dapat Menggunakan Camera", Toast.LENGTH_SHORT).show()
                }
                binding.btnUp.isEnabled = true
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            binding= ActivityPindaiTanamanBinding.inflate(layoutInflater)
            setContentView(binding.root)
            if (requestCode== REQUEST_CODE && resultCode== Activity.RESULT_OK){
                val takenImage= data?.extras?.get("data") as Bitmap
                binding.imageView.setImageBitmap(takenImage)
                binding.btnDown.isEnabled = false
                UploadUtility(this).uploadFile(getImageUriFromBitmap(this,takenImage), "images" ) //Ini
                binding.btnUp.setOnClickListener {
                    binding.progressBar.visibility= View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        Toast.makeText(this, "Berhasil Mengunggah", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility= View.INVISIBLE
                        binding.btnDown.isEnabled = true
                        binding.btnUp.isEnabled = false
                        binding.textView2.visibility=View.VISIBLE
                    }, 1000)
                }
                binding.btnDown.setOnClickListener {
                    binding.progressBar.visibility= View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        Toast.makeText(this, "Berhasil Mengunduh Solusi", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility= View.INVISIBLE
                        binding.btnUp.isEnabled = false
                        binding.textView2.visibility=View.VISIBLE
                        val intent= Intent(this, SolusiActivity::class.java)
                        startActivity(intent)
                    }, 1000)
                }
            }else{ super.onActivityResult(requestCode, resultCode, data)}

        }
        fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            val quality = 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bytes)
            val imageBytes: ByteArray = ByteArrayOutputStream.toByteArray() //deprecated ?
            val imageString: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            textView.text = imageString //ini
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Judul", null)
            return Uri.parse(path.toString())
        }
    }