package kg.geektech.taskapp36.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task> list;
    private OnItemClickListener onItemClickListener;
    private View view;

    public TaskAdapter() {
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_task, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(holder.getIvImage())
                .load(list.get(position).getImgUri()).into(holder.getIvImage());

        holder.getTextTitle().setText(list.get(position).getText());

        if (position % 2 == 0) {
            view.setBackgroundResource(R.color.red);
        } else {
            view.setBackgroundResource(R.color.green);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void addItems(List<Task> tasks) {
        list.clear();
        list.addAll(tasks);
        notifyDataSetChanged();
    }

    public Task getItem(int position) {
        return list.get(position);
    }









    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitleTask);
            imageView = itemView.findViewById(R.id.ivTask);

            itemView.setOnClickListener(view -> {
                onItemClickListener.onClick(getAdapterPosition());
            });

            itemView.setOnLongClickListener(view -> {
                onItemClickListener.onLongClick(getAdapterPosition());
                return true;
            });

        }

        public ImageView getIvImage() {
            return imageView;
        }

        public TextView getTextTitle() {
            return textTitle;
        }
    }
}
