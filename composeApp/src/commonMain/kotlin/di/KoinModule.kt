package di

import data.TodoRepository
import data.TodoRepositoryImpl
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.HomeScreenViewModel


//
// Created by Code For Android on 23/05/24.
// Copyright (c) 2024 CFA. All rights reserved.
//

val appModule = module {
    single<TodoRepository> { TodoRepositoryImpl() }
    factory {
        HomeScreenViewModel(todoRepository = get())
    }
}


fun initializeKoin() {
    startKoin {
        modules(appModule)
    }

}