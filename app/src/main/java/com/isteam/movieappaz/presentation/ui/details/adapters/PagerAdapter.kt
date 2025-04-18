package com.isteam.movieappaz.presentation.ui.details.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.isteam.movieappaz.presentation.ui.details.pager.CommentsFragment
import com.isteam.movieappaz.presentation.ui.details.pager.MoreLikeThisFragment
import com.isteam.movieappaz.presentation.ui.details.pager.VideosFragment

class PagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VideosFragment()
            1 -> MoreLikeThisFragment()
            2 -> CommentsFragment()
            else -> VideosFragment()
        }
    }
}