package com.example.myapplication.features.time.domain.usercases

import com.example.myapplication.features.time.domain.model.ServerTime
import com.example.myapplication.features.time.domain.repository.ITimeRepository

class GetServerTimeUseCase(private val repository: ITimeRepository) {
    suspend fun invoke(): Result<ServerTime> {
        return repository.getCurrentTime()
    }
}
