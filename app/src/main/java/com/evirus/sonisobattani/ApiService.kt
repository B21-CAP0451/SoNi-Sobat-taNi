package com.evirus.sonisobattani


import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @Headers("content-type: image/jpeg")
    @POST("/predict")
    fun uploadImage(
       // @Field("image_name") title: String?,
        @Field("data") image: String
    ): Call<ImgResponse?>?
}