package com.silvanoalbuquerque.mynotesapp.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.silvanoalbuquerque.mynotesapp.R
import com.silvanoalbuquerque.mynotesapp.db.entities.Note
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FIFTH_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FIFTH_VALUE
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FIRST_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FIRST_VALUE
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FOURTH_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FOURTH_VALUE
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_SECOND_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_SECOND_VALUE
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_THIRD_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_THIRD_VALUE
import com.silvanoalbuquerque.mynotesapp.other.Constants.IMAGE_FILE_CURSOR_COLUMN_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.NOTE_TEXTUAL_DATETIME_PATTERN
import com.silvanoalbuquerque.mynotesapp.other.Constants.REQUEST_CODE_SELECT_IMAGE
import com.silvanoalbuquerque.mynotesapp.other.Constants.REQUEST_CODE_STORAGE_PERMISSION
import com.silvanoalbuquerque.mynotesapp.other.Constants.SELECTED_COLOR_DEFAULT_IMAGE_RESOURCE
import com.silvanoalbuquerque.mynotesapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.android.synthetic.main.layout_add_url.*
import kotlinx.android.synthetic.main.layout_miscellaneous.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateNoteFragment : Fragment(R.layout.fragment_create_note) {

    private val viewModel: MainViewModel by viewModels()
    private var dialogAddURL: AlertDialog? = null

    private lateinit var availablePickers: List<ImageView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setupViewModelObservables()
    }

    private fun initUi() {
        availablePickers = getAvailablePickers()

        textDateTime.text =
            SimpleDateFormat(NOTE_TEXTUAL_DATETIME_PATTERN, Locale.getDefault()).format(Date())

        buttonSave.setOnClickListener { createNote() }
        imageBack.setOnClickListener { backToNotesListView() }
        handlePickColorClick()

        val bottomSheetBehavior: BottomSheetBehavior<LinearLayout> =
            BottomSheetBehavior.from(layoutMiscellaneous) as AutoCloseBottomSheetBehavior

        layoutMiscellaneous.setOnClickListener { handleBottomSheetClick(bottomSheetBehavior) }

        layoutAddImage.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            if (ContextCompat.checkSelfPermission(
                    requireContext().applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_STORAGE_PERMISSION
                )
            } else {
                selectImage()
            }
        }

        layoutAddUrl.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showAddURLDialog()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        //if (intent.resolveActivity(requireActivity().packageManager) != null) { TODO check how to do that on Android 11
        requireActivity().startActivityFromFragment(this, intent, REQUEST_CODE_SELECT_IMAGE)
        //}
    }

    private fun getPathFromUrl(contentUrl: Uri): String {
        val filePath: String
        val cursor = requireActivity().contentResolver.query(contentUrl, null, null, null, null)

        if (cursor == null) {
            filePath = contentUrl.path.toString()
        } else {
            cursor.moveToFirst()

            val index = cursor.getColumnIndex(IMAGE_FILE_CURSOR_COLUMN_INDEX)
            filePath = cursor.getString(index)
            cursor.close()
        }

        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            data?.let {
                val selectedImageUri = it.data
                if (selectedImageUri != null) {
                    try {

                        val inputStream =
                            requireActivity().contentResolver.openInputStream(selectedImageUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        imageNote.setImageBitmap(bitmap)
                        imageNote.visibility = View.VISIBLE

                        viewModel.setSelectedImagePath(getPathFromUrl(selectedImageUri))

                    } catch (ex: Exception) {
                        Toast.makeText(requireContext(), ex.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage()
            } else {
                Snackbar.make(requireView(), R.string.alert_permisison_denied, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupViewModelObservables() {
        viewModel.finishedSavingNote.observe(viewLifecycleOwner) {
            if (it) {
                backToNotesListView()
            }
        }

        viewModel.selectedColor.observe(viewLifecycleOwner) {
            val gradientDrawable = viewSubtitleIndicator.background as GradientDrawable
            gradientDrawable.setColor(Color.parseColor(it))
        }

        viewModel.noteLink.observe(viewLifecycleOwner) {
            textWebURL.text = it
            layoutWebURL.visibility = View.VISIBLE
        }
    }

    private fun handleBottomSheetClick(bottomSheetBehavior: BottomSheetBehavior<LinearLayout>) {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun getAvailablePickers() = listOf<ImageView>(
        firstImageColor,
        secondImageColor,
        thirdImageColor,
        fourthImageColor,
        fifthImageColor
    )

    private fun handlePickColorClick() {
        firstViewColor.setOnClickListener {
            markSelectedColor(COLOR_PICKER_FIRST_INDEX)
            viewModel.setSelectedColor(COLOR_PICKER_FIRST_VALUE)
        }

        secondViewColor.setOnClickListener {
            markSelectedColor(COLOR_PICKER_SECOND_INDEX)
            viewModel.setSelectedColor(COLOR_PICKER_SECOND_VALUE)
        }

        thirdImageColor.setOnClickListener {
            markSelectedColor(COLOR_PICKER_THIRD_INDEX)
            viewModel.setSelectedColor(COLOR_PICKER_THIRD_VALUE)
        }

        fourthViewColor.setOnClickListener {
            markSelectedColor(COLOR_PICKER_FOURTH_INDEX)
            viewModel.setSelectedColor(COLOR_PICKER_FOURTH_VALUE)
        }

        fifthViewColor.setOnClickListener {
            markSelectedColor(COLOR_PICKER_FIFTH_INDEX)
            viewModel.setSelectedColor(COLOR_PICKER_FIFTH_VALUE)
        }
    }

    private fun markSelectedColor(selectedIndex: Int) {
        for (i in availablePickers.indices) {
            if (selectedIndex == i) {
                availablePickers[i].setImageResource(R.drawable.ic_done)
            } else {
                availablePickers[i].setImageResource(SELECTED_COLOR_DEFAULT_IMAGE_RESOURCE)
            }
        }
    }

    private fun backToNotesListView() {
        findNavController().popBackStack()
    }

    private fun createNote() {
        val title = inputNoteTitle.text.toString()
        if (title.isEmpty()) {
            Snackbar.make(requireView(), R.string.alert_empty_note_title, Snackbar.LENGTH_LONG)
                .show()
            return
        }

        val subtitle = inputNoteSubtitle.text.toString()
        val noteText = inputNote.text.toString()
        val dateTime = Calendar.getInstance()

        val note = Note(
            title = title,
            color = viewModel.selectedColor.value ?: COLOR_PICKER_FIRST_VALUE,
            datetime = dateTime,
            imagePath = viewModel.selectedImagePath.value ?: "",
            subtitle = subtitle,
            noteText = noteText,
            webLink = viewModel.noteLink.value ?: ""
        )

        viewModel.insertNote(note)
    }

    private fun showAddURLDialog() {
        if (dialogAddURL == null) {
            val builder = AlertDialog.Builder(requireActivity())
            val view = LayoutInflater.from(requireContext()).inflate(
                R.layout.layout_add_url,
                layoutAddUrlContainer
            )
            builder.setView(view)

            dialogAddURL = builder.create()
            dialogAddURL?.let {
                if (it.window != null) {
                    it.window!!.setBackgroundDrawable(ColorDrawable(0))
                }

                val inputUrl = view.findViewById<EditText>(R.id.inputUrl)
                inputUrl.requestFocus()

                val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)
                buttonAdd.setOnClickListener {
                    val urlValue = inputUrl.text.toString()

                    if (urlValue.trim().isEmpty()) {
                        Snackbar.make(view, R.string.alert_enter_url, Snackbar.LENGTH_SHORT).show()
                    } else if (!Patterns.WEB_URL.matcher(urlValue).matches()) {
                        Snackbar.make(view, R.string.alert_enter_valid_url, Snackbar.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.setWebLink(urlValue)
                        dialogAddURL!!.dismiss()
                    }
                }

                val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
                buttonCancel.setOnClickListener {
                    dialogAddURL!!.dismiss()
                }
            }
        }

        dialogAddURL!!.show()
    }
}