package com.example.taskflow.domain.model

import java.time.LocalDate

data class Task(
    val id: Int,
    val title: String,
    val category: Category,
    val priority: Priority,
    val dueDate: LocalDate? = null,
    val isDone: Boolean = false
)