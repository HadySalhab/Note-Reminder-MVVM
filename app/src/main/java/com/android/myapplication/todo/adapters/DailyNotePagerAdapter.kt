package com.android.myapplication.todo.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.myapplication.todo.ui.list.NotesListFragment
import com.android.myapplication.todo.ui.list.RemindersListFragment
import java.lang.IndexOutOfBoundsException


const val NOTES_LIST_PAGE_INDEX = 0
const val REMINDERS_LIST_PAGE_INDEX = 1


class DailyNotePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreator: Map<Int, () -> Fragment> = mapOf(
        NOTES_LIST_PAGE_INDEX to { NotesListFragment() },
        REMINDERS_LIST_PAGE_INDEX to { RemindersListFragment() }
    )

    override fun getItemCount(): Int = tabFragmentsCreator.size

    override fun createFragment(position: Int): Fragment =
        tabFragmentsCreator[position]?.invoke() ?: throw IndexOutOfBoundsException()

}