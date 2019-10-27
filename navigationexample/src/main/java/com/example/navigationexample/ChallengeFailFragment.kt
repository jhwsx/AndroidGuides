package com.example.navigationexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.navigationexample.databinding.ChallengeFailFragmentBinding

/**
 * @author wangzhichao
 * @date 2019/10/27
 */
class ChallengeFailFragment : Fragment() {
    private lateinit var binding: ChallengeFailFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChallengeFailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBarTitle()
        binding.btnTryAgain.setOnClickListener {
            findNavController().navigate(R.id.action_challengeFailFragment_to_challengeFragment)
        }
    }

    private fun setupAppBarTitle() {
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.title_challenge_fail)
    }
}