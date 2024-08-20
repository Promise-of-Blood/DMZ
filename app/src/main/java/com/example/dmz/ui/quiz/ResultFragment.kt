package com.example.dmz.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        initView()
    }

    private fun initView() = with(binding) {
        val isPassedQuiz = args.score >= 2
        tvQuizResult.text = highlightKeyword(
            getString(R.string.quiz_result, args.score),
            "${args.score}문제",
            requireContext().getColor(if (isPassedQuiz) R.color.sky_blue else R.color.pink)
        )
        tvQuizResultButton.text =
            getString(if (isPassedQuiz) R.string.quiz_button_collect else R.string.quiz_button_back)
        tvQuizResultButton.setOnClickListener {
            if (isPassedQuiz) {
                val action = ResultFragmentDirections.actionQuizResultToMyPage()
                findNavController().navigate(action)
            } else {
                val action = ResultFragmentDirections.actionQuizResultToQuizStart()
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.navigation_quiz, true).build()
                findNavController().navigate(action, navOptions)
            }
        }
        ivQuizResultKeywordImage.setImageResource(args.keyword.keyImage)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}