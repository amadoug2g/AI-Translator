package com.playgroundagc.core.usecase

import com.playgroundagc.core.repository.Repository

/**
 * Created by Amadou on 15/07/2022, 18:11
 *
 * Purpose: API Usage Use Case
 *
 */

class GetAPIUsageUC(private val repository: Repository) {
    suspend operator fun invoke() = repository.getUsage()
}