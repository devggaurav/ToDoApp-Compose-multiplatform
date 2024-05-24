package presentation.addEditTodo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.Todo
import ui.UiEvent


//
// Created by Code For Android on 24/05/24.
// Copyright (c) 2024 CFA. All rights reserved.
//

class AddEditScreen(private val todo: Todo? = null) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val addEditViewModel = getScreenModel<AddEditTodoViewModel>()
        addEditViewModel.loadTodo(todo)

        val snackBarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = true) {

            addEditViewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.PopBackStack -> {
                        navigator.pop()
                    }

                    is UiEvent.ShowSnackBar -> {
                        snackBarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action
                        )


                    }

                    else -> Unit
                }


            }


        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            modifier = Modifier.fillMaxSize().padding(16.dp),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { addEditViewModel.onEvent(AddEditTodoEvent.OnSaveTodoClick) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save"
                    )
                }
            }
        ) {

            Column(
                modifier = Modifier.fillMaxSize().padding(it)
            ) {
                TextField(
                    value = addEditViewModel.title,
                    onValueChange = { addEditViewModel.onEvent(AddEditTodoEvent.OnTitleChange(it)) },
                    placeholder = {
                        Text(text = "Title")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = addEditViewModel.description,
                    onValueChange = {
                        addEditViewModel.onEvent(AddEditTodoEvent.OnDescriptionChange(it))
                    },
                    placeholder = {
                        Text(text = "Description")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5
                )


            }


        }


    }
}