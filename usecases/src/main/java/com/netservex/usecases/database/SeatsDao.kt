package com.netservex.usecases.database

import androidx.room.Dao
import androidx.room.Query
import com.netservex.entities.SeatDB

@Dao
interface SeatsDao {

    @Query("select * from SeatDB ")
    fun queryAll(): List<SeatDB>
}