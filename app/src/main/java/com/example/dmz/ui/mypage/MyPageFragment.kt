package com.example.dmz.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dmz.R
import com.example.dmz.databinding.FragmentMyPageBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val myPageAdapter by lazy { MyPageAdapter().apply { submitList(getMyPageList()) } }

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
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
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
        rvMyPage.adapter = myPageAdapter
        rvMyPage.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getMyPageList(): List<MyPageListItem> {
        return listOf<MyPageListItem>(
            MyPageListItem.Profile(
                R.drawable.ic_launcher_background,
                "김태영",
                7,
                "여",
                "2024-08-05T01:01:01Z"
            ),
            MyPageListItem.Header("Card collection", false),
            MyPageListItem.CardList(
                listOf(
                    MyPageListItem.Card(R.drawable.ic_launcher_background, "요아정"),
                    MyPageListItem.Card(R.drawable.ic_launcher_background, "요아정"),
                    MyPageListItem.Card(R.drawable.ic_launcher_background, "요아정"),
                )
            ),
            MyPageListItem.Header("Bookmark", true),
            MyPageListItem.Video(
                "https://picsum.photos/200/300",
                "티모로 다 해줬잖아",
                "https://picsum.photos/200",
                "Untara",
                "100000",
                "2024-08-13T01:01:01Z"
            ),
        )
    }
}