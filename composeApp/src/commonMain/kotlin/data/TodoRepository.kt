package data

import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId


//
// Created by Code For Android on 23/05/24.
// Copyright (c) 2024 CFA. All rights reserved.
//

interface TodoRepository {

    fun configureDB()

    suspend fun insertTodo(todo: Todo)

    suspend fun getTodoById(id: ObjectId): Todo?

    fun getTodos(): Flow<List<Todo>>

    suspend fun deleteTodo(todo: Todo)


    suspend fun deleteAllTodos()

}