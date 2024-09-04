package com.teamwable.profile.profiletabs

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import com.teamwable.model.Comment
import com.teamwable.model.Ghost
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileCommentBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.getSerializableCompat
import com.teamwable.ui.extensions.setDivider
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.shareAdapter.CommentAdapter
import com.teamwable.ui.shareAdapter.CommentClickListener
import com.teamwable.ui.type.AlarmTriggerType
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.util.Arg.FEED_ID
import com.teamwable.ui.util.BundleKey
import com.teamwable.ui.util.CommentActionHandler
import com.teamwable.ui.util.FeedTransformer
import com.teamwable.ui.util.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileCommentListFragment : BindingFragment<FragmentProfileCommentBinding>(FragmentProfileCommentBinding::inflate) {
    private val viewModel: ProfileCommentListViewModel by viewModels()
    private val commentAdapter: CommentAdapter by lazy { CommentAdapter(onClickCommentItem()) }
    private lateinit var userType: ProfileUserType
    private lateinit var userNickname: String
    private var userId: Long = -1
    private lateinit var commentActionHandler: CommentActionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userType = setUserType()
        userNickname = arguments?.getString(BundleKey.USER_NICKNAME).orEmpty()
        userId = arguments?.getLong(BundleKey.USER_ID) ?: -1
    }

    private fun setUserType() = arguments?.getSerializableCompat(BundleKey.USER_TYPE, ProfileUserType::class.java) ?: ProfileUserType.EMPTY

    override fun initView() {
        commentActionHandler = CommentActionHandler(requireContext(), findNavController(), requireParentFragment().parentFragmentManager, viewLifecycleOwner)
        collect()
        setAdapter()
    }

    private fun collect() {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    is ProfileCommentUiState.Error -> (activity as Navigation).navigateToErrorFragment()
                    else -> Unit
                }
            }
        }

        viewLifeCycleScope.launch {
            viewModel.event.flowWithLifecycle(viewLifeCycle).collect { sideEffect ->
                when (sideEffect) {
                    is ProfileCommentSideEffect.DismissBottomSheet -> findNavController().popBackStack()
                    is ProfileCommentSideEffect.ShowSnackBar -> parentFragment?.let { Snackbar.make(it.view ?: return@let, sideEffect.type).show() }
                }
            }
        }
    }

    private fun onClickCommentItem() = object : CommentClickListener {
        override fun onGhostBtnClick(postAuthorId: Long, commentId: Long) {
            commentActionHandler.onGhostBtnClick(DialogType.TRANSPARENCY) {
                viewModel.updateGhost(Ghost(stringOf(AlarmTriggerType.COMMENT.type), postAuthorId, commentId))
            }
        }

        override fun onLikeBtnClick(id: Long) {
            toast("commentlike")
        }

        override fun onPostAuthorProfileClick(id: Long) {}

        override fun onKebabBtnClick(comment: Comment) {
            commentActionHandler.onKebabBtnClick(
                comment,
                fetchUserType = { userType },
                removeComment = { viewModel.removeComment(it) },
                reportUser = { nickname, content -> viewModel.reportUser(nickname, content) },
            )
        }

        override fun onItemClick(feedId: Long) {
            findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.HomeDetail, mapOf(FEED_ID to feedId))
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
                val transformedPagingData = pagingData.map { FeedTransformer.handleCommentsData(it, binding.root.context) }
                commentAdapter.submitData(transformedPagingData)
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
