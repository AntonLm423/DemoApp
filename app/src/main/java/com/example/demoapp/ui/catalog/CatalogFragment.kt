package com.example.demoapp.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoapp.R
import com.example.demoapp.data.remote.Response
import com.example.demoapp.databinding.FragmentCatalogBinding
import com.example.demoapp.extensions.stringOrEmpty
import com.example.demoapp.ui.base.BaseFragment
import com.example.demoapp.ui.base.paging.PagingLoadStateAdapter
import com.example.demoapp.ui.util.SnackbarManager.Companion.createSnackbar

class CatalogFragment : BaseFragment() {

    override val destinationId = R.id.catalogFragment

    private val binding get() = viewBinding as FragmentCatalogBinding

    private val catalogViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CatalogViewModel::class]
    }

    private val moviesAdapter by lazy {
        MoviesAdapter(onMovieInteractionListener).apply {
            this.addLoadStateListener { loadState -> catalogViewModel.bindPagingState(loadState) }
        }
    }

    private val adapter by lazy {
        moviesAdapter.withLoadStateFooter(footer = PagingLoadStateAdapter { moviesAdapter.retry() })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) {
        updateState(DisplayedState.Initial())
        initSearch()
        initRecyclerView()
    }

    override fun onSubscribeViewModel() {
        catalogViewModel.movies.observe { pagingData ->
            moviesAdapter.submitData(lifecycle, pagingData)
        }

        catalogViewModel.state.observe { result ->
            when (result) {
                is Response.Success -> {
                    handleSuccessState(isEndOfPaging = result.data)
                }

                is Response.Error -> {
                    handleErrorState()
                }

                is Response.Loading -> {
                    updateState(DisplayedState.Loading())
                }

                else -> {
                }
            }
        }
    }

    private fun initSearch() = with(binding.editTextSearch) {
        catalogViewModel.searchQuery?.let { setText(it) }

        doAfterTextChanged { text ->
            catalogViewModel.onSearchQueryChanged(text.stringOrEmpty())
        }

        setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                catalogViewModel.searchMovies(
                    query = text.stringOrEmpty(),
                    isForce = true
                )
                true
            } else {
                false
            }
        }
    }

    /**
     * Main recycler view
     */
    private fun initRecyclerView() = with(binding.recyclerViewMovies) {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        itemAnimator = null
        adapter = this@CatalogFragment.adapter
        if (itemDecorationCount == 0) {
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    private val onMovieInteractionListener = object : MoviesAdapter.OnItemInteractionListener {
        override fun updateFavoriteStatus(id: Int, inFavorite: Boolean) {
            if (inFavorite) {
                preferenceStorage.addToLocalFavoriteMovies(id)
            } else {
                preferenceStorage.removeFromLocalFavoriteMovies(id)
            }
        }
    }

    /**
     * Displayed state logic
     */
    private fun updateState(newState: DisplayedState) = with(binding) {
        when (newState) {
            is DisplayedState.Initial -> textViewEmptyDescription.text = getString(R.string.catalog_initial_description)
            is DisplayedState.Empty -> textViewEmptyDescription.text = getString(R.string.catalog_nothing_found)
            else -> Unit
        }

        viewFlipperState.displayedChild = newState.viewFlipperChild
    }

    private fun handleSuccessState(isEndOfPaging: Boolean) {
        val isEmpty = moviesAdapter.itemCount == 0 && isEndOfPaging
        updateState(
            when {
                isEmpty && catalogViewModel.searchQuery.isNullOrBlank() -> DisplayedState.Initial()
                isEmpty -> DisplayedState.Empty()
                else -> DisplayedState.Success()
            }
        )
    }

    private fun handleErrorState() {
        updateState(DisplayedState.Initial())
        snackbarManager.addToQueue(
            createSnackbar(
                view = binding.root,
                titleResId = R.string.common_error_title,
                messageResId = R.string.common_error_description
            )
        )
    }

    private sealed class DisplayedState(val viewFlipperChild: Int) {

        class Initial : DisplayedState(viewFlipperChild = 0)
        class Empty : DisplayedState(viewFlipperChild = 0)
        class Loading : DisplayedState(viewFlipperChild = 1)
        class Success : DisplayedState(viewFlipperChild = 2)
    }
}
