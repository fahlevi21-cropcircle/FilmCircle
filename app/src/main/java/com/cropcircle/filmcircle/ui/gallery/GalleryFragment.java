package com.cropcircle.filmcircle.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.layout_empty_states, container, false);

        String title = "Page Is Under Construction!";
        String message = "Stay tune for more features, checkout github pages (link)";
        int icon = R.drawable.error_construction;

        TextView textTitle = root.findViewById(R.id.error_title);
        TextView textMessage = root.findViewById(R.id.error_message);
        Button actionButton = root.findViewById(R.id.error_action);
        ImageView imageView = root.findViewById(R.id.error_image);

        actionButton.setVisibility(View.GONE);
        textTitle.setText(title);
        textMessage.setText(message);
        imageView.setImageResource(icon);
        return root;
    }
}