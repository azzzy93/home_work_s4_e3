package kg.geektech.taskapp36.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import kg.geektech.taskapp36.App;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentHomeBinding;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.models.Task;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private TaskAdapter adapter;
    private int pos;
    private boolean b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        adapter = new TaskAdapter();
        adapter.addItems(App.getInstance().getDatabase().taskDao().getAll());
        adapter.sortLastFirst();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                pos = position;
                Task task = adapter.getItem(position);
                openFragment(task);
            }

            @Override
            public void onLongClick(int position) {
                Task task = adapter.getItem(position);
                App.getInstance().getDatabase().taskDao().delete(task);
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
            pos = -1;
            openFragment(null);
        });
    }

    private void openFragment(Task task) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", task);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.task_fragment, bundle);
    }

    private void initList() {
        binding.recyclerView.setAdapter(adapter);

        getParentFragmentManager().setFragmentResultListener("rk_task",
                getViewLifecycleOwner(),
                (requestKey, result) -> {
                    Task task = (Task) result.getSerializable("task");
                    if (pos == -1) {
                        adapter.addItem(task);
                    } else {
                        adapter.updateItem(pos, task);
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sort) {
            if (!b) {
                adapter.sortABC();
                b = true;
            } else {
                adapter.clearList();
                adapter.addItems(App.getInstance().getDatabase().taskDao().getAll());
                adapter.sortLastFirst();
                b = false;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}