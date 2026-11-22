package com.forge.app.data.repository

import com.forge.app.data.db.dao.UserDao
import com.forge.app.data.db.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override fun getUser(): Flow<UserEntity?> = userDao.getUser()
    override suspend fun getUserOnce(): UserEntity? = userDao.getUserOnce()
    override suspend fun saveUser(user: UserEntity) = userDao.upsertUser(user)
}
