package com.corbi.pizzashop;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.corbi.pizzashop.databinding.ActivityMainBinding;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final Set<ChooseButton> chooseButtonSet = new HashSet<>();
    private ChooseButton dessertsButton, pizzaButton, drinksButton;
    private Spinner citiChoice;
    ClickListener listener = new ClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        initFields();
        makeImagesCornersRound();

        pizzaButton.setSelected(true);
    }

    private void makeImagesCornersRound() {
        findViewById(R.id.discount_1).setClipToOutline(true);
        findViewById(R.id.discount_2).setClipToOutline(true);
    }

    private void initFields() {
        dessertsButton = new ChooseButton(findViewById(R.id.button_dessert));
        pizzaButton = new ChooseButton(findViewById(R.id.button_pizza));
        drinksButton = new ChooseButton(findViewById(R.id.button_drinks));
        citiChoice = findViewById(R.id.spinner);
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