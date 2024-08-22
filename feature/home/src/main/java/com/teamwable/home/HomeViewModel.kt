package com.teamwable.home

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.teamwable.data.repository.FeedRepository
import com.teamwable.model.Feed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
) : ViewModel() {
    fun updateFeeds(): Flow<PagingData<Feed>> = feedRepository.getHomeFeeds(20)
}
