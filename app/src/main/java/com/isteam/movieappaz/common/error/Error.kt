package com.isteam.movieappaz.common.error

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("error_description") val description: String? = null,
    @SerializedName("custom_error") val customError: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("succeeded") var succeeded: Boolean? = null,
    @SerializedName("error") var error: String? = null,
    @SerializedName("errorCode") val errorCode: String? = null,
    @SerializedName("statusCode") var code: Int? = 0,
    @SerializedName("errors") var errors: ArrayList<Message>? = null,
    @SerializedName("data") val data: Any? = null,
    @SerializedName("messages") val messages: ArrayList<String>? = null,
    @SerializedName("message") override val message: String?
) : Throwable() {

    override fun getLocalizedMessage(): String {
        return messages?.get(0) ?: "System Failure"
    }
}
