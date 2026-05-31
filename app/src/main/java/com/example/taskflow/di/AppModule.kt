package com.example.taskflow.di

import com.example.taskflow.data.repository.TaskRepositoryImpl
import com.example.taskflow.domain.repository.TaskRepository

object AppModule {

    val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl()
    }
}