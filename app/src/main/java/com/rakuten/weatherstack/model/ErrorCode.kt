package com.rakuten.weatherstack.model

import com.google.gson.annotations.SerializedName

data class ErrorCode(@SerializedName("code")
                     val code: Int,
                     @SerializedName("type")
                     val type: String,
                     @SerializedName("info")
                     val info : String){

    companion object {
        val EMPTY = ErrorCode(-1, "", "")
    }
}
