package com.playgroundagc.core.data

/**
 * Created by Amadou on 17/07/2022, 19:42
 *
 * Purpose:
 *
 */

data class UsageResponse(
    val character_count: Int = 0,
    val character_limit: Int = 500000
) {
    fun usagePercent(): Double {
        val count = character_count.toDouble()
        val limit = character_count.toDouble()

        return count.div(limit)
    }
}
