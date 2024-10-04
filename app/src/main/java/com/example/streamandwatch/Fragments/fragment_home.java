package com.example.streamandwatch.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.streamandwatch.Adapter.CategoryListAdapter;
import com.example.streamandwatch.Adapter.FilmListAdapter;
import com.example.streamandwatch.Adapter.SliderAdapters;
import com.example.streamandwatch.Domain.Genres;
import com.example.streamandwatch.Domain.GenresItems;
import com.example.streamandwatch.Domain.ListFilm;
import com.example.streamandwatch.Domain.SliderItems;
import com.example.streamandwatch.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_home extends Fragment {

    private RecyclerView.Adapter adapterBestMovies, adapterUpComing, adapterCategory;

    private RecyclerView recyclerViewBestMovies, recyclerViewUpComing, recyclerViewCategory;

    private RequestQueue mRequestQueue;

    private StringRequest mStringRequest, mStringRequest2, mStringRequest3;

    private ProgressBar loading1, loading2, loading3;
    private ViewPager2 viewPager2;

    private EditText searchBar;
    private Handler sliderHandler = new Handler();
    public fragment_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_home.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_home newInstance(String param1, String param2) {
        fragment_home fragment = new fragment_home();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        banners();
        sendRequestBestMovies();
        sendRequestUpComing();
        sendRequestCategory();
    }

    private void sendRequestBestMovies() {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        loading1.setVisibility(View.VISIBLE);
        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=1", response -> {
            Gson gson = new Gson();
            loading1.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response, ListFilm.class);
            adapterBestMovies = new FilmListAdapter(items);
            recyclerViewBestMovies.setAdapter(adapterBestMovies);
        }, error -> {
            loading1.setVisibility(View.GONE);
            Log.i("StreamAndWatch", "OnErrorResponse " + error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }

    private void sendRequestUpComing() {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        loading3.setVisibility(View.VISIBLE);
        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=2", response -> {
            Gson gson = new Gson();
            loading3.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response, ListFilm.class);
            adapterUpComing = new FilmListAdapter(items);
            recyclerViewUpComing.setAdapter(adapterUpComing);
        }, error -> {
            loading3.setVisibility(View.GONE);
            Log.i("StreamAndWatch", "OnErrorResponse " + error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }

    private void sendRequestCategory() {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        loading2.setVisibility(View.VISIBLE);
        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/genres", response -> {
            Gson gson = new Gson();
            loading2.setVisibility(View.GONE);
            ArrayList<GenresItems> catList = gson.fromJson(response, new TypeToken<ArrayList<GenresItems>>(){
            }.getType());
            adapterCategory = new CategoryListAdapter(catList);
            recyclerViewCategory.setAdapter(adapterCategory);
        }, error -> {
            loading2.setVisibility(View.GONE);
            Log.i("StreamAndWatch", "OnErrorResponse " + error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }

    private void banners(){
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.wide1));
        sliderItems.add(new SliderItems(R.drawable.wide2));

        viewPager2.setAdapter(new SliderAdapters(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }


    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    private void initView(View view){
        viewPager2 = view.findViewById(R.id.viewPagerSlider);

        recyclerViewBestMovies = view.findViewById(R.id.view1);
        recyclerViewBestMovies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory = view.findViewById(R.id.view2);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUpComing = view.findViewById(R.id.view3);
        recyclerViewUpComing.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        loading1 = view.findViewById(R.id.progressBar1);
        loading2 = view.findViewById(R.id.progressBar2);
        loading3 = view.findViewById(R.id.progressBar3);

    }
}