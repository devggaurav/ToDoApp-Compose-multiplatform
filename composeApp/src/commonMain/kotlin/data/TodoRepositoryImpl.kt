package data

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId


//
// Created by Code For Android on 23/05/24.
// Copyright (c) 2024 CFA. All rights reserved.
//

class TodoRepositoryImpl : TodoRepository {

    private var realm: Realm? = null

    init {
        configureDB()
    }

    override fun configureDB() {

        if (realm == null || realm!!.isClosed()) {

            val config = RealmConfiguration.Builder(schema = setOf(Todo::class))
                .compactOnLaunch().build()
            realm = Realm.open(config)

        }

    }

    override suspend fun insertTodo(todo: Todo) {
        realm?.write {
            copyToRealm(todo)
        }
    }

    override suspend fun getTodoById(id: ObjectId): Todo? {
        return realm?.query<Todo>("id == $0", id)?.first()?.find()
    }

    override fun getTodos(): Flow<List<Todo>> {
        return realm?.query<Todo>()
            ?.asFlow()
            ?.map { it.list }
            ?: emptyFlow()
    }

    override suspend fun deleteTodo(todo: Todo) {
        realm?.write {
            delete(query<Todo>("id == ${todo._id}"))
        }
    }

    override suspend fun deleteAllTodos() {
        realm?.write {
            val todos = this.query<Todo>()
            delete(todos)
        }
    }
}