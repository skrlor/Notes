package com.example.notes

import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.ui.components.NoteListItem
import com.example.notes.ui.theme.NotesTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes.data.Note
import com.google.android.gms.tagmanager.Container
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import com.example.notes.ui.AddNoteScreen
import com.example.notes.ui.EditNoteScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "note_screen") {
                    composable("note_screen") {
                        NoteScreen(navController = navController, viewModel = viewModel())
                    }
                    composable(
                        "edit_note_screen/{noteId}",
                        arguments = listOf(navArgument("noteId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getString("noteId")
                        if (noteId != null) {
                            EditNoteScreen(navController, noteId)
                        }
                    }
                    composable("add_note_screen") {
                        AddNoteScreen(navController = navController, viewModel = viewModel())
                    }
//                NoteScreen(viewModel = viewModel())
                }
            }
        }
    }
}

@Composable
fun NoteScreen(navController: NavHostController, viewModel: NotesViewModel) {
    val selectedNoteForDelete = remember { mutableStateOf<Note?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_note_screen") },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    Icons.Filled.Add, "Floating Action Button.")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 10.dp)
                .padding(innerPadding)
        ) {
//            Spacer(modifier = Modifier.height(50.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(5.dp))
                Text("Notes", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.weight(1f))
                FloatingActionButton(
                    onClick = {},
                    modifier = Modifier.wrapContentSize(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Filled.Search, "Floating Action Button.")
                }
                Spacer(modifier = Modifier.width(5.dp))
                FloatingActionButton(
                    onClick = {},
                    modifier = Modifier.wrapContentSize(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Filled.Info, "Floating Action Button.")
                }
                Spacer(modifier = Modifier.width(5.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            selectedNoteForDelete.value = null // Reset delete state when tapping outside
                        })
                    },
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.fetchNotes()
                items(viewModel.noteList, key = { note -> note.id }) { note ->
                    NoteListItem(
                        note = note,
                        navController = navController,
                        onDeleteSelect = { noteToDelete ->
                            viewModel.deleteNote((noteToDelete.id))
                        },
                        selectedNoteForDelete = selectedNoteForDelete
                    )
                }
            }
        }
    }
}






