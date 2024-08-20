package com.example.dmz.ui.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.dmz.DMZApplication
import com.example.dmz.MainActivity
import com.example.dmz.R
import com.example.dmz.data.repository.MyPageRepositoryImpl
import com.example.dmz.data.repository.QuizRepositoryImpl
import com.example.dmz.databinding.FragmentStartBinding
import com.example.dmz.viewmodel.MyPageViewModel
import com.example.dmz.viewmodel.QuizViewModel

class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private val quizRepository = QuizRepositoryImpl()
    private val quizViewModel: QuizViewModel by activityViewModels()
    private val myPageViewModel: MyPageViewModel by activityViewModels {
        viewModelFactory { initializer { MyPageViewModel(MyPageRepositoryImpl(requireActivity().application as DMZApplication)) } }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as MainActivity).handleQuizNavigation()
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
//        handleIsCompleted()
        quizRepository.shuffleQuizList()
        initView()
        handleIsCollected()
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

    private fun handleIsCompleted() {
        val isCompleted = quizViewModel.isCompleted.value ?: false

        if (isCompleted) {
            val action =
                StartFragmentDirections.actionQuizStartToQuizResult(quizRepository.getTodayQuiz().keyword)
            val navOptions =
                NavOptions.Builder().setPopUpTo(R.id.navigation_quiz_result, true).build()
            findNavController().navigate(action, navOptions)
        } else quizViewModel.clearAnswers()
    }

    private fun handleIsCollected() {
        val isCollected =
            myPageViewModel.keywordCardList.value?.any { it.keyword == quizRepository.getTodayQuiz().keyword.keyText }
                ?: false

        if (isCollected) {
            binding.tvQuizStartButton.text = "이미 수집한 키워드입니다."
            binding.tvQuizStartButton.setOnClickListener {}
            binding.tvQuizStartButton.setTextColor(requireContext().getColor(R.color.gray))
            binding.tvQuizStartButton.backgroundTintList =
                requireContext().getColorStateList(R.color.gray)
        } else quizViewModel.clearAnswers()
    }
}