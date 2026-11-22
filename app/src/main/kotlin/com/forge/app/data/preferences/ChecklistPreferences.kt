package com.forge.app.data.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChecklistPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("forge_checklist_prefs", Context.MODE_PRIVATE)

    fun getCompletedTasks(dateKey: String): Set<String> {
        return prefs.getStringSet("tasks_$dateKey", emptySet()) ?: emptySet()
    }

    fun toggleTask(dateKey: String, taskId: String): Set<String> {
        val current = getCompletedTasks(dateKey).toMutableSet()
        if (current.contains(taskId)) {
            current.remove(taskId)
        } else {
            current.add(taskId)
        }
        prefs.edit().putStringSet("tasks_$dateKey", current).apply()
        return current
    }

    fun saveCompletedTasks(dateKey: String, tasks: Set<String>) {
        prefs.edit().putStringSet("tasks_$dateKey", tasks).apply()
    }
}
