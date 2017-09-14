package jru.restaurantapp.ui.restaurant;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import jru.restaurantapp.R;
import jru.restaurantapp.app.Constants;
import jru.restaurantapp.databinding.ItemProdBinding;
import jru.restaurantapp.model.data.Product;

/**
 * Created by Mark Jansen Calderon on 1/12/2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private RestaurantView restaurantView;
    private List<Product> list;
    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;
    private boolean loading;

    public ProductListAdapter(RestaurantView restaurantView) {
        this.restaurantView = restaurantView;
        list = new ArrayList<>();

    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemProdBinding itemProdBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_prod, parent, false);
        return new ProductListAdapter.ViewHolder(itemProdBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductListAdapter.ViewHolder viewHolder = (ProductListAdapter.ViewHolder) holder;
        viewHolder.itemProdBinding.setProduct(list.get(position));
        viewHolder.itemProdBinding.setView(restaurantView);
        Glide.with(viewHolder.itemView.getContext())
                .load(Constants.URL_IMAGE + list.get(position).getProdImage())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(viewHolder.itemProdBinding.imageView);

    }

    public void setList(List<Product> list) {
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
        private ItemProdBinding itemProdBinding;

        public ViewHolder(ItemProdBinding itemProdBinding) {
            super(itemProdBinding.getRoot());
            this.itemProdBinding = itemProdBinding;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
