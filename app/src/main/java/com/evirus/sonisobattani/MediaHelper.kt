package com.evirus.sonisobattani

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MediaHelper {
    private var namaFile = ""
    private var fileUri: Uri = Uri.parse("")
    private val RC_CAMERA = 100
    fun getMyFileName(): String{
        return this.namaFile
    }
    fun getRCCamera():Int{
        return this.RC_CAMERA
    }
    fun getOutputMediaFile(): File {
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DCIM), "appx06")
        if (!mediaStorageDir.exists())
            if (!mediaStorageDir.mkdirs()){
                Log.e("mkdir", "Gagal Membuat Folder")
            }
        return File(mediaStorageDir.path+File.separator+ "${this.namaFile}")
    }
    fun getOutputMediaFileUri():Uri{
        val timeStamp:String = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        this.namaFile = "DT_${timeStamp}.jpg"
        this.fileUri = Uri.fromFile(getOutputMediaFile())
        return this.fileUri
    }
    private fun bitmapToString(bitmap:Bitmap):String{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray:ByteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
    fun getBitmapToString(imageView: ImageView, uri: Uri):String{
        var bitmap: Bitmap
        bitmap=BitmapFactory.decodeFile(this.fileUri.path)
        val dimming = 720
        if (bitmap.height > bitmap.width){
            bitmap=Bitmap.createScaledBitmap(
                bitmap, (bitmap.width*dimming).div(bitmap.height),dimming, true)
        }else{
            bitmap = Bitmap.createScaledBitmap(
                bitmap, (bitmap.height*dimming).div(bitmap.width),dimming, true)
        }
        imageView.setImageBitmap(bitmap)
        return bitmapToString(bitmap)
        return ""
    }
}