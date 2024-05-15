package com.example.filmapp.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.filmapp.R;
import com.example.filmapp.adapters.EpisodesAdapter;
import com.example.filmapp.adapters.ImageSliderAdapter;
import com.example.filmapp.databinding.ActivityTvshowDetailBinding;
import com.example.filmapp.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.filmapp.viewmodels.TVShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

public class TVShowDetailActivity extends AppCompatActivity {

    private ActivityTvshowDetailBinding activityTvshowDetailBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvshowDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_detail);
        doInitialization();
    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTvshowDetailBinding.imageBack.setOnClickListener(view -> onBackPressed());
        getTVShowDetails();
    }

    private void getTVShowDetails() {
        activityTvshowDetailBinding.setIsLoading(true);
        String tvShowId = String.valueOf(getIntent().getIntExtra("id", -1));
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(
                this, tvShowDetailsResponse -> {
                    activityTvshowDetailBinding.setIsLoading(false);
                    if (tvShowDetailsResponse.getTvShowDetails() != null) {
                        if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                            loadingImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                        }
                        activityTvshowDetailBinding.setTvShowImageURL(
                                tvShowDetailsResponse.getTvShowDetails().getImagePath()
                        );
                        activityTvshowDetailBinding.imageTVShow.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                tvShowDetailsResponse.getTvShowDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        activityTvshowDetailBinding.textDescription.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.textReadMore.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.textReadMore.setOnClickListener(view -> {
                            if (activityTvshowDetailBinding.textReadMore.getText().toString().equals("Read More")) {
                                activityTvshowDetailBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                activityTvshowDetailBinding.textDescription.setEllipsize(null);
                                activityTvshowDetailBinding.textReadMore.setText(R.string.read_less);
                            }else {
                                activityTvshowDetailBinding.textDescription.setMaxLines(4);
                                activityTvshowDetailBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityTvshowDetailBinding.textReadMore.setText(R.string.read_more);
                            }
                        });
                        activityTvshowDetailBinding.setRating(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                                )
                        );
                        if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
                            activityTvshowDetailBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                        }else {
                            activityTvshowDetailBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                        }
                        activityTvshowDetailBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                        activityTvshowDetailBinding.viewDivider1.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.layoutMisc.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.viewDivider2.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.buttonWebsite.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                            startActivity(intent);
                        });
                        activityTvshowDetailBinding.buttonWebsite.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.buttonEpisodes.setOnClickListener(v -> {
                            if (episodesBottomSheetDialog == null) {
                                episodesBottomSheetDialog = new BottomSheetDialog(TVShowDetailActivity.this);
                                layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                        LayoutInflater.from(TVShowDetailActivity.this),
                                        R.layout.layout_episodes_bottom_sheet,
                                        findViewById(R.id.episodesContainer),
                                        false
                                );
                                episodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                                layoutEpisodesBottomSheetBinding.episodesRecyclerview.setAdapter(
                                        new EpisodesAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                                );
                                layoutEpisodesBottomSheetBinding.textTitle.setText(
                                        String.format("Episodes | %s", getIntent().getStringExtra("name"))
                                );
                                layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(view1 -> episodesBottomSheetDialog.dismiss());
                            }

                            //Optional section start
                                    FrameLayout frameLayout = episodesBottomSheetDialog.findViewById(
                                            com.google.android.material.R.id.design_bottom_sheet
                            );
                            if (frameLayout != null) {
                                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }

                            //Optional section end
                            episodesBottomSheetDialog.show();

                        });
                        loadBasicTVShowDetails();
                    }
                }
        );
    }

    private void loadingImageSlider(String[] sliderImages) {
        activityTvshowDetailBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvshowDetailBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTvshowDetailBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvshowDetailBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        activityTvshowDetailBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            activityTvshowDetailBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityTvshowDetailBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }


    private void setCurrentSliderIndicator(int position) {
        int childCount = activityTvshowDetailBinding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityTvshowDetailBinding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive)
                );
            }
        }
    }

    private void loadBasicTVShowDetails(){
        activityTvshowDetailBinding.setTvShowName(getIntent().getStringExtra("name"));
        activityTvshowDetailBinding.setNetworkCountry(
                getIntent().getStringExtra("network") + "( " +
                        getIntent().getStringExtra("country") + ")"
        );
        activityTvshowDetailBinding.setStatus(getIntent().getStringExtra("status"));
        activityTvshowDetailBinding.setStartedDate(getIntent().getStringExtra("startDate"));
        activityTvshowDetailBinding.textName.setVisibility(View.VISIBLE);
        activityTvshowDetailBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTvshowDetailBinding.textStatus.setVisibility(View.VISIBLE);
        activityTvshowDetailBinding.textStarted.setVisibility(View.VISIBLE);

    }
}