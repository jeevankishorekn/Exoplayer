package com.jeevan.exoplayer.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeevan.exoplayer.R
import com.jeevan.exoplayer.adapters.AllMusicAdapter
import com.jeevan.exoplayer.databinding.FragmentPlayerBinding
import com.jeevan.exoplayer.listeners.SongClickListener
import com.jeevan.exoplayer.model.MusicItem

class PlayerFragment : Fragment(R.layout.fragment_player), SongClickListener {

    private val TAG = "PlayerFragment"
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var allMusicAdapter: AllMusicAdapter
    private lateinit var player: ExoPlayer
    private var currentMusicPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val musicList = getAllMusic()
        allMusicAdapter = AllMusicAdapter(musicList, this)
        binding.allMusicRecyclerView.adapter = allMusicAdapter
        binding.allMusicRecyclerView.layoutManager = LinearLayoutManager(activity)

        player = activity?.let { ExoPlayer.Builder(it.applicationContext).build() }!!

        binding.musicPlayer.playPause.setOnClickListener {
            if (player.isPlaying) {
                binding.musicPlayer.playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                player.pause()
            } else {
                binding.musicPlayer.playPause.setImageResource(R.drawable.ic_baseline_pause_24)
                player.play()
            }
        }

        binding.musicPlayer.playSkipForward.setOnClickListener {
            val nextMusicPosition = currentMusicPosition + 1
            if (nextMusicPosition <= musicList.size - 1) {
                currentMusicPosition = nextMusicPosition
                playMusicItem(MediaItem.fromUri(musicList[nextMusicPosition].Uri))
                updatePlayerUi(
                    musicList[nextMusicPosition].title,
                    musicList[nextMusicPosition].artist
                )
            } else {
                playMusicItem(MediaItem.fromUri(musicList[0].Uri))
                updatePlayerUi(musicList[0].title, musicList[0].artist)
                currentMusicPosition = 0
            }
        }
        binding.musicPlayer.playSkipPrevious.setOnClickListener {
            val previousMusicPosition = currentMusicPosition - 1
            if (previousMusicPosition < 0) {
                currentMusicPosition = musicList.size - 1
                playMusicItem(MediaItem.fromUri(musicList[musicList.size - 1].Uri))
                updatePlayerUi(
                    musicList[musicList.size - 1].title,
                    musicList[musicList.size - 1].artist
                )
            } else {
                currentMusicPosition = previousMusicPosition
                playMusicItem(MediaItem.fromUri(musicList[previousMusicPosition].Uri))
                updatePlayerUi(
                    musicList[previousMusicPosition].title,
                    musicList[previousMusicPosition].artist
                )
            }
        }

        binding.musicPlayer.bottomPlayerView.setOnClickListener{
            //requireActivity().findNavController(R.id.bottomNavigationView).navigate(R.id.audioPlayer)
            findNavController().navigate(R.id.audioPlayerFragment)
        }

    }

    @SuppressLint("Range")
    fun getAllMusic(): List<MusicItem> {
        var list = mutableListOf<MusicItem>()
        var cursor = activity?.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Audio.Media.DISPLAY_NAME + " ASC"
        )
        while (cursor?.moveToNext()!!) {
            Log.d(
                TAG,
                "getAllMusic: ${cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))}"
            )
            Log.d(
                TAG,
                "getAllMusic: ${
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.buildUpon()
                        .appendPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)))
                        .build().toString()
                }"
            )
            Log.d(
                TAG,
                "getAllMusic: ${cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))}"
            )
            list.add(
                MusicItem(
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                    null,
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.buildUpon()
                        .appendPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)))
                        .build().toString()
                )
            )
        }

        return list
    }

    override fun getClickedMusicItem(musicItem: MusicItem, position: Int) {
        binding.musicPlayer.root.visibility = VISIBLE
        binding.musicPlayer.playTitle.isSelected = true
        binding.musicPlayer.playPause.setImageResource(R.drawable.ic_baseline_pause_24)
        binding.musicPlayer.playArtist.text = musicItem.artist
        binding.musicPlayer.playTitle.text = musicItem.title
        currentMusicPosition = position
        val mediaItem = MediaItem.fromUri(musicItem.Uri)
        playMusicItem(mediaItem)
    }

    private fun playMusicItem(mediaItem: MediaItem) {
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    private fun updatePlayerUi(playTitle: String, playArtist: String) {
        binding.musicPlayer.playTitle.text = playTitle
        binding.musicPlayer.playArtist.text = playArtist
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
    }
}