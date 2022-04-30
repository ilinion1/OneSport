package com.odinsporrt.presentation.fragment

import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.odinsporrt.R
import com.odinsporrt.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (normLevel){
            binding.rd1.isChecked = true
        } else {
            binding.rd2.isChecked = true
        }
        binding.rd1.setOnClickListener {
            normLevel = true
            binding.rd2.isChecked = false
        }
        binding.rd2.setOnClickListener {
            normLevel = false
            binding.rd1.isChecked = false
        }
        binding.btBack.setOnClickListener {
            sendLevel()
            requireActivity().onBackPressed()
        }
        Glide.with(this).load(R.drawable.analitic).into(binding.imAnalitic)

    }

    private fun sendLevel(){
        GameFragment.hardLevel = !binding.rd1.isChecked
    }

    companion object {
        var normLevel = true
        fun newInstance() = SettingsFragment()
    }
}