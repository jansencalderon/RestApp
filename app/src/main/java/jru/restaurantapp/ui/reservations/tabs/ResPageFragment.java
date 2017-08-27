package jru.restaurantapp.ui.reservations.tabs;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.realm.Realm;
import jru.restaurantapp.R;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.databinding.DialogMarkerClickBinding;
import jru.restaurantapp.databinding.DialogReservationBinding;
import jru.restaurantapp.databinding.FragmentResPageBinding;
import jru.restaurantapp.model.data.Reservation;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.ui.map.MapActivity;

/**
 * Created by Sen on 2/28/2017.
 */

public class ResPageFragment extends MvpFragment<ResPageView, ResPagePresenter> implements ResPageView {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_TYPE = "ARG_TYPE";
    private static final String TAG = ResPageFragment.class.getSimpleName();
    private List<Reservation> list;
    private int mPage;
    private String type;
    private FragmentResPageBinding binding;
    private Realm realm;
    private ReservationListAdapter adapter;

    public static ResPageFragment newInstance(int page, String s) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_TYPE, s);
        ResPageFragment mainPageFragment = new ResPageFragment();
        mainPageFragment.setArguments(args);
        return mainPageFragment;
    }

    @NonNull
    @Override
    public ResPagePresenter createPresenter() {
        return new ResPagePresenter();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        type = getArguments().getString(ARG_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_res_page, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ReservationListAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onStart(type);
    }

    @Override
    public void setEvents(List<Reservation> list) {
        adapter.setList(list);
        checkResult(list.size());
    }


    @Override
    public void internet(Boolean status) {
        if (status) {
            binding.noInternet.noInternetLayout.setVisibility(View.VISIBLE);
        } else {
            binding.noInternet.noInternetLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkResult(int count) {
        if (type.equals("Today")) {
            binding.noResult.resultText.setText("No Reservations for Today\nSee Upcoming");
        } else {
            binding.noResult.resultText.setText("No Upcoming Reservations");
        }
        if (count > 0) {
            binding.noResult.noResultLayout.setVisibility(View.GONE);
        } else {
            binding.noResult.noResultLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnItemClicked(Reservation reservation) {
        if(!type.equals("Upcoming")){
            return;
        }
        DialogReservationBinding binding = DataBindingUtil.inflate(
                getActivity().getLayoutInflater(),
                R.layout.dialog_reservation,
                null,
                false);
        binding.setReservation(reservation);

        switch (reservation.getTransStatus()){
            case Constants.STATUS_PENDING:
                binding.status
                        .setTextColor(ContextCompat.getColor(getActivity(),
                                R.color.orange));
                break;
            case Constants.STATUS_ACCEPTED:
                binding.status
                        .setTextColor(ContextCompat.getColor(getActivity(),
                                R.color.greenSuccess));
                break;
            case Constants.STATUS_CANCELED:
                binding.status
                        .setTextColor(ContextCompat.getColor(getActivity(),
                                R.color.redFailed));
                break;
        }

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        dialog.show();
    }


    @Override
    public void showAlert(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onStop();
        realm.close();
    }
}
