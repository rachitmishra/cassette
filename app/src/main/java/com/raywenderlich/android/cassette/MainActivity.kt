package com.raywenderlich.android.cassette

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), AddSongFragment.OnSongAdded {

  private lateinit var songStore: SongStore

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    songStore = SongStore.from(applicationContext)
  }

  override fun onSongAdded() {
  }
}
