package kg.geektech.taskapp36.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentHomeBinding;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.models.Task;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private TaskAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TaskAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Bundle setBundle = new Bundle();
                setBundle.putString("keyString", adapter.getStringPosition(position));
                setBundle.putInt("keyInt", position);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.task_fragment, setBundle);
            }

            @Override
            public void onLongClick(int position) {
                adapter.removeItem(position);
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListeners();
        initList();
    }

    private void initListeners() {
        binding.fab.setOnClickListener(view1 -> {
            openFragment();
        });
    }

    private void openFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.task_fragment);
    }

    private void initList() {
        binding.recyclerView.setAdapter(adapter);

        getParentFragmentManager().setFragmentResultListener("rk_task",
                getViewLifecycleOwner(),
                (requestKey, result) -> {
                    Task task = (Task) result.getSerializable("task");
                    Log.e("Home", "result = " + task.getText());
                    adapter.addItem(task);
                });

        getParentFragmentManager().setFragmentResultListener("rk_task1",
                getViewLifecycleOwner(),
                (requestKey, result) -> {
                    adapter.changeString(result.getString("keyString1"), result.getInt("keyInt1"));
                });
    }
}