package com.example.dmz.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.dmz.R
import com.example.dmz.data.repository.QuizRepositoryImpl
import com.example.dmz.databinding.FragmentQuestionBinding
import com.example.dmz.utils.Util.highlightKeyword
import com.example.dmz.viewmodel.QuizViewModel

private const val ARG_POSITION = "position"

class QuestionFragment : Fragment() {
    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    private var position: Int = 0
    private val quizViewModel: QuizViewModel by activityViewModels()
    private val todayQuiz = QuizRepositoryImpl().getTodayQuiz()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { position = it.getInt(ARG_POSITION) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
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
        val currentQuestion = todayQuiz.questions[position]

        tvQuizQuestion.text = highlightKeyword(
            currentQuestion.question,
            todayQuiz.keyword.keyText,
            requireContext().getColor(R.color.sky_blue)
        )

        val answerRadioGroup = listOf(rbQuizAnswer1, rbQuizAnswer2, rbQuizAnswer3)
        currentQuestion.answers.forEachIndexed { index, answer ->
            answerRadioGroup[index].text = answer
        }
        rgQuizAnswer.setOnCheckedChangeListener { _, checkedId ->
            val selectedAnswer = answerRadioGroup.indexOfFirst { checkedId == it.id }
            quizViewModel.submitAnswer(position, selectedAnswer)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }
    }
}