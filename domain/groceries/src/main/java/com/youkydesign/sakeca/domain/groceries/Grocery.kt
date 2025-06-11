package com.youkydesign.sakeca.domain.groceries

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Grocery(
    val id: Long = 0,
    val name: String,
    val quantity: Int = 1,
) : Parcelable