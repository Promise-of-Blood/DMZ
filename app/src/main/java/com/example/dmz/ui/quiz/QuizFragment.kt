package com.example.dmz.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dmz.R
import com.example.dmz.data.CacheDataSource
import com.example.dmz.data.repository.QuizRepositoryImpl
import com.example.dmz.databinding.FragmentQuizBinding
import com.example.dmz.viewmodel.QuizViewModel

class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val quizViewModel: QuizViewModel by activityViewModels()
    private val quizRepository = QuizRepositoryImpl(CacheDataSource.getCacheDataSource())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
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
        vpQuiz.adapter = ViewPagerAdapter(requireActivity(), 3)
        vpQuiz.isUserInputEnabled = false
        vpQuiz.offscreenPageLimit = 2
        tvQuizNextBtn.text =
            getString(if (vpQuiz.currentItem == 2) R.string.quiz_button_submit else R.string.quiz_button_next)
        tvQuizNextBtn.setOnClickListener { moveToNextQuestion() }
    }

    private fun moveToNextQuestion() = with(binding) {
        val nextItem = vpQuiz.currentItem + 1
        val selectedAnswer = quizViewModel.getSubmittedAnswer(vpQuiz.currentItem)

        if (nextItem > 2) {
            val score = getScore()
            if (score == -1) {
                Toast.makeText(requireContext(), "정답을 처리하지 못했습니다.", Toast.LENGTH_SHORT).show()
                vpQuiz.currentItem = 0
                findNavController().popBackStack()
            }
            val keyword = quizRepository.getTodayQuiz().keyword
            val action =
                QuizFragmentDirections.actionQuizQuestionToNavigationQuizResult(score, keyword)
            findNavController().navigate(action)
        } else {
            if (selectedAnswer != -1) vpQuiz.currentItem = nextItem
            else Toast.makeText(requireContext(), "정답을 선택해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getScore(): Int {
        val correctAnswers = quizRepository.getTodayQuizAnswer()
        val submittedAnswers = quizViewModel.answer.value
        return submittedAnswers?.count { it.value == correctAnswers[it.key] } ?: -1
    }
}