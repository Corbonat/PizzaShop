package com.corbi.pizzashop;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.corbi.pizzashop.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final Set<ChooseButton> chooseButtonSet = new HashSet<>();
    private ChooseButton dessertsButton, pizzaButton, drinksButton;
    private Spinner citiChoice;
    RecyclerView productRecyclerView;
    ProductAdapter productAdapter;
    ClickListener listener = new ClickListener();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        initFields();
        roundImagesCorners();

        recyclerView = findViewById(R.id.product_recycler_view);


        productAdapter = new ProductAdapter();

        recyclerView.setAdapter(productAdapter);


        pizzaButton.setSelected(true);
    }

    private void roundImagesCorners() {
        findViewById(R.id.discount_1).setClipToOutline(true);
        findViewById(R.id.discount_2).setClipToOutline(true);
    }

    private void initFields() {
        dessertsButton = new ChooseButton(findViewById(R.id.button_dessert));
        pizzaButton = new ChooseButton(findViewById(R.id.button_pizza));
        drinksButton = new ChooseButton(findViewById(R.id.button_drinks));
        citiChoice = findViewById(R.id.citi_choice_spinner);
    }

    private ChooseButton getChooseButton(Button button) {
        for (ChooseButton chooseButton : chooseButtonSet) {
            if (chooseButton.button.equals(button)) return chooseButton;
        }
        throw new NullPointerException("Button " + button + " wasn't found");
    }

    private void chooseCategory(ChooseButton button) {
        for (ChooseButton chooseButton : chooseButtonSet) {
            chooseButton.setSelected(false);
        }

        button.setSelected(true);
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            chooseCategory(getChooseButton(((Button) v)));
        }
    }

    public class ChooseButton {
        public final Button button;
        public final ColorStateList defaultColor;
        public final ColorStateList selectedColor;
        public final int defaultTextColor;
        public final int selectedTextColor;

        public ChooseButton(Button button) {
            this.button = button;

            defaultColor = button.getBackgroundTintList();
            selectedColor = ColorStateList.valueOf(getColor(R.color.white_pink));

            defaultTextColor = button.getCurrentTextColor();
            selectedTextColor = getColor(R.color.white);

            button.setOnClickListener(listener);
            chooseButtonSet.add(this);
        }

        @SuppressLint("ResourceAsColor")
        public void setSelected(boolean is) {
            button.setTextColor(is ? selectedTextColor : defaultTextColor);
            button.setBackgroundTintList(is ? selectedColor : defaultColor);
        }
    }
}