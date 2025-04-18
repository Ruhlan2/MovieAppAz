package com.isteam.movieappaz.data.dto.remote.profile

import android.net.Uri

data class EditProfileDTO(
    val fullName: String,
    val nickName: String,
    val image: Uri? = null
)
