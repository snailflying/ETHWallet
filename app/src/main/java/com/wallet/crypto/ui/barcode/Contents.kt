package com.wallet.crypto.ui.barcode

import android.provider.ContactsContract

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 08/03/2018 17:52
 * @description
 */
object Contents {

    const val URL_KEY = "URL_KEY"

    const val NOTE_KEY = "NOTE_KEY"

    // When using Type.CONTACT, these arrays provide the keys for adding or retrieving multiple phone numbers and addresses.
    val PHONE_KEYS = arrayOf(ContactsContract.Intents.Insert.PHONE, ContactsContract.Intents.Insert.SECONDARY_PHONE, ContactsContract.Intents.Insert.TERTIARY_PHONE)

    val PHONE_TYPE_KEYS = arrayOf(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE)

    val EMAIL_KEYS = arrayOf(ContactsContract.Intents.Insert.EMAIL, ContactsContract.Intents.Insert.SECONDARY_EMAIL, ContactsContract.Intents.Insert.TERTIARY_EMAIL)

    val EMAIL_TYPE_KEYS = arrayOf(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE, ContactsContract.Intents.Insert.TERTIARY_EMAIL_TYPE)

    object Type {

        // Plain text. Use Intent.putExtra(DATA, string). This can be used for URLs too, but string
        // must include "http://" or "https://".
        const val TEXT = "TEXT_TYPE"

        // An email type. Use Intent.putExtra(DATA, string) where string is the email address.
        const val EMAIL = "EMAIL_TYPE"

        // Use Intent.putExtra(DATA, string) where string is the phone number to call.
        const val PHONE = "PHONE_TYPE"

        // An SMS type. Use Intent.putExtra(DATA, string) where string is the number to SMS.
        const val SMS = "SMS_TYPE"

        const val CONTACT = "CONTACT_TYPE"

        const val LOCATION = "LOCATION_TYPE"
    }
}