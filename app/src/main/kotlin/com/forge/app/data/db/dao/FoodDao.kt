package com.forge.app.data.db.dao

import androidx.room.*
import com.forge.app.data.db.entities.FoodItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFoods(foods: List<FoodItemEntity>)

    @Query("SELECT * FROM food_item ORDER BY name ASC")
    fun getAllFoodsFlow(): Flow<List<FoodItemEntity>>

    @Query("SELECT * FROM food_item WHERE name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' ORDER BY isVerified DESC, name ASC")
    fun searchFoodsFlow(query: String): Flow<List<FoodItemEntity>>

    @Query("SELECT * FROM food_item WHERE category = :category ORDER BY name ASC")
    fun getFoodsByCategoryFlow(category: String): Flow<List<FoodItemEntity>>

    @Query("SELECT * FROM food_item WHERE id = :id")
    suspend fun getFoodById(id: Long): FoodItemEntity?

    @Query("SELECT COUNT(*) FROM food_item")
    suspend fun getFoodCount(): Int
}
