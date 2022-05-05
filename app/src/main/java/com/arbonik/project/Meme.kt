package com.arbonik.project

import com.google.gson.annotations.SerializedName

data class Data (
    @SerializedName("data")
    val memes: List<Meme>
)
data class Meme (
        @SerializedName("id")
    val id: Int? = null,
        @SerializedName("title")
    val title: String? = null,
        @SerializedName("description")
    val description: String? = null,
        @SerializedName("url")
    val url: String? = null,
)