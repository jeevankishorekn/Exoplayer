package com.jeevan.exoplayer.model

data class MusicItem(
    val title : String,
    val artist : String,
    val imageUrl : String ?= null,
    val Uri : String
)
