package kg.geektech.taskapp36.ui.on_board;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;

import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentBoardBinding;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;


public class BoardFragment extends Fragment {
    private FragmentBoardBinding binding;
    BoardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBoardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new BoardAdapter();
        binding.viewPager.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigateUp();
            }

            @Override
            public void onLongClick(int position) {

            }
        });

        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayoutBoard, binding.viewPager, ((tab, position) -> {

        })).attach();
    }
}