package com.example.search.api

data class APIResponse(
    val documents: MutableList<Document>
)

data class Document(
    val thumbnail_url: String,
    val display_sitename: String,
    val datetime: String
)
