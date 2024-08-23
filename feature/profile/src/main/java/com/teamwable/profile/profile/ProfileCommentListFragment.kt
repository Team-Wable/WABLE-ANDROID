package com.teamwable.profile.profile

import android.os.Bundle
import com.teamwable.model.Comment
import com.teamwable.profile.ProfileUserType
import com.teamwable.profile.R
import com.teamwable.profile.databinding.FragmentProfileCommentBinding
import com.teamwable.ui.base.BindingFragment
import com.teamwable.ui.extensions.setDivider
import com.teamwable.ui.extensions.toast
import com.teamwable.ui.extensions.visible
import com.teamwable.ui.shareAdapter.CommentAdapter
import com.teamwable.ui.shareAdapter.CommentClickListener
import com.teamwable.ui.util.BundleKey

class ProfileCommentListFragment : BindingFragment<FragmentProfileCommentBinding>(FragmentProfileCommentBinding::inflate) {
    private val commentAdapter: CommentAdapter by lazy { CommentAdapter(onClickCommentItem()) }
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
            toast("commentkebab")
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
        val mock = mutableListOf<Comment>()
        repeat(5) {
            mock.add(
                Comment(
                    postAuthorId = 0,
                    postAuthorProfile = "",
                    postAuthorNickname = "페이커최고",
                    commentId = 0,
                    content = "어떤 순간에도 너를 찾을 수 있게 반대가 끌리는 천만번째 이유를 내일의 우리는 알지도 몰라 오늘따라 왠지",
                    uploadTime = "5",
                    isPostAuthorGhost = false,
                    postAuthorGhost = 100,
                    isLiked = false,
                    likedNumber = "100",
                    postAuthorTeamTag = "T1",
                ),
            )
        }
        if (mock.isEmpty()) {
            setEmptyView(true)
        } else {
            commentAdapter.submitList(mock)
            setEmptyView(false)
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
        fun newInstance(userId: Long, nickName: String): ProfileCommentListFragment {
            return ProfileCommentListFragment().apply {
                arguments = Bundle().apply {
                    putLong(BundleKey.USER_ID, userId)
                    putString(BundleKey.USER_NICKNAME, nickName)
                }
            }
        }
    }
}
