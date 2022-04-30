package com.odinsporrt.data

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("isdef" ) var isdef : String? = null,
    @SerializedName("Link" ) var linka : String? = null
)
