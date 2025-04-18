package com.isteam.movieappaz.data.dto.remote.celebrities.detail


import com.google.gson.annotations.SerializedName

data class CelebrityDetailsDTO(
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("known_for_department")
    val knownForDepartment: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("profile_path")
    val profilePath: String?,
    @SerializedName("biography")
    val biography: String?

)