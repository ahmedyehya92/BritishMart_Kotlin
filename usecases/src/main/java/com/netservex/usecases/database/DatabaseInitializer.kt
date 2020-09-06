package com.netservex.usecases.database

import androidx.room.Room
import android.content.Context

private const val DATABASE_NAME = "CafDatabaseGateway.db"



fun initializeDatabase(context: Context): CafDatabase {
    return buildDatabase(context)
}


private fun buildDatabase(context: Context) =
    Room.databaseBuilder(context, CafDatabase::class.java, DATABASE_NAME).build()