package com.evirus.sonisobattani

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import org.json.JSONObject
import java.lang.reflect.Method

class PindaiTanaman : AppCompatActivity(),View.OnClickListener {
    var imstr = ""
    var namafile = ""
    val url: String = "https://cap0451.heroku.com/predict"
    var fileUri = Uri.parse("")
    lateinit var mediaHelper:MediaHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pindai_tanaman)
        try {
            val method: Method = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
            method.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaHelper = MediaHelper()
        val btnTake: Button = findViewById(R.id.btn_take)
        val btnUp: Button = findViewById(R.id.btn_up)
        val btnDown:Button = findViewById(R.id.btn_down)
        btnTake.setOnClickListener (this)
        btnUp.setOnClickListener (this)
        btnDown.setOnClickListener(this)

    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_take->{
                requestPermission()
            }
            R.id.btn_up->{
                uploadFile()
            }
            R.id.btn_down->{
                val Intent1 = Intent(this@PindaiTanaman, SolusiActivity::class.java)
                startActivity(Intent1)
            }
        }
    }
    fun uploadFile(){
        val request = object :StringRequest(Method.POST, url,
            Response.Listener{response ->
                val jsonObject = JSONObject(response)
                val code = jsonObject.getString("code")
                if (code.equals("000")){
                    Toast.makeText(this, "Unggah Foto Sukses", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"Unggah Foto Gagal", Toast.LENGTH_SHORT).show()
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
        Manifest.permission.CAMERA){
            fileUri=mediaHelper.getOutputMediaFileUri()
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri)
            startActivityForResult(intent, mediaHelper.getRCCamera())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageView:ImageView=findViewById(R.id.imageView)
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == mediaHelper.getRCCamera()){
                imstr = mediaHelper.getBitmapToString(imageView , fileUri)
                namafile = mediaHelper.getMyFileName()
            }
    }
}