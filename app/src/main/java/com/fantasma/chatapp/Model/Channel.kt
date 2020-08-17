package com.fantasma.chatapp.Model

import androidx.lifecycle.ViewModel

class Channel(val name: String, val description: String, val id: String) : ViewModel() {
    override fun toString(): String {
        return "#$name"
    }
}