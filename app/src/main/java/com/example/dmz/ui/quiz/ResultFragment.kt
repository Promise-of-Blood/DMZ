package com.example.dmz.ui.quiz

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dmz.R
import com.example.dmz.databinding.FragmentResultBinding
import com.example.dmz.utils.Util.highlightKeyword

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.score >= 2) initSuccessView() else initFailView()
    }

    private fun initSuccessView() = with(binding) {
        tvQuizResult.text = highlightKeyword(
            getString(R.string.quiz_result, args.score),
            "${args.score}문제",
            requireContext().getColor(R.color.sky_blue)
        )
        tvQuizResultButton.text = getString(R.string.quiz_button_collect)
        tvQuizResultButton.setOnClickListener {
            val action = ResultFragmentDirections.actionQuizResultToMyPage()
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.navigation_quiz_result, true).build()
            findNavController().navigate(action, navOptions)
        }
        ivQuizResultKeywordImage.setImageResource(args.keyword.keyImage)
    }

    private fun initFailView() = with(binding) {
        tvQuizResult.text = highlightKeyword(
            getString(R.string.quiz_result, args.score),
            "${args.score}문제",
            requireContext().getColor(R.color.pink)
        )
        tvQuizResultButton.text = getString(R.string.quiz_button_back)
        tvQuizResultButton.setOnClickListener {
            val action = ResultFragmentDirections.actionQuizResultToQuizStart()
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.navigation_quiz, true).build()
            findNavController().navigate(action, navOptions)
        }
        ivQuizResultKeywordImage.setImageResource(args.keyword.keyImage)
        ivQuizResultKeywordImage.setGrayScaleFilter()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun ImageView.setGrayScaleFilter() {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(matrix)
        this.colorFilter = filter
    }
}