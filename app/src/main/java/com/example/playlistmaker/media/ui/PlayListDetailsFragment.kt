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
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayListDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.FileOutputStream

class PlayListDetailsFragment : Fragment() {

    private var _binding: FragmentPlayListDetailsBinding? = null
    private val binding
        get() = _binding!!

    lateinit var confirmDialog: androidx.appcompat.app.AlertDialog
    private lateinit var playlistNameText: EditText
    private lateinit var playlistDescriptionText: EditText
    private lateinit var nameTextWatcher: TextWatcher
    private var nameTextValue: String? = ""
    private lateinit var descriptionTextWatcher: TextWatcher
    private var descriptionTextValue: String? = ""
    private var imagePlayList: Boolean = false


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

//        val filePath =
//            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
//        val file = File(filePath, "first_cover.jpg")
//        binding.imagePlaylistDetails.setImageURI(file.toUri())

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

            Toast.makeText(
                activity,
                "Плейлист $nameTextValue создан",
                Toast.LENGTH_SHORT
            ).show()
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
//                    context?.let {
//                        ContextCompat.getColor(
//                            it,
//                            R.color.blue
//                        )
//                    }?.let {
//                        binding.newPlayListButton.setBackgroundColor(
//                            it
//                        )
//                    }

                } else {
                    binding.newPlayListButton.isEnabled = false
//                    context?.let {
//                        ContextCompat.getColor(
//                            it,
//                            R.color.aluminium
//                        )
//                    }?.let {
//                        binding.newPlayListButton.setBackgroundColor(
//                            it
//                        )
//                    }
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
            .setTitle("Завершить создание плейлиста?") // Заголовок диалога
            .setMessage("Все несохраненные данные будут потеряны") // Описание диалога
            .setNeutralButton("Отмена") { dialog, which -> // Добавляет кнопку «Отмена»
            }
            .setPositiveButton("Завершить") { dialog, which -> // Добавляет кнопку «Да»
                findNavController().navigateUp()
            }
            .show()
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "first_cover.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

}