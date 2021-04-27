package com.chen.fy.niceshop.main.search.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.fy.niceshop.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<String> list;
    private IClickItem mClickItem;

    public void setList(List<String> list) {
        this.list = list;
    }

    public void setOnClickSearchItem(IClickItem clickItem) {
        this.mClickItem = clickItem;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        holder.tvText.setText(list.get(position));
        holder.itemView.setOnClickListener(v -> {
            mClickItem.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_history_text);
        }
    }
    public interface IClickItem {
        void onClick(int position);
    }

}
