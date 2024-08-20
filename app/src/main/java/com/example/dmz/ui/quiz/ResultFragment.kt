package com.example.dmz.ui.quiz

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dmz.DMZApplication
import com.example.dmz.R
import com.example.dmz.data.CacheDataSource
import com.example.dmz.data.repository.MyPageRepositoryImpl
import com.example.dmz.data.repository.QuizRepositoryImpl
import com.example.dmz.databinding.FragmentResultBinding
import com.example.dmz.model.KeywordCard
import com.example.dmz.utils.Util.highlightKeyword
import com.example.dmz.viewmodel.MyPageViewModel
import com.example.dmz.viewmodel.QuizViewModel
import java.util.UUID

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val args: ResultFragmentArgs by navArgs()
    private val quizViewModel: QuizViewModel by activityViewModels()
    private val quizRepository = QuizRepositoryImpl(CacheDataSource.getCacheDataSource())
    private val myPageViewModel: MyPageViewModel by activityViewModels {
        viewModelFactory { initializer { MyPageViewModel(MyPageRepositoryImpl(requireActivity().application as DMZApplication)) } }
    }
    private var score = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        score = getScore()
        quizViewModel.setIsCompleted(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (score >= 2) initSuccessView() else initFailView()
    }

    private fun initSuccessView() = with(binding) {
        tvQuizResult.text = highlightKeyword(
            getString(R.string.quiz_result, score),
            "${score}문제",
            requireContext().getColor(R.color.sky_blue)
        )
        tvQuizResultButton.text = getString(R.string.quiz_button_collect)
        tvQuizResultButton.setOnClickListener {
            val action = ResultFragmentDirections.actionQuizResultToMyPage()
            val navOptions =
                NavOptions.Builder().setPopUpTo(R.id.navigation_quiz, true).build()
            findNavController().navigate(action, navOptions)
        }
        ivQuizResultKeywordImage.setImageResource(args.keyword.keyImage)
    }

    private fun initFailView() = with(binding) {
        tvQuizResult.text = highlightKeyword(
            getString(R.string.quiz_result, score),
            "${score}문제",
            requireContext().getColor(R.color.pink)
        )
        tvQuizResultButton.text = getString(R.string.quiz_button_back)
        tvQuizResultButton.setOnClickListener {
            val action = ResultFragmentDirections.actionQuizResultToHome()
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

    private fun getScore(): Int {
        val correctAnswers = quizRepository.getTodayQuizAnswer()
        val submittedAnswers = quizViewModel.answer.value
        val score = submittedAnswers?.count { it.value == correctAnswers[it.key] } ?: -1
        if (score >= 2) collectKeyword()
        return score
    }

    private fun collectKeyword() {
        val keyword = args.keyword
        myPageViewModel.addKeywordCard(
            KeywordCard(
                id = UUID.randomUUID().toString(),
                keyword = keyword.keyText,
                thumbnail = keyword.keyImage
            )
        )
    }

    private fun ImageView.setGrayScaleFilter() {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(matrix)
        this.colorFilter = filter
    }
}