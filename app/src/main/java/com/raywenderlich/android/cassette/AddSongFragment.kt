package com.raywenderlich.android.cassette

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_add_song.*

class AddSongFragment : BottomSheetDialogFragment() {

  companion object {

    private const val FRAGMENT_TAG = "add_song_fragment"

    fun show(fragmentManager: FragmentManager) {
      val fragment = AddSongFragment()
      fragment.show(fragmentManager, FRAGMENT_TAG)
    }
  }

  interface OnSongAdded {
    fun onSongAdded()
  }

  private lateinit var songStore: SongStore

  private lateinit var onSongAdded: OnSongAdded

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    songStore = SongStore.from(context!!.applicationContext)
    return inflater.inflate(R.layout.dialog_add_song, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    handleSongSaveClick()
  }


  private fun handleSongSaveClick() {
    button_save.setOnClickListener {
      saveSong()
      dismiss()
    }
  }

  private fun saveSong() {
    val title = edit_text_title.text?.toString()
    val artist = edit_text_artist.text?.toString()
    val year = edit_text_year.text?.toString()

    if (title.isNullOrEmpty()) {
      showError(R.string.error_title_empty)
      return
    }

    if (!isValidArtist(artist)) {
      return
    }

    if (!isValidYear(year)) {
      showError(R.string.error_year_invalid)
      return
    }

    songStore.saveSong("$title, $artist, $year", onSongAdded::onSongAdded, this::showError)
  }

  private fun showError(message: Int) {
    Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
  }

  private fun isValidArtist(artist: String?): Boolean {

    // Anonymous function
    return isValid(artist, fun(value: String?): Boolean {
      if (value.isNullOrEmpty()) {
        showError(R.string.error_artist_empty)
        return false
      }

      if (value.length < 2) {
        showError(R.string.error_artist_invalid)
        return false
      }

      return true
    })
  }

  private fun isValidYear(year: String?): Boolean {
    val yearValidator: (String?) -> Boolean = { it != null && it.toInt() in 1877..2019 }

    return isValid(year, yearValidator)
  }

  private fun isValid(value: String?, validator: (String) -> Boolean): Boolean =
      value != null && validator(value)

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    onSongAdded = context as AddSongFragment.OnSongAdded
  }
}
