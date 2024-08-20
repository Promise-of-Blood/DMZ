package com.example.dmz.ui.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.dmz.R
import com.example.dmz.data.CacheDataSource
import com.example.dmz.data.repository.QuizRepositoryImpl
import com.example.dmz.databinding.FragmentStartBinding
import com.example.dmz.viewmodel.QuizViewModel

class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private val quizRepository = QuizRepositoryImpl(CacheDataSource.getCacheDataSource())
    private val quizViewModel: QuizViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val isCompleted = quizViewModel.isCompleted.value ?: false
        if (isCompleted) {
            val action =
                StartFragmentDirections.actionQuizStartToQuizResult(quizRepository.getTodayQuiz().keyword)
            val navOptions =
                NavOptions.Builder().setPopUpTo(R.id.navigation_quiz_result, true).build()
            findNavController().navigate(action, navOptions)
        } else quizViewModel.clearAnswers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() = with(binding) {
        tvQuizStartTitle.text = quizRepository.getTodayQuiz().keyword.keyText
        tvQuizStartButton.setOnClickListener { startQuiz() }
    }

    private fun startQuiz() {
        val action = StartFragmentDirections.actionQuizStartToQuizQuestion()
        findNavController().navigate(action)
    }
}