package com.cropcircle.filmcircle.ui.home.sub;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cropcircle.filmcircle.Constants;
import com.cropcircle.filmcircle.PreferenceManager;
import com.cropcircle.filmcircle.databinding.ActivityMovieDetailsBinding;
import com.cropcircle.filmcircle.models.action.AccountStates;
import com.cropcircle.filmcircle.models.action.ActionResponse;
import com.cropcircle.filmcircle.models.action.OnAccountStatesResponse;
import com.cropcircle.filmcircle.models.action.OnActionResponse;
import com.cropcircle.filmcircle.models.movie.Backdrop;
import com.cropcircle.filmcircle.models.movie.Genre;
import com.cropcircle.filmcircle.models.movie.Movie;
import com.cropcircle.filmcircle.models.movie.MovieDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cropcircle.filmcircle.R;
import com.cropcircle.filmcircle.models.movie.Video;
import com.cropcircle.filmcircle.models.people.Actors;
import com.cropcircle.filmcircle.models.review.Review;
import com.cropcircle.filmcircle.ui.auth.AuthActivity;
import com.cropcircle.filmcircle.ui.home.adapter.ImageAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.MovieAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.ReviewAdapter;
import com.cropcircle.filmcircle.ui.home.adapter.VideoAdapter;
import com.cropcircle.filmcircle.ui.home.itemdecoration.ActorsItemDecoration;
import com.cropcircle.filmcircle.ui.home.itemdecoration.HorizontalItemDecoration;
import com.cropcircle.filmcircle.ui.home.itemdecoration.ImageGridItemDecoration;
import com.cropcircle.filmcircle.ui.home.itemdecoration.VerticalItemDecoration;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

public class MovieDetailsActivity extends AppCompatActivity {
    ActivityMovieDetailsBinding binding;
    MovieDetailsViewModel viewModel;
    PreferenceManager manager;
    private Menu menu;
    int movieId;
    private CountDownTimer timer;
    private int emptyCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        movieId = intent.getIntExtra(Constants.MOVIE_ID_KEY, 0);

        setSupportActionBar(binding.movieDetailsToolbar);
        setTitle(null);
        binding.setLifecycleOwner(this);
        binding.movieDetailsToolbar.setTitle(null);
        manager = new PreferenceManager(this);
        viewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);

        binding.layoutEmpty.emptyRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });

        binding.movieDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.layoutContent.movieDetailsOverviewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdvancedDetailsShown()) {
                    hideAdvancedDetails();
                } else {
                    showAdvancedDetails();
                }
            }
        });

        binding.layoutContent.movieDetailsOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdvancedDetailsShown()) {
                    hideAdvancedDetails();
                } else {
                    showAdvancedDetails();
                }
            }
        });

        observeMovieDetails();
        getEmptyCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_to_favorite) {
            viewModel.getMovieStates(movieId, manager.getSessionId(), new OnAccountStatesResponse() {
                @Override
                public void OnResponse(AccountStates accountStates) {
                    if (accountStates != null) {
                        viewModel.addToFavorites(manager.getSessionId(), movieId, manager.getUserdata().getId(), new OnActionResponse() {
                            @Override
                            public void OnResponse(ActionResponse response) {
                                getMovieStates();
                            }

                            @Override
                            public void OnError(String e) {
                                Toast.makeText(MovieDetailsActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void OnError(String e) {

                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Deprecated()
    private void initTimer() {
        timer = new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long l) {
                //checkConnection();
            }

            @Override
            public void onFinish() {
                //show error connection view
                errorShowNetworkError();
                Toast.makeText(MovieDetailsActivity.this, "Network Unavailable!", Toast.LENGTH_SHORT).show();
            }
        };

        timer.start();
    }

    private void observeMovieDetails() {
        viewModel.setMovieId(movieId);
        viewModel.getDetails().observe(this, new Observer<MovieDetails>() {
            @Override
            public void onChanged(MovieDetails movieDetails) {
                if (movieDetails != null) {
                    binding.movieDetailsLoading.setVisibility(View.GONE);
                    binding.setData(movieDetails);
                    initAdvancedDetails(movieDetails);
                    binding.layoutContent.movieDetailsVote.setStar(movieDetails.getVoteAverage().floatValue() - 5.0f);
                    String quot = " &rdquo; ";
                    binding.layoutContent.movieDetailsVoteAvg.setText(movieDetails.getVoteAverage().toString());
                    String voteCount = movieDetails.getVoteCount() + " Votes";
                    binding.layoutContent.movieDetailsVoteCount.setText(voteCount);

                    if (movieDetails.getTagline() != null && !movieDetails.getTagline().trim().isEmpty()) {
                        String tagline = quot + movieDetails.getTagline() + quot;
                        binding.layoutContent.movieDetailsTagline.setText(Html.fromHtml(tagline));
                        binding.layoutContent.movieDetailsTagline.setVisibility(View.VISIBLE);
                    } else {
                        binding.layoutContent.movieDetailsTagline.setVisibility(View.GONE);
                    }

                    if (movieDetails.getGenres().size() >= 3) {
                        getMovieGenres(movieDetails.getGenres().subList(0, 3));
                    } else {
                        getMovieGenres(movieDetails.getGenres());
                    }

                    if (movieDetails.getOverview() != null && !movieDetails.getOverview().isEmpty()) {
                        binding.layoutContent.movieDetailsOverview.setText(movieDetails.getOverview());
                    } else {
                        setEmptyCount();
                        binding.layoutContent.movieDetailsOverview.setText("No Info");
                    }

                    if (movieDetails.getPosterPath() != null && movieDetails.getPosterPath().isEmpty() &&
                            movieDetails.getBackdropPath() != null && movieDetails.getBackdropPath().isEmpty()) {
                        setEmptyCount();
                    }
                }
            }
        });

        getMovieCasters();
        getMovieReviews();
        getMovieImages();
        getMovieVideos();
        getMovieStates();
        getRecommendedMovies();
        getSimilarMovies();
        //prepareTabs();
    }

    private void initAdvancedDetails(MovieDetails movieDetails) {
        String dot = " &bull; ";
        String runtime = "";
        String status = "";

        if (movieDetails.getOriginalTitle() != null && !movieDetails.getOriginalTitle().isEmpty()) {
            binding.layoutContent.movieDetailsMoreOriginalTitle.setText(movieDetails.getOriginalTitle());
        } else {
            binding.layoutContent.movieDetailsMoreOriginalTitle.setText("No information");
        }

        if (movieDetails.getReleaseDate() != null && !movieDetails.getReleaseDate().isEmpty()) {
            binding.layoutContent.movieDetailsMoreDate.setText(Constants.getInstance().simpleDateFormatter(movieDetails.getReleaseDate()));
        } else {
            binding.layoutContent.movieDetailsMoreDate.setText("No information");
        }

        if (movieDetails.getStatus() != null && !movieDetails.getReleaseDate().isEmpty()) {
            binding.layoutContent.movieDetailsStatus.setText(movieDetails.getStatus());
        } else {
            setEmptyCount();
            binding.layoutContent.movieDetailsStatus.setText("No information");
        }

        if (movieDetails.getHomepage() != null && !movieDetails.getHomepage().isEmpty()) {
            binding.layoutContent.movieDetailsHomepage.setText(movieDetails.getHomepage());
            binding.layoutContent.movieDetailsHomepage.setTextColor(getResources().getColor(R.color.primaryLightColor));
            binding.layoutContent.movieDetailsHomepage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri link = Uri.parse(movieDetails.getHomepage());
                    startActivity(new Intent(Intent.ACTION_VIEW, link));
                }
            });
        } else {
            binding.layoutContent.movieDetailsHomepage.setText("No information");
        }

        if (movieDetails.getGenres() != null && movieDetails.getGenres().size() > 0) {
            for (int i = 0; i < movieDetails.getGenres().size(); i++) {
                Chip chip = new Chip(this);
                chip.setText(movieDetails.getGenres().get(i).getName());
                //chip.setLeft(16);
                chip.setRight(16);
                binding.layoutContent.movieDetailsMoreGenre.addView(chip);
            }
        } else {
            binding.layoutContent.movieDetailsAdvancedGenreLabel.setVisibility(View.GONE);
            binding.layoutContent.movieDetailsMoreGenre.setVisibility(View.GONE);
        }

        if (movieDetails.getProductionCompanies() != null && movieDetails.getProductionCompanies().size() > 0) {
            binding.layoutContent.titleProductionCompanyName.setText(movieDetails.getProductionCompanies().get(0).getName());
            ProductionCompanyAdapter adapter = new ProductionCompanyAdapter(R.layout.item_production_company);
            binding.layoutContent.movieDetailsMoreProductionCompany.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.layoutContent.movieDetailsMoreProductionCompany.setHasFixedSize(true);
            binding.layoutContent.movieDetailsMoreProductionCompany.addItemDecoration(new ActorsItemDecoration(16));
            binding.layoutContent.movieDetailsMoreProductionCompany.setAdapter(adapter);
            adapter.setList(movieDetails.getProductionCompanies());
        } else {
            setEmptyCount();
            binding.layoutContent.movieDetailsMoreCompLabel.setVisibility(View.GONE);
        }

        if (movieDetails.getRuntime() != null && movieDetails.getRuntime() != 0) {
            Constants constants = Constants.getInstance();
            binding.layoutContent.movieDetailsRuntime.setText(constants.simpleDurationFormatter(movieDetails.getRuntime()));
        } else {
            binding.layoutContent.movieDetailsRuntime.setText("No information");
        }

        if (movieDetails.getRuntime() != null && movieDetails.getOriginalLanguage() != null
                && movieDetails.getRuntime() > 0 && !movieDetails.getOriginalLanguage().isEmpty()) {
            runtime = Constants.getInstance().simpleDurationFormatter(movieDetails.getRuntime());
            status = movieDetails.getOriginalLanguage().toUpperCase();
            String otherText = runtime + dot + status;
            binding.layoutContent.titleOther.setText(Html.fromHtml(otherText));
        } else {
            binding.layoutContent.titleOther.setVisibility(View.GONE);
        }
    }

    private void showAdvancedDetails() {
        binding.layoutContent.movieDetailsOverview.setMaxLines(100);
        binding.layoutContent.movieDetailsAdvancedInfoContainer.setVisibility(View.VISIBLE);
        binding.layoutContent.movieDetailsOverviewMore.setText("less information");
    }

    private void hideAdvancedDetails() {
        binding.layoutContent.movieDetailsOverview.setMaxLines(6);
        binding.layoutContent.movieDetailsAdvancedInfoContainer.setVisibility(View.GONE);
        binding.layoutContent.movieDetailsOverviewMore.setText("more information");
    }

    private boolean isAdvancedDetailsShown() {
        return binding.layoutContent.movieDetailsAdvancedInfoContainer.getVisibility() == View.VISIBLE;
    }

    private void getMovieGenres(List<Genre> genreList) {
        for (int i = 0; i < genreList.size(); i++) {
            binding.layoutContent.movieDetailsGenre.append(genreList.get(i).getName());
            if (i != genreList.size() - 1) {
                binding.layoutContent.movieDetailsGenre.append(", ");
            } else {
                binding.layoutContent.movieDetailsGenre.append(" ");
            }
        }
    }

    private void getMovieCasters() {
        CastRecyclerViewAdapter adapter = new CastRecyclerViewAdapter(R.layout.item_caster);
        binding.layoutContent.movieDetailsCastRc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.layoutContent.movieDetailsCastRc.setHasFixedSize(true);
        binding.layoutContent.movieDetailsCastRc.addItemDecoration(new ActorsItemDecoration(16));
        viewModel.getCasters().observe(this, new Observer<List<Actors>>() {
            @Override
            public void onChanged(List<Actors> actors) {
                if (actors != null && actors.size() > 0) {
                    if (actors.size() >= 8) {
                        int remainder = actors.size() - 8;
                        String text = "See all(" + remainder + ")";
                        binding.layoutContent.movieDetailsCastAll.setText(text);
                        adapter.setList(actors.subList(0, 8));
                    } else if (actors.size() < 5) {
                        binding.layoutContent.movieDetailsCastRc.setVisibility(View.GONE);
                        binding.layoutContent.movieDetailsCastLabel.setVisibility(View.GONE);
                        binding.layoutContent.movieDetailsCastAll.setVisibility(View.GONE);
                        setEmptyCount();
                    } else {
                        binding.layoutContent.movieDetailsCastAll.setVisibility(View.GONE);
                        adapter.setList(actors);
                    }
                    binding.layoutContent.movieDetailsCastRc.setAdapter(adapter);
                } else {
                    binding.layoutContent.movieDetailsCastRc.setVisibility(View.GONE);
                    binding.layoutContent.movieDetailsCastLabel.setVisibility(View.GONE);
                    binding.layoutContent.movieDetailsCastAll.setVisibility(View.GONE);
                    setEmptyCount();
                }
            }
        });
    }

    private void getMovieReviews() {
        ReviewAdapter adapter = new ReviewAdapter();
        binding.layoutContent.movieDetailsReviewRc.setHasFixedSize(true);
        binding.layoutContent.movieDetailsReviewRc.setLayoutManager(new LinearLayoutManager(this));
        binding.layoutContent.movieDetailsReviewRc.setAdapter(adapter);
        binding.layoutContent.movieDetailsReviewRc.addItemDecoration(new VerticalItemDecoration(16, 16, 16, 16));

        viewModel.getReview().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                if (reviews != null && reviews.size() > 0) {
                    binding.layoutContent.movieDetailsNoReview.setVisibility(View.GONE);
                    if (reviews.size() > 3) {
                        adapter.setList(reviews.subList(0, 3));
                        int totalReview = reviews.size() - 3;
                        binding.layoutContent.movieDetailsReviewAll.setText("See all (" + totalReview + ")");
                    } else {
                        binding.layoutContent.movieDetailsReviewAll.setVisibility(View.GONE);
                        adapter.setList(reviews);
                    }
                } else {
                    binding.layoutContent.movieDetailsReviewAll.setVisibility(View.GONE);
                    binding.layoutContent.movieDetailsNoReview.setVisibility(View.VISIBLE);
                    setEmptyCount();
                }
            }
        });
    }

    private void getMovieImages() {
        ImageAdapter adapter = new ImageAdapter(R.layout.item_image_small_grid);
        binding.layoutContent.movieDetailsImagesRc.setHasFixedSize(true);
        binding.layoutContent.movieDetailsImagesRc.setLayoutManager(new GridLayoutManager(this, 3));
        binding.layoutContent.movieDetailsImagesRc.addItemDecoration(new ImageGridItemDecoration(8, 8, 8, 8));
        binding.layoutContent.movieDetailsImagesRc.setAdapter(adapter);

        viewModel.getImages().observe(this, new Observer<List<Backdrop>>() {
            @Override
            public void onChanged(List<Backdrop> backdrops) {
                if (backdrops != null && backdrops.size() > 0) {
                    if (backdrops.size() >= 6) {
                        int remainder = backdrops.size() - 6;
                        String seeAllText = "See all(" + remainder + ")";
                        binding.layoutContent.movieDetailsImagesAll.setText(seeAllText);
                        adapter.setList(backdrops.subList(0, 6));
                    } else if (backdrops.size() <= 3) {
                        binding.layoutContent.movieDetailsImagesLabel.setVisibility(View.GONE);
                        binding.layoutContent.movieDetailsImagesRc.setVisibility(View.GONE);
                        binding.layoutContent.movieDetailsImagesAll.setVisibility(View.GONE);
                        setEmptyCount();
                    } else {
                        binding.layoutContent.movieDetailsImagesAll.setVisibility(View.GONE);
                        adapter.setList(backdrops);
                    }
                } else {
                    binding.layoutContent.movieDetailsImagesLabel.setVisibility(View.GONE);
                    binding.layoutContent.movieDetailsImagesRc.setVisibility(View.GONE);
                    binding.layoutContent.movieDetailsImagesAll.setVisibility(View.GONE);
                    setEmptyCount();
                }
            }
        });
    }

    private void getMovieVideos() {
        VideoAdapter adapter = new VideoAdapter(R.layout.item_video_large);
        adapter.setUseEmpty(true);
        binding.layoutContent.movieDetailsVideoRc.setHasFixedSize(true);
        binding.layoutContent.movieDetailsVideoRc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.layoutContent.movieDetailsVideoRc.addItemDecoration(new HorizontalItemDecoration(24, 8, 16, 16, 16));
        binding.layoutContent.movieDetailsVideoRc.setAdapter(adapter);

        viewModel.getVideos().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                if (videos != null && videos.size() > 0) {
                    for (int i = 0; i < videos.size(); i++) {
                        //searching for trailers
                        if (videos.get(i).getType().toLowerCase().contains("trailer")) {
                            binding.movieDetailsPoster.movieDetailsPlayTrailerText.setVisibility(View.VISIBLE);
                            binding.movieDetailsPoster.movieDetailsPlayTrailerButton.setVisibility(View.VISIBLE);
                        } else {
                            binding.movieDetailsPoster.movieDetailsPlayTrailerText.setVisibility(View.GONE);
                            binding.movieDetailsPoster.movieDetailsPlayTrailerButton.setVisibility(View.GONE);
                        }
                    }
                    if (videos.size() >= 4) {
                        adapter.setList(videos.subList(0, 4));
                    } else if (videos.size() <= 1) {
                        binding.layoutContent.movieDetailsVideoRc.setVisibility(View.GONE);
                        binding.layoutContent.movieDetailsVideoLabel.setVisibility(View.GONE);
                    } else {
                        adapter.setList(videos);
                    }
                } else {
                    binding.movieDetailsPoster.movieDetailsPlayTrailerButton.setVisibility(View.GONE);
                    binding.movieDetailsPoster.movieDetailsPlayTrailerText.setVisibility(View.GONE);
                    binding.layoutContent.movieDetailsVideoRc.setVisibility(View.GONE);
                    binding.layoutContent.movieDetailsVideoLabel.setVisibility(View.GONE);
                    binding.movieDetailsPoster.movieDetailsPlayTrailerText.setVisibility(View.GONE);
                    binding.movieDetailsPoster.movieDetailsPlayTrailerButton.setVisibility(View.GONE);
                    binding.movieDetailsPoster.fadeBackground.setVisibility(View.GONE);
                    setEmptyCount();
                }
            }
        });
    }

    private void getSimilarMovies() {
        MovieAdapter newReleaseAdapter = new MovieAdapter(R.layout.item_small_linear);
        TextView emptyView = new TextView(this);
        emptyView.setText("No Information at this moment");
        newReleaseAdapter.setEmptyView(emptyView);
        binding.layoutContent.movieDetailsSimilarRc.setHasFixedSize(true);
        binding.layoutContent.movieDetailsSimilarRc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.layoutContent.movieDetailsSimilarRc.setAdapter(newReleaseAdapter);
        binding.layoutContent.movieDetailsSimilarRc.addItemDecoration(new HorizontalItemDecoration(8, 32, 8, 8, 16));
        newReleaseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Movie data = (Movie) adapter.getItem(position);
                Intent intent = new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
                intent.putExtra(Constants.MOVIE_ID_KEY, data.getId());
                startActivity(intent);
            }
        });

        viewModel.getSimilar().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null && movies.size() > 0) {
                    Comparator<Movie> comparator = new Comparator<Movie>() {
                        @Override
                        public int compare(Movie movie, Movie t1) {
                            return movie.getPopularity().compareTo(t1.getPopularity());
                        }
                    };

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(movies, comparator.reversed());
                    }
                    newReleaseAdapter.setList(movies);
                } else {
                    newReleaseAdapter.setUseEmpty(true);
                    setEmptyCount();
                }
            }
        });
    }

    private void getRecommendedMovies() {
        MovieAdapter newReleaseAdapter = new MovieAdapter(R.layout.item_small_linear);
        TextView emptyView = new TextView(this);
        emptyView.setText("No Information at this moment");
        newReleaseAdapter.setEmptyView(emptyView);
        binding.layoutContent.movieDetailsRecommendedRc.setHasFixedSize(true);
        binding.layoutContent.movieDetailsRecommendedRc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.layoutContent.movieDetailsRecommendedRc.setAdapter(newReleaseAdapter);
        binding.layoutContent.movieDetailsRecommendedRc.addItemDecoration(new HorizontalItemDecoration(8, 32, 8, 8, 16));
        newReleaseAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Movie data = (Movie) adapter.getItem(position);
                Intent intent = new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
                intent.putExtra(Constants.MOVIE_ID_KEY, data.getId());
                startActivity(intent);
            }
        });

        viewModel.getRecommended().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null && movies.size() > 0) {
                    Comparator<Movie> comparator = new Comparator<Movie>() {
                        @Override
                        public int compare(Movie movie, Movie t1) {
                            return movie.getPopularity().compareTo(t1.getPopularity());
                        }
                    };

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(movies, comparator.reversed());
                    }
                    newReleaseAdapter.setList(movies);
                } else {
                    newReleaseAdapter.setUseEmpty(true);
                    setEmptyCount();
                    Log.d("Movie Details Empty", "count: " + emptyCount);
                }
            }
        });
    }

    private void getMovieStates() {
        viewModel.getMovieStates(movieId, manager.getSessionId(), new OnAccountStatesResponse() {
            @Override
            public void OnResponse(AccountStates accountStates) {
                if (accountStates != null && accountStates.getId() != null) {
                    if (accountStates.getFavorite()) {
                        menu.getItem(1).setIcon(R.drawable.ic_baseline_favorite);
                    } else {
                        menu.getItem(1).setIcon(R.drawable.ic_baseline_favorite_border_24);
                    }

                    /*if (accountStates.getWatchlist()){
                        //unimplemented
                    }else {
                        //unimplemented
                    }*/
                }
            }

            @Override
            public void OnError(String e) {

            }
        });
    }

    private void errorShowNetworkError() {
        binding.layoutContent.getRoot().setVisibility(View.GONE);
        //binding.movieDetailFabFavorites.setVisibility(View.GONE);
        binding.layoutEmpty.getRoot().setVisibility(View.VISIBLE);
        String title = "Network Unavailable";
        String message = "Try to check your current connection and refresh this page";
        int icon = R.drawable.error_conn;

        binding.layoutEmpty.errorImage.setImageResource(icon);
        binding.layoutEmpty.errorMessage.setText(message);
        binding.layoutEmpty.errorTitle.setText(title);
        binding.layoutEmpty.errorAction.setVisibility(View.GONE);
    }

    private void hideErrorPage() {
        binding.layoutContent.getRoot().setVisibility(View.VISIBLE);
        //binding.movieDetailFabFavorites.setVisibility(View.VISIBLE);
        binding.layoutEmpty.getRoot().setVisibility(View.GONE);
    }

    private void refreshPage() {
        binding.layoutEmpty.emptyRefresh.setRefreshing(true);
        observeMovieDetails();
    }

    private void getEmptyCount() {
        viewModel.getEmptyCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer >= 4) {
                    Snackbar.make(binding.getRoot(), "Not Recommended to see", Snackbar.LENGTH_LONG).show();
                }
                Log.d("Observe Empty", "count: " + integer);
            }
        });
    }

    private void setEmptyCount() {
        emptyCount++;
        viewModel.setEmptyCount(emptyCount);
    }

}