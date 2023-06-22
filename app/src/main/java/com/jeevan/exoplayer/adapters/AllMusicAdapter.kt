package com.jeevan.exoplayer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.RecyclerView
import com.jeevan.exoplayer.databinding.MusicItemBinding
import com.jeevan.exoplayer.listeners.SongClickListener
import com.jeevan.exoplayer.model.MusicItem

class AllMusicAdapter(private var allMusicData: List<MusicItem>,private var songClickListener: SongClickListener ) : RecyclerView.Adapter<AllMusicAdapter.AllMusicViewHolder>() {

    lateinit var binding : MusicItemBinding
    inner class AllMusicViewHolder(view: View) : RecyclerView.ViewHolder(view)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMusicViewHolder {
        binding = MusicItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return AllMusicViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: AllMusicViewHolder, position: Int) {
        binding.musicArtist.text = allMusicData[position].artist
        binding.musicTitle.text = allMusicData[position].title
        binding.root.setOnClickListener {
            songClickListener.getClickedMusicItem(allMusicData[position], position)
        }

    }

    override fun getItemCount(): Int = allMusicData.size
}