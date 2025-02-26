package com.kworkapp.audiogid

interface SimpleQuest {
    fun setCardData(
        title: String,
        description: String,
        link: String,
        imageUrl: String,
        avatarRes: Int
    )
}
