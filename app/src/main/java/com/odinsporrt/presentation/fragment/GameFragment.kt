package com.odinsporrt.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.FocusFinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.odinsporrt.R
import com.odinsporrt.databinding.FragmentGameBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class GameFragment : Fragment() {
    lateinit var binding: FragmentGameBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (SettingsFragment.normLevel) {
            binding.tvCof.text = "1/1"
        } else {
            binding.tvCof.text = "1/2"
        }
        binding.tvBalans.text = balans.toString()
          nextImage()

        clickImage()
        setClickBet()
        setOnClickPay()
        binding.btBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun clickImage() {
        binding.cardIm1.setOnClickListener {
            imagePressed = true
            im1Pressed = true
            it.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.blue))
            binding.cardIm2.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.transparent
                )
            )
        }
        binding.cardIm2.setOnClickListener {
            imagePressed = true
            it.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.blue))
            binding.cardIm1.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.transparent
                )
            )
        }
    }

    private fun setClickBet() {
        binding.btBet.setOnClickListener {
            if (binding.edBet.text.isNotEmpty()) {
                betSum = binding.edBet.text.toString().toInt()
            }
            if (binding.edPay.text.isNotEmpty()) {
                balans = binding.edPay.text.toString().toInt()
            }
            if (betSum > balans) {
                lifecycleScope.launch {
                    binding.tvnoMany.setText(R.string.noMany)
                    binding.cardNoMany.visibility = View.VISIBLE
                    delay(3000)
                    binding.cardNoMany.visibility = View.GONE
                }
            }
            if (!imagePressed) {
                lifecycleScope.launch {
                    binding.tvnoMany.setText(R.string.choice)
                    binding.cardNoMany.visibility = View.VISIBLE
                    delay(3000)
                    binding.cardNoMany.visibility = View.GONE
                }
            }
            if (binding.edBet.text.isEmpty()) {
                lifecycleScope.launch {
                    binding.tvnoMany.setText(R.string.not_bet)
                    binding.cardNoMany.visibility = View.VISIBLE
                    delay(3000)
                    binding.cardNoMany.visibility = View.GONE
                }
            }
            if (binding.edBet.text.isNotEmpty() && betSum <= balans && imagePressed) {
                checkWinOrLose()
                binding.edBet.setText("")
                binding.edBet.clearFocus()
                if (countRaund == 3){
                    countRaund = 0
                }
                countRaund++
                nextImage()
            }
        }
    }

    private fun nextImage() {
        if (countRaund == 1) {
            Glide.with(this).load(R.drawable.real).into(binding.im2)
            Glide.with(this).load(R.drawable.barsa).into(binding.im1)
        }
        if (countRaund == 2) {
            Glide.with(this).load(R.drawable.miamy).into(binding.im2)
            Glide.with(this).load(R.drawable.chicago).into(binding.im1)
        }
        if (countRaund == 3) {
            Glide.with(this).load(R.drawable.betmen).into(binding.im2)
            Glide.with(this).load(R.drawable.super_im).into(binding.im1)
        }
    }

    private fun checkWinOrLose() {
        if (win()) {
            lifecycleScope.launch {
                binding.tvnoMany.setText(R.string.win)
                binding.cardNoMany.visibility = View.VISIBLE
                delay(3000)
                binding.cardNoMany.visibility = View.GONE
            }
            if (hardLevel) {
                ResultFragment.count.add(ResultFragment.count1++.toString())
                ResultFragment.bet.add(betSum.toString())
                result = binding.edBet.text.toString().toInt() * 2
                ResultFragment.result.add(result.toString())
                balans += result
                binding.tvBalans.text = balans.toString()
            } else {
                ResultFragment.count.add(ResultFragment.count1++.toString())
                ResultFragment.bet.add(betSum.toString())
                result = binding.edBet.text.toString().toInt()
                ResultFragment.result.add(result.toString())
                balans += result
                binding.tvBalans.text = balans.toString()
            }
        } else {
            lifecycleScope.launch {
                binding.tvnoMany.setText(R.string.lose)
                binding.cardNoMany.visibility = View.VISIBLE
                delay(3000)
                binding.cardNoMany.visibility = View.GONE
            }
            if (hardLevel) {
                ResultFragment.count.add(ResultFragment.count1++.toString())
                ResultFragment.bet.add(betSum.toString())
                result -= betSum * 2
                ResultFragment.result.add(result.toString())
                balans -= betSum * 2
                binding.tvBalans.text = balans.toString()
            } else {
                ResultFragment.count.add(ResultFragment.count1++.toString())
                ResultFragment.bet.add(betSum.toString())
                result -= betSum
                ResultFragment.result.add(result.toString())
                balans -= betSum
                binding.tvBalans.text = balans.toString()
            }
        }
    }

    private fun setOnClickPay() {
        binding.btPay.setOnClickListener {
            if (binding.edPay.text.isEmpty()) {
                binding.cardPay.visibility = View.VISIBLE
            } else {
                balans = binding.edPay.text.toString().toInt()
                binding.tvBalans.text = binding.edPay.text.toString()
                binding.edPay.setText("")
                binding.cardPay.visibility = View.GONE
            }
        }
    }


    private fun win(): Boolean {
        return (1..2).random() == 1 && im1Pressed
    }

    companion object {
        var countRaund = 1
        var result = 0
        var imagePressed = false
        var im1Pressed = false
        var hardLevel = false
        var betSum = 0
        var balans = 0
        fun newInstance() = GameFragment()
    }
}