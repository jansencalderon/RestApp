package jru.restaurantapp.ui.main.category;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import jru.restaurantapp.R;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.databinding.ActivityCategoryBinding;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.ui.main.MainActivity;
import jru.restaurantapp.ui.restaurant.RestaurantActivity;

public class CategoryActivity extends MvpActivity<CategoryView,CategoryPresenter> implements CategoryView{

    private ActivityCategoryBinding binding;
    private CategoryListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);
        binding.setView(getMvpView());
        presenter.onStart();
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CategoryListAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);
        adapter.setList(presenter.getRestaurants(i.getStringExtra(Constants.REALM.REST_CATEGORY)));
        binding.toolbar.setSubtitle(i.getStringExtra(Constants.REALM.REST_CATEGORY));
    }


    @Override
    public void OnItemClicked (Restaurant restaurant) {
        Intent intent = new Intent(CategoryActivity.this, RestaurantActivity.class);
        intent.putExtra("id",restaurant.getRestId());
        startActivity(intent);
    }


    @NonNull
    @Override
    public CategoryPresenter createPresenter() {
        return new CategoryPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
