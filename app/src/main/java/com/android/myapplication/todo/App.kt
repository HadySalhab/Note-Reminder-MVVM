package com.android.myapplication.todo

import android.app.Application
import com.android.myapplication.todo.data.db.NoteAndRemindersDB
import com.android.myapplication.todo.repositories.Repository
import com.android.myapplication.todo.ui.HomeViewPagerViewModel
import com.android.myapplication.todo.ui.display.NotesDisplayViewModel
import com.android.myapplication.todo.ui.edit.NotesEditViewModel
import com.android.myapplication.todo.ui.edit.ReminderEditViewModel
import com.android.myapplication.todo.ui.list.NotesListViewModel
import com.android.myapplication.todo.ui.list.RemindersListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    private val koinModule = module {
        single {
            //Database
            NoteAndRemindersDB.getInstance(androidContext())
        }
        single {
            val db: NoteAndRemindersDB = get()
            Repository(db.notesDao, db.remindersDao, androidContext())
        }

        viewModel {
            HomeViewPagerViewModel(get())
        }

        viewModel {
            NotesListViewModel(get(), this@App)
        }
        viewModel { (noteId: String) ->
            NotesDisplayViewModel(get(), noteId)
        }
        viewModel { (noteId: String) ->
            NotesEditViewModel(get(), noteId, this@App)
        }
        viewModel { (reminderId: String) ->
            ReminderEditViewModel(get(), reminderId, this@App)
        }
        viewModel {
            RemindersListViewModel(get())
        }

    }


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(koinModule)
        }
    }
}