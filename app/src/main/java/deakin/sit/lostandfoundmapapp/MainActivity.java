package deakin.sit.lostandfoundmapapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    FragmentContainerView fragmentContainerView;
    FragmentManager fragmentManager;

    HomeFragment homeFragment;
    CreateNewAdvertFragment createNewAdvertFragment;
    AllLostAndFoundFragment allLostAndFoundFragment;
    PostDetailFragment postDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fragmentContainerView = findViewById(R.id.fragmentContainerView);
        homeFragment = new HomeFragment();
        createNewAdvertFragment = new CreateNewAdvertFragment();
        allLostAndFoundFragment = new AllLostAndFoundFragment();
        postDetailFragment = new PostDetailFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, homeFragment).commit();
    }

    public void toHomeFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, homeFragment)
                .commit();
    }

    public void toCreateAdvertFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, createNewAdvertFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public void toALlLostAndFoundFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, allLostAndFoundFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public void toPostDetailFragment(Bundle bundle) {
        postDetailFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, postDetailFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }
}