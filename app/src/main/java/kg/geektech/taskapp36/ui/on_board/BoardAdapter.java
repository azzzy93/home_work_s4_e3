package kg.geektech.taskapp36.ui.on_board;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    private String[] titles = new String[]{"Салам", "Привет", "Hello"};
    private String[] desc = new String[]{"Кош келиниз!", "Добро пожаловать!", "Welcome!"};
    private Integer[] img = new Integer[]{R.raw.city, R.raw.city2, R.raw.city3};
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textDesc;
        private LottieAnimationView lottieAnimationView;
        private Button btnStart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitleBoard);
            textDesc = itemView.findViewById(R.id.textDesc);
            lottieAnimationView = itemView.findViewById(R.id.animation_view);
            btnStart = itemView.findViewById(R.id.btnStart);
            btnStart.setOnClickListener(view -> {
                onItemClickListener.onClick(getAdapterPosition());
            });
        }

        public void bind(int position) {
            textTitle.setText(titles[position]);
            textDesc.setText(desc[position]);
            lottieAnimationView.setAnimation(img[position]);

            if (position == titles.length - 1) {
                btnStart.setVisibility(View.VISIBLE);
            } else {
                btnStart.setVisibility(View.INVISIBLE);
            }
        }
    }
}
