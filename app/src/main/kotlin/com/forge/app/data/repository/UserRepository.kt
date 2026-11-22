package com.forge.app.data.repository

import com.forge.app.data.db.entities.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<UserEntity?>
    suspend fun getUserOnce(): UserEntity?
    suspend fun saveUser(user: UserEntity)
}
