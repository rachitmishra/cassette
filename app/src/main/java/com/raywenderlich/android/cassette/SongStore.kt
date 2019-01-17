package com.raywenderlich.android.cassette

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


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
    return preferences.getStringSet(STORE_KEY, mutableSetOf<String>()) ?: mutableSetOf()
  }
}
