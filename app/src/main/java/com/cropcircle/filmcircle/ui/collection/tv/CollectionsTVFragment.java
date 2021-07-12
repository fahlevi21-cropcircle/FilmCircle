package com.cropcircle.filmcircle.ui.collection.tv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cropcircle.filmcircle.PreferenceManager;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.models.tv.MediaTV;
import com.cropcircle.filmcircle.ui.home.adapter.MediaTVAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.CustomDividerItemDecoration;
import com.cropcircle.filmcircle.ui.home.itemdecoration.VerticalItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class CollectionsTVFragment extends Fragment {

    private CollectionsTVViewModel mViewModel;
    private PreferenceManager manager;

    public static CollectionsTVFragment newInstance() {
        return new CollectionsTVFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(CollectionsTVViewModel.class);
        manager = new PreferenceManager(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragement_collections_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        RecyclerView recyclerView = view.findViewById(R.id.rc_collections_tv_favorite);
        Toolbar toolbar = view.findViewById(R.id.collections_tv_toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        MediaTVAdapter adapter = new MediaTVAdapter(R.layout.item_favorite_tv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        VerticalItemDecoration decoration = new VerticalItemDecoration(16,16,16,16);
        //decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.custom_divider)));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);

        mViewModel.getFavoriteTv(manager.getUserdata().getId(), manager.getSessionId()).observe(getViewLifecycleOwner(), new Observer<List<MediaTV>>() {
            @Override
            public void onChanged(List<MediaTV> mediaTVS) {
                if (mediaTVS != null && mediaTVS.size() > 0){
                    adapter.setList(mediaTVS);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}