package com.mristudio.blooddonation.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mristudio.blooddonation.R;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class PostARequestActivity extends AppCompatActivity {

    private EditText areaofCityET, relationShipET;
    private Spinner bloodGroupSP;
    private CheckBox cheakBox;
    private TextView shedulledTVButton;
    private Button pulishPostButton;
    private ArrayAdapter<String> mainSpAdapter;
    private Toolbar toolbar;
    private TextView adminHomePageTittle, cscheduleTime;
    private ImageButton adminToolBarBack, selectDateIB;
    private String bloodname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_a_request);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adminHomePageTittle = toolbar.findViewById(R.id.adminHomePageTittle);
        adminToolBarBack = toolbar.findViewById(R.id.adminToolBarBack);
        adminHomePageTittle.setText("Post A Request");
        initView();
    }

    private void initView() {

        areaofCityET = findViewById(R.id.areaofCityET);
        relationShipET = findViewById(R.id.relationShipET);
        bloodGroupSP = findViewById(R.id.bloodGroupSP);
        cheakBox = findViewById(R.id.cheakBox);
        shedulledTVButton = findViewById(R.id.shedulledTVButton);
        cscheduleTime = findViewById(R.id.cscheduleTime);
        selectDateIB = findViewById(R.id.selectDateIB);
        pulishPostButton = findViewById(R.id.pulishPostButton);
        initDatePicker();

        List<String> bloodNames = new ArrayList<>();
        bloodNames.add("A+");
        bloodNames.add("A-");
        bloodNames.add("B+");
        bloodNames.add("B-");
        bloodNames.add("O+");
        bloodNames.add("O-");
        bloodNames.add("AB+");
        bloodNames.add("AB-");
        mainSpAdapter = new ArrayAdapter<String>(PostARequestActivity.this, R.layout.spinner_text, bloodNames);
        mainSpAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        bloodGroupSP.setSelection(0);
        bloodGroupSP.setAdapter(mainSpAdapter);

        adminToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bloodGroupSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    Toasty.success(PostARequestActivity.this, "" + bloodNames.get(position), Toast.LENGTH_SHORT, true).show();

                bloodname = bloodNames.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        pulishPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = areaofCityET.getText().toString().trim();
                String relationShip = relationShipET.getText().toString().trim();
                if (cheakSignUpValidationTwo(area, relationShip, bloodname)) {

                }
            }
        });

        shedulledTVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectDialog();
            }
        });

    }

    private void openSelectDialog() {

        AlertDialog.Builder mBuilder=  new AlertDialog.Builder(getApplicationContext());
    }

    /**
     * Cheak  Validation
     */
    private void initDatePicker() {

    }

    /**
     * Cheak  Validation
     */
    private boolean cheakSignUpValidationTwo(String area, String relationship, String bloodGroup) {

        boolean valid = true;

        areaofCityET.setError(null);
        relationShipET.setError(null);

        if (area.isEmpty()) {
            areaofCityET.setError("Empty field not allowed");
            valid = false;

        } else if (bloodGroup.isEmpty()) {
            Toasty.warning(PostARequestActivity.this, "Plese Select Blood Group !", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (relationship.isEmpty()) {
            relationShipET.setError("Empty field not allowed");
            valid = false;
        }

        return valid;
    }

}