package com.example.dmz.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.dmz.DMZApplication
import com.example.dmz.R
import com.example.dmz.data.CacheDataSource
import com.example.dmz.data.repository.MyPageRepositoryImpl
import com.example.dmz.data.repository.QuizRepositoryImpl
import com.example.dmz.databinding.FragmentQuizBinding
import com.example.dmz.model.KeywordCard
import com.example.dmz.viewmodel.MyPageViewModel
import com.example.dmz.viewmodel.QuizViewModel
import java.util.UUID

class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val quizViewModel: QuizViewModel by activityViewModels()
    private val quizRepository = QuizRepositoryImpl(CacheDataSource.getCacheDataSource())
    private val myPageViewModel: MyPageViewModel by activityViewModels {
        viewModelFactory { initializer { MyPageViewModel(MyPageRepositoryImpl(requireActivity().application as DMZApplication)) } }
    }

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
            when (val score = getScore()) {
                -1 -> {
                    Toast.makeText(requireContext(), "정답을 처리하지 못했습니다.", Toast.LENGTH_SHORT).show()
                    vpQuiz.currentItem = 0
                    quizViewModel.clearAnswers()
                    findNavController().popBackStack()
                }

                else -> {
                    val keyword = quizRepository.getTodayQuiz().keyword
                    if (score >= 2) {
                        myPageViewModel.addKeywordCard(
                            KeywordCard(
                                id = UUID.randomUUID().toString(),
                                keyword = keyword.keyText,
                                thumbnail = keyword.keyImage
                            )
                        )
                    }
                    val action =
                        QuizFragmentDirections.actionQuizQuestionToNavigationQuizResult(
                            score,
                            keyword
                        )
                    val navOptions = NavOptions.Builder().setPopUpTo(R.id.navigation_quiz, true).build()
                    quizViewModel.clearAnswers()
                    findNavController().navigate(action, navOptions)
                }
            }
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