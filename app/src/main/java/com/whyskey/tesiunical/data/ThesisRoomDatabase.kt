package com.whyskey.tesiunical.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Thesis::class], version = 1, exportSchema = false)
abstract class ThesisRoomDatabase : RoomDatabase() {

    abstract fun thesisDao(): ThesisDao

    companion object {
        @Volatile
        private var INSTANCE: ThesisRoomDatabase? = null

        fun getDatabase(context: Context): ThesisRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ThesisRoomDatabase::class.java,
                    "thesis_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}