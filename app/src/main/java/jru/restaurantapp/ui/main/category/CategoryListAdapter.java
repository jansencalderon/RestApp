package jru.restaurantapp.ui.main.category;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jru.restaurantapp.R;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.databinding.CardRestCatBinding;
import jru.restaurantapp.model.data.Restaurant;
import jru.restaurantapp.ui.main.category.CategoryView;

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private CategoryView view;
    private List<Restaurant> list;
    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private boolean loading;

    public CategoryListAdapter(CategoryView view) {
        this.view = view;
        list = new ArrayList<>();

    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardRestCatBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.card_rest_cat, parent, false);
        return new CategoryListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryListAdapter.ViewHolder viewHolder = (CategoryListAdapter.ViewHolder) holder;
        viewHolder.binding.setRestaurant(list.get(position));
        viewHolder.binding.setView(view);
        Glide.with(viewHolder.itemView.getContext())
                .load(Constants.URL_IMAGE + list.get(position).getRestImage().concat(".jpg"))
                .centerCrop()
                .into(viewHolder.binding.restImage);

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
        private CardRestCatBinding binding;

        public ViewHolder(CardRestCatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
