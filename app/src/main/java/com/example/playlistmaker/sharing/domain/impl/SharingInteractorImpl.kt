package com.example.playlistmaker.settings.domain.impl


import android.content.res.Resources
import androidx.core.content.ContextCompat
import com.example.playlistmaker.R

import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(

    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
//        externalNavigator.shareLink(getShareAppLink())
        externalNavigator.shareLink()
    }

    override fun openTerms() {
//        externalNavigator.openLink(getSupportEmailData())
        externalNavigator.openLink()
    }

    override fun openSupport() {
//        externalNavigator.openEmail(getTermsLink())
        externalNavigator.openEmail()
    }

//    private fun getShareAppLink(): String {
//        return ContextCompat.getString(R.string.agreementUser)
//
//    }
//
//    private fun getSupportEmailData(): EmailData {
//        return EmailData(
//            "mailto:",
//            Resources.getSystem().getString(R.string.supportMail),
//            Resources.getSystem().getString(R.string.supportSubject),
//            Resources.getSystem().getString(R.string.supportMessage)
//        )
//    }
//
//    private fun getTermsLink(): String {
//    return Resources.getSystem().getString(R.string.shareButton)
////        return (R.string.shareButton.toString())
//    }
}

