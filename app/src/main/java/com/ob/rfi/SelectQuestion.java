package com.ob.rfi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.multispinner.MultiSelectSpinner;
import com.ob.database.db_tables.BuildingAllocateTaskModel;
import com.ob.database.db_tables.CheckListAllocateTaskModel;
import com.ob.database.db_tables.ClientAllocateTaskModel;
import com.ob.database.db_tables.FloorAllocateTaskModel;
import com.ob.database.db_tables.GroupListAllocateTaskModel;
import com.ob.database.db_tables.ProjectAllocateTaskModel;
import com.ob.database.db_tables.SubUnitAllocateTaskModel;
import com.ob.database.db_tables.UnitAllocateTaskModel;
import com.ob.database.db_tables.WorkTypeAllocateTaskModel;
import com.ob.rfi.db.RfiDatabase;
import com.ob.select_questions.viewmodels.SelectQuestionViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("static-access")
public class SelectQuestion extends CustomTitle {

    private Spinner checklistspin;
    public static CustomTitle slectionclass;
    private Integer[] schemId;
    private Button okBtn;
    private Button backBtn;
    private String errorMessage;
    private RfiDatabase db;
    private String[] clientName;
    private Integer[] clientId;
    private Spinner projSpin;
    private Spinner clintSpin;
    private Spinner structureSpin;
    private Spinner stageSpin;
    private Spinner unitSpin;
    private TextView subunitspin;

    private Spinner grouptspin;

    private EditText coverageedit;
    private Spinner worktypeSpin;
    private String[] wTypeId;
    private String[] wTypeLevelId;
    private EditText drawingeedit;
    private String[] scroll_status;
    public static int nval;
    public int noRfi = 0;
    public static CustomTitle destruct;
    public static final String TAG = "SelectScreen";
    public static boolean insertFlag = false;
    boolean isGroup = false;
    private boolean isGroupDataAvailable = false;
    private SelectQuestionViewModel viewModel;
    private String statusValue = "";
    private String cov;
    private SharedPreferences checkPreferences;
    private Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SelectQuestionViewModel.class);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.select_question);
        // title.setText("CQRA Question selection");
        destruct = this;
        slectionclass = this;
        MultiSelectSpinner multiSelectSpinner = new MultiSelectSpinner(getApplicationContext());
        //	rfiSpin = (Spinner) findViewById(R.id.rfi_id);
        clintSpin = (Spinner) findViewById(R.id.rfi_client_id);
        projSpin = (Spinner) findViewById(R.id.rfi_project_id);
        worktypeSpin = (Spinner) findViewById(R.id.rfi_worktype_id);
        structureSpin = (Spinner) findViewById(R.id.rfi_structure_id);
        stageSpin = (Spinner) findViewById(R.id.rfi_statge_id);
        unitSpin = (Spinner) findViewById(R.id.rfi_unit_id);
        subunitspin = findViewById(R.id.rfi_sub_unit_id);
        checklistspin = (Spinner) findViewById(R.id.rfi_checklist_id);
        grouptspin = (Spinner) findViewById(R.id.rfi_group_id);

        coverageedit = (EditText) findViewById(R.id.coverage_id);

        drawingeedit = (EditText) findViewById(R.id.drawing_id);
        RadioGroup rgStatus = findViewById(R.id.rgStatus);
        checkPreferences = getSharedPreferences("RFI_File",
                MODE_PRIVATE);
        editor = checkPreferences.edit();

        rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedId = group.getCheckedRadioButtonId();

            // Find the radio button by the selected ID
            RadioButton selectedRadioButton = findViewById(selectedId);

            if (selectedRadioButton != null) {
                // Get the text of the selected radio button
                statusValue = selectedRadioButton.getText().toString();
                if (statusValue.equalsIgnoreCase("Partial RFI")){
                    displayErrorDialog("Status","RFI Status Partially Completed: The work with the selected stage is not yet fully completed. RFI can be created again for the same selection.");
                }else {
                    displayErrorDialog("Status","RFI Status Fully Completed: The work with the selected stage is fully completed. RFI cannot be created again for the same selection.");
                }
                //Toast.makeText(this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
            } else {
                statusValue = "";
                Toast.makeText(this, "No option selected", Toast.LENGTH_SHORT).show();
            }
        });


       /* int a=0;
        db.questionSubmitCount= String.valueOf(a);
        System.out.println("Before submit clicked--- "+db.questionSubmitCount);*/

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                char[] chars = {'\'', '"', '@', '$', '&', '?', '!', '*', '<', '>', '~', '^'};
                for (int i = start; i < end; i++) {
                    if (new String(chars).contains(String.valueOf(source.charAt(i)))) {
                        return "";
                    }
                }
                return null;
            }
        };
        coverageedit.setFilters(new InputFilter[]{filter});
        drawingeedit.setFilters(new InputFilter[]{filter});


        ResetId();
        setupLiveData();
        viewModel.getCreatedRFI();

        okBtn = (Button) findViewById(R.id.question_select_submit);
        backBtn = (Button) findViewById(R.id.question_select_Back);

        db = new RfiDatabase(getApplicationContext());

        okBtn.setOnClickListener(new OnClickListener() {
            private SharedPreferences checkPreferences;

            @Override
            public void onClick(View arg0) {

                cov = coverageedit.getText().toString();
                if (cov.isEmpty()) {
                    displayErrorDialog("Error", "Please Enter Coverage.");
                    return;
                }else if (viewModel.isCoveragePresent(cov)){
                    displayErrorDialog("Error", "Coverage already present.");
                    return;
                }else if (validateScreen()) {
                    displayErrorDialog("Error", errorMessage);
                    return;
                }else if (!isGroupDataAvailable) {
                    displayErrorDialog("Error", "Please select group");
                    return;
                }else if (statusValue.isEmpty()) {
                    displayErrorDialog("Error", "Please select Status");
                    return;
                }

                viewModel.checkIsQuestionAvailable();
            }

        });

        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /***** AKSHAY SHAH *****/
        // CHANGE MADE ON : 12-MAY-2015
        /*
         * if(!db.userId.equalsIgnoreCase("")) { if(isDataAvialable("Client")){
         * setRFIData(); //setClientData(); }else { updateData(); }
         * //disableList(false); } else { logout(); }
         *//***** AKSHAY SHAH *****/

        if (!db.userId.equalsIgnoreCase("")) {

            //if (isDataAvialable("Client") && isDataAvialableAllocateTask("AllocateTask")) {

            viewModel.getClientAllocateDataFromDB();
            //setClientData();
            clintSpin.setClickable(true);
            clintSpin.setSelection(1);// changed pramod
            //}
        }

    }

    private void setupLiveData() {
        Objects.requireNonNull(viewModel.getLvClientAllocateData()).observe(
                this, value -> {
                    Log.d(TAG, "getLvClientAllocateData: value: " + value);

                    if (value != 0) {
                        setClientData();
                    } else {
                        displayErrorDialog("Error", "No Data");
                    }
                }
        );
        Objects.requireNonNull(viewModel.getLvSchemaAllocateData()).observe(
                this, value -> {
                    Log.d(TAG, "getLvSchemaAllocateData: value: " + value);

                    if (value != 0) {
                        setSchemeSpinnerData();
                    } else {
                        displayErrorDialog("Error", "No Data");
                    }
                }
        );
        Objects.requireNonNull(viewModel.getLvWorkTypeAllocateData()).observe(
                this, value -> {
                    Log.d(TAG, "getLvWorkTypeAllocateData: value: " + value);

                    if (value != 0) {
                        setWorkTypeSpinnerData();
                    } else {
                        displayErrorDialog("Error", "No Data");
                    }
                }
        );
        Objects.requireNonNull(viewModel.getLvBuildingAllocateData()).observe(
                this, value -> {
                    Log.d(TAG, "getLvBuildingAllocateData: value: " + value);

                    if (value != 0) {
                        setBuildingSpinnerData();
                    } else {
                        displayErrorDialog("Error", "No Data");
                    }
                }
        );
        Objects.requireNonNull(viewModel.getLvFloorAllocateData()).observe(
                this, value -> {
                    Log.d(TAG, "getLvFloorAllocateData: value: " + value);

                    if (value != 0) {
                        setFloorSpinnerData();
                    } else {
                        displayErrorDialog("Error", "No Data");
                    }
                }
        );
        Objects.requireNonNull(viewModel.getLvUnitAllocateData()).observe(
                this, value -> {
                    Log.d(TAG, "getLvUnitAllocateData: value: " + value);

                    if (value != 0) {
                        setUnitSpinnerData();
                    } else {
                        viewModel.getCheckListAllocateDataFromDB();
                        unitSpin.setVisibility(View.GONE);
                    }
                }
        );
        Objects.requireNonNull(viewModel.getLvSubUnitAllocateData()).observe(
                this, value -> {
                    Log.d(TAG, "getLvSubUnitAllocateData: value: " + value);

                    if (value != 0) {
                        setSubUnitSpinnerData();
                    } else {
                        viewModel.getCheckListAllocateDataFromDB();
                       // subunitspin.setVisibility(View.GONE);
                        //displayErrorDialog("Error","No Data");
                    }
                }
        );
        Objects.requireNonNull(viewModel.getLvCheckListAllocateData()).observe(
                this, value -> {
                    Log.d(TAG, "getLvCheckListAllocateData: value: " + value);
                    if (value != 0) {
                        setCheckListSpinnerData();
                    } else {
                        displayErrorDialog("Error", "No Data");
                    }
                }
        );
        Objects.requireNonNull(viewModel.getLvGroupListAllocateData()).observe(
                this, value -> {
                    Log.d(TAG, "getLvGroupListAllocateData: value: " + value);
                    if (value != 0) {
                        setGroupSpinnerData();
                    } else {
                        displayErrorDialog("Error", "No Data");
                    }
                }
        );
        Objects.requireNonNull(viewModel.getLvQuestionsData()).observe(
                this, value -> {
                    Log.d(TAG, "getLvGroupListAllocateData: value: " + value);
                    if (value != 0) {
                        viewModel.insertCreatedRFI(cov);
                        Intent int1 = new Intent(SelectQuestion.this,
                                RfiQuestionSelect.class);
                        int1.putExtra("coverage", cov);
                        int1.putExtra("drawingId", drawingeedit.getText()
                                .toString());
                        int1.putExtra("coverage", cov);

                        editor.putString("coverage", cov);
                        editor.putString("drawing", drawingeedit.getText()
                                .toString());
                        editor.commit();

                        startActivity(int1);
                    } else {
                        displayErrorDialog("Error", "Please Select Another Combination!");
                    }
                }
        );

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setSpiner();
        if (db.userId.equalsIgnoreCase("")) {
            //logout();
        }
    }

    public void setSpiner() {
        if (db.setSpinner) {
            clintSpin.setSelection(0);
            projSpin.setSelection(0);
            structureSpin.setSelection(0);
            stageSpin.setSelection(0);
            unitSpin.setSelection(0);
            subunitspin.setText("");
            checklistspin.setSelection(0);
            grouptspin.setSelection(0);
            db.setSpinner = false;
            System.out.println("setspinner");

        }

    }

    protected void clearData() {
        db.selectedGroupId = "";
        db.selectedGroupName = "";
        db.selectedSchemeId = "";
        db.selectedBuildingId = "";
        db.selectedFloorId = "";
        db.selectedChecklistId = "";
        db.selectedSubGroupId = "";
        db.selectedHeadingId = "";
        db.selectedQuestionId = "";
        db.selectedTradeId = "";
        db.selectedSupervisorId = "";
        db.selectedForemanId = "";
        db.selectedClientId = "";
        db.selectedScrollStatus = "";

    }

    protected boolean validateScreen() {
        boolean validate = true;
        if (drawingeedit.getText().toString().equalsIgnoreCase("")) {
            errorMessage = "Please Enter Drawing No.";
            return true;
        } else {
            validate = false;
        }
        return validate;
    }

    @SuppressWarnings("deprecation")
    public void displayErrorDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    // /---client
    private void setClientData() {

        insertFlag = false;
        /***** AKSHAY SHAH *****/
        //CHANGES MADE ON 13-MAY-2015
	/*	String where = "user_id='" + db.userId + "'";

		// Client_ID TEXT,Clnt_Name TEXT,CL_Dispaly_Name TEXT

		System.out.println("cleint where -==========>" + where);
		Cursor cursor = db.select("Client as ct",
				"distinct(ct.Client_ID),ct.Clnt_Name", where, null, null, null,
				null);

		clientId = new String[cursor.getCount()];
		client = new String[cursor.getCount() + 1];

		client[0] = "--select--";

		if (cursor.moveToFirst()) {

			do {
				System.out.println("nnnnnnnnnnnnnnn");
				clientId[cursor.getPosition()] = cursor.getString(0);
				client[cursor.getPosition() + 1] = cursor.getString(1);
			} while (cursor.moveToNext());
		} else {
			client[0] = "Client not allocated";

		}

		if (cursor != null && !cursor.isClosed()) {

			cursor.close();
		}		*/

        ArrayList<ClientAllocateTaskModel> list = viewModel.getListOfClientAllocateTaskModel();
        int size = list.size();
        clientId = new Integer[size];
        clientName = new String[size];
        for (int i = 0; i < size; i++) {
            clientName[i] = list.get(i).getClientName();
            clientId[i] = list.get(i).getClientId();
        }

        try {


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, clientName);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            clintSpin.setAdapter(adapter);
            clintSpin.setSelection(2);// changed by pramod
            adapter.notifyDataSetChanged();
            clintSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> aview, View view,
                                           int position, long rowid) {
                    System.out.println("position" + position);
                    if (position >= 1) {

                        db.selectedclientname = clintSpin.getSelectedItem()
                                .toString();
                        System.out.println("client selected not present");
                        db.selectedClientId = clientId[position].toString();
                        viewModel.getSchemaOrProjectData();

                        //setSchemeSpinnerData();
                        worktypeSpin.setSelection(0);
                        worktypeSpin.setClickable(false);
                        projSpin.setSelection(1);
                        projSpin.setClickable(true);

                        // db.selectedClient = client[position];

                    } else {
                        worktypeSpin.setSelection(0);
                        worktypeSpin.setClickable(false);
                        projSpin.setSelection(1);
                        projSpin.setClickable(false);

                        db.selectedClientId = "";

                        try {
                            db.selectedclientname = clintSpin.getSelectedItem().toString();
                        } catch (Exception e) {

                        }

                    }
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        } catch (Exception e) {


        }
    }

    private void setSchemeSpinnerData() {

        ArrayList<ProjectAllocateTaskModel> list = viewModel.getListOfSchemaAllocateTaskModel();
        int size = list.size();
        schemId = new Integer[size];
        final String[] scrolling_status = new String[size];
        String[] schemeName = new String[size];
        for (int i = 0; i < size; i++) {
            ProjectAllocateTaskModel model = list.get(i);
            schemeName[i] = model.getSchemaOrProjectName();
            schemId[i] = model.getSchemaOrProjectId();
            scrolling_status[i] = model.getScrollingStatus();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, schemeName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projSpin.setAdapter(adapter);
        projSpin.setSelection(1);//changed pramod

        projSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedSchemeId = schemId[position].toString();
                    db.selectedScrollStatus = scrolling_status[position];
                    viewModel.getWorkTypeAllocatedData();
                    worktypeSpin.setClickable(true);

                    db.selectedSchemeName = projSpin.getSelectedItem()
                            .toString();
                    System.out.println("heloooooooo===="
                            + db.selectedSchemeName);
                } else {
                    db.selectedSchemeId = "";
                    db.selectedScrollStatus = "";
                    db.selectedBuildingId = "";
                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    worktypeSpin.setSelection(0);
                    worktypeSpin.setClickable(false);
                    structureSpin.setClickable(false);
                    structureSpin.setSelection(0);
                    checklistspin.setClickable(false);
                    checklistspin.setSelection(0);

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setWorkTypeSpinnerData() {
        ArrayList<WorkTypeAllocateTaskModel> list = viewModel.getListOfWorkTypeAllocateTaskModel();
        int size = list.size();
        final Integer[] workTypeID = new Integer[size];
        String[] workTypeName = new String[size];
        for (int i = 0; i < size; i++) {
            WorkTypeAllocateTaskModel model = list.get(i);
            workTypeName[i] = model.getWorkTypeName();
            workTypeID[i] = model.getWorkTypeId();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, workTypeName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        worktypeSpin.setAdapter(adapter);

        worktypeSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedWorkTypeId = workTypeID[position].toString();
                    System.out
                            .println("***** Inside workTypeSpin.onItemSelectedListener *****");
                    // db.selectedSchemeName=projSpin.getSelectedItem().toString();
                    System.out.println("db.selectedWorkTypeId : "
                            + db.selectedWorkTypeId);
                    viewModel.getBuildingDataFromDB();
                    //setBulidngSpinnerData();
                    structureSpin.setClickable(true);
                    structureSpin.setSelection(0);
                    System.out.println("SelectedLevelID : "
                            + db.selectedlevelId);
                    System.out.println("SelectedWorkTypeID : "
                            + db.selectedWorkTypeId);
                    System.out.println("SelectedSchemeID : "
                            + db.selectedSchemeId);

                } else {
                    db.selectedWorkTypeId = "";
                    db.selectedBuildingId = "";
                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedlevelId = "";
                    structureSpin.setClickable(false);
                    structureSpin.setSelection(0);
                    checklistspin.setClickable(false);
                    checklistspin.setSelection(0);

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setBuildingSpinnerData() {

        ArrayList<BuildingAllocateTaskModel> list = viewModel.getListOfBuildingAllocateTaskModel();
        int size = list.size();
        final Integer[] buildingID = new Integer[size];
        String[] buildingName = new String[size];
        for (int i = 0; i < size; i++) {
            BuildingAllocateTaskModel model = list.get(i);
            buildingName[i] = model.getBuildingName();
            buildingID[i] = model.getBuildingId();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, buildingName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        structureSpin.setAdapter(adapter);
        structureSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position > 0) {
                    db.selectedBuildingId = buildingID[position].toString();
                    db.selectedNodeId = buildingID[position].toString();
                    viewModel.getFloorDataFromDB();

//                    setFloorSpinnerData(buildingID[position - 1], schemeid);
                    stageSpin.setClickable(true);
                    stageSpin.setSelection(0);

                } else {
                    stageSpin.setClickable(false);
                    stageSpin.setSelection(0);
                    unitSpin.setClickable(false);
                    unitSpin.setSelection(0);
                    subunitspin.setText("");
                    checklistspin.setClickable(false);
                    checklistspin.setSelection(0);

                    db.selectedBuildingId = "";
                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedFloorId = "";
                    db.selectedUnitId = "";
                    db.selectedSubUnitId = "";
                    db.selectedElementId = "";
                    db.selectedSubElementId = "";

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setFloorSpinnerData() {

        ArrayList<FloorAllocateTaskModel> list = viewModel.getListOfFloorAllocateTaskModel();
        int size = list.size();
        final Integer[] floorID = new Integer[size];
        String[] floorName = new String[size];
        for (int i = 0; i < size; i++) {
            FloorAllocateTaskModel model = list.get(i);
            floorName[i] = model.getFloorName();
            floorID[i] = model.getFloorId();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, floorName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stageSpin.setAdapter(adapter);

        stageSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position > 0) {
                    db.selectedFloorId = floorID[position].toString();
                    db.selectedNodeId = floorID[position].toString();
                    viewModel.getUnitDataFromDB();
                    /*
                    Cursor cursor = db.select("Unit u, AllocateTask a", "distinct(u.Unit_Des),u.Unit_ID", "u.Unit_ID = a.Unit AND u.Fk_Floor_ID = '" + db.selectedFloorId + "' AND u.Unit_Scheme_ID = '" + db.selectedSchemeId + "'", null, null, null, null);
                    String[] unitName = new String[cursor.getCount() + 1];
                    if (cursor.getCount() > 0) {
                        setUnitSpinnerData(db.selectedFloorId, db.selectedSchemeId);
                        unitSpin.setClickable(true);
                        unitSpin.setSelection(0);
                    } else {
                        setCheckListSpinnerData();
                        checklistspin.setClickable(true);
                        checklistspin.setSelection(0);
                    }*/
                } else {
                    unitSpin.setClickable(false);
                    unitSpin.setSelection(0);
                    subunitspin.setText("");
                    checklistspin.setClickable(false);
                    checklistspin.setSelection(0);

                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedFloorId = "";
                    db.selectedUnitId = "";
                    db.selectedSubUnitId = "";
                    db.selectedElementId = "";
                    db.selectedSubElementId = "";

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setUnitSpinnerData() {

        ArrayList<UnitAllocateTaskModel> list = viewModel.getListOfUnitAllocateTaskModel();
        int size = list.size();
        final Integer[] unitID = new Integer[size];
        String[] unitName = new String[size];
        for (int i = 0; i < size; i++) {
            UnitAllocateTaskModel model = list.get(i);
            unitName[i] = model.getUnitName();
            unitID[i] = model.getUnitId();
        }
        if (size == 0) {
            checklistspin.setClickable(true);
            checklistspin.setSelection(0);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, unitName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        unitSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedUnitId = unitID[position].toString();
                    db.selectedNodeId = unitID[position].toString();
                    coverageedit.setText(unitName[position]);
                    viewModel.getSubUnitAllocateDataFromDB();
                } else {
                    subunitspin.setText("");
                    checklistspin.setClickable(false);
                    checklistspin.setSelection(0);

                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedUnitId = "";
                    db.selectedSubUnitId = "";
                    db.selectedElementId = "";
                    db.selectedSubElementId = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        /*if (isUnit && unitName.length > 0) {
            unitSpin.setVisibility(View.GONE);
            unitSpinMulti.setVisibility(View.VISIBLE);

            unitSpinMulti.setItems(unitName);
            unitSpinMulti.hasNoneOption(true);
            unitSpinMulti.setSelection(new int[]{0});
            unitSpinMulti.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
                @Override
                public void selectedIndices(List<Integer> indices) {
                    db.selectedNodeId = "";
                    db.selectedUnitId = "";
                    for (int count = 0; count < indices.size(); count++) {
                        if (count == indices.size() - 1) {
                            db.selectedNodeId += unitID[indices.get(count) - 1];

                            db.selectedUnitId += unitID[indices.get(count) - 1];
                        } else {
                            db.selectedNodeId += unitID[indices.get(count) - 1] + ",";
                            db.selectedUnitId += unitID[indices.get(count) - 1] + ",";
                        }

                    }
                    setSubUnitSpinnerData(db.selectedUnitId, db.selectedSchemeId);

                }

                @Override
                public void selectedStrings(List<String> strings) {

                }
            });

        } else {
            unitSpin.setVisibility(View.VISIBLE);
            unitSpinMulti.setVisibility(View.GONE);
        }*/


    }

    private void setSubUnitSpinnerData() {

        ArrayList<SubUnitAllocateTaskModel> list = viewModel.getListOfSubUnitAllocateTaskModel();
        int size = list.size();
        final Integer[] subUnitID = new Integer[size];
        String[] subUnitName = new String[size];
        for (int i = 0; i < size; i++) {
            SubUnitAllocateTaskModel model = list.get(i);
            subUnitName[i] = model.getSubUnitName();
            subUnitID[i] = model.getSubUnitId();
        }
        if (size == 0) {
            checklistspin.setClickable(true);
            checklistspin.setSelection(0);
        }
        final boolean[] checkedItems = new boolean[size];
        final List<String> selectedItems = Arrays.asList(subUnitName);
        subunitspin.setOnClickListener(view -> {

            // initialise the alert dialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);

            // set the title for the alert dialog
            builder.setTitle("Select Sub Unit");

            // now this is the function which sets the alert dialog for multiple item selection ready
            builder.setMultiChoiceItems(subUnitName, checkedItems, (dialog, which, isChecked) -> {
                checkedItems[which] = isChecked;
                //String currentItem = selectedItems.get(which);
            });

            // alert dialog shouldn't be cancellable
            builder.setCancelable(false);

            // handle the positive button of the dialog
            builder.setPositiveButton("Done", (dialog, which) -> {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        Log.d(TAG, "setSubUnitSpinnerData: " + (selectedItems.get(i)));
                        stringBuilder.append(selectedItems.get(i));
                        stringBuilder.append(", ");
                    }
                }
                if (checkedItems.length > 0) {
                    Log.d(TAG, "b4 setSubUnitSpinnerData: stringBuilder: " + stringBuilder);
                    if (!stringBuilder.toString().isEmpty()) {
                        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                        Log.d(TAG, "after setSubUnitSpinnerData: stringBuilder: " + stringBuilder);
                        subunitspin.setText(stringBuilder.toString());
                        coverageedit.setText(stringBuilder.toString());
                    } else {
                        subunitspin.setText("Select Sub Unit");
                    }

                } else {
                    subunitspin.setText("Select Sub Unit");
                }
            });

            // handle the negative button of the alert dialog
            builder.setNegativeButton("CANCEL", (dialog, which) -> {
            });

            // handle the neutral button of the dialog to clear the selected items boolean checkedItem
            builder.setNeutralButton("CLEAR ALL", (dialog, which) -> {
                Arrays.fill(checkedItems, false);
                subunitspin.setText("Select Sub Unit");
            });

            // create the builder
            builder.create();

            // create the alert dialog with the alert dialog builder instance
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        /*subunitspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedSubUnitId = subUnitID[position].toString();
                    db.selectedNodeId = subUnitID[position].toString();

                } else {
                    try {
                        db.selectedChecklistId = "";
                        db.selectedSubGroupId = "";
                        db.selectedSubUnitId = "";
                        db.selectedElementId = "";
                        db.selectedSubElementId = "";
                    } catch (Exception e) {
                        System.out.println("exception=====" + e.getMessage());
                    }
                    System.out.println("hello sub unit else");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });*/


        /*if (isSubUnit && subUnitName.length > 0) {
            subunitspin.setVisibility(View.GONE);
            subUnitSpinMulti.setVisibility(View.VISIBLE);
            subUnitSpinMulti.setItems(subUnitName);
            subUnitSpinMulti.hasNoneOption(true);
            subUnitSpinMulti.setSelection(new int[]{0});
            subUnitSpinMulti.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
                @Override
                public void selectedIndices(List<Integer> indices) {
                    db.selectedNodeId = "";
                    db.selectedSubElementId = "";
                    for (int count = 0; count < indices.size(); count++) {
                        if (count == indices.size() - 1) {
                            db.selectedNodeId += subUnitID[indices.get(count) - 1];

                            db.selectedSubUnitId += subUnitID[indices.get(count) - 1];
                        } else {
                            db.selectedNodeId += subUnitID[indices.get(count) - 1] + ",";
                            db.selectedSubUnitId += subUnitID[indices.get(count) - 1] + ",";
                        }

                    }

                }

                @Override
                public void selectedStrings(List<String> strings) {

                }
            });

        } else {
            subunitspin.setVisibility(View.VISIBLE);
            subUnitSpinMulti.setVisibility(View.GONE);
        }*/


    }

    public boolean isDataAvialableInCheckList(String nodeID, String worktypeID,
                                              String userID) {
        Cursor cursor = db.select("CheckList", "count(*) as noitem",
                "user_id='" + db.userId + "' AND Node_Id ='" + nodeID
                        + "' AND FK_WorkTyp_ID = '" + worktypeID + "'", null,
                null, null, null);
        System.out.println("user_id='" + db.userId + "' AND Node_Id ='"
                + nodeID + "' AND FK_WorkTyp_ID = '" + worktypeID + "'");
        if (cursor.moveToFirst()) {
            if (Integer.parseInt(cursor.getString(0)) > 0) {
                Log.d("CheckList", cursor.getString(0));
                cursor.close();
                return true;
            } else {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                return false;
            }
        } else {
            return false;
        }
    }

    // CheckList(Checklist_ID TEXT,Checklist_Name TEXT,Node_Id TEXT, user_id
    // TEXT)");

    private void setCheckListSpinnerData() {
        checklistspin.setClickable(true);
        ArrayList<CheckListAllocateTaskModel> list = viewModel.getListOfCheckListAllocateTaskModel();
        int size = list.size();
        final Integer[] checklistID = new Integer[size];
        String[] checklistName = new String[size];
        for (int i = 0; i < size; i++) {
            CheckListAllocateTaskModel model = list.get(i);
            checklistName[i] = model.getCheckListName();
            checklistID[i] = model.getCheckListId();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, checklistName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checklistspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        checklistspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedChecklistId = checklistID[position].toString();
                    db.selectedChecklistName = checklistspin.getSelectedItem()
                            .toString();
                    grouptspin.setClickable(true);
                    grouptspin.setSelection(0);
                    viewModel.getGroupAllocateDataFromDB();

                } else {
                    db.selectedChecklistId = "";
                    grouptspin.setClickable(false);
                    grouptspin.setSelection(0);
                    db.selectedGroupId = "";
                    System.out.println("else checklist...");

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setGroupSpinnerData() {

        ArrayList<GroupListAllocateTaskModel> list = viewModel.getListOfGroupListAllocateTaskModel();
        int size = list.size();
        final Integer[] groupID = new Integer[size];
        String[] groupName = new String[size];
        for (int i = 0; i < size; i++) {
            GroupListAllocateTaskModel model = list.get(i);
            groupName[i] = model.getGroupName();
            groupID[i] = model.getGroupId();
        }
        isGroupDataAvailable = size != 0;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, groupName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grouptspin.setAdapter(adapter);

        grouptspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedGroupId = groupID[position].toString();
                    System.out.println("Selected Group ID is : "
                            + db.selectedGroupId);

                    db.selectedGroupName = groupName[position];
                    //coverageData();

                    System.out.println("selected group===" + db.selectedGroupId);
                    /*if (isSelectedGroupSequenceOne(db.selectedGroupId)) {
                        if (isCoverageSpinner) {
                            isCoverageSpinner = false;
                            isCoverageTextViewNew = true;
                            System.out.println("In group sequence = 1 block");
                            View change = (View) findViewById(R.id.coverage_id_spinner);
                            ViewGroup parent = (ViewGroup) change.getParent();
                            int index = parent.indexOfChild(change);
                            parent.removeView(change);
                            LayoutInflater inflater = getLayoutInflater();
                            change = inflater.inflate(
                                    R.layout.select_question_coverage_textview,
                                    parent, false);
                            parent.addView(change, index);
                            coverageedit = (EditText) findViewById(R.id.coverage_id_edittext);
                            //coverageedit=coverageedit.replace("\n", "").replace("\r", "");
                        } else {
                            if (isCoverageTextViewNew) {
                                coverageedit = (EditText) findViewById(R.id.coverage_id_edittext);
                                isCoverageSpinner = false;
                            } else {
                                coverageedit = (EditText) findViewById(R.id.coverage_id);
                                isCoverageSpinner = false;
                            }
                        }
                        System.out.println("Enter Coverage Manually!!!");
                    }*/
                } else {
                    db.selectedGroupId = "";
                    System.out.println("else..................");
                }
            }

            public boolean isSelectedGroupSequenceOne(String userSelectedGroupID) {
				/*
				String columns1 = "min(GRP_Sequence_tint)";
				//String where1 = "Grp_ID = '" + userSelectedGroupID + "'";
				Cursor cursor1 = db.select("Group1", columns1, null, null, null,
						null, null);
				String group_id_req=cursor1.getString(cursor1
						.getColumnIndex("GRP_Sequence_tint"));
				if(group_id_req<userSelectedGroupID)*/
                String columns = "GRP_Sequence_tint";
                String where = "Grp_ID = '" + userSelectedGroupID + "'";
                Cursor cursor = db.select("Group1", columns, where, null, null,
                        null, null);
                if (cursor.moveToFirst()) {
                    String value = cursor.getString(cursor
                            .getColumnIndex("GRP_Sequence_tint"));

                    System.out.println("selected sequesnce byPramod " + value);
                    if (value.equalsIgnoreCase("1")) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    System.out.println("Cusror Count Pramod : " + cursor.getCount());
                    return false;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (isGroup && groupName.length > 0) {
            grouptspin.setVisibility(View.VISIBLE);


        } else {
            grouptspin.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.selectedHeadingId = "";
        finish();
        db.closeDb();
        startActivity(new Intent(this, HomeScreen.class));
    }

    private String blockCharacterSet = "\'";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    //changed by sayali
    ////27-02-2024
    public void saveCoverageData(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            System.out.println("No Data");
        } else {

            try {
//                SharedPreferences sharedPreferences = getSharedPreferences("coverageData", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("data_key", data);
//                editor.apply();

                // Update the spinner with the new data
                setMultipleCoverageDataToSpinner(data);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    String strCoverage = "";

    private void setMultipleCoverageDataToSpinner(String data) {
        String[] coverageData = data.split("~");

        // ArrayList<String> myCoverageData = (ArrayList<String>) Arrays.asList(coverageData);
        strCoverage = "";
        ArrayList<String> myCoverageData = new ArrayList<String>();
        myCoverageData.addAll(Arrays.asList(coverageData));
        for (int count = 0; count < coverageData.length; count++) {
            strCoverage += (count + 1) + ") " + myCoverageData.get(count) + "\n";
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, myCoverageData);
        /*adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coveragespin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        coveragespin.setSelection(0);
        coveragespin.setClickable(true);

        coveragespin.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  db.selectedCoverage = myCoverageData.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


    }

    public void saveDataScheme(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            displayDialog("No Record Found",
                    "(Note: You may not have the updated data.)");
        } else {

            try {
                String[] tabledata = data.split("\\$");

                /*
                 * AKSHAY
                 * System.out.println("data========="+tabledata.toString());
                 *
                 * String column =
                 * "Client_ID,Clnt_Name,CL_Dispaly_Name,Clnt_Adrs,user_id"
                 * ;//project saveToDatabase("Client", column,
                 * tabledata[0],true,4); AKSHAY
                 */

                String column = "PK_Scheme_ID,Scheme_Name,Scheme_Cl_Id,Scheme_Diplay_Name,Scheme_Adrs,Scheme_Region,scrolling_status,user_id";
                saveToDatabase("Scheme", column, tabledata[1], true, 7);
                System.out.println("table 1========" + tabledata[1].toString());

                /***** AKSHAY *****/

                /*
                 * column =
                 * "WorkTyp_ID,WorkTyp_Name,WorkTyp_level,FK_PRJ_Id,user_id"
                 * ;//worktype saveToDatabase("WorkType", column,
                 * tabledata[2],true,4);
                 *
                 *
                 *
                 * column =
                 * "Bldg_ID,Bldg_Name,Build_scheme_id,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("Building", column,
                 * tabledata[3],true,4);
                 *
                 *
                 * column =
                 * "floor_Id,floor_Name,Floor_Scheme_ID,FK_Bldg_ID,FK_WorkTyp_ID, user_id"
                 * ;//buildng saveToDatabase("floor", column,
                 * tabledata[4],true,5);
                 *
                 *
                 *
                 *
                 *
                 * //Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id
                 * TEXT,Fk_Floor_ID TEXT, user_id TEXT)") column =
                 * "Unit_ID,Unit_Des,Unit_Scheme_id,Fk_Floor_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("Unit", column,
                 * tabledata[5],true,5);
                 *
                 *
                 *
                 * column =
                 * "Sub_Unit_ID,Sub_Unit_Des,Sub_Unit_Scheme_id,FK_Unit_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("SubUnit", column,
                 * tabledata[6],true,5);
                 *
                 *
                 * column =
                 * "Elmt_ID,Elmt_Des,Elmt_Scheme_id,FK_Sub_Unit_ID,FK_WorkTyp_ID, user_id"
                 * ;//buildng saveToDatabase("Element", column,
                 * tabledata[7],true,5);
                 *
                 *
                 * column =
                 * "Sub_Elmt_ID,Sub_Elmt_Des,Sub_Elmt_Scheme_id,FK_Elmt_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("SubElement", column,
                 * tabledata[8],true,5);
                 *
                 *
                 *
                 *
                 *
                 * column =
                 * "Checklist_ID,Checklist_Name,Node_Id,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("CheckList", column,
                 * tabledata[9],true,4);
                 *
                 * column =
                 * "Grp_ID,Grp_Name,Node_id,FK_Checklist_ID, user_id";//buildng
                 * saveToDatabase("Group1", column, tabledata[10],true,4);
                 *
                 *
                 *
                 * column =
                 * "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type, NODE_Id, Fk_CHKL_Id, Fk_Grp_ID,user_id"
                 * ;//buildng saveToDatabase("question", column,
                 * tabledata[11],true,7);
                 *
                 *
                 * System.out.println("client data=========="+column.length());
                 *//***** AKSHAY *****/

                /*
                 * column = "q_type_id,q_type_text,q_type_desc, user_id";
                 * saveToDatabase("question_type", column, tabledata[7],true,4);
                 *
                 * column =
                 * "severity_id,mild,moderate,severe,very_severe,exstream, user_id"
                 * ; saveToDatabase("severity", column, tabledata[8],true,7);
                 */

                /*
                 * String tempdata="0~Other"; column =
                 * "q_heading_id,q_heading_text, user_id";
                 * saveToDatabase("question_heading", column, tempdata,true,3);
                 */

                // setSchemeSpinnerData();

                // setRFIData();
                // setClientData();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void saveDataWorkType(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            displayDialog("No Record Found",
                    "(Note: You may not have the updated data.)");
        } else {

            try {
                String[] tabledata = data.split("\\$");

                /*
                 * AKSHAY
                 * System.out.println("data========="+tabledata.toString());
                 *
                 * String column =
                 * "Client_ID,Clnt_Name,CL_Dispaly_Name,Clnt_Adrs,user_id"
                 * ;//project saveToDatabase("Client", column,
                 * tabledata[0],true,4);
                 *
                 *
                 * String column =
                 * "PK_Scheme_ID,Scheme_Name,Scheme_Cl_Id,Scheme_Diplay_Name,Scheme_Adrs,Scheme_Region,scrolling_status,user_id"
                 * ; saveToDatabase("Scheme", column, tabledata[1],true,7);
                 * System
                 * .out.println("table 1========"+tabledata[1].toString());
                 * AKSHAY
                 */

                String column = "WorkTyp_ID,WorkTyp_Name,WorkTyp_level,FK_PRJ_Id,user_id";// worktype
                saveToDatabase("WorkType", column, tabledata[2], true, 4);

                /***** AKSHAY *****/

                /*
                 * column =
                 * "Bldg_ID,Bldg_Name,Build_scheme_id,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("Building", column,
                 * tabledata[3],true,4);
                 *
                 *
                 * column =
                 * "floor_Id,floor_Name,Floor_Scheme_ID,FK_Bldg_ID,FK_WorkTyp_ID, user_id"
                 * ;//buildng saveToDatabase("floor", column,
                 * tabledata[4],true,5);
                 *
                 *
                 *
                 *
                 *
                 * //Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id
                 * TEXT,Fk_Floor_ID TEXT, user_id TEXT)") column =
                 * "Unit_ID,Unit_Des,Unit_Scheme_id,Fk_Floor_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("Unit", column,
                 * tabledata[5],true,5);
                 *
                 *
                 *
                 * column =
                 * "Sub_Unit_ID,Sub_Unit_Des,Sub_Unit_Scheme_id,FK_Unit_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("SubUnit", column,
                 * tabledata[6],true,5);
                 *
                 *
                 * column =
                 * "Elmt_ID,Elmt_Des,Elmt_Scheme_id,FK_Sub_Unit_ID,FK_WorkTyp_ID, user_id"
                 * ;//buildng saveToDatabase("Element", column,
                 * tabledata[7],true,5);
                 *
                 *
                 * column =
                 * "Sub_Elmt_ID,Sub_Elmt_Des,Sub_Elmt_Scheme_id,FK_Elmt_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("SubElement", column,
                 * tabledata[8],true,5);
                 *
                 *
                 *
                 *
                 *
                 * column =
                 * "Checklist_ID,Checklist_Name,Node_Id,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("CheckList", column,
                 * tabledata[9],true,4);
                 *
                 * column =
                 * "Grp_ID,Grp_Name,Node_id,FK_Checklist_ID, user_id";//buildng
                 * saveToDatabase("Group1", column, tabledata[10],true,4);
                 *
                 *
                 *
                 * column =
                 * "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type, NODE_Id, Fk_CHKL_Id, Fk_Grp_ID,user_id"
                 * ;//buildng saveToDatabase("question", column,
                 * tabledata[11],true,7);
                 *
                 *
                 * System.out.println("client data=========="+column.length());
                 *//***** AKSHAY *****/

                /*
                 * column = "q_type_id,q_type_text,q_type_desc, user_id";
                 * saveToDatabase("question_type", column, tabledata[7],true,4);
                 *
                 * column =
                 * "severity_id,mild,moderate,severe,very_severe,exstream, user_id"
                 * ; saveToDatabase("severity", column, tabledata[8],true,7);
                 */

                /*
                 * String tempdata="0~Other"; column =
                 * "q_heading_id,q_heading_text, user_id";
                 * saveToDatabase("question_heading", column, tempdata,true,3);
                 */

                // setSchemeSpinnerData();

                // setRFIData();
                // setClientData();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void saveDataCheckList(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            displayDialog("No Record Found",
                    "(Note: You may not have the updated data.)");
        } else {

            try {
                String[] tabledata = data.split("\\$");

                /*
                 * AKSHAY
                 * System.out.println("data========="+tabledata.toString());
                 *
                 * String column =
                 * "Client_ID,Clnt_Name,CL_Dispaly_Name,Clnt_Adrs,user_id"
                 * ;//project saveToDatabase("Client", column,
                 * tabledata[0],true,4);
                 *
                 *
                 * String column =
                 * "PK_Scheme_ID,Scheme_Name,Scheme_Cl_Id,Scheme_Diplay_Name,Scheme_Adrs,Scheme_Region,scrolling_status,user_id"
                 * ; saveToDatabase("Scheme", column, tabledata[1],true,7);
                 * System
                 * .out.println("table 1========"+tabledata[1].toString());
                 *
                 *
                 *
                 * String column =
                 * "WorkTyp_ID,WorkTyp_Name,WorkTyp_level,FK_PRJ_Id,user_id"
                 * ;//worktype saveToDatabase("WorkType", column,
                 * tabledata[2],true,4); AKSHAY
                 */

                String column = "Bldg_ID,Bldg_Name,Build_scheme_id,FK_WorkTyp_ID,user_id";// buildng
                saveToDatabase("Building", column, tabledata[3], true, 4);

                column = "floor_Id,floor_Name,Floor_Scheme_ID,FK_Bldg_ID,FK_WorkTyp_ID, user_id";// buildng
                saveToDatabase("floor", column, tabledata[4], true, 5);

                // Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id
                // TEXT,Fk_Floor_ID TEXT, user_id TEXT)")
                column = "Unit_ID,Unit_Des,Unit_Scheme_id,Fk_Floor_ID,FK_WorkTyp_ID,user_id";// buildng
                saveToDatabase("Unit", column, tabledata[5], true, 5);

                column = "Sub_Unit_ID,Sub_Unit_Des,Sub_Unit_Scheme_id,FK_Unit_ID,FK_WorkTyp_ID,user_id";// buildng
                saveToDatabase("SubUnit", column, tabledata[6], true, 5);

                column = "Elmt_ID,Elmt_Des,Elmt_Scheme_id,FK_Sub_Unit_ID,FK_WorkTyp_ID, user_id";// buildng
                saveToDatabase("Element", column, tabledata[7], true, 5);

                column = "Sub_Elmt_ID,Sub_Elmt_Des,Sub_Elmt_Scheme_id,FK_Elmt_ID,FK_WorkTyp_ID,user_id";// buildng
                saveToDatabase("SubElement", column, tabledata[8], true, 5);

                column = "Checklist_ID,Checklist_Name,Node_Id,FK_WorkTyp_ID,user_id";// buildng
                saveToDatabase("CheckList", column, tabledata[9], true, 4);

                column = "Grp_ID,Grp_Name,Node_id,FK_Checklist_ID, user_id";// buildng
                saveToDatabase("Group1", column, tabledata[10], true, 4);

                column = "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type, NODE_Id, Fk_CHKL_Id, Fk_Grp_ID,user_id";// buildng
                saveToDatabase("question", column, tabledata[11], true, 7);

                System.out.println("client data==========" + column.length());

                /*
                 * column = "q_type_id,q_type_text,q_type_desc, user_id";
                 * saveToDatabase("question_type", column, tabledata[7],true,4);
                 *
                 * column =
                 * "severity_id,mild,moderate,severe,very_severe,exstream, user_id"
                 * ; saveToDatabase("severity", column, tabledata[8],true,7);
                 */

                /*
                 * String tempdata="0~Other"; column =
                 * "q_heading_id,q_heading_text, user_id";
                 * saveToDatabase("question_heading", column, tempdata,true,3);
                 */

                // setSchemeSpinnerData();

                // setRFIData();
                // setClientData();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void saveToDatabase(String table, String columns, String respose,
                               boolean adduserId, int colCnt) {
        Cursor cursor = db.select(table, columns,
                "user_id='" + db.userId + "'", null, null, null, null);
        String existingData = "";
        if (cursor.moveToFirst()) {
            for (int i = 0; i < colCnt; i++) {
                existingData += "'" + cursor.getString(i) + "',";
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        int a = 0;
        String[] rowdata = respose.split("\\|");

        System.out.println("size of the table=" + rowdata.length);
        for (int len = 0; len < rowdata.length; len++) {
            String[] singlerow = rowdata[len].split("~");
            StringBuffer values = new StringBuffer("'");
            for (int i = 0; i < (singlerow.length - 1); i++) {
                /*
                 * if(singlerow[i].contains("'"))
                 * singlerow[i].replaceAll("'"," ");
                 */
                System.out.println("apostropeeeee----------------"
                        + singlerow[i]);
                values.append(singlerow[i] + "','");
                a++;
            }
            values.append(singlerow[singlerow.length - 1] + "'");
            if (adduserId)
                values.append(",'" + db.userId + "'");

            if (a > 1) {
                if (!existingData.contains(values.toString()))
                    db.insert(table, columns, values.toString());
                System.out.println("colum=" + table + "table value==="
                        + values.toString());
            }
        }

    }

    private boolean isDataAvialable(String table) {

        Cursor cursor = db.select(table, "count(*) as noitem", "user_id='"
                + db.userId + "'", null, null, null, null);
        if (cursor.moveToFirst()) {
            if (Integer.parseInt(cursor.getString(0)) > 0) {
                Log.d(table, cursor.getString(0));
                cursor.close();
                return true;
            } else {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                return false;
            }
        } else {
            return false;
        }
    }


    private boolean isDataAvialableAllocateTask(String table) {

        Cursor cursor = db.select(table, "count(*) as noitem", null, null, null, null, null);
        if (cursor.moveToFirst()) {
            if (Integer.parseInt(cursor.getString(0)) > 0) {
                Log.d(table, cursor.getString(0));
                cursor.close();
                return true;
            } else {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                return false;
            }
        } else {
            return false;
        }
    }


    private boolean isDataAvialableForID(String table, String targetColumn,
                                         String targetValue) {

        Cursor cursor = db.select(table, "count(*) as noitem", "user_id='"
                + db.userId + "' AND " + targetColumn + " ='" + targetValue
                + "'", null, null, null, null);
        System.out.println("user_id='" + db.userId + "' AND " + targetColumn
                + " = '" + targetValue + "'");
        if (cursor.moveToFirst()) {
            if (Integer.parseInt(cursor.getString(0)) > 0) {
                Log.d(table, cursor.getString(0));
                cursor.close();
                return true;
            } else {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                return false;
            }
        } else {
            return false;
        }
    }

    protected void displayDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        if (message.equals("Problem in connection.")) {
            alertDialog.setButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.setButton2("Try again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // retry();
                        }
                    });
        } else {
            alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        alertDialog.show();
    }

    private void flushData() {
        db.delete("CheckList", "user_id='" + db.userId + "'");
        db.delete("Scheme", "user_id='" + db.userId + "'");
        db.delete("Building", "user_id='" + db.userId + "'");
        /*
         * db.delete("subgroup", "user_id='" + db.userId + "'");
         * db.delete("answare", "user_id='" + db.userId + "'");
         * db.delete("floor", "user_id='" + db.userId + "'");
         * db.delete("question_heading", "user_id='" + db.userId + "'");
         * db.delete("question_group", "user_id='" + db.userId + "'");
         * db.delete("question", "user_id='" + db.userId + "'");
         * db.delete("userMaster", "Pk_User_ID='" + db.userId + "'");
         * db.delete("card_details","user_id='" + db.userId + "'");
         * db.delete("alert_details", "user_id='" + db.userId + "'");
         * db.delete("scheme_foreman", "user_id='" + db.userId + "'");
         * db.delete("scheme_superviser", "user_id='" + db.userId + "'");
         * db.delete("mockuptable", "user_id='" + db.userId + "'");
         * db.delete("inspection_log", "user_id='" + db.userId + "'");
         * db.delete("trade_match", "user_id='" + db.userId + "'");
         * db.delete("contractor_sf", "user_id='" + db.userId + "'");
         */
        // db.update("sqlite_sequence", "seq=0", "name = 'question'");

        System.out
                .println("data flush+++++++++++++++++++++++++++++++++++++++++++++++++");
        // deleteImages();
    }

    public void ResetId() {
        db.selectedClientId = "";
        db.selectedSchemeId = "";
        db.selectedWorkTypeId = "";
        db.selectedBuildingId = "";
        db.selectedFloorId = "";
        db.selectedUnitId = "";
        db.selectedSubUnitId = "";
        db.selectedElementId = "";
        db.selectedSubElementId = "";
        db.selectedChecklistId = "";
        db.selectedGroupId = "";
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


    void displayCoverageDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);


        alertDialog.setButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        /*alertDialog.setButton2("Try again",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // retry();
                    }
                });*/

        alertDialog.show();
    }

}
