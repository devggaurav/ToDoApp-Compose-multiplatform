package presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import presentation.addEditTodo.AddEditScreen
import presentation.todoList.TodoItem
import presentation.todoList.TodoListEvent
import ui.UiEvent
import ui.theme.onPrimaryContainerLight
import ui.theme.primaryContainerLight


//
// Created by Code For Android on 23/05/24.
// Copyright (c) 2024 CFA. All rights reserved.
//

class HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<HomeScreenViewModel>()

        val todos = viewModel.todos.collectAsState(initial = emptyList())

        val snackBarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = true) {
            viewModel.uiEvent.collect { event ->
                when (event) {

                    is UiEvent.ShowSnackBar -> {
                        snackBarHostState.showSnackbar(
                            event.message,
                            actionLabel = event.action,
                            duration = SnackbarDuration.Short
                        )

                    }

                    is UiEvent.Navigate -> {
                        navigator.push(AddEditScreen(event.id))
                    }

                    else -> Unit


                }


            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.onEvent(TodoListEvent.OnAddTodoClick) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            },
            topBar = {
                TopAppBar(
                    title = { Text("Todos") },
                    colors = TopAppBarColors(
                        containerColor = onPrimaryContainerLight,
                        titleContentColor = primaryContainerLight,
                        actionIconContentColor = primaryContainerLight,
                        navigationIconContentColor = primaryContainerLight,
                        scrolledContainerColor = onPrimaryContainerLight
                    )
                )
            },
            containerColor = onPrimaryContainerLight
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(it)
            ) {
                items(todos.value) { todo ->
                    TodoItem(
                        todo = todo,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier.fillMaxWidth()
                            .clickable {
                                viewModel.onEvent(TodoListEvent.OnTodoClick(todo))
                            }.padding(16.dp)
                    )

                }

            }


        }

    }
}

