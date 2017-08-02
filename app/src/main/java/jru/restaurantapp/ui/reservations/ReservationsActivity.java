package jru.restaurantapp.ui.reservations;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import jru.restaurantapp.R;
import jru.restaurantapp.databinding.ActivityReservationsBinding;
import jru.restaurantapp.model.data.Reservation;

public class ReservationsActivity extends MvpActivity<ReservationsView,ReservationsPresenter> implements ReservationsView {

    private ActivityReservationsBinding binding;
    private ReservationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reservations);
        binding.setView(getMvpView());
        presenter.onStart();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //adapter
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservationListAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);


        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getReservations();
            }
        });
    }


    @Override
    public void OnItemClicked(Reservation item) {

    }


    @Override
    public void startLoading() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showAlert(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setList(List<Reservation> list) {
        if(!list.isEmpty()){
            adapter.setList(list);
            binding.noResult.getRoot().setVisibility(View.GONE);
        }else {
            binding.noResult.getRoot().setVisibility(View.VISIBLE);
            binding.noResult.resultText.setText("No Reservations Yet");
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
    }

    @NonNull
    @Override
    public ReservationsPresenter createPresenter() {
        return new ReservationsPresenter();
    }
}
