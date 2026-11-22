package com.forge.app.data.seed

import com.forge.app.data.db.dao.FoodDao
import com.forge.app.data.db.dao.NutritionLogDao
import com.forge.app.data.db.entities.DailyNutritionTargetEntity
import com.forge.app.data.db.entities.FoodItemEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodDatabaseSeeder @Inject constructor(
    private val foodDao: FoodDao,
    private val nutritionLogDao: NutritionLogDao
) {
    suspend fun seedIfNeeded() {
        if (foodDao.getFoodCount() == 0) {
            foodDao.insertAllFoods(getSeedFoods())
        }
        if (nutritionLogDao.getTarget() == null) {
            nutritionLogDao.insertTarget(
                DailyNutritionTargetEntity(
                    id = 1L,
                    targetCalories = 2200f,
                    targetProtein = 140f,
                    targetCarbs = 240f,
                    targetFat = 65f,
                    targetFiber = 30f,
                    waterTargetMl = 3000,
                    goalType = "MAINTENANCE"
                )
            )
        }
    }

    companion object {
        fun getSeedFoods(): List<FoodItemEntity> = listOf(
            // Indian Staples & Breakfast
            FoodItemEntity(name = "Idli", category = "INDIAN", servingSize = 2f, servingUnit = "pieces", calories = 130f, protein = 4.0f, carbs = 28.0f, fat = 0.4f, fiber = 2.0f),
            FoodItemEntity(name = "Plain Dosa", category = "INDIAN", servingSize = 1f, servingUnit = "piece", calories = 165f, protein = 3.8f, carbs = 29.0f, fat = 3.7f, fiber = 1.5f),
            FoodItemEntity(name = "Masala Dosa", category = "INDIAN", servingSize = 1f, servingUnit = "piece", calories = 250f, protein = 5.0f, carbs = 38.0f, fat = 9.0f, fiber = 3.0f),
            FoodItemEntity(name = "Roti / Phulka", category = "INDIAN", servingSize = 1f, servingUnit = "piece", calories = 100f, protein = 3.1f, carbs = 20.0f, fat = 0.7f, fiber = 2.8f),
            FoodItemEntity(name = "Cooked White Rice", category = "GRAINS", servingSize = 150f, servingUnit = "g", calories = 195f, protein = 4.1f, carbs = 43.0f, fat = 0.4f, fiber = 0.6f),
            FoodItemEntity(name = "Cooked Brown Rice", category = "GRAINS", servingSize = 150f, servingUnit = "g", calories = 168f, protein = 3.8f, carbs = 35.0f, fat = 1.4f, fiber = 2.7f),
            FoodItemEntity(name = "Dal Tadka", category = "INDIAN", servingSize = 200f, servingUnit = "g", calories = 180f, protein = 9.0f, carbs = 24.0f, fat = 5.5f, fiber = 6.0f),
            FoodItemEntity(name = "Chana Masala", category = "INDIAN", servingSize = 200f, servingUnit = "g", calories = 280f, protein = 12.0f, carbs = 38.0f, fat = 8.5f, fiber = 9.0f),
            FoodItemEntity(name = "Rajma Curry", category = "INDIAN", servingSize = 200f, servingUnit = "g", calories = 260f, protein = 13.0f, carbs = 36.0f, fat = 6.0f, fiber = 10.0f),
            FoodItemEntity(name = "Moong Dal Khichdi", category = "INDIAN", servingSize = 200f, servingUnit = "g", calories = 220f, protein = 8.5f, carbs = 38.0f, fat = 4.0f, fiber = 4.5f),
            FoodItemEntity(name = "Poha", category = "INDIAN", servingSize = 150f, servingUnit = "g", calories = 250f, protein = 4.5f, carbs = 46.0f, fat = 5.5f, fiber = 2.5f),
            FoodItemEntity(name = "Upma", category = "INDIAN", servingSize = 150f, servingUnit = "g", calories = 230f, protein = 5.0f, carbs = 39.0f, fat = 6.5f, fiber = 3.0f),
            FoodItemEntity(name = "Besan Chilla", category = "INDIAN", servingSize = 1f, servingUnit = "piece", calories = 140f, protein = 6.5f, carbs = 18.0f, fat = 4.5f, fiber = 3.0f),
            FoodItemEntity(name = "Sambar", category = "INDIAN", servingSize = 200f, servingUnit = "ml", calories = 110f, protein = 4.5f, carbs = 18.0f, fat = 2.5f, fiber = 4.0f),
            FoodItemEntity(name = "Coconut Chutney", category = "INDIAN", servingSize = 40f, servingUnit = "g", calories = 100f, protein = 1.2f, carbs = 3.5f, fat = 9.5f, fiber = 2.0f),
            FoodItemEntity(name = "Palak Paneer", category = "INDIAN", servingSize = 200f, servingUnit = "g", calories = 240f, protein = 12.0f, carbs = 8.0f, fat = 18.0f, fiber = 4.0f),
            FoodItemEntity(name = "Paneer Bhurji", category = "INDIAN", servingSize = 150f, servingUnit = "g", calories = 260f, protein = 16.0f, carbs = 6.0f, fat = 19.0f, fiber = 1.5f),
            FoodItemEntity(name = "Mixed Vegetable Sabzi", category = "INDIAN", servingSize = 150f, servingUnit = "g", calories = 130f, protein = 3.0f, carbs = 16.0f, fat = 6.0f, fiber = 4.0f),
            FoodItemEntity(name = "Bhindi Masala", category = "INDIAN", servingSize = 150f, servingUnit = "g", calories = 120f, protein = 2.5f, carbs = 14.0f, fat = 6.5f, fiber = 4.5f),

            // High Protein & Meat
            FoodItemEntity(name = "Chicken Breast (Raw)", category = "PROTEIN", servingSize = 100f, servingUnit = "g", calories = 120f, protein = 26.0f, carbs = 0.0f, fat = 1.5f, fiber = 0.0f),
            FoodItemEntity(name = "Chicken Breast (Cooked/Grilled)", category = "PROTEIN", servingSize = 100f, servingUnit = "g", calories = 165f, protein = 31.0f, carbs = 0.0f, fat = 3.6f, fiber = 0.0f),
            FoodItemEntity(name = "Home-style Chicken Curry", category = "PROTEIN", servingSize = 200f, servingUnit = "g", calories = 240f, protein = 22.0f, carbs = 6.0f, fat = 14.0f, fiber = 1.5f),
            FoodItemEntity(name = "Whole Boiled Egg", category = "PROTEIN", servingSize = 1f, servingUnit = "piece", calories = 74f, protein = 6.3f, carbs = 0.4f, fat = 5.0f, fiber = 0.0f),
            FoodItemEntity(name = "Boiled Egg White", category = "PROTEIN", servingSize = 1f, servingUnit = "piece", calories = 17f, protein = 3.6f, carbs = 0.2f, fat = 0.1f, fiber = 0.0f),
            FoodItemEntity(name = "Fish Fillet (Rohu/Katla)", category = "PROTEIN", servingSize = 150f, servingUnit = "g", calories = 190f, protein = 24.0f, carbs = 0.0f, fat = 10.0f, fiber = 0.0f),
            FoodItemEntity(name = "Canned Tuna in Water", category = "PROTEIN", servingSize = 100f, servingUnit = "g", calories = 116f, protein = 26.0f, carbs = 0.0f, fat = 1.0f, fiber = 0.0f),
            FoodItemEntity(name = "Salmon (Grilled)", category = "PROTEIN", servingSize = 100f, servingUnit = "g", calories = 206f, protein = 22.0f, carbs = 0.0f, fat = 12.0f, fiber = 0.0f),
            FoodItemEntity(name = "Soya Chunks (Dry)", category = "PROTEIN", servingSize = 50f, servingUnit = "g", calories = 173f, protein = 26.0f, carbs = 16.5f, fat = 0.2f, fiber = 6.5f),
            FoodItemEntity(name = "Whey Protein Isolate", category = "PROTEIN", servingSize = 32f, servingUnit = "g", calories = 125f, protein = 25.0f, carbs = 2.5f, fat = 1.5f, fiber = 0.5f),
            FoodItemEntity(name = "Paneer (Fresh Raw)", category = "DAIRY", servingSize = 100f, servingUnit = "g", calories = 290f, protein = 18.3f, carbs = 3.4f, fat = 22.0f, fiber = 0.0f),

            // Dairy
            FoodItemEntity(name = "Plain Curd / Dahi", category = "DAIRY", servingSize = 100f, servingUnit = "g", calories = 60f, protein = 3.5f, carbs = 4.7f, fat = 3.0f, fiber = 0.0f),
            FoodItemEntity(name = "Greek Yogurt (Plain)", category = "DAIRY", servingSize = 100f, servingUnit = "g", calories = 97f, protein = 10.0f, carbs = 3.6f, fat = 5.0f, fiber = 0.0f),
            FoodItemEntity(name = "Cow Milk (Toned)", category = "DAIRY", servingSize = 250f, servingUnit = "ml", calories = 120f, protein = 8.0f, carbs = 12.5f, fat = 4.5f, fiber = 0.0f),
            FoodItemEntity(name = "Cow Milk (Whole)", category = "DAIRY", servingSize = 250f, servingUnit = "ml", calories = 150f, protein = 8.0f, carbs = 12.0f, fat = 8.0f, fiber = 0.0f),
            FoodItemEntity(name = "Soy Milk (Unsweetened)", category = "DAIRY", servingSize = 250f, servingUnit = "ml", calories = 80f, protein = 7.0f, carbs = 4.0f, fat = 4.0f, fiber = 1.0f),

            // Grains & Oats
            FoodItemEntity(name = "Rolled Oats", category = "GRAINS", servingSize = 50f, servingUnit = "g", calories = 190f, protein = 6.8f, carbs = 33.0f, fat = 3.5f, fiber = 5.0f),
            FoodItemEntity(name = "Whole Wheat Bread", category = "GRAINS", servingSize = 1f, servingUnit = "slice", calories = 75f, protein = 3.5f, carbs = 13.0f, fat = 1.0f, fiber = 2.0f),
            FoodItemEntity(name = "Brown Rice Cake", category = "GRAINS", servingSize = 1f, servingUnit = "piece", calories = 35f, protein = 0.7f, carbs = 7.3f, fat = 0.3f, fiber = 0.5f),

            // Nuts, Seeds & Healthy Fats
            FoodItemEntity(name = "Peanut Butter", category = "PROTEIN", servingSize = 32f, servingUnit = "g", calories = 190f, protein = 8.0f, carbs = 6.0f, fat = 16.0f, fiber = 2.0f),
            FoodItemEntity(name = "Almonds (Raw)", category = "SNACKS", servingSize = 28f, servingUnit = "g", calories = 164f, protein = 6.0f, carbs = 6.1f, fat = 14.2f, fiber = 3.5f),
            FoodItemEntity(name = "Walnuts", category = "SNACKS", servingSize = 28f, servingUnit = "g", calories = 185f, protein = 4.3f, carbs = 3.9f, fat = 18.5f, fiber = 1.9f),
            FoodItemEntity(name = "Roasted Chana", category = "SNACKS", servingSize = 40f, servingUnit = "g", calories = 145f, protein = 8.5f, carbs = 23.0f, fat = 2.2f, fiber = 5.0f),
            FoodItemEntity(name = "Sprouts Salad (Moong)", category = "SNACKS", servingSize = 150f, servingUnit = "g", calories = 150f, protein = 11.0f, carbs = 26.0f, fat = 1.0f, fiber = 6.0f),
            FoodItemEntity(name = "Chia Seeds", category = "SNACKS", servingSize = 12f, servingUnit = "g", calories = 58f, protein = 2.0f, carbs = 5.0f, fat = 3.7f, fiber = 4.0f),
            FoodItemEntity(name = "Flax Seeds", category = "SNACKS", servingSize = 10f, servingUnit = "g", calories = 55f, protein = 1.9f, carbs = 3.0f, fat = 4.2f, fiber = 2.8f),
            FoodItemEntity(name = "Olive Oil", category = "SNACKS", servingSize = 14f, servingUnit = "g", calories = 119f, protein = 0.0f, carbs = 0.0f, fat = 13.5f, fiber = 0.0f),
            FoodItemEntity(name = "Pure Desi Ghee", category = "SNACKS", servingSize = 5f, servingUnit = "g", calories = 45f, protein = 0.0f, carbs = 0.0f, fat = 5.0f, fiber = 0.0f),

            // Fruits & Vegetables
            FoodItemEntity(name = "Banana", category = "FRUITS", servingSize = 1f, servingUnit = "piece", calories = 105f, protein = 1.3f, carbs = 27.0f, fat = 0.3f, fiber = 3.1f),
            FoodItemEntity(name = "Apple", category = "FRUITS", servingSize = 1f, servingUnit = "piece", calories = 95f, protein = 0.5f, carbs = 25.0f, fat = 0.3f, fiber = 4.4f),
            FoodItemEntity(name = "Orange", category = "FRUITS", servingSize = 1f, servingUnit = "piece", calories = 62f, protein = 1.2f, carbs = 15.4f, fat = 0.2f, fiber = 3.1f),
            FoodItemEntity(name = "Papaya (Diced)", category = "FRUITS", servingSize = 145f, servingUnit = "g", calories = 62f, protein = 0.7f, carbs = 15.7f, fat = 0.4f, fiber = 2.5f),
            FoodItemEntity(name = "Watermelon (Diced)", category = "FRUITS", servingSize = 150f, servingUnit = "g", calories = 46f, protein = 0.9f, carbs = 11.5f, fat = 0.2f, fiber = 0.6f),
            FoodItemEntity(name = "Boiled Potatoes", category = "VEGETABLES", servingSize = 150f, servingUnit = "g", calories = 130f, protein = 3.0f, carbs = 30.0f, fat = 0.2f, fiber = 2.4f),
            FoodItemEntity(name = "Boiled Sweet Potato", category = "VEGETABLES", servingSize = 130f, servingUnit = "g", calories = 112f, protein = 2.0f, carbs = 26.0f, fat = 0.1f, fiber = 3.9f),
            FoodItemEntity(name = "Cucumber (Sliced)", category = "VEGETABLES", servingSize = 150f, servingUnit = "g", calories = 25f, protein = 1.0f, carbs = 5.0f, fat = 0.2f, fiber = 1.0f),
            FoodItemEntity(name = "Fresh Green Salad", category = "VEGETABLES", servingSize = 150f, servingUnit = "g", calories = 30f, protein = 1.5f, carbs = 6.0f, fat = 0.3f, fiber = 2.5f),
            FoodItemEntity(name = "Steamed Broccoli", category = "VEGETABLES", servingSize = 100f, servingUnit = "g", calories = 35f, protein = 2.4f, carbs = 7.0f, fat = 0.4f, fiber = 2.6f),
            FoodItemEntity(name = "Dark Chocolate (70%)", category = "SNACKS", servingSize = 20f, servingUnit = "g", calories = 120f, protein = 1.6f, carbs = 9.0f, fat = 8.5f, fiber = 2.2f),
            FoodItemEntity(name = "Black Coffee", category = "SNACKS", servingSize = 200f, servingUnit = "ml", calories = 2f, protein = 0.3f, carbs = 0.0f, fat = 0.0f, fiber = 0.0f)
        )
    }
}
