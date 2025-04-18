package com.isteam.movieappaz.data.dto.remote.credits


import com.google.gson.annotations.SerializedName

data class CreditsDTO(
    @SerializedName("cast")
    val cast: List<Cast?>?,
    @SerializedName("crew")
    val crew: List<Crew?>?,
    @SerializedName("id")
    val id: Int?
)