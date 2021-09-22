package com.picpay.desafio.android.framework.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserNetwork(
    @SerializedName("img") val img: String?=null,
    @SerializedName("name") val name: String?=null,
    @SerializedName("id") val id: Int?=null,
    @SerializedName("username") val username: String?=null
) : Parcelable