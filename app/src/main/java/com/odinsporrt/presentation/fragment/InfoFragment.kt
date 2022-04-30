package com.odinsporrt.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.odinsporrt.R
import com.odinsporrt.databinding.FragmentInfoBinding


class InfoFragment : Fragment() {
    lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this).load(R.drawable.rules).into(binding.imRules)
        binding.btBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    companion object {
        fun newInstance() = InfoFragment()

    }
}