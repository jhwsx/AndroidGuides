package com.example.databindingexamples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.databindingexamples.databinding.MainFragmentBinding

/**
 *
 * @author wangzhichao
 * @since 2020/12/7
 */
class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tv.text = "I'm in fragment."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //  Fragments outlive their views. Make sure you clean up any references to the binding class instance in the fragment's onDestroyView() method.
        _binding = null
    }

    companion object {
        fun newInstance(): MainFragment {
            val args = Bundle()
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }
}