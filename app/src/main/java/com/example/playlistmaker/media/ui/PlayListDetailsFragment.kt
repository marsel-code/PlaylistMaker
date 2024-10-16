package com.example.playlistmaker.media.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayListDetailsBinding
import com.example.playlistmaker.media.domain.model.PlayList
import com.example.playlistmaker.media.presentation.view_model.PlayListDetailsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class PlayListDetailsFragment() : Fragment() {

    private var _binding: FragmentPlayListDetailsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var nameTextWatcher: TextWatcher
    private var nameTextValue: String = ""
    private lateinit var descriptionTextWatcher: TextWatcher
    private var descriptionTextValue: String = ""
    private var imagePlayList: Boolean = false
    private val viewModel by viewModel<PlayListDetailsViewModel>()
    private var uriImage: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayListDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(this)
                        .load(uri)
                        .transform(
                            MultiTransformation(
                                CenterCrop(),
                                RoundedCorners(
                                    TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        8F,
                                        binding.imagePlaylistDetails.resources.displayMetrics
                                    ).toInt()
                                )
                            )
                        )
                        .into(binding.imagePlaylistDetails)

                    saveImageToPrivateStorage(uri)
                    imagePlayList = true

                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.imagePlaylistDetails.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.newPlayListButton.setOnClickListener {

            viewModel.addPlayList(
                PlayList(
                    playListId = 0,
                    nameTextValue,
                    descriptionTextValue,
                    uriImage,
                    0,
                    Gson().toJson(ArrayList<Long>())
                )
            )

            Toast.makeText(
                activity,
                "Плейлист $nameTextValue создан",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigateUp()
        }

        binding.backButtonPlayer.setNavigationOnClickListener {
            if (nameTextValue != "" || descriptionTextValue != "" || imagePlayList) dialog() else findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this.viewLifecycleOwner) {
            if (nameTextValue != "" || descriptionTextValue != "" || imagePlayList) dialog() else findNavController().navigateUp()
        }

        nameTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    nameTextValue = s.toString()
                    binding.newPlayListButton.isEnabled = true
                } else {
                    binding.newPlayListButton.isEnabled = false
                    nameTextValue = ""
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        descriptionTextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    descriptionTextValue = s.toString()
                } else {
                    descriptionTextValue = ""
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        nameTextWatcher.let { binding.playlistNameInputText.addTextChangedListener(it) }

        descriptionTextWatcher.let { binding.playlistDescriptionInputText.addTextChangedListener(it) }

    }

    private fun dialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.dialogTheme)
            .setTitle(resources.getString(R.string.finishAddPlayList))
            .setMessage(resources.getString(R.string.saveData))
            .setNeutralButton(resources.getString(R.string.cansel)) { dialog, which ->
            }
            .setPositiveButton(resources.getString(R.string.complete)) { dialog, which ->
                findNavController().navigateUp()
            }
            .show()
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val currentTime = System.currentTimeMillis()
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${currentTime}cover.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        uriImage = file.path
    }
}