package com.ob.rfi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.ob.allocate_task.presentationlayer.AllocateTaskViewModel;
import com.ob.database.db_tables.AllocateTaskTableModel;
import com.ob.database.db_tables.ChecklistTableModel;
import com.ob.database.db_tables.ClientTableModel;
import com.ob.database.db_tables.GroupListTableModel;
import com.ob.database.db_tables.ProjectTableModel;
import com.ob.database.db_tables.StageTableModel;
import com.ob.database.db_tables.StructureTableModel;
import com.ob.database.db_tables.SubUnitTableModel;
import com.ob.database.db_tables.UnitTableModel;
import com.ob.database.db_tables.WorkTypeTableModel;
import com.ob.rfi.db.RfiDatabase;
import com.ob.rfi.models.SpinnerType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("static-access")
public class AllocateTask extends CustomTitle {

    private Spinner schemeSpin;
    private Spinner buildingSpin;
    private Spinner checklistspin;
    private Spinner subgrpSpin;
    private Spinner tradeSpin;
    private Spinner floorSpin;
    private Spinner supervisorspin;
    private Spinner foremanspin;
    private Spinner contractorspin;
    public static CustomTitle slectionclass;
    private String[] schemId;
    private String[] tradeId;
    private String[] chklstId;
    private String[] bldgId;
    private String[] subgrpId;
    private String[] supervisorId;
    private String[] supervisor;
    private String[] foremanId;
    private String[] foreman;
    private String[] contractorId;
    private String[] contractorNewId;
    private String[] contractorId1;
    private String[] Citems4;
    private String[] contractor;
    private String[] contid;
    private Button okBtn;
    private Button backBtn;
    private String errorMessage;
    private RfiDatabase db;
    private String selected = "";
    private Spinner clientspin;
    private String[] client;
    private Integer[] clientId;
    private Spinner rfiSpin;
    private Spinner projSpin;
    private Spinner clintSpin;
    private Spinner structureSpin;
    private Spinner stageSpin;
    private Spinner unitSpin;
    private TextView subunitspin;

    private Spinner grouptspin;
    private Spinner coverageSpinner;
    private String method;
    private String[] param;
    private String[] value;
    private String[] unitId;
    private String[] subunitId;
    private String[] elementId;
    private String[] subelementId;
    private String[] checklistId;
    private String[] groupId;
    private String[] NodeId;
    private String[] floorid;
    private String[] RfiId;
    private EditText coverageedit;
    private Spinner worktypeSpin;
    private String[] wTypeId;
    private String[] wTypeLevelId;
    private EditText drawingeedit;
    private String[] scroll_status;
    public static int nval;
    public static CustomTitle destruct;

    private static final String TAG = "AllocateTask";
    public static boolean insertFlag = false;
    private String responseData;
    private int selectedClient = 0;
    /***** AKSHAY *****/
    ArrayList<Group> groupList;
    private boolean isCoverageSpinner = false;
    private boolean isCoverageTextViewNew = false;
    private AllocateTaskViewModel viewModel;


    @Override
    protected Dialog onCreateDialog(int id) {
        return super.onCreateDialog(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.allocate_task);
        //Toast.makeText(getApplicationContext(), "In onCreate", Toast.LENGTH_SHORT).show();
        // title.setText("CQRA Question selection");
        destruct = this;
        slectionclass = this;
        rfiSpin = (Spinner) findViewById(R.id.rfi_id);
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

        okBtn = (Button) findViewById(R.id.question_select_submit);
        backBtn = (Button) findViewById(R.id.question_select_Back);


        viewModel = new ViewModelProvider(this).get(AllocateTaskViewModel.class);
        //RFIRoomDb dbObject = RoomDbObject.INSTANCE.getDbObject(this);
        ResetId();
        setSpiner();
        viewModel.getClientData();

        db = new RfiDatabase(getApplicationContext());
        if (db.userId.equalsIgnoreCase("")) {
            logout();
            return;
        }

        observerData();

        okBtn.setOnClickListener(new OnClickListener() {
            private SharedPreferences checkPreferences;
            private Editor editor;

            @Override
            public void onClick(View arg0) {

                try {
                    viewModel.isError = validateMandatory();
                    if (!viewModel.isError){
                        AllocateTaskTableModel model = new AllocateTaskTableModel();
                        model.setClientId(viewModel.getClientId());
                        model.setWorkTypeId(Integer.parseInt(db.selectedWorkTypeId));
                        model.setCheckListId(Integer.parseInt(db.selectedChecklistId));
                        model.setProjectId(Integer.parseInt(db.selectedSchemeId));
                        model.setStructureId(Integer.parseInt(db.selectedBuildingId));
                        model.setStageId(Integer.parseInt(db.selectedFloorId));
                        model.setGroupId(Integer.parseInt(db.selectedGroupId));
                        model.setUnitId(Integer.parseInt(db.selectedUnitId));
                        model.setActivitySequenceId(Integer.parseInt(db.selectedNodeId));
                        model.setSubUnitId(db.selectedSubUnitId);
                        model.setUserId(Integer.parseInt(db.userId));
                        viewModel.insertAllocateTask(model);
                    }

                } catch (NumberFormatException e) {
                    Log.d(TAG, "onClick: NumberFormatException: ",e);
                    errorMessage = "Please select all mandatory fields";
                    viewModel.isError = true;
                    //throw new RuntimeException(e);
                }

                if (viewModel.isError) {
                    displayErrorDialog("Error", errorMessage);
                    viewModel.isError = false;
                } else if (viewModel.getListOfQuestions().size() != 1/*isQuestionAvailable()*/) {
                    if (db.selectedScrollStatus.equalsIgnoreCase("true")) {
                        Intent int1 = new Intent(AllocateTask.this,
                                HomeScreen.class);
                        startActivity(int1);

                    } else {
                        Intent int1 = new Intent(AllocateTask.this,
                                HomeScreen.class);
                        startActivity(int1);
                    }

                }
            }


        });

        backBtn.setOnClickListener(v -> onBackPressed());
    }

    private Boolean validateMandatory() {
        if (viewModel.getClientId() == 0) {
            errorMessage = "Please select Client";
            //displayErrorDialog("Error", );
            return true;
        }
        if (db.selectedBuildingId.isEmpty()) {
            errorMessage = "Please select Project";
            return true;
        }
        if (db.selectedWorkTypeId.isEmpty()) {
            errorMessage = "Please select Work Type";
            return true;
        }
        if (db.selectedChecklistId.isEmpty()) {
            errorMessage = "Please select Checklist";
            return true;
        }
        if (db.selectedGroupId.isEmpty()) {
            errorMessage = "Please select Group";
            return true;
        }
        if (db.selectedNodeId.isEmpty()) {
            errorMessage = "Please select Node";
            return true;
        }
        if (db.selectedFloorId.isEmpty()) {
            db.selectedFloorId = "0";
        }
        if (db.selectedUnitId.isEmpty()) {
            db.selectedUnitId = "0";
        }
        if (db.selectedSubUnitId.isEmpty()) {
            db.selectedSubUnitId = "0";
        }
        return false;
    }

    private void observerData() {

        Objects.requireNonNull(viewModel.getLvClientData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: value: "+value);

                    if (value!=0){
                        setClientData();
                        hideProgressDialog();
                    }else {
                        //updateData();
                        showProgressDialogWithoutMessage();
                        viewModel.getClientProjectWorkType(31, "Maker");
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvProjectData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: getLvProjectData: "+value);

                    if (value!=0){
                        Log.d(TAG, "observerData: getLvProjectData");
                        hideProgressDialog();
                        setSchemeSpinnerData();
                    }else {
                        //updateData();
                        Log.d(TAG, "observerData: getLvProjectData with no data ");
                        showProgressDialogWithoutMessage();
                        viewModel.getProjectApi(31, "Maker");
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvWorkTypeData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: getLvWorkTypeData: "+value);

                    if (value!=0){
                        Log.d(TAG, "observerData: getLvWorkTypeData");
                        hideProgressDialog();
                        setWorkTypeSpinnerData();
                    }else {
                        //updateData();
                        showProgressDialogWithoutMessage();
                        Log.d(TAG, "observerData: getLvWorkTypeData ");
                        viewModel.getWorkTypeSequenceApi(31, "Maker");
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvStructureData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: getLvStructureData: "+value);

                    if (value!=0){
                        Log.d(TAG, "observerData: getLvStructureData");
                        hideProgressDialog();
                        setBuildingSpinnerData();
                    }else {
                        //updateData();
                        showProgressDialogWithoutMessage();
                        Log.d(TAG, "observerData: getLvStructureData ");
                        viewModel.getStructureApi(31, "Maker");
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvCheckListData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: getLvCheckListData: "+value);

                    if (value!=0){
                        Log.d(TAG, "observerData: setCheckListSpinnerData");
                        hideProgressDialog();
                        setCheckListSpinnerData();
                    }else {
                        //updateData();
                        showProgressDialogWithoutMessage();
                        Log.d(TAG, "observerData: getLvWorkTypeData ");
                        viewModel.getCheckListApi(31, "Maker");
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvGroupListData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: getLvGroupListData: "+value);

                    if (value!=0){
                        Log.d(TAG, "observerData: setCheckListSpinnerData");
                        hideProgressDialog();
                        setGroupSpinnerData();
                    }else {
                        //updateData();
                        showProgressDialogWithoutMessage();
                        Log.d(TAG, "observerData: getLvWorkTypeData ");
                        viewModel.getGroupListApi(31, "Maker");
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvStageData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: getLvStageData: "+value);

                    if (value!=0){
                        Log.d(TAG, "observerData: getLvStageData");
                        hideProgressDialog();
                        setFloorSpinnerData();
                    }else {
                        //updateData();
                        showProgressDialogWithoutMessage();
                        Log.d(TAG, "observerData: getLvStageData ");
                        viewModel.getStageApi(31, "Maker");
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvUnitData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: getLvStageData: "+value);

                    if (value!=0){
                        Log.d(TAG, "observerData: getLvStageData");
                        hideProgressDialog();
                        setUnitSpinnerData();
                    }else {
                        //updateData();
                        showProgressDialogWithoutMessage();
                        Log.d(TAG, "observerData: getLvStageData ");
                        viewModel.getUnitApi(31, "Maker");
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvSubUnitData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: getLvSubUnitData: "+value);

                    if (value!=0){
                        Log.d(TAG, "observerData: getLvSubUnitData");
                        hideProgressDialog();
                        setSubUnitSpinnerData();
                    }else {
                        //updateData();
                        showProgressDialogWithoutMessage();
                        Log.d(TAG, "observerData: getLvSubUnitData ");
                        viewModel.getSubUnitApi(31, "Maker");
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvQuestionsData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: getLvQuestionsData: "+value);
                    if (value!=0){
                        Log.d(TAG, "observerData: getLvQuestionsData");
                        hideProgressDialog();
                        displayErrorDialog("Questions","Questions are available");
                    }else {
                        //updateData();
                        showProgressDialogWithoutMessage();
                        Log.d(TAG, "observerData: getLvQuestionsData ");
                        viewModel.getQuestionsApi(31);
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvErrorData()).observe(
                this, value ->{
                    hideProgressDialog();
                    Log.d(TAG, "onCreate: getLvStageData: "+value);
                    resetSpinner(value.getSpinnerType());
                    displayErrorDialog("Error",value.getMessage());
                }
        );
    }

    private void resetSpinner(SpinnerType spinnerType) {
        if (spinnerType.equals(SpinnerType.CLIENT)){
            setClientData();
        }
        if (spinnerType.equals(SpinnerType.STAGE)){
            setFloorSpinnerData();
        }
        if (spinnerType.equals(SpinnerType.PROJECT)){
            setSchemeSpinnerData();
        }
        else if (spinnerType.equals(SpinnerType.GROUP_LIST)){
            setGroupSpinnerData();
        }
        else if (spinnerType.equals(SpinnerType.WORK_TYPE)){
            setWorkTypeSpinnerData();
        }
        else if (spinnerType.equals(SpinnerType.CHECK_LIST)){
            setCheckListSpinnerData();
        }
        else if (spinnerType.equals(SpinnerType.UNIT_LIST)){
            setUnitSpinnerData();
        }
        else if (spinnerType.equals(SpinnerType.SUB_UNIT_LIST)){
            setSubUnitSpinnerData();
        }
        else if (spinnerType.equals(SpinnerType.STRUCTURE)){
            setBuildingSpinnerData();
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (savedInstanceState != null) {
            clintSpin.setSelection(savedInstanceState.getInt("client"));
            clintSpin.setClickable(true);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        outState.putInt("client", clintSpin.getSelectedItemPosition());
        selectedClient = clintSpin.getSelectedItemPosition();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db.userId.equalsIgnoreCase("")) {
            //logout();
        }
    }

    private void logout() {
        finish();
        Toast.makeText(AllocateTask.this, "Session expired... Please login.",
                Toast.LENGTH_SHORT).show();
        Intent logout = new Intent(AllocateTask.this, LoginScreen.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        db.copyDatabase(getApplicationContext(), "RFI.db");
        startActivity(logout);
    }

    public void setSpiner() {
        if (db.setSpinner) {

            rfiSpin.setSelection(0);
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
        viewModel.setClientId(0);
        db.selectedScrollStatus = "";

    }

    @SuppressWarnings("deprecation")
    public void displayErrorDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton("Ok", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    // ------------------------------------------------------------------------------

    private void setRFIData() {


        String where = "user_id='" + db.userId + "'";
        Cursor cursor = db.select("Rfi_New_Create", "FK_rfi_Id", where, null,
                null, null, null);

        String id = "";
        if (cursor.moveToFirst()) {

            do {

                id = cursor.getString(0);
            } while (cursor.moveToNext());
        } else {
            id = "0";
        }

        cursor.close();
        nval = Integer.parseInt(id);
        nval = nval + 1;

        setClientData();
    }

    // /---client
    private void setClientData() {

        ArrayList<ClientTableModel> list = viewModel.getList();
        int size = list.size();
        clientId = new Integer[size];
        client = new String[size];
        for(int i =0; i<size; i++){
            client[i] = list.get(i).getClientName();
            clientId[i] = list.get(i).getClientId();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, client);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        clintSpin.setAdapter(adapter);
        //clintSpin.setSelection(2);//changed by pramod
        adapter.notifyDataSetChanged();
        clintSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                System.out.println("position" + position);
                if (position >= 1) {
                    db.selectedclientname = clintSpin.getSelectedItem()
                            .toString();
                    viewModel.setClientId(clientId[position]);
                    viewModel.getProjectDataFromDB();
                    worktypeSpin.setSelection(0);
                    worktypeSpin.setClickable(false);
                    projSpin.setSelection(1);//changed by pramod
                    projSpin.setClickable(true);
                }else {
                    worktypeSpin.setSelection(0);
                    worktypeSpin.setClickable(false);
                    projSpin.setSelection(1);//changed by pramod
                    projSpin.setClickable(false);
                    viewModel.setClientId(0);
                    db.selectedclientname = clintSpin.getSelectedItem()
                            .toString();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setSchemeSpinnerData() {

        ArrayList<ProjectTableModel> list = viewModel.getListOfProject();
        int size = list.size();
        String[] items = new String[size];
        schemId = new String[size];
        scroll_status = new String[size];
        for(int i =0; i<size; i++){
            schemId[i] = list.get(i).getPK_Scheme_ID();
            items[i] = list.get(i).getScheme_Name();
            scroll_status[i] = list.get(i).getScrolling_status();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projSpin.setAdapter(adapter);

        projSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedSchemeId = schemId[position];
                    db.selectedScrollStatus = scroll_status[position];
                    worktypeSpin.setSelection(0);
                    worktypeSpin.setClickable(true);
                    db.selectedSchemeName = projSpin.getSelectedItem()
                            .toString();
                    viewModel.getWorkTypeFromDB();
                    //setWorkTypeSpinnerData();
                } else {
                    db.selectedSchemeId = "";
                    db.selectedScrollStatus = "";
                    db.selectedBuildingId = "";
                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    worktypeSpin.setSelection(0);
                    grouptspin.setSelection(0);
                    structureSpin.setSelection(0);
                    checklistspin.setSelection(0);
                    stageSpin.setSelection(0);
                    unitSpin.setSelection(0);
                    subunitspin.setText("");

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setWorkTypeSpinnerData() {

        ArrayList<WorkTypeTableModel> list = viewModel.getListOfWorkType();
        int size = list.size();
        wTypeId = new String[size];
        wTypeLevelId = new String[size];
        String[] items = new String[size];

        for(int i =0; i<size; i++){
            wTypeId[i] = String.valueOf(list.get(i).getActivitySequenceGroupId());
            items[i] = list.get(i).getActivitySequenceName();
            wTypeLevelId[i] = String.valueOf(list.get(i).getActivitySequenceLevel());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        worktypeSpin.setAdapter(adapter);

        worktypeSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid)
            {
                if (position > 0) {
                    db.selectedWorkTypeId = wTypeId[position];
                    db.selectedlevelId = wTypeLevelId[position];
                    db.selectedNodeId = wTypeId[position];
                    structureSpin.setClickable(true);
                    structureSpin.setSelection(0);

                    viewModel.getCheckListDataFromDB();
                } else {
                    db.selectedWorkTypeId = "";
                    db.selectedBuildingId = "";
                    db.selectedlevelId = "";
                    grouptspin.setSelection(0);
                    structureSpin.setSelection(0);
                    checklistspin.setSelection(0);
                    stageSpin.setSelection(0);
                    unitSpin.setSelection(0);
                    subunitspin.setText("");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setBuildingSpinnerData() {

        ArrayList<StructureTableModel> list = viewModel.getListOfStructure();
        int size = list.size();
        String[] items = new String[size];
        bldgId = new String[size];

        for(int i =0; i<size; i++){
            bldgId[i] = list.get(i).getBldg_ID();
            items[i] = list.get(i).getBldg_Name();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        structureSpin.setAdapter(adapter);
        structureSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position > 0) {
                    db.selectedBuildingId = bldgId[position];
                    db.selectedNodeId = bldgId[position];
                    viewModel.getStageDataFromDB();
                    stageSpin.setClickable(true);
                    stageSpin.setSelection(0);
                }
                else {
                    stageSpin.setClickable(false);
                    stageSpin.setSelection(0);
                    unitSpin.setClickable(false);
                    unitSpin.setSelection(0);
                    subunitspin.setClickable(false);
                    subunitspin.setText("");

                    db.selectedBuildingId = "";
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

        ArrayList<StageTableModel> list = viewModel.getListOfStage();
        int size = list.size();
        String[] items = new String[size];
        floorid = new String[size];

        for(int i =0; i<size; i++){
            floorid[i] = list.get(i).getFloor_Id();
            items[i] = list.get(i).getFloor_Name();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stageSpin.setAdapter(adapter);

        stageSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position > 0) {
                    db.selectedFloorId = floorid[position];
                    db.selectedNodeId = floorid[position];
                    viewModel.getUnitListDataFromDB();
                    unitSpin.setClickable(true);
                    unitSpin.setSelection(0);

                } else {
                    unitSpin.setClickable(false);
                    unitSpin.setSelection(0);

                    subunitspin.setClickable(false);
                    subunitspin.setText("");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setUnitSpinnerData() {

        ArrayList<UnitTableModel> list = viewModel.getListOfUnitList();
        int size = list.size();
        String[] items = new String[size];
        unitId = new String[size];

        for(int i =0; i<size; i++){
            unitId[i] = list.get(i).getUnit_ID();
            items[i] = list.get(i).getUnit_Name();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        unitSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedUnitId = unitId[position];
                    db.selectedNodeId = unitId[position];
                    viewModel.getSubUnitListDataFromDB();
                    subunitspin.setClickable(true);
                    subunitspin.setText("");

                    /*if (isDataAvialableForID("SubUnit", "FK_Unit_ID",
                            db.selectedUnitId)) {
                        setSubUnitSpinnerData(db.selectedUnitId,
                                db.selectedSchemeId);
                        subunitspin.setClickable(true);
                        subunitspin.setText("");
                    } else {
                        method = "getSubUnit";
                        param = new String[]{"userID", "userRole",
                                "projectId", "workTypeId", "parentId"};
                        value = new String[]{db.userId, "maker",
                                db.selectedSchemeId, db.selectedWorkTypeId,
                                db.selectedUnitId};
                        callSubUnitService();
                    }*/

                } else {
                    subunitspin.setClickable(false);
                    subunitspin.setText("");
                    System.out.println("hello else unit");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setSubUnitSpinnerData() {
        subunitspin.setText("Select Sub Unit");
        ArrayList<SubUnitTableModel> list = viewModel.getListOfSubUnitList();
        int size = list.size();
        String[] items = new String[size];
        subunitId = new String[size];
        final boolean[] checkedItems = new boolean[size];

        for(int i =0; i<size; i++){
            subunitId[i] = list.get(i).getSubUnitId();
            items[i] = list.get(i).getSubUnitName();
        }
        final List<String> selectedItems = Arrays.asList(items);
        final List<String> ids = Arrays.asList(subunitId);
        subunitspin.setOnClickListener(view -> {

            // initialise the alert dialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);

            // set the title for the alert dialog
            builder.setTitle("Select Sub Unit");

            // now this is the function which sets the alert dialog for multiple item selection ready
            builder.setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                checkedItems[which] = isChecked;
                //String currentItem = selectedItems.get(which);
            });

            // alert dialog shouldn't be cancellable
            builder.setCancelable(false);

            // handle the positive button of the dialog
            builder.setPositiveButton("Done", (dialog, which) -> {
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder subUnitIds = new StringBuilder();
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        Log.d(TAG, "setSubUnitSpinnerData: "+(selectedItems.get(i)));
                        stringBuilder.append(selectedItems.get(i));
                        subUnitIds.append(ids.get(i));
                        stringBuilder.append(", ");
                        subUnitIds.append(",");
                    }
                }
                if (checkedItems.length > 0){
                    Log.d(TAG, "b4 setSubUnitSpinnerData: stringBuilder: "+ stringBuilder);
                    if (!stringBuilder.toString().isEmpty()){
                        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                        subUnitIds.delete(subUnitIds.length() - 1, subUnitIds.length());
                        subunitspin.setText(stringBuilder.toString());
                        RfiDatabase.selectedSubUnitId = subUnitIds.toString();
                        Log.d(TAG, "after setSubUnitSpinnerData: stringBuilder: "+ stringBuilder+ " subUnitIds: "+subUnitIds);

                    }else {
                        RfiDatabase.selectedSubUnitId = "";
                        subunitspin.setText("Select Sub Unit");
                    }

                }else {
                    RfiDatabase.selectedSubUnitId = "";
                    subunitspin.setText("Select Sub Unit");
                }
            });

            // handle the negative button of the alert dialog
            builder.setNegativeButton("CANCEL", (dialog, which) -> {});

            // handle the neutral button of the dialog to clear the selected items boolean checkedItem
            builder.setNeutralButton("CLEAR ALL", (dialog, which) -> {
                Arrays.fill(checkedItems, false);
                RfiDatabase.selectedSubUnitId = "";
                subunitspin.setText("Select Sub Unit");
            });

            // create the builder
            builder.create();

            // create the alert dialog with the alert dialog builder instance
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

    }

    private void setCheckListSpinnerData() {

        ArrayList<ChecklistTableModel> list = viewModel.getListOfCheckList();
        int size = list.size();
        String[] items = new String[size];
        checklistId = new String[size];


        for(int i =0; i<size; i++){
            checklistId[i] = list.get(i).getChecklist_ID();
            items[i] = list.get(i).getChecklist_Name();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checklistspin.setAdapter(adapter);

        checklistspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedChecklistId = checklistId[position];
                    db.selectedChecklistName = checklistspin.getSelectedItem()
                            .toString();
                    //callGroupService(db.selectedNodeId);
                    viewModel.getStructureDataFromDB();
                    viewModel.getGroupListDataFromDB();
                    /*	} */

                } else {
                    checklistspin.setClickable(true);
                    checklistspin.setSelection(0);
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

        ArrayList<GroupListTableModel> list = viewModel.getListOfGroupList();
        int size = list.size();
        String[] items = new String[size];
        groupId = new String[size];

        for(int i =0; i<size; i++){
            groupId[i] = list.get(i).getGrp_ID();
            items[i] = list.get(i).getGrp_Name();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grouptspin.setAdapter(adapter);

        grouptspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedGroupId = groupId[position];
                    viewModel.getQuestionsDataFromDB();
                    // TODO: 30/11/24 get questions api
                   /* if (isQuestionAvailable()) {
                        System.out
                                .println("Data Already Available in Question table");
                    } else {
						method = "getQuestion";
						param = new String[] { "userID", "userRole", "nodeId",
								"groupId" };
						value = new String[] { db.userId, "maker",
								db.selectedNodeId, db.selectedGroupId };
						callQuestionService();
                    }*/

                } else {
                    db.selectedGroupId = "";
                    System.out.println("else..................");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.selectedHeadingId = "";
        finish();
        startActivity(new Intent(this, HomeScreen.class));
    }

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        return super.onCreatePanelView(featureId);
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

        System.out
                .println("data flush+++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    public void ResetId() {
        viewModel.setClientId(0);
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

    private void cleanData() {
        db.selectedSchemeId = "";
        db.selectedSchemeName = "";
        db.selectedclientname = "";
        viewModel.setClientId(0);
        db.selectedBuildingId = "";
        db.selectedFloorId = "";
        db.selectedUnitId = "";
        db.selectedSubUnitId = "";
        db.selectedElementId = "";
        db.selectedSubElementId = "";
        db.selectedChecklistId = "";
        db.selectedChecklistName = "";
        db.selectedGroupId = "";

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

}
