package com.teamwable.home

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.teamwable.data.repository.CommentRepository
import com.teamwable.model.Comment
import com.teamwable.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeDetailViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
) : ViewModel() {
    fun updateComments(feedId: Long): Flow<PagingData<Comment>> = commentRepository.getHomeDetailComments(feedId)
}

sealed interface HomeDetailUiState {
    data object Loading : HomeDetailUiState

    data class Success(val profile: Profile) : HomeDetailUiState

    data class RemoveFeed(val feedId: Long) : HomeDetailUiState

    data class Error(val errorMessage: String) : HomeDetailUiState
}
