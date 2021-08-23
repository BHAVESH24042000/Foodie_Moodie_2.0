package com.example.foodie_moodie_20.utils

import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.foodie_moodie_20.firebase.FirebaseAddEditMemory


class ImageFileExtension(private val activity: FirebaseAddEditMemory, private val uri: Uri?){
    fun getFileExtension():String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}