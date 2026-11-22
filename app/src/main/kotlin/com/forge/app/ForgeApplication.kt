package com.forge.app

import android.app.Application
import com.forge.app.data.seed.FoodDatabaseSeeder
import com.forge.app.data.seed.ForgeSeeder
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ForgeApplication : Application() {

    @Inject
    lateinit var seeder: ForgeSeeder

    @Inject
    lateinit var foodSeeder: FoodDatabaseSeeder

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            seeder.seedIfNeeded()
            foodSeeder.seedIfNeeded()
        }
    }
}
