package com.example.project2.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AnimalsEntity::class], version = 2)
abstract class RoomDB : RoomDatabase() {
    abstract fun animalsDao(): AnimalsDao
}
