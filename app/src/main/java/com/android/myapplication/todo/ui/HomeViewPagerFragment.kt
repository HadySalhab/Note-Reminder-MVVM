package com.android.myapplication.todo.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2

import com.android.myapplication.todo.R
import com.android.myapplication.todo.adapters.DailyNotePagerAdapter
import com.android.myapplication.todo.adapters.NOTES_LIST_PAGE_INDEX
import com.android.myapplication.todo.adapters.REMINDERS_LIST_PAGE_INDEX
import com.android.myapplication.todo.databinding.FragmentHomeViewPagerBinding
import com.android.myapplication.todo.util.EventObserver
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.IndexOutOfBoundsException

/**
 * A simple [Fragment] subclass.
 */
class HomeViewPagerFragment : Fragment() {
    private lateinit var tabLayout:TabLayout
    private lateinit var viewPager:ViewPager2
    private val viewPagerViewModel:HomeViewPagerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeViewPagerBinding.inflate(layoutInflater,container,false)
        binding.viewModel = viewPagerViewModel
        tabLayout = binding.tabs
        viewPager = binding.viewPager
        viewPager.adapter = DailyNotePagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFabNavigation()
    }

    fun setupFabNavigation(){
        viewPagerViewModel.fabNavListenner.observe(viewLifecycleOwner,EventObserver{
            when(viewPager.currentItem){
                0-> {
                    val action =
                        HomeViewPagerFragmentDirections.actionHomeViewPagerFragmentToNotesEditFragment()
                    findNavController().navigate(action)
                }

            }

        })
    }

    private fun getTabIcon(position:Int):Int=
         when(position){
            NOTES_LIST_PAGE_INDEX -> R.drawable.ic_note_list_selector
            REMINDERS_LIST_PAGE_INDEX->R.drawable.ic_reminder_list_selector
            else->throw IndexOutOfBoundsException()
        }

    private fun getTabTitle(position: Int):String? =
        when(position){
            NOTES_LIST_PAGE_INDEX->getString(R.string.my_notes_list_title)
            REMINDERS_LIST_PAGE_INDEX->getString(R.string.my_reminders_list_title)
            else->null
        }


}
