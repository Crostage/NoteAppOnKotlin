package com.bar.youday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bar.youday.data.repository.NotesRepository
import java.lang.IllegalArgumentException

class NotesViewModelFactory(private val repository: NotesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java))
            return NotesViewModel(repository) as T
        else
            throw  IllegalArgumentException("Unknown View Model class")
    }
}