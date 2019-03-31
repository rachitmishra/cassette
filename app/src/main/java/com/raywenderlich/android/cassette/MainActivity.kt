package com.raywenderlich.android.cassette

import android.os.Bundle
import android.text.*
import android.text.style.UnderlineSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), AddSongFragment.OnSongAdded {

  private lateinit var store: SongStore

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    store = SongStore.from(application)
    setClickListeners()
    showAllSongs(store.allSongs.toList())
  }

  private fun showAddSongDialog() {
    AddSongFragment.show(supportFragmentManager)
  }

  private fun setClickListeners() {

    button_add_song_empty.setOnClickListener {
      showAddSongDialog()
    }

    button_add_song.setOnClickListener {
      showAddSongDialog()
    }
  }

  val toggleEmptyView = { show: Boolean ->
    group_empty.visibility = if (show) View.VISIBLE else View.GONE
    button_add_song.visibility = if (show) View.GONE else View.VISIBLE
  }

  private fun showAllSongs(songs: List<String>) {

    val spans = mutableListOf<Spanned>()

     val underlineTitle: (String) -> SpannableString = {
      val songTitle = it.split(",")[0]
      SpannableString(it).apply {
        setSpan(UnderlineSpan(), 0, songTitle.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
      }
    }

    for (song in songs) {
      spans.add(underlineTitle(song))
      spans.add(SpannedString("\n\n"))
    }

    text_view_all_songs.text = TextUtils.concat(*spans.toTypedArray())

    findPlaylistYearRange(songs)
    calculateTotalSongs(songs)
    toggleEmptyView(spans.isEmpty())
  }

  private fun findPlaylistYearRange(songs: List<String>) {
    if (songs.isEmpty()) {
      return
    }

    var startYear = songs[0].split(",")[2].trim().toInt()
    var endYear = startYear

    val findStartEndYear = {
      songs.forEach { song ->
        val songYear = song.split(",")[2].trim().toInt()
        if (songYear > endYear) {
          endYear = songYear
        } else if (songYear < startYear) {
          startYear = songYear
        }
      }
    }

    findStartEndYear()

    val endYearString = if (endYear == startYear) "" else endYear.toString()

    text_view_year_range.text = getString(R.string.text_range, startYear.toString(), endYearString)
  }

  private fun calculateTotalSongs(songs: List<String>) {
    text_view_total.text = getString(R.string.text_total, songs.size)
  }

  override fun onSongAdded() {
    showAllSongs(store.allSongs.toList())
    toggleEmptyView(false)
  }
}
