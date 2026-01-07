package com.example.skillsharex.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * Open WhatsApp chat with given phone number
 * Phone format: countrycode + number (NO +, NO spaces)
 */
fun openWhatsApp(context: Context, phone: String?) {

    if (phone.isNullOrBlank()) {
        Toast.makeText(context, "Phone number not available", Toast.LENGTH_SHORT).show()
        return
    }

    val uri = Uri.parse("https://wa.me/$phone")
    val intent = Intent(Intent.ACTION_VIEW, uri)

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Open dialer with given phone number
 */
fun makeCall(context: Context, phone: String?) {

    if (phone.isNullOrBlank()) {
        Toast.makeText(context, "Phone number not available", Toast.LENGTH_SHORT).show()
        return
    }

    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phone")
    }

    context.startActivity(intent)
}
