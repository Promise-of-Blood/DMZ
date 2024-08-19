package com.example.dmz.ui.mypage

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.request.transition.ViewPropertyTransition.Animator
import com.example.dmz.DMZApplication
import com.example.dmz.data.repository.MyPageRepositoryImpl
import com.example.dmz.databinding.FragmentMyPageBinding
import com.example.dmz.model.MyPageListItem
import com.example.dmz.viewmodel.MyPageViewModel

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val myPageAdapter by lazy { MyPageAdapter(::handleOnClickBookmarkedVideo, ::handleOnClickMoreButton) }
    private val myPageViewModel: MyPageViewModel by activityViewModels {
        viewModelFactory { initializer { MyPageViewModel(MyPageRepositoryImpl(requireActivity().application as DMZApplication)) } }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() = with(binding) {
//        rvMyPage.visibility = View.GONE
        rvMyPage.adapter = myPageAdapter
        rvMyPage.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        Handler(Looper.getMainLooper()).postDelayed({
//            rvMyPage.visibility = View.VISIBLE
//        }, 400)
    }

    private fun initViewModel() = with(myPageViewModel) {
        myPageData.observe(viewLifecycleOwner) {
            myPageAdapter.submitList(it)
        }
    }

    private fun handleOnClickBookmarkedVideo(item: MyPageListItem) {
        if (item !is MyPageListItem.Video) return
        val videoId = item.item.video?.videoId ?: ""
        val action = MyPageFragmentDirections.actionMyPageToDetail(videoId)
        findNavController().navigate(action)
    }

    private fun handleOnClickMoreButton() {
        val action = MyPageFragmentDirections.actionMyPageToBookmark()
        val navOptions = NavOptions.Builder().build()
        findNavController().navigate(action, navOptions)
    }
}