package com.example.marvelapp.presentation.characters.adapters

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class CharactersLoadMoreStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CharactersRefreshStateViewHolder>() {

    override fun onBindViewHolder(
        holder: CharactersRefreshStateViewHolder,
        loadState: LoadState) = holder.bind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = CharactersRefreshStateViewHolder.create(parent, retry)

}