package com.evirus.sonisobattani

import com.google.gson.annotations.SerializedName

class ImgResponse {
    @SerializedName("data")
    var Image: String? = null

    @SerializedName("prediction")
    private val Response: String? = null

    fun getResponse(): String? {
        return Response
    }
}