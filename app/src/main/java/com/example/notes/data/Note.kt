package com.example.notes.data

import androidx.compose.ui.graphics.Color

data class Note (
    var id: String,
    var title: String,
    var content: String
) {
    constructor() : this("", "", "")
}