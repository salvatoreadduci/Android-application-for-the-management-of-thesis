package com.whyskey.tesiunical.data

import android.graphics.Bitmap
import android.media.Image
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "user")
data class Account(
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "role", defaultValue = "0")
    val role: Int,
    @ColumnInfo(name = "site")
    val site: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "info")
    val info: String,
    @ColumnInfo(name = "pic", defaultValue = "R.drawable.account_pic_default")
    val pic: Bitmap,
    @ColumnInfo(name = "max_experimental",defaultValue = "0")
    val maxExperimental: Int,
    @ColumnInfo(name = "max_compilation",defaultValue = "0")
    val maxCompilation: Int,
    @ColumnInfo(name = "max_corporate",defaultValue = "0")
    val maxCorporate: Int,
    @ColumnInfo(name = "max_erasmus",defaultValue = "0")
    val maxErasmus: Int,
)