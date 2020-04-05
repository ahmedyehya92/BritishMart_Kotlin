package com.netservex.usecases.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.netservex.entities.SeatDB
import com.netservex.usecases.applicationLiveData
import com.netservex.usecases.getApplication

val cafDatabase by lazy {
    initializeDatabase(applicationLiveData.getApplication())
}

@Database(
    entities = [SeatDB::class],
    version = 1,
    exportSchema = false
)

abstract class CafDatabase : RoomDatabase() {
    abstract val seatsDao: SeatsDao

}