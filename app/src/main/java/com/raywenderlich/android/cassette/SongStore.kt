package com.raywenderlich.android.cassette

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit


/**
 * Helper to store and retrieve songs from preferences
 */
class SongStore private constructor(context: Context) {

  private val preferences: SharedPreferences =
      PreferenceManager.getDefaultSharedPreferences(context)

  val allSongs: Set<String>
    get() = getStoredSongs()

  companion object {

    /**
     * store preference key
     */
    const val STORE_KEY: String = "songs"

    /**
     * Factory method to create new store instance
     *
     * @param context Context for new store
     */
    fun from(context: Context): SongStore = SongStore(context)
  }

  /**
   * Get all songs
   *
   */
  private fun getStoredSongs(): MutableSet<String> {
    val storedSet = preferences.getStringSet(STORE_KEY, mutableSetOf<String>()) ?: mutableSetOf()
    val newSet = mutableSetOf<String>()
    if (storedSet.isNotEmpty()) {
      newSet.addAll(storedSet)
    }
    return newSet
  }

  /**
   * Save a song to store
   *
   * @param song song in CSV format (title, artist, year)
   * @param onSuccess function callback to handle success
   * @param onError function callback to handle error
   */
  fun saveSong(song: String, onSuccess: (() -> Unit)?, onError: (Int) -> Unit) {
    val songs = getStoredSongs()

    if (songs.contains(song)) {
      onError(R.string.error_song_exists)
    } else {
      songs.add(song)
      preferences.edit(true) {
        putStringSet(STORE_KEY, songs)
      }
      onSuccess?.invoke()
    }
  }
}
