package com.isteam.movieappaz.common.error

import com.google.gson.annotations.SerializedName

class Message {
    @SerializedName("type")
    var type: String? = null

    @SerializedName("reason")
    var reason: String? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("subject")
    val subject: String? = null

    override fun toString(): String {
        return message ?: ""
    }
}