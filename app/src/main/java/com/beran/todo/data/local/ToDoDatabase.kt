package com.beran.todo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.beran.todo.data.models.TodoTask

@Database(entities = [TodoTask::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}