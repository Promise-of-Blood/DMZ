package com.example.dmz.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dmz.DMZApplication
import com.example.dmz.MainActivity
import com.example.dmz.data.repository.MyPageRepositoryImpl
import com.example.dmz.databinding.FragmentBookmarkBinding
import com.example.dmz.model.MyPageListItem
import com.example.dmz.utils.Util.handleBottomNavigationVisibility
import com.example.dmz.viewmodel.MyPageViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val bookmarkAdapter by lazy { MyPageAdapter(::handleOnClickBookmarkedVideo) {} }
    private val bookmarkViewModel: MyPageViewModel by activityViewModels {
        viewModelFactory { initializer { MyPageViewModel(MyPageRepositoryImpl(requireActivity().application as DMZApplication)) } }
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        (activity as MainActivity).handleBottomNavigationVisibility(true)
    }

    private fun initView() = with(binding) {
        (activity as MainActivity).handleBottomNavigationVisibility(false)
        rvBookmark.adapter = bookmarkAdapter
        rvBookmark.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        ivBookmarkBackButton.setOnClickListener { findNavController().popBackStack() }
    }

    private fun initViewModel() = with(bookmarkViewModel) {
        bookmarkList.observe(viewLifecycleOwner) {
            val list = it.map { video -> MyPageListItem.Video(video) }
            bookmarkAdapter.submitList(list)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookmarkFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookmarkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun handleOnClickBookmarkedVideo(item: MyPageListItem, sharedElement: View) {
        if (item !is MyPageListItem.Video) return
        val videoId = item.item.video?.videoId ?: "null"
        val thumbnail = item.item.video?.thumbnail ?: "null"
        val action = BookmarkFragmentDirections.actionBookmarkToDetail(videoId, thumbnail)
        val extras = FragmentNavigatorExtras(
            sharedElement to "thumbnail_$videoId"
        )
        findNavController().navigate(action, extras)
    }
}