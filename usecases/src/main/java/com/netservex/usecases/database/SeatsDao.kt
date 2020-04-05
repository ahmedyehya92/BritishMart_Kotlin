package com.netservex.usecases.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.netservex.entities.SeatDB

@Dao
interface SeatsDao {

    @Query("select * from SeatDB ")
    fun queryAll(): List<SeatDB>
}