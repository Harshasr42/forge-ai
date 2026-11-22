package com.forge.app.data.db.dao

import androidx.room.*
import com.forge.app.data.db.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id = 1")
    fun getUser(): Flow<UserEntity?>

    @Query("SELECT * FROM user WHERE id = 1")
    suspend fun getUserOnce(): UserEntity?

    @Upsert
    suspend fun upsertUser(user: UserEntity)
}
