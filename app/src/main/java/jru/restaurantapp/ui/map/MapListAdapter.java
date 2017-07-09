package jru.restaurantapp.ui.map;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jru.restaurantapp.R;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.databinding.CardRestBinding;
import jru.restaurantapp.databinding.ItemNearestBinding;
import jru.restaurantapp.model.data.NearestRestaurant;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.ui.main.MainView;

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public class MapListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private MapView mapView;
    private List<NearestRestaurant> list;
    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private boolean loading;

    public MapListAdapter(MapView mapView) {
        this.mapView = mapView;
        list = new ArrayList<>();

    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         ItemNearestBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_nearest, parent, false);
        return new MapListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MapListAdapter.ViewHolder viewHolder = (MapListAdapter.ViewHolder) holder;
        viewHolder.binding.setRestaurant(list.get(position));
        viewHolder.binding.setView(mapView);

    }

    public void setList(List<NearestRestaurant> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemNearestBinding binding;

        public ViewHolder(ItemNearestBinding binding) {
            super(binding.getRoot());
            this.binding= binding;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
