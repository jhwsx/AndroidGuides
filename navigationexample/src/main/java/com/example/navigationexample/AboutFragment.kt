package com.example.navigationexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.navigationexample.databinding.AboutFragmentBinding

/**
 * @author wangzhichao
 * @date 2019/10/27
 */
class AboutFragment : Fragment() {

    private lateinit var binding: AboutFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AboutFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBarTitle()
    }
    private fun setupAppBarTitle() {
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.title_about)
    }
}