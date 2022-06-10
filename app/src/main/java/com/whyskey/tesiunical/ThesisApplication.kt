package com.whyskey.tesiunical

import android.app.Application
import com.whyskey.tesiunical.data.ThesisRoomDatabase

class ThesisApplication: Application() {
    val database: ThesisRoomDatabase by lazy { ThesisRoomDatabase.getDatabase(this) }
}