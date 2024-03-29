package kg.geektech.taskapp36.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentDashboardBinding;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.models.Task;
import kg.geektech.taskapp36.ui.home.TaskAdapter;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private TaskAdapter adapter;
    private FirebaseFirestore db;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        adapter = new TaskAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Task task = adapter.getItem(position);
                openFragment(task);
            }

            @Override
            public void onLongClick(int position) {
                Task task = adapter.getItem(position);
                db.collection("tasks").document(task.getDocId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            String deleted = task.getText() + " deleted";
                            Toast.makeText(requireActivity(), deleted, Toast.LENGTH_SHORT).show();
                            adapter.removeItem(position);
                        });
            }
        });
    }

    private void openFragment(Task task) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", task);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.task_fragment, bundle);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setAdapter(adapter);

        getBroadcastRec();
    }

    private void getBroadcastRec() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (isOnline(context)) {
                        Toast.makeText(context, "Подключено к интернету", Toast.LENGTH_SHORT).show();
                        dialog(true);
                    } else {
                        Toast.makeText(context, "Интернет отключен", Toast.LENGTH_SHORT).show();
                        dialog(false);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public boolean isOnline(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void getDataLive() {
        db.collection("tasks")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<Task> list = new ArrayList<>();
                        for (DocumentSnapshot snapshot : value) {
                            Task task = snapshot.toObject(Task.class);
                            assert task != null;
                            task.setDocId(snapshot.getId());
                            list.add(task);
                        }
//                    List<Task>list = snapshots.toObjects(Task.class);
                        adapter.addItems(list);
                    }
                });
    }

    private void getData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);

        db.collection("tasks")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(snapshots -> {
                    List<Task> list = new ArrayList<>();
                    for (DocumentSnapshot snapshot : snapshots) {
                        Task task = snapshot.toObject(Task.class);
                        assert task != null;
                        task.setDocId(snapshot.getId());
                        list.add(task);
                    }
//                    List<Task>list = snapshots.toObjects(Task.class);
                    adapter.addItems(list);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                });
    }

    public void dialog(boolean value) {
        if (value) {
            binding.tvNoInternet.setVisibility(View.GONE);
            getData();
        } else {
            binding.tvNoInternet.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        unRegisterNetwork();
    }

    private void unRegisterNetwork() {
        try {
            getActivity().unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}