package jru.restaurantapp.ui.restaurant.form;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jru.restaurantapp.R;
import jru.restaurantapp.databinding.ItemReservationResBinding;
import jru.restaurantapp.model.data.Reservation;

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public class RestaurantFormListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private RestaurantFormView view;
    private List<Reservation> list;
    private boolean loading;

    public RestaurantFormListAdapter(RestaurantFormView view) {
        this.view = view;
        list = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemReservationResBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_reservation_res, parent, false);
        return new RestaurantFormListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RestaurantFormListAdapter.ViewHolder viewHolder = (RestaurantFormListAdapter.ViewHolder) holder;
        Reservation item = list.get(position);
        viewHolder.binding.setReservation(item);
        viewHolder.binding.setView(view);

    }

    public void setList(List<Reservation> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemReservationResBinding binding;

        public ViewHolder(ItemReservationResBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
