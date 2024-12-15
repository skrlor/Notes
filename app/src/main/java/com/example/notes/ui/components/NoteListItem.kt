package com.example.notes.ui.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notes.data.Note
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(
    note: Note,
    navController: NavHostController,
    onDeleteSelect: (Note) -> Unit,
    selectedNoteForDelete: MutableState<Note?>
) {
//    val isDeleting = remember { mutableStateOf(false) }
    val isDeleting = selectedNoteForDelete.value == note
    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = {
                    navController.navigate("edit_note_screen/${note.id}")
                },
                onLongClick = {
//                    isDeleting.value = true
                    selectedNoteForDelete.value = note
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor =  if (isDeleting) Color.Red else MaterialTheme.colorScheme.primaryContainer
        )

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp)
        ) {

            if (isDeleting) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { showDeleteConfirmationDialog.value = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Buton")
                    }
                }
            } else
                Text(text = note.title, style = MaterialTheme.typography.titleMedium)


        }

        if (showDeleteConfirmationDialog.value) {
            AlertDialog(
                onDismissRequest = {showDeleteConfirmationDialog.value = false},
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Floating Action Button."
                    )
                },
                title = { Text(text = "Confirm Delete") },
                text = { Text(text = "Are you sure you want to delete this note?") },
                confirmButton = {
                    Button(
                        onClick = {
                            onDeleteSelect(note)
                            showDeleteConfirmationDialog.value = false
//                            isDeleting.value = false
                            selectedNoteForDelete.value = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
                        Text("Confirm", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDeleteConfirmationDialog.value = false
//                        isDeleting.value = false
                        selectedNoteForDelete.value = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
                        Text("Dismiss", color = Color.White)
                    }
                }
            )
        }
    }
}

//var testNote = Note("A", "AAA", "AAA AA")
//
//@Preview
//@Composable
//fun NoteListPreview() {
//    NoteListItem(testNote)
//}


