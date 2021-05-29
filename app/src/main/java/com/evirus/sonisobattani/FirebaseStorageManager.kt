package com.evirus.sonisobattani

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage

class FirebaseStorageManager {
    private val TAG = "FirebasetorageManager"
    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mProgressDialog: ProgressDialog
    fun uploadImage(mContex: Context, imageURI: Uri) {
        mProgressDialog = ProgressDialog(mContex)
        mProgressDialog.setMessage("Please wait, image being uploading...")
        val uploadTask = mStorageRef.child("image/images.JPEG").putFile(imageURI)
        uploadTask.addOnSuccessListener {
            Log.e(TAG, "Image upload successfully")
        }.addOnCanceledListener {
            Log.e(TAG, "Image upload failed")
        }
    }
}
}