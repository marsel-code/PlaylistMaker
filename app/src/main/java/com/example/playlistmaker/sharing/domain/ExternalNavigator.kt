package com.example.playlistmaker.sharing.domain

import android.provider.ContactsContract
import com.example.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink()
    fun shareText(text: String)
    fun openLink()
    fun openEmail()
}