package com.jeevan.exoplayer.listeners

import androidx.media3.common.MediaItem
import com.jeevan.exoplayer.model.MusicItem

interface SongClickListener {
    fun  getClickedMusicItem(musicItem: MusicItem, position: Int)
}