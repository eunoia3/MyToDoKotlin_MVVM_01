package com.eun.mytodokotlin_mvvm_01.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eun.mytodokotlin_mvvm_01.data.model.TodoModel

@Dao
interface TodoDao {

    @Query("SELECT * FROM tb_todo ORDER BY SEQ ASC")
    fun getTodoListAll(): LiveData<List<TodoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)  //@Insert 의 onConflict = : 중복된 데이터의 경우 어떻게 처리할 것인지에 대한 처리를 지정
    fun insert(todo: TodoModel)

    @Delete
    fun delete(todo: TodoModel)

}