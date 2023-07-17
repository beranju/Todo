package com.beran.todo.data.repository

import com.beran.todo.data.local.ToDoDao
import com.beran.todo.data.models.TodoTask
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TodoRepository @Inject constructor(
    private val dao: ToDoDao
) {
    val getAllTask: Flow<List<TodoTask>> = dao.getAllTask()

    val sortByLowPriority: Flow<List<TodoTask>> = dao.sortByLowPriority()

    val sortByHighPriority: Flow<List<TodoTask>> = dao.sortByHighPriority()

    fun getSelectedTask(taskId: Int): Flow<TodoTask> = dao.getSelectedTask(taskId)

    suspend fun addTask(task: TodoTask) = dao.addTask(task)

    suspend fun updateTask(task: TodoTask) = dao.updateTask(task)

    suspend fun deleteTask(task: TodoTask) = dao.delete(task)

    suspend fun deleteAllTask() = dao.deleteAllTask()

    fun searchDatabase(query: String): Flow<List<TodoTask>> = dao.searchDatabase(query)
}