package com.bar.youday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bar.youday.data.Note
import com.bar.youday.data.repository.NotesRepository
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    val noteList = repository.getNoteList()

    fun insert(note:Note) = viewModelScope.launch{repository.insertNote(note)}

    fun remove(note:Note) = repository.removeNote(note)

    fun delById(id:Int) = repository.deleteNoteById(id)

    fun removeAll() = repository.removeAll()


}