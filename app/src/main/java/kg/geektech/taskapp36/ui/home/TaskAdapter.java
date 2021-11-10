package kg.geektech.taskapp36.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        holder.bind(list.get(position));

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

    public void addItem(Task task) {
        list.add(0, task);
        notifyItemInserted(list.indexOf(task));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void addItems(List<Task> tasks) {
        list.addAll(tasks);
        notifyDataSetChanged();
    }

    public Task getItem(int position) {
        return list.get(position);
    }

    public void updateItem(Integer pos, Task task) {
        list.set(pos, task);
        notifyItemChanged(pos);
    }

    public void sortABC() {
        Collections.sort(list, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getText().compareTo(o2.getText());
            }
        });
        notifyDataSetChanged();
    }

    public void sortLastFirst() {
        Collections.reverse(list);
        notifyDataSetChanged();
    }

    public void clearList() {
        list.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitleTask);

            itemView.setOnClickListener(view -> {
                onItemClickListener.onClick(getAdapterPosition());
            });

            itemView.setOnLongClickListener(view -> {
                onItemClickListener.onLongClick(getAdapterPosition());
                return true;
            });
        }

        public void bind(Task task) {
            textTitle.setText(task.getText());
        }
    }
}
