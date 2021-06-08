package com.evirus.sonisobattani


import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiService {
    @FormUrlEncoded
    @POST("/predict")
    fun uploadImage(
       // @Field("image_name") title: String?,
        @Field("image") image: String?
    ): Call<ImagePojo?>?
}