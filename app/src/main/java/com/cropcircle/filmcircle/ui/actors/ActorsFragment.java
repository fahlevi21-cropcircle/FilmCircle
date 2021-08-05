package com.cropcircle.filmcircle.ui.actors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.cropcircle.filmcircle.MainActivity;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.FragmentActorsBinding;
import com.cropcircle.filmcircle.models.people.Actors;
import com.cropcircle.filmcircle.ui.home.itemdecoration.HorizontalItemDecoration;
import com.cropcircle.filmcircle.ui.home.itemdecoration.VerticalItemDecoration;
import com.cropcircle.filmcircle.ui.home.sub.CastRecyclerViewAdapter;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchQueryChangeListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener;
import com.paulrybitskyi.persistentsearchview.utils.SuggestionCreationUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActorsFragment extends Fragment {
    private ActorsViewModel viewModel;
    private FragmentActorsBinding binding;
    private CastRecyclerViewAdapter adapter;
    private String mQuery;
    private int page = 1;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ActorsViewModel.class);
        resetPage();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentActorsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.actorsToolbar);*/

        initSearchView();
        initRecyclerView();
        observeLatest();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.actorsSearch.isExpanded()){
                    binding.actorsSearch.collapse();
                }else {
                    ((AppCompatActivity) getActivity()).onBackPressed();
                }
            }
        };

        callback.handleOnBackPressed();
    }

    private void initSearchView() {
        binding.actorsSearch.setDimBackground(false);
        binding.actorsSearch.setOnLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        binding.actorsSearch.setOnSearchConfirmedListener(new OnSearchConfirmedListener() {
            @Override
            public void onSearchConfirmed(PersistentSearchView searchView, String query) {
                mQuery = query;
                doSearch(query);
                binding.actorsSearch.setProgressBarEnabled(true);
                observeSearchedData();
                binding.actorsSearch.collapse();
            }
        });

        binding.actorsSearch.setOnSearchQueryChangeListener(new OnSearchQueryChangeListener() {
            @Override
            public void onSearchQueryChanged(PersistentSearchView searchView, String oldQuery, String newQuery) {
                //better to set only suggestions
            }
        });

        binding.actorsSearch.setOnClearInputBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.actorsSearch.collapse();
                resetPage();
                observeLatest();
            }
        });

        binding.actorsSearch.setSuggestionsDisabled(true);

    }

    private void initRecyclerView() {
        adapter = new CastRecyclerViewAdapter(R.layout.item_latest_actors);
        binding.latestActorsRc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.latestActorsRc.setHasFixedSize(true);
       /* DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.custom_divider));*/

        binding.latestActorsRc.addItemDecoration(new VerticalItemDecoration(32,12,24,24));
        binding.latestActorsRc.setAdapter(adapter);

        adapter.getLoadMoreModule().setAutoLoadMore(false);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        adapter.setAnimationEnable(true);
        adapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInLeft);
        adapter.setDiffCallback(new DiffUtil.ItemCallback<Actors>() {
            @Override
            public boolean areItemsTheSame(@NonNull @NotNull Actors oldItem, @NonNull @NotNull Actors newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull @NotNull Actors oldItem, @NonNull @NotNull Actors newItem) {
                return oldItem.getId().equals(newItem.getId())
                        && oldItem.getName().equals(newItem.getName())
                        && oldItem.getProfilePath().equals(newItem.getProfilePath());
            }
        });
        adapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                nextPage();
                if (mQuery == null){
                    observeLatest();
                }else {
                    observeSearchedData();
                }
            }
        });
    }

    private void observeSearchedData(){
        viewModel.searchActors(page, mQuery).observe(getViewLifecycleOwner(), new Observer<List<Actors>>() {
            @Override
            public void onChanged(List<Actors> actors) {
                if (actors != null && actors.size() > 0){
                    binding.actorsSearch.setProgressBarEnabled(false);
                    if(isFirstPage()){
                        adapter.setList(actors);
                    }else {
                        adapter.addData(actors);
                    }

                    if (page == 5) {
                        adapter.getLoadMoreModule().loadMoreEnd();
                    } else {
                        adapter.getLoadMoreModule().loadMoreComplete();
                    }
                    //adapter.getLoadMoreModule().setAutoLoadMore(false);
                    adapter.getLoadMoreModule().setEnableLoadMore(true);
                }else if (actors != null && actors.size() == 0){
                    observeLatest();
                    Toast.makeText(getContext(), "Query not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void doSearch(String query){
        viewModel.setQuerySearch(query);
    }

    private boolean isQueryEmpty(){
        return mQuery.isEmpty();
    }

    private void observeLatest() {
        viewModel.getPopularActors(page).observe(getViewLifecycleOwner(), new Observer<List<Actors>>() {
            @Override
            public void onChanged(List<Actors> actors) {
                if (actors != null && actors.size() > 0) {
                    if (isFirstPage()) {
                        /*List<String> suggestionList = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            suggestionList.add(actors.get(i).getName());
                        }
                        binding.actorsSearch.setSuggestions(SuggestionCreationUtil.asRegularSearchSuggestions(suggestionList));
                        binding.actorsSearch.collapse();*/
                        adapter.setList(actors);
                    } else {
                        adapter.addData(actors);
                    }

                    if (page == 5) {
                        adapter.getLoadMoreModule().loadMoreEnd();
                    } else {
                        adapter.getLoadMoreModule().loadMoreComplete();
                    }
                    //adapter.getLoadMoreModule().setAutoLoadMore(false);
                    adapter.getLoadMoreModule().setEnableLoadMore(true);
                }
            }
        });
    }

    private void nextPage() {
        page++;
    }

    private void resetPage() {
        page = 1;
    }

    private boolean isFirstPage() {
        return page == 1;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}