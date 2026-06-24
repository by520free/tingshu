package com.example.tingshu.domain.model

data class Category(
    val id: String,
    val name: String,
    val coverUrl: String = "",
    val sourceId: String = ""
)
