package com.raywenderlich.android.cassette

import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), AddSongFragment.OnSongAdded {

  lateinit var store: SongStore

  val toggleEmptyView = { show: Boolean ->
    group_empty.visibility = if (show) View.VISIBLE else View.GONE
    button_add_song.visibility = if (show) View.GONE else View.VISIBLE
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    store = SongStore.from(application)
    setClickListeners()
    showAllSongs(store.allSongs.toList())
  }

  private fun setClickListeners() {
    // A simple lambda call
    button_add_song.setOnClickListener {
      showAddSongDialog()
    }

    button_add_song_empty.setOnClickListener {
      showAddSongDialog()
    }
  }

  private fun showAddSongDialog() {
    AddSongFragment.show(supportFragmentManager)
  }

  private fun showAllSongs(songs: List<String>) {

    val spans = mutableListOf<Spanned>()

    for (song in songs) {
      spans.add(prettifySong(song) {
        prettifyList().random().invoke(it)
      })
      spans.add(SpannedString("\n\n"))
    }

    text_view_all_songs.text = TextUtils.concat(*spans.toTypedArray())

    calculateTotalSongs(songs)
    toggleEmptyView(spans.isEmpty())
  }

  private fun calculateTotalSongs(songs: List<String>) {

    val totalSongCalculator: () -> Int = {
      var totalSongs = 0
      for (song in songs) {
        totalSongs += 1
      }
      totalSongs
    }

    text_view_total.text = getString(R.string.text_total, totalSongCalculator())
  }

  override fun onSongAdded() {
    showAllSongs(store.allSongs.toList())
    toggleEmptyView(false)
  }

  private fun prettifySong(song: String, prettifyer: (String) -> SpannableString) =
      prettifyer(song)

  private fun prettifyList(): List<(String) -> SpannableString> {

    val coloredTitle: (String) -> SpannableString = { song ->
      val songTitle = song.split(",")[0]
      SpannableString(song).apply {
        setSpan(ForegroundColorSpan(Color.BLUE), 0, songTitle.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
      }
    }

    val underlinedYear: (String) -> SpannableString = { song ->
      val songYear = song.split(",")[2]
      SpannableString(song).apply {
        setSpan(UnderlineSpan(), song.length - songYear.length + 1, song.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
      }
    }

    val boldArtist: (String) -> SpannableString = { song ->
      val songArtist = song.split(",")[1]
      val songYear = song.split(",")[2]
      SpannableString(song).apply {
        setSpan(BackgroundColorSpan(Color.LTGRAY), song.length -
            (songYear.length + songArtist.length), song.length -
            songYear.length - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
      }
    }

    return listOf(coloredTitle, underlinedYear, boldArtist)
  }
}
