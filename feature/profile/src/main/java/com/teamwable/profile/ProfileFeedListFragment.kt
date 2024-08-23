package com.teamwable.profile

import android.os.Bundle
import com.teamwable.model.Feed
import com.teamwable.profile.databinding.FragmentProfileFeedBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.setDivider
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.shareAdapter.FeedAdapter
import com.teamwable.ui.shareAdapter.FeedClickListener
import com.teamwable.ui.util.BundleKey

class ProfileFeedListFragment : BindingFragment<FragmentProfileFeedBinding>(FragmentProfileFeedBinding::inflate) {
    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(onClickFeedItem()) }
    private lateinit var userType: ProfileUserType
    private lateinit var userNickname: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userType = setUserType()
        userNickname = arguments?.getString(BundleKey.USER_NICKNAME).orEmpty()
    }

    private fun setUserType(): ProfileUserType {
        val userId = arguments?.getLong(BundleKey.USER_ID)
        val authId: Long = 0 // TODO : authId datastore에서 가져오기
        return when (userId) {
            authId -> ProfileUserType.AUTH
            null -> ProfileUserType.EMPTY
            else -> ProfileUserType.MEMBER
        }
    }

    override fun initView() {
        setAdapter()
    }

    // TODO : test용 toast 지우기
    private fun onClickFeedItem() = object : FeedClickListener {
        override fun onItemClick(feed: Feed) {
            toast("item")
        }

        override fun onGhostBtnClick(postAuthorId: Long) {
            toast("ghost")
        }

        override fun onLikeBtnClick(id: Long) {
            toast("like")
        }

        override fun onPostAuthorProfileClick(id: Long) {
            toast("profile")
        }

        override fun onFeedImageClick(image: String) {
            toast("image")
        }

        override fun onKebabBtnClick(feedId: Long, postAuthorId: Long) {
            toast("kebab")
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
        val mock = mutableListOf<Feed>()
        repeat(5) {
            mock.add(
                Feed(
                    postAuthorId = 0,
                    postAuthorProfile = "",
                    postAuthorNickname = "페이커최고",
                    feedId = 0,
                    title = "내가 S면 넌 나의 N이 되어줘 ",
                    content = "어떤 순간에도 너를 찾을 수 있게 반대가 끌리는 천만번째 이유를 내일의 우리는 알지도 몰라 오늘따라 왠지 말이 꼬여 성을 빼고 부르는 건 아직 어색해 (지훈아..!) 너와 나 우리 모여보니까 같은 색 더듬이가 자라나 창문 너머의 춤을 따라 \n" +
                        "https://www.wable.com",
                    uploadTime = "5",
                    isPostAuthorGhost = false,
                    postAuthorGhost = 100,
                    isLiked = false,
                    likedNumber = "100",
                    commentNumber = "200",
                    image = "https://github.com/user-attachments/assets/66fdd6f1-c0c5-4438-81f4-bea09b09acd1",
                    postAuthorTeamTag = "T1",
                ),
            )
        }
        if (mock.isEmpty()) {
            setEmptyView(true)
        } else {
            setEmptyView(false)
            feedAdapter.submitList(mock)
        }
    }

    private fun setEmptyView(isEmpty: Boolean) = with(binding) {
        when (userType) {
            ProfileUserType.AUTH -> {
                tvProfileFeedAuthEmptyLabel.text = getString(R.string.label_profile_feed_auth_empty, userNickname)
                groupAuthEmpty.visible(isEmpty)
            }

            ProfileUserType.MEMBER -> {
                tvProfileFeedMemberEmpty.text = getString(R.string.label_profile_feed_member_empty, userNickname)
                tvProfileFeedMemberEmpty.visible(isEmpty)
            }

            ProfileUserType.EMPTY -> return
        }
    }

    companion object {
        fun newInstance(userId: Long, nickName: String): ProfileFeedListFragment {
            return ProfileFeedListFragment().apply {
                arguments = Bundle().apply {
                    putLong(BundleKey.USER_ID, userId)
                    putString(BundleKey.USER_NICKNAME, nickName)
                }
            }
        }
    }
}
