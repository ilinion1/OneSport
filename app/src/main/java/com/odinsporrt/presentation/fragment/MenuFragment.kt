package com.odinsporrt.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.odinsporrt.R
import com.odinsporrt.databinding.FragmentMenuBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MenuFragment : Fragment() {
    lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this).load(R.drawable.game_start).into(binding.imStart)
        clickListener()
    }


    private fun clickListener(){
        animationImage(binding.imInfo)
        animationImage(binding.imSettings)
        animationImage(binding.imResult)
        animationImage(binding.imStart)
    }

    private fun animationImage(image: ImageView){
        val firstParams = image.layoutParams
        firstParams.width = 120
        firstParams.height = 120
        image.layoutParams = firstParams
        image.setOnClickListener {
            val firstParams = image.layoutParams
            val layoutParams = image.layoutParams
            layoutParams.width = 135
            layoutParams.height = 135
            image.layoutParams = layoutParams
            lifecycleScope.launch {
                delay(200)
                firstParams.width = 120
                firstParams.height = 120
                image.layoutParams = firstParams
                when(image){
                    binding.imInfo -> startNextFragment(InfoFragment.newInstance(), )
                    binding.imSettings -> startNextFragment(SettingsFragment.newInstance(), )
                    binding.imResult -> startNextFragment(ResultFragment.newInstance(), )
                    binding.imStart -> startNextFragment(GameFragment.newInstance(), )
                }
            }
        }
    }

    private fun startNextFragment(fragment: Fragment){
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
            .add(R.id.fragContainer, fragment).commit()
    }

    companion object {

        fun newInstance() = MenuFragment()

    }
}