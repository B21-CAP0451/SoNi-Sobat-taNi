package com.evirus.sonisobattani

import com.google.gson.annotations.SerializedName




class ImagePojo {
  //  @SerializedName("image_name")
 //   private val Title: String? = null

    @SerializedName("image")
    private val Image: String? = null

    @SerializedName("response")
    private val Response: String? = null

    fun getResponse(): String? {
        return Response
    }
}