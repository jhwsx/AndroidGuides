package com.example.navigationexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.navigationexample.databinding.RulesFragmentBinding

/**
 * @author wangzhichao
 * @date 2019/10/27
 */
class RulesFragment : Fragment() {
    private lateinit var binding: RulesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RulesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBarTitle()
    }
    private fun setupAppBarTitle() {
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.title_rules)
    }
}