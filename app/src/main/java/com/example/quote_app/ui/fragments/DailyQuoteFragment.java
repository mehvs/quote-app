package com.example.quote_app.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quote_app.R;
import com.example.quote_app.data.database.model.Quote;
import com.example.quote_app.databinding.FragmentDailyQuoteBinding;
import com.example.quote_app.data.api.ApiServer;
import com.example.quote_app.ui.activities.MainActivity;
import com.example.quote_app.ui.viewmodels.QuoteViewModel;

import java.util.Objects;


public class DailyQuoteFragment extends Fragment {

    private FragmentDailyQuoteBinding binding;
    private QuoteViewModel quoteViewModel;

    private static Boolean isHeartClicked = false;

    public DailyQuoteFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getQuoteBasedOnLanguage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        binding = FragmentDailyQuoteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        quoteViewModel = new ViewModelProvider(this).get(QuoteViewModel.class);

        setupSwipeDownRefresh();
        listenForRefresh();
        setupHeartButton();
        setupShareButton();

        return view;
    }


    private void onGetQuoteClickedEnglish() {

        ApiServer.getInstance().getRandomQuoteEnglish(new ApiServer.ApiListener() {
            @Override
            public void onQuoteReceived(String quote, String author) {
                binding.quoteTxtView.setText(quote);
                binding.authorTxtView.setText(author);
            }

            @Override
            public void onFailure() {

            }
        });


    }

    private void onGetQuoteClickedRussian() {

        ApiServer.getInstance().getRandomQuoteRussian(new ApiServer.ApiListener() {
            @Override
            public void onQuoteReceived(String quote, String author) {
                binding.quoteTxtView.setText(quote);
                binding.authorTxtView.setText(author);
            }

            @Override
            public void onFailure() {

            }
        });


    }

    private void listenForRefresh() {
        ((MainActivity) getActivity()).setRefreshListener(new MainActivity.OnRefreshClickListener() {
            @Override
            public void onRefreshClick() {
                getQuoteBasedOnLanguage();
                removeClickOnHeartButton();
            }


        });
    }

    private void setupSwipeDownRefresh() {
        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getQuoteBasedOnLanguage();
                removeClickOnHeartButton();
                binding.swiperefresh.setRefreshing(false);
            }
        });
    }

    private void removeClickOnHeartButton() {
        isHeartClicked = false;
        binding.imageView.setBackgroundResource(R.drawable.ic_favorite_border_black_24px);
    }

    private void setupHeartButton() {
        binding.imageView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (!isHeartClicked) {
                    Quote quote = new Quote(binding.quoteTxtView.getText().toString(), binding.authorTxtView.getText().toString());
                    quoteViewModel.insert(quote);
                    binding.imageView.setBackgroundResource(R.drawable.ic_favorite_black_24px);
                    isHeartClicked = true;
                } else {
                    quoteViewModel.deleteByQuoteText(binding.quoteTxtView.getText().toString());
                    removeClickOnHeartButton();
                }
            }
        });
    }

    private void setupShareButton() {
        binding.shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareText = "\"" + binding.quoteTxtView.getText().toString() + "\"" + "-" + binding.authorTxtView.getText().toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                sendIntent.setType("text/*");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
    }

    private boolean getSwitchStatus() {
        SwitchCompat thumbSwitch = ((MainActivity) (Objects.requireNonNull(getActivity()))).findViewById(R.id.thumbSwitch);
        return thumbSwitch.isChecked();
    }

    private void getQuoteBasedOnLanguage() {
        if (!getSwitchStatus()) {
            onGetQuoteClickedEnglish();
        } else {
            onGetQuoteClickedRussian();
        }
    }


}