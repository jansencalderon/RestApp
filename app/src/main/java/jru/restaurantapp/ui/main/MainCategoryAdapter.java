package jru.restaurantapp.ui.main;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jru.restaurantapp.R;
import jru.restaurantapp.databinding.ItemCategoriesBinding;
import jru.restaurantapp.model.data.RestCategory;

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public class MainCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private MainView mainView;
    private List<RestCategory> list;
    private boolean loading;

    public MainCategoryAdapter(MainView mainView) {
        this.mainView = mainView;
        list = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCategoriesBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_categories, parent, false);
        return new MainCategoryAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainCategoryAdapter.ViewHolder viewHolder = (MainCategoryAdapter.ViewHolder) holder;
        viewHolder.binding.setCategory(list.get(position));
        viewHolder.binding.setView(mainView);

    }

    public void setList(List<RestCategory> list) {
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
        private ItemCategoriesBinding binding;

        public ViewHolder(ItemCategoriesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}






