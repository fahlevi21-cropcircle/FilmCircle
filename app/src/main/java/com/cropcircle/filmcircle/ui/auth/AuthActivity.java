package com.cropcircle.filmcircle.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.cropcircle.filmcircle.MainActivity;
import com.cropcircle.filmcircle.PreferenceManager;
import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.databinding.ActivityAuthBinding;
import com.cropcircle.filmcircle.models.auth.OnRequestTokenResponse;
import com.cropcircle.filmcircle.models.auth.SessionId;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.cropcircle.filmcircle.models.user.OnUserDetailsResponse;
import com.cropcircle.filmcircle.models.user.User;
import com.cropcircle.filmcircle.ui.home.HomeViewModel;
import com.cropcircle.filmcircle.ui.home.adapter.MovieAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.HorizontalItemDecoration;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager manager = new PreferenceManager(this);
        if (manager.getLoggedIn()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.loginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri signUpLink = Uri.parse("https://www.themoviedb.org/signup");
                startActivity(new Intent(Intent.ACTION_VIEW, signUpLink));
            }
        });

        binding.loginRc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.loginRc.addItemDecoration(new HorizontalItemDecoration(8, 8, 8, 8, 32));
        binding.loginRc.setHasFixedSize(true);
        binding.loginGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AuthActivity.this, "Implemented soon!", Toast.LENGTH_SHORT).show();
            }
        });
        MovieAdapter adapter = new MovieAdapter(R.layout.item_image_small);
        binding.loginRc.setAdapter(adapter);

        if (isNetworkConnected()){
            viewModel.getPopularMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(List<Movie> movies) {
                    if (movies != null && movies.size() > 0){
                        Toast.makeText(AuthActivity.this, "Size " + movies.size(), Toast.LENGTH_SHORT).show();
                        binding.authProgressbar.setVisibility(View.GONE);
                        adapter.setList(movies.subList(0,5));
                    }
                }
            });
        }else {
            new CountDownTimer(5000, 1000){

                @Override
                public void onTick(long l) {
                    viewModel.getPopularMovies().observe(AuthActivity.this, new Observer<List<Movie>>() {
                        @Override
                        public void onChanged(List<Movie> movies) {
                            if (movies != null && movies.size() > 0){
                                binding.authProgressbar.setVisibility(View.GONE);
                                adapter.setList(movies);
                            }else {
                                binding.authProgressbar.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }

                @Override
                public void onFinish() {
                    binding.authProgressbar.setVisibility(View.GONE);
                    Toast.makeText(AuthActivity.this, "Network Unavailable!", Toast.LENGTH_SHORT).show();
                }
            }.start();
        }

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Authenticate();
            }
        });
    }

    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        for (Network network : connectivityManager.getAllNetworks()){
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                isConnected = true;
            }
        }
        return isConnected;
    }

    private void Authenticate() {
        String username = binding.loginUsername.getEditText().getText().toString().trim();
        String password = binding.loginPassword.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(username)){
            binding.loginUsername.setError("This field is required");
            binding.loginPassword.setError(null);
        }else if (TextUtils.isEmpty(password)){
            binding.loginPassword.setError("This field is required");
            binding.loginUsername.setError(null);
        }else {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("authenticating, please wait ");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();

            viewModel.createRequestToken(new OnRequestTokenResponse() {
                @Override
                public void onResponse(String requestToken) {
                    //Toast.makeText(AuthActivity.this, "token " + requestToken, Toast.LENGTH_SHORT).show();
                    if (requestToken != null){
                        viewModel.validateRequestToken(username, password, requestToken);
                    }
                }

                @Override
                public void onError(String error) {
                    Snackbar.make(AuthActivity.this,
                            binding.getRoot(),
                            "Connection error : " + error,
                            Snackbar.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            viewModel.getSessionId().observe(this, new Observer<SessionId>() {
                @Override
                public void onChanged(SessionId sessionId) {
                    if (sessionId != null && sessionId.getSessionId() != null){
                        PreferenceManager manager = new PreferenceManager(AuthActivity.this);
                        viewModel.getUserDetails(sessionId.getSessionId(), new OnUserDetailsResponse() {
                            @Override
                            public void onResponse(User user) {
                                if (user != null && user.getName() != null){
                                    manager.setUserdata(user);
                                    dialog.dismiss();
                                    manager.setSessionId(sessionId.getSessionId());
                                    manager.setLoggedIn(true);
                                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onError(String e) {
                                //do nothing
                            }
                        });

                    }else {
                        dialog.dismiss();
                        Snackbar.make(AuthActivity.this,
                                binding.getRoot(),
                                "Username or Password is invalid",
                                Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}