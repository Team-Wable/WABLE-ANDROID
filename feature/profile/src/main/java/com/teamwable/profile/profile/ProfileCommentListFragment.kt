package com.teamwable.profile.profile

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.teamwable.profile.ProfileUiState
import com.teamwable.profile.ProfileViewModel
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileCommentBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.setDivider
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.shareAdapter.CommentAdapter
import com.teamwable.ui.shareAdapter.CommentClickListener
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.util.BundleKey
import com.teamwable.ui.util.CommentActionHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileCommentListFragment : BindingFragment<FragmentProfileCommentBinding>(FragmentProfileCommentBinding::inflate) {
    private val commentAdapter: CommentAdapter by lazy { CommentAdapter(onClickCommentItem()) }
    private lateinit var userType: ProfileUserType
    private lateinit var userNickname: String
    private var userId: Long = -1
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var commentActionHandler: CommentActionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userType = setUserType()
        userNickname = arguments?.getString(BundleKey.USER_NICKNAME).orEmpty()
        userId = arguments?.getLong(BundleKey.USER_ID) ?: -1
    }

    private fun setUserType(): ProfileUserType {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(BundleKey.USER_TYPE, ProfileUserType::class.java)
        } else {
            arguments?.getSerializable(BundleKey.USER_TYPE) as? ProfileUserType
        } ?: ProfileUserType.EMPTY
    }

    override fun initView() {
        commentActionHandler = CommentActionHandler(requireContext(), findNavController(), requireParentFragment().parentFragmentManager, viewLifecycleOwner)
        collect()
        setAdapter()
    }

    private fun collect() {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    is ProfileUiState.RemoveComment -> {
                        findNavController().popBackStack()
                        commentAdapter.removeComment(uiState.commentId)
                    }

                    else -> Unit
                }
            }
        }
    }

    // TODO : test용 toast 지우기
    private fun onClickCommentItem() = object : CommentClickListener {
        override fun onGhostBtnClick(postAuthorId: Long) {
            toast("commentghost")
        }

        override fun onLikeBtnClick(id: Long) {
            toast("commentlike")
        }

        override fun onPostAuthorProfileClick(id: Long) {
            toast("commentprofile")
        }

        override fun onKebabBtnClick(feedId: Long, postAuthorId: Long) {
            commentActionHandler.onKebabBtnClick(
                feedId,
                postAuthorId,
                fetchUserType = { userType },
                removeComment = { viewModel.removeComment(it) },
            )
        }
    }

    private fun setAdapter() {
        binding.rvProfileComment.apply {
            adapter = commentAdapter
            if (itemDecorationCount == 0) setDivider(com.teamwable.ui.R.drawable.recyclerview_item_1_divider)
        }
        submitList()
    }

    private fun submitList() {
        viewLifeCycleScope.launch {
            viewModel.updateComments(userId).collectLatest { pagingData ->
                commentAdapter.submitData(pagingData)
            }
        }

        viewLifeCycleScope.launch {
            commentAdapter.loadStateFlow.collectLatest { loadStates ->
                val isEmptyList = loadStates.refresh is LoadState.NotLoading && commentAdapter.itemCount == 0
                setEmptyView(isEmptyList)
            }
        }
    }

    private fun setEmptyView(isEmpty: Boolean) = with(binding) {
        when (userType) {
            ProfileUserType.AUTH -> tvProfileCommentAuthEmptyLabel.visible(isEmpty)

            ProfileUserType.MEMBER -> {
                tvProfileCommentMemberEmptyLabel.text = getString(R.string.label_profile_comment_member_empty, userNickname)
                tvProfileCommentMemberEmptyLabel.visible(isEmpty)
            }

            ProfileUserType.EMPTY -> return
        }
    }

    companion object {
        fun newInstance(userId: Long, nickName: String, type: ProfileUserType): ProfileCommentListFragment {
            return ProfileCommentListFragment().apply {
                arguments = Bundle().apply {
                    putLong(BundleKey.USER_ID, userId)
                    putString(BundleKey.USER_NICKNAME, nickName)
                    putSerializable(BundleKey.USER_TYPE, type)
                }
            }
        }
    }
}
