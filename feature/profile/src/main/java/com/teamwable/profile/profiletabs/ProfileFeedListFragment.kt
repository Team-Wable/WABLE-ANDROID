package com.teamwable.profile.profiletabs

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import com.teamwable.model.Feed
import com.teamwable.model.Ghost
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileFeedBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.component.Snackbar
import com.teamwable.ui.extensions.DeepLinkDestination
import com.teamwable.ui.extensions.deepLinkNavigateTo
import com.teamwable.ui.extensions.setDivider
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.viewLifeCycle
import com.teamwable.ui.extensions.viewLifeCycleScope
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.shareAdapter.FeedAdapter
import com.teamwable.ui.shareAdapter.FeedClickListener
import com.teamwable.ui.type.AlarmTriggerType
import com.teamwable.ui.type.DialogType
import com.teamwable.ui.type.ProfileUserType
import com.teamwable.ui.type.SnackbarType
import com.teamwable.ui.util.Arg.FEED_ID
import com.teamwable.ui.util.BundleKey
import com.teamwable.ui.util.FeedActionHandler
import com.teamwable.ui.util.FeedTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFeedListFragment : BindingFragment<FragmentProfileFeedBinding>(FragmentProfileFeedBinding::inflate) {
    private val viewModel: ProfileFeedListViewModel by viewModels()
    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(onClickFeedItem()) }
    private lateinit var userType: ProfileUserType
    private lateinit var userNickname: String
    private var userId: Long = -1
    private lateinit var feedActionHandler: FeedActionHandler

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
            @Suppress("DEPRECATION")
            arguments?.getSerializable(BundleKey.USER_TYPE) as? ProfileUserType
        } ?: ProfileUserType.EMPTY
    }

    override fun initView() {
        feedActionHandler = FeedActionHandler(requireContext(), findNavController(), requireParentFragment().parentFragmentManager, viewLifecycleOwner)
        setAdapter()
        collect()
    }

    private fun collect() {
        viewLifeCycleScope.launch {
            viewModel.uiState.flowWithLifecycle(viewLifeCycle).collect { uiState ->
                when (uiState) {
                    else -> Unit
                }
            }
        }

        viewLifeCycleScope.launch {
            viewModel.event.flowWithLifecycle(viewLifeCycle).collect { sideEffect ->
                when (sideEffect) {
                    is ProfileFeedSideEffect.DismissBottomSheet -> findNavController().popBackStack()
                    is ProfileFeedSideEffect.ShowSnackBar -> parentFragment?.let { Snackbar.make(it.view ?: return@let, SnackbarType.GHOST).show() }
                }
            }
        }
    }

    private fun onClickFeedItem() = object : FeedClickListener {
        override fun onItemClick(feed: Feed) {
            findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.HomeDetail, mapOf(FEED_ID to feed.feedId))
        }

        override fun onGhostBtnClick(postAuthorId: Long, feedId: Long) {
            feedActionHandler.onGhostBtnClick(DialogType.TRANSPARENCY) {
                viewModel.updateGhost(Ghost(stringOf(AlarmTriggerType.CONTENT.type), postAuthorId, feedId))
            }
        }

        override fun onLikeBtnClick(id: Long) {
            toast("like")
        }

        override fun onPostAuthorProfileClick(id: Long) {}

        override fun onFeedImageClick(image: String) {
            feedActionHandler.onImageClick(image)
        }

        override fun onKebabBtnClick(feedId: Long, postAuthorId: Long) {
            feedActionHandler.onKebabBtnClick(
                feedId,
                postAuthorId,
                fetchUserType = { userType },
                removeFeed = { viewModel.removeFeed(it) },
                parentFragment?.view ?: return,
            )
        }

        override fun onCommentBtnClick(feedId: Long) {}
    }

    private fun setAdapter() {
        binding.rvProfileFeed.apply {
            adapter = feedAdapter
            if (itemDecorationCount == 0) setDivider(com.teamwable.ui.R.drawable.recyclerview_item_1_divider)
        }
        submitList()
    }

    private fun submitList() {
        viewLifeCycleScope.launch {
            viewModel.updateFeeds(userId).collectLatest { pagingData ->
                val transformedPagingData = pagingData.map { FeedTransformer.handleFeedsData(it, binding.root.context) }
                feedAdapter.submitData(transformedPagingData)
            }
        }

        viewLifeCycleScope.launch {
            feedAdapter.loadStateFlow.collectLatest { loadStates ->
                val isEmptyList = loadStates.refresh is LoadState.NotLoading && feedAdapter.itemCount == 0
                setEmptyView(isEmptyList)
            }
        }
    }

    private fun setEmptyView(isEmpty: Boolean) = with(binding) {
        when (userType) {
            ProfileUserType.AUTH -> {
                tvProfileFeedAuthEmptyLabel.text = getString(R.string.label_profile_feed_auth_empty, userNickname)
                groupAuthEmpty.visible(isEmpty)
                initNavigateToPostingBtnClickListener()
            }

            ProfileUserType.MEMBER -> {
                tvProfileFeedMemberEmpty.text = getString(R.string.label_profile_feed_member_empty, userNickname)
                tvProfileFeedMemberEmpty.visible(isEmpty)
            }

            ProfileUserType.EMPTY -> return
        }
    }

    private fun initNavigateToPostingBtnClickListener() {
        binding.btnProfileFeedAuthNavigatePosting.setOnClickListener {
            findNavController().deepLinkNavigateTo(requireContext(), DeepLinkDestination.Posting)
        }
    }

    companion object {
        fun newInstance(userId: Long, nickName: String, type: ProfileUserType): ProfileFeedListFragment {
            return ProfileFeedListFragment().apply {
                arguments = Bundle().apply {
                    putLong(BundleKey.USER_ID, userId)
                    putString(BundleKey.USER_NICKNAME, nickName)
                    putSerializable(BundleKey.USER_TYPE, type)
                }
            }
        }
    }
}
