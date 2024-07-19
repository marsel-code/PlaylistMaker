package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink() {
        val shareIntent = Intent(Intent.ACTION_VIEW)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.data = Uri.parse(context.getString(R.string.agreementUser))
//        shareIntent.data = Uri.parse(shareAppLink)
        context.startActivity(shareIntent)
    }

    override fun openLink() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.setType("text/playn")
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.shareButton))
//        shareIntent.putExtra(Intent.EXTRA_TEXT, termsLink)
        context.startActivity(shareIntent)
    }

    override fun openEmail() {
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.data = Uri.parse("mailto:")
        shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.supportMail)))
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.supportSubject))
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.supportMessage))
        context.startActivity(shareIntent)

//        shareIntent.data = Uri.parse(supportEmailData.uriString)
//        shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.supportMail))
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, supportEmailData.supportSubject)
//        shareIntent.putExtra(Intent.EXTRA_TEXT, supportEmailData.supportMessage)
//        context.startActivity(shareIntent)
    }
}