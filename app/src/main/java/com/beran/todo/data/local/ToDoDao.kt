package com.beran.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.beran.todo.data.models.TodoTask
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTask(): Flow<List<TodoTask>>

    @Query("SELECT * FROM todo_table WHERE id=:id")
    fun getSelectedTask(id: Int): Flow<TodoTask>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: TodoTask)

    @Update
    suspend fun updateTask(task: TodoTask)

    @Delete
    suspend fun delete(task: TodoTask)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTask()

    @Query("SELECT * FROM todo_table WHERE title LIKE :query OR description LIKE :query")
    fun searchDatabase(query: String): Flow<List<TodoTask>>

    @Query(
        """
        SELECT * FROM todo_table ORDER BY 
        CASE 
            WHEN priority LIKE 'L%' THEN 1 
            WHEN priority LIKE 'M%' THEN 2 
            WHEN priority LIKE 'H%' THEN 3 
        END
    """
    )
    fun sortByLowPriority(): Flow<List<TodoTask>>

    @Query(
        """
        SELECT * FROM todo_table ORDER BY 
        CASE 
            WHEN priority LIKE 'H%' THEN 1 
            WHEN priority LIKE 'M%' THEN 2 
            WHEN priority LIKE 'L%' THEN 3 
        END
    """
    )
    fun sortByHighPriority(): Flow<List<TodoTask>>
}