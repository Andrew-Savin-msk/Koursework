package com.example.koursework.ui.outbox

import android.content.Context
import android.content.SharedPreferences

object SearchHistoryManager {
    private const val PREFS_NAME = "search_history_prefs"
    private const val KEY_HISTORY = "history"

    fun saveQuery(context: Context, query: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = getHistory(context).toMutableList()
        current.remove(query) // remove duplicates
        current.add(0, query)
        val trimmed = current.take(10).toSet() // max 10
        prefs.edit().putStringSet(KEY_HISTORY, trimmed).apply()
    }

    fun getHistory(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_HISTORY, emptySet())?.toList()?.sortedByDescending {
            prefs.getLong(it, 0)
        } ?: emptyList()
    }

    fun clearHistory(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().remove(KEY_HISTORY).apply()
    }
}
