package com.example.taskflow.data.repository

import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class TaskRepositoryImpl : TaskRepository {

    private val _tasks = MutableStateFlow(
        listOf(
            Task(
                id = 1,
                title = "Finalizar informe Q2",
                category = Category.TRABAJO,
                priority = Priority.ALTA,
                dueDate = LocalDate.now().minusDays(1),
                isDone = false
            ),
            Task(
                id = 2,
                title = "Implementar principio SRP en ViewModel",
                category = Category.ESTUDIO,
                priority = Priority.ALTA,
                dueDate = LocalDate.now().plusDays(5),
                isDone = false
            ),
            Task(
                id = 3,
                title = "Correr 30 minutos",
                category = Category.SALUD,
                priority = Priority.MEDIA,
                dueDate = LocalDate.now(),
                isDone = true
            ),
            Task(
                id = 4,
                title = "Leer sobre Clean Architecture",
                category = Category.ESTUDIO,
                priority = Priority.MEDIA,
                dueDate = LocalDate.now().plusDays(10),
                isDone = false
            ),
            Task(
                id = 5,
                title = "Reunión con el equipo de diseño",
                category = Category.TRABAJO,
                priority = Priority.ALTA,
                dueDate = LocalDate.now().plusDays(2),
                isDone = false
            ),
            Task(
                id = 6,
                title = "Comprar víveres",
                category = Category.PERSONAL,
                priority = Priority.BAJA,
                dueDate = LocalDate.now().plusDays(3),
                isDone = false
            )
        )
    )

    private var nextId = 7

    override fun getTasks(): Flow<List<Task>> = _tasks.asStateFlow()

    override suspend fun addTask(task: Task) {
        _tasks.update { current ->
            current + task.copy(id = nextId++)
        }
    }

    override suspend fun updateTask(task: Task) {
        _tasks.update { current ->
            current.map { if (it.id == task.id) task else it }
        }
    }

    override suspend fun deleteTask(taskId: Int) {
        _tasks.update { current ->
            current.filter { it.id != taskId }
        }
    }

    override suspend fun toggleDone(taskId: Int) {
        _tasks.update { current ->
            current.map { if (it.id == taskId) it.copy(isDone = !it.isDone) else it }
        }
    }
}