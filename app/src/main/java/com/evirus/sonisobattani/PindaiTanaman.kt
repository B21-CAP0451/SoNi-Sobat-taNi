package com.evirus.sonisobattani

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.evirus.sonisobattani.databinding.ActivityPindaiTanamanBinding
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.lang.reflect.Method

class PindaiTanaman : AppCompatActivity() {
    var imstr = ""
    var namafile = ""
    val url: String = "https://cap0451.heroku.com/predict"
    var fileUri = Uri.parse("")
    private val REQUEST_CODE_GALERRY = 8
    private val REQUEST_CODE_CAMERA = 7
    lateinit var mediaHelper:MediaHelper
    private lateinit var binding: ActivityPindaiTanamanBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPindaiTanamanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnDown.isEnabled = false
        binding.btnUp.isEnabled = false
        try {
            val method: Method = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            method.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaHelper = MediaHelper()
        binding.btnTake.setOnClickListener {
            //requestPermission()
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
            } else {
                Toast.makeText(this, "Unable to use camera", Toast.LENGTH_SHORT).show()
            }
           }
        binding.btnGallery.setOnClickListener {

            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type="image/*"

            if (galleryIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(galleryIntent, REQUEST_CODE_GALERRY)
            } else {
                Toast.makeText(this, "Unable to open gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun uploadFile(){
        val request = object :StringRequest(Method.POST, url,
                Response.Listener { response ->
                    val jsonObject = JSONObject(response)
                    val code = jsonObject.getString("code")
                    if (code.equals("000")) {
                        Toast.makeText(this, "Unggah Foto Sukses", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Unggah Foto Gagal", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Proses Unggah Mengalami Gangguan", Toast.LENGTH_SHORT).show()
                }){
            override fun getParams(): MutableMap<String, String> {
                val hashmap=HashMap<String, String>()
                hashmap.put("imstr", imstr)
                hashmap.put("namafile", namafile)
                return hashmap
            }
        }
        val q=Volley.newRequestQueue(this)
        q.add(request)
    }
    fun requestPermission()=runWithPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    ){
            fileUri=mediaHelper.getOutputMediaFileUri()
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            startActivityForResult(intent, mediaHelper.getRCCamera())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageView:ImageView=findViewById(R.id.imageView)
        if (resultCode == Activity.RESULT_OK)
          //  if (requestCode == mediaHelper.getRCCamera()){
              if (requestCode == REQUEST_CODE_CAMERA){
                binding.btnUp.isEnabled = true
               // imstr = mediaHelper.getBitmapToString(imageView, fileUri)

                val takenImage= data?.extras?.get("data") as Bitmap
                  imstr = mediaHelper.bitmapToString(takenImage)

                binding.imageView.setImageBitmap(takenImage)
                binding.btnDown.isEnabled = false
                binding.btnUp.setOnClickListener {
                    //uploadFile()
                    uploadImage(imstr)
                    binding.progressBar.visibility= View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.btnDown.isEnabled = true
                        binding.btnUp.isEnabled = false
                        binding.textView2.visibility = View.VISIBLE
                    }, 1000)
                }
                binding.btnDown.setOnClickListener {
                    binding.progressBar.visibility= View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        Toast.makeText(this, "Berhasil Mengunduh Solusi", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.btnUp.isEnabled = false
                        binding.textView2.visibility = View.VISIBLE
                        val intent = Intent(this, SolusiActivity::class.java)
                        startActivity(intent)
                    }, 1000)
                }
            }else if (requestCode ==REQUEST_CODE_GALERRY){
                binding.btnUp.isEnabled = true
                  val takenImage= data?.data
                binding.imageView.setImageURI(takenImage)
                  val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                      ImageDecoder.decodeBitmap(ImageDecoder.createSource(baseContext.contentResolver, takenImage!!))
                  } else {
                      MediaStore.Images.Media.getBitmap(baseContext.contentResolver, takenImage)
                  }
                  imstr = mediaHelper.bitmapToString(bitmap)

                binding.btnDown.isEnabled = false
                binding.btnUp.setOnClickListener {
                    //uploadFile()
                   uploadImage(imstr)
                    binding.progressBar.visibility= View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.btnDown.isEnabled = true
                        binding.btnUp.isEnabled = false
                        binding.textView2.visibility = View.VISIBLE
                    }, 1000)
                }
                binding.btnDown.setOnClickListener {
                    binding.progressBar.visibility= View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        Toast.makeText(this, "Berhasil Mengunduh Solusi", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.btnUp.isEnabled = false
                        binding.textView2.visibility = View.VISIBLE
                        val intent = Intent(this, SolusiActivity::class.java)
                        startActivity(intent)
                    }, 1000)
                }

            }else {
                super.onActivityResult(requestCode, resultCode, data)
            }
    }
    private fun uploadImage(imgString: String):ArrayList<String> {
        val image: String = imgString
        val apiInterface: ApiService = ApiClient.provideApiService()
        val resultArray=ArrayList<String>()
        val call: Call<ImgResponse?>? = apiInterface.uploadImage(image)
        if (call != null) {
            call.enqueue(object : Callback<ImgResponse?> {
                override fun onResponse(
                        call: Call<ImgResponse?>,
                        response: retrofit2.Response<ImgResponse?>
                ) {
                    val img_pojo: ImgResponse? = response.body()
                    if (img_pojo != null) {
                        Log.d("Server Response", "success " + img_pojo.getResponse())
                    }
                }
                override fun onFailure(call: Call<ImgResponse?>, t: Throwable) {
                    Log.d("Server Response", "error " + t.toString())
                }
            })
        }else{
            Toast.makeText(baseContext, "call null", Toast.LENGTH_SHORT).show()
        }
        return resultArray
    }
}