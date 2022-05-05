package com.arbonik.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class NothingFragment : Fragment() {
    var mView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView  = inflater.inflate(R.layout.nothing_fragment, null)
        return mView
    }
}