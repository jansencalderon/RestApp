package jru.restaurantapp.ui.main;

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

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public class MainListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private MainView mainView;
    private List<Restaurant> list;
    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private boolean loading;

    public MainListAdapter(MainView mainView) {
        this.mainView = mainView;
        list = new ArrayList<>();

    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardRestBinding cardRestBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.card_rest, parent, false);
        return new MainListAdapter.ViewHolder(cardRestBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainListAdapter.ViewHolder viewHolder = (MainListAdapter.ViewHolder) holder;
        viewHolder.cardRestBinding.setRestaurant(list.get(position));
        viewHolder.cardRestBinding.setView(mainView);
        Glide.with(viewHolder.itemView.getContext())
                .load(Constants.URL_IMAGE + list.get(position).getRestImage().concat(".jpg"))
                .centerCrop()
                .error(R.drawable.ic_gallery)
                .into(viewHolder.cardRestBinding.restImage);

    }

    public void setList(List<Restaurant> list) {
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
        private CardRestBinding cardRestBinding;

        public ViewHolder(CardRestBinding cardRestBinding) {
            super(cardRestBinding.getRoot());
            this.cardRestBinding = cardRestBinding;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
