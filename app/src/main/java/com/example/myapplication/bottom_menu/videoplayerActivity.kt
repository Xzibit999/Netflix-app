package com.example.myapplication.bottom_menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.myapplication.R

class VideoPlayerActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videoplayer)

        playerView = findViewById(R.id.playerView)
        initializePlayer()
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        val youtubeUrl = intent.getStringExtra("VIDEO_URL")
        // Note: Real YouTube playback requires YouTube Player API or a specialized library like android-youtube-player
        // For now, if no custom URL is passed, we play the HD sample trailer
        val videoUrl = if (youtubeUrl != null && !youtubeUrl.contains("youtube.com")) {
            youtubeUrl 
        } else {
            "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        }

        val mediaItem = MediaItem.fromUri(videoUrl)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }
}
