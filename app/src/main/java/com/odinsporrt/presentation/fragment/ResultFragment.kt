package com.odinsporrt.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.odinsporrt.R
import com.odinsporrt.databinding.FragmentResultBinding
import com.odinsporrt.presentation.GameAdapter


class ResultFragment : Fragment() {
    lateinit var binding: FragmentResultBinding
    lateinit var adapter: GameAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this).load(R.drawable.coin_main).into(binding.imLogo)
        adapter = GameAdapter(requireActivity())
        Log.d("test1","1")
        binding.rcView.adapter = adapter
        if (count.isNotEmpty()){
            adapter.countList.addAll(count)
        }

        if (bet.isNotEmpty()){
            adapter.betList.addAll(bet)
        }

        if (result.isNotEmpty()){
            adapter.resultList.addAll(result)
        }

        binding.btBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    companion object {
        var count1 = 1
        var count = arrayListOf<String>()
        var bet = arrayListOf<String>()
        var result = arrayListOf<String>()
        fun newInstance() = ResultFragment()
    }
}