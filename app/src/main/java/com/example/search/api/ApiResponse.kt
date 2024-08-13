package com.example.search.api

import com.google.gson.annotations.SerializedName

data class APIResponse(
    val documents: ArrayList<Document>
)

data class Document(
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,

    @SerializedName("display_sitename")
    val displaySitename: String,

    val datetime: String,

    var isSelected: Boolean = false
)



