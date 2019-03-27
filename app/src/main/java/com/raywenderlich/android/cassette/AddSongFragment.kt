package com.raywenderlich.android.cassette

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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

  private lateinit var onSongAdded: OnSongAdded

  private lateinit var songStore: SongStore

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    songStore = SongStore.from(context!!.applicationContext)
    return inflater.inflate(R.layout.dialog_add_song, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }

  private fun showError(message: Int) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    onSongAdded = context as AddSongFragment.OnSongAdded
  }
}
