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
import com.ob.rfi.service.Webservice;
import com.ob.rfi.service.Webservice.downloadListener;

import org.json.JSONArray;
import org.json.JSONObject;

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
                    AllocateTaskTableModel model = new AllocateTaskTableModel();
                    model.setCheckListId(viewModel.getClientId());
                    model.setWorkTypeId(Integer.parseInt(db.selectedWorkTypeId));
                    model.setCheckListId(Integer.parseInt(db.selectedChecklistId));
                    model.setProjectId(Integer.parseInt(db.selectedBuildingId));
                    model.setStructureId(Integer.parseInt(db.selectedFloorId));
                    model.setGroupId(Integer.parseInt(db.selectedGroupId));
                    model.setUnitId(Integer.parseInt(db.selectedUnitId));
                    model.setActivitySequenceId(Integer.parseInt(db.selectedNodeId));
                    model.setSubUnitId(Integer.parseInt(db.selectedSubUnitId));
                    model.setUserId(Integer.parseInt(db.userId));
                    viewModel.insertAllocateTask(model);
                } catch (NumberFormatException e) {
                    Log.d(TAG, "onClick: NumberFormatException: ",e);
                    //throw new RuntimeException(e);
                }
                /*String table_fields = "Client, Project, WorkType, Structure, Stage, Unit, SubUnit, Element, SubElement, CheckList, GroupColumn, UserID,NodeID";
                String table_values = "'" + db.selectedClientId + "','"
                        + db.selectedSchemeId + "','" + db.selectedWorkTypeId
                        + "','" + db.selectedBuildingId + "','"
                        + db.selectedFloorId + "','" + db.selectedUnitId
                        + "','" + db.selectedSubUnitId + "','"
                        + db.selectedElementId + "','"
                        + db.selectedSubElementId + "','"
                        + db.selectedChecklistId + "','" + db.selectedGroupId + "','" + db.userId + "','" + db.selectedNodeId + "'";
                db.insert("AllocateTask", table_fields, table_values);

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

                nval = Integer.parseInt(id);
                nval = nval + 1;

                String value = String.valueOf(nval);

                db.insert("Rfi_New_Create", "FK_rfi_Id,user_id", "'" + value + "','" + db.userId + "'");

*/
                if (validateScreen()) {
                    displayErrorDialog("Error", errorMessage);

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

    private void observerData() {

        Objects.requireNonNull(viewModel.getLvClientData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: value: "+value);

                    if (value!=0){
                        setClientData();
                    }else {
                        //updateData();
                        viewModel.getClientProjectWorkType(31, "Maker");
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvProjectData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: getLvProjectData: "+value);

                    if (value!=0){
                        Log.d(TAG, "observerData: setSchemeSpinnerData");
                        setSchemeSpinnerData();
                    }else {
                        //updateData();
                        Log.d(TAG, "observerData: getLvProjectData ");
                        viewModel.getProjectApi(31, "Maker");
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvWorkTypeData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: getLvWorkTypeData: "+value);

                    if (value!=0){
                        Log.d(TAG, "observerData: getLvWorkTypeData");
                        setWorkTypeSpinnerData();
                    }else {
                        //updateData();
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
                        setBuildingSpinnerData();
                    }else {
                        //updateData();
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
                        setCheckListSpinnerData();
                    }else {
                        //updateData();
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
                        setGroupSpinnerData();
                    }else {
                        //updateData();
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
                        setFloorSpinnerData();
                    }else {
                        //updateData();
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
                        setUnitSpinnerData();
                    }else {
                        //updateData();
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
                        setSubUnitSpinnerData();
                    }else {
                        //updateData();
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
                        displayErrorDialog("Questions","Questions are available");
                    }else {
                        //updateData();
                        Log.d(TAG, "observerData: getLvQuestionsData ");
                        viewModel.getQuestionsApi(31);
                    }
                }
        );

        Objects.requireNonNull(viewModel.getLvErrorData()).observe(
                this, value ->{
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

    public void updateData() {
        method = "getClientProjectWorkType";
        param = new String[]{"userID", "userRole"};
        value = new String[]{db.userId, "maker"};
        callService();
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

    protected boolean validateScreen() {
        return false;
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
        clintSpin.setSelection(2);//changed by pramod
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
                    /*if (isDataAvialableForID("Unit", "Fk_Floor_ID",
                            db.selectedFloorId)) {
                        setUnitSpinnerData(db.selectedFloorId,
                                db.selectedSchemeId);
                        unitSpin.setClickable(true);
                        unitSpin.setSelection(0);
                    } else {
                        method = "getUnit";
                        param = new String[] { "userID", "userRole",
                                "projectId", "workTypeId", "parentId"};
                        value = new String[] { db.userId, "maker",
                                db.selectedSchemeId, db.selectedWorkTypeId,
                                db.selectedFloorId};
                        callUnitService();
                    }*/

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
        subunitspin.setOnClickListener(view -> {

            // initialise the alert dialog builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // set the title for the alert dialog
            builder.setTitle("Select Sub Unit");

            // now this is the function which sets the alert dialog for multiple item selection ready
            builder.setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                checkedItems[which] = isChecked;
                String currentItem = selectedItems.get(which);
            });

            // alert dialog shouldn't be cancellable
            builder.setCancelable(false);

            // handle the positive button of the dialog
            builder.setPositiveButton("Done", (dialog, which) -> {
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        Log.d(TAG, "setSubUnitSpinnerData: "+(selectedItems.get(i)));
                       // tvSelectedItemsPreview.setText(String.format("%s%s, ", tvSelectedItemsPreview.getText(), selectedItems.get(i)));
                    }
                }
            });

            // handle the negative button of the alert dialog
            builder.setNegativeButton("CANCEL", (dialog, which) -> {});

            // handle the neutral button of the dialog to clear the selected items boolean checkedItem
            builder.setNeutralButton("CLEAR ALL", (dialog, which) -> {
                Arrays.fill(checkedItems, false);
            });

            // create the builder
            builder.create();

            // create the alert dialog with the alert dialog builder instance
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });



    /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        subunitspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        subunitspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedSubUnitId = subunitId[position];
                    db.selectedNodeId = subunitId[position];

                } else {
                    db.selectedSubUnitId = "0";
                    System.out.println("hello sub unit else");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });*/
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
        db.closeDb();
        startActivity(new Intent(this, HomeScreen.class));
    }

    protected void callService() {
        Webservice service = new Webservice(AllocateTask.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)

                responseData = data;
                System.out.println("success data: "+responseData);
                saveData(data);

            }

            @Override
            public void dataDownloadFailed() {
                displayDialog("Error", "Problem in connection.");
            }

            @Override
            public void netNotAvailable() {
                displayDialog("Error", "No network connection.");
            }
        });
        service.execute("");
    }

    public void saveStageData(String data) {
        if (data.equalsIgnoreCase("$")) {
            System.out.println("No DATA!!!");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray stageArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < stageArray.length(); i++) {
                    JSONObject stageObject = stageArray.getJSONObject(i);
                    String floor_id = stageObject.getString("NODE_Id");
                    String floor_name = stageObject
                            .getString("NODE_Description_var");
                    String floor_scheme_id = db.selectedSchemeId/*stageObject
							.getString("NODE_PRJ_Id")*/;
                    String fk_building_id = db.selectedBuildingId/*stageObject
							.getString("NODE_Parent_Id")*/;
                    String fk_worktype_id = db.selectedWorkTypeId/*stageObject
							.getString("NDCHKL_WT_Id")*/;
                    String column = "floor_Id,floor_Name,Floor_Scheme_ID,FK_Bldg_ID,FK_WorkTyp_ID,user_id";
                    String values = "'" + floor_id + "','" + floor_name + "','"
                            + floor_scheme_id + "','" + fk_building_id + "','"
                            + fk_worktype_id + "','" + db.userId + "'";
                    db.insert("floor", column, values);
                }
                //setFloorSpinnerData();
                stageSpin.setClickable(true);
                stageSpin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callUnitService() {
        Webservice service = new Webservice(AllocateTask.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveUnitData(data);
            }

            @Override
            public void dataDownloadFailed() {
                displayDialog("Error", "Problem in connection.");
            }

            @Override
            public void netNotAvailable() {
                displayDialog("Error", "No network connection.");
            }
        });
        service.execute("");
    }

    public void saveUnitData(String data) {
        if (data.equalsIgnoreCase("$")) {
            unitSpin.setClickable(false);
            subunitspin.setClickable(false);
            if (isDataAvialableInCheckList(db.selectedNodeId,
                    db.selectedWorkTypeId, db.userId)) {
                //setCheckListSpinnerData();
                checklistspin.setClickable(true);
                checklistspin.setSelection(0);
            } else {
                method = "getCheckList";
                param = new String[]{"userID", "userRole", "workTypeId",
                        "nodeId"};
                value = new String[]{db.userId, "maker",
                        db.selectedWorkTypeId, db.selectedNodeId};
                callCheckListService();
            }
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray unitArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < unitArray.length(); i++) {
                    JSONObject stageObject = unitArray.getJSONObject(i);
                    String unit_id = stageObject.getString("NODE_Id");
                    String unit_name = stageObject
                            .getString("NODE_Description_var");
                    String unit_scheme_id = db.selectedSchemeId
							/*stageObject
							.getString("NODE_PRJ_Id")*/;
                    String fk_floor_id = db.selectedFloorId/* stageObject
							.getString("NODE_Parent_Id")*/;
                    String fk_worktype_id = db.selectedWorkTypeId/*stageObject
							.getString("NDCHKL_WT_Id")*/;
                    String column = "Unit_ID,Unit_Des,Unit_Scheme_id,Fk_Floor_ID,FK_WorkTyp_ID,user_id";
                    String values = "'" + unit_id + "','" + unit_name + "','"
                            + unit_scheme_id + "','" + fk_floor_id + "','"
                            + fk_worktype_id + "','" + db.userId+ "'";
                    db.insert("Unit", column, values);
                }
                //setUnitSpinnerData(db.selectedFloorId, db.selectedSchemeId);
                unitSpin.setClickable(true);
                unitSpin.setSelection(0);
            } catch (Exception e) {
                Log.e(TAG,"Exception: "+ e);
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callSubUnitService() {
        Webservice service = new Webservice(AllocateTask.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveSubUnitData(data);
            }

            @Override
            public void dataDownloadFailed() {
                displayDialog("Error", "Problem in connection.");
            }

            @Override
            public void netNotAvailable() {
                displayDialog("Error", "No network connection.");
            }
        });
        service.execute("");
    }

    public void saveSubUnitData(String data) {
        if (data.equalsIgnoreCase("$")) {
            System.out.println("No Data");
            if (isDataAvialableInCheckList(db.selectedNodeId,
                    db.selectedWorkTypeId, db.userId)) {
                //setCheckListSpinnerData();
                checklistspin.setClickable(true);
                checklistspin.setSelection(0);
            } else {
                method = "getCheckList";
                param = new String[]{"userID", "userRole", "workTypeId",
                        "nodeId"};
                value = new String[]{db.userId, "maker",
                        db.selectedWorkTypeId, db.selectedNodeId};
                callCheckListService();
            }
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray subunitArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < subunitArray.length(); i++) {
                    JSONObject stageObject = subunitArray.getJSONObject(i);
                    String sub_unit_id = stageObject.getString("NODE_Id");
                    String sub_unit_name = stageObject
                            .getString("NODE_Description_var");
                    String sub_unit_scheme_id = db.selectedSchemeId/*stageObject
							.getString("NODE_PRJ_Id")*/;
                    String fk_unit_id = stageObject.getString("NODE_Parent_Id");
                    String fk_worktype_id = db.selectedWorkTypeId/* stageObject
							.getString("NDCHKL_WT_Id")*/;
                    String column = "Sub_Unit_ID,Sub_Unit_Des,Sub_Unit_Scheme_id,FK_Unit_ID,FK_WorkTyp_ID,user_id";
                    String values = "'" + sub_unit_id + "','" + sub_unit_name
                            + "','" + sub_unit_scheme_id + "','" + fk_unit_id
                            + "','" + fk_worktype_id + "','" + db.userId + "'";
                    db.insert("SubUnit", column, values);
                }
                //setSubUnitSpinnerData(db.selectedUnitId, db.selectedSchemeId);
                subunitspin.setClickable(true);
                subunitspin.setText("");
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
                if (isDataAvialableInCheckList(db.selectedNodeId,
                        db.selectedWorkTypeId, db.userId)) {
                    //setCheckListSpinnerData();
                    checklistspin.setClickable(true);
                    checklistspin.setSelection(0);
                } else {
                    method = "getCheckList";
                    param = new String[]{"userID", "userRole", "workTypeId",
                            "nodeId"};
                    value = new String[]{db.userId, "maker",
                            db.selectedWorkTypeId, db.selectedNodeId};
                    callCheckListService();
                }
            }
        }
    }

    public void callCheckListService() {
        Webservice service = new Webservice(AllocateTask.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveCheckListData(data);
            }

            @Override
            public void dataDownloadFailed() {
                displayDialog("Error", "Problem in connection.");
            }

            @Override
            public void netNotAvailable() {
                displayDialog("Error", "No network connection.");
            }
        });
        service.execute("");
    }

    public void saveCheckListData(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            displayDialog("No Record Found",
                    "(Note: You may not have the updated data.)");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray checkListArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < checkListArray.length(); i++) {
                    JSONObject stageObject = checkListArray.getJSONObject(i);
                    String checklist_id = stageObject.getString("CHKL_Id");
                    String checklist_name = stageObject
                            .getString("CHKL_Name_var");
                    String node_id = db.selectedNodeId/*stageObject.getString("NDUSER_NODE_Id")*/;
                    String worktype_id = db.selectedWorkTypeId;//stageObject.getString("NDUSER_WT_Id");
                    String column = "Checklist_ID,Checklist_Name,Node_Id,FK_WorkTyp_ID,user_id";
                    String values = "'" + checklist_id + "','" + checklist_name
                            + "','" + node_id + "','" + worktype_id + "','"
                            + db.userId + "'";
                    db.insert("CheckList", column, values);
                }
                //setCheckListSpinnerData();
                checklistspin.setClickable(true);
                checklistspin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callGroupService(final String nodeID) {
        Webservice service = new Webservice(AllocateTask.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveGroupData(data, nodeID);
            }

            @Override
            public void dataDownloadFailed() {
                displayDialog("Error", "Problem in connection.");
            }

            @Override
            public void netNotAvailable() {
                displayDialog("Error", "No network connection.");
            }
        });
        service.execute("");
    }

    public void saveGroupData(String data, String NodeID) {
        if (data.equalsIgnoreCase("No Record Found")) {
            System.out.println("No Data");
        } else {

            try {

                String[] tabledata = data.split("\\$");
                groupList = new ArrayList<Group>();
                JSONArray groupArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < groupArray.length(); i++) {
                    Group group = new Group();
                    JSONObject stageObject = groupArray.getJSONObject(i);
                    String group_id = stageObject.getString("GRP_Id");
                    group.setGroupID(group_id);
                    String group_name = stageObject.getString("GRP_Name_var");
                    group.setGroupName(group_name);

                    String node_id = db.selectedNodeId/*stageObject.getString("NDUSER_NODE_Id")*/;
                    String checklist_id = db.selectedChecklistId;//stageObject.getString("NDUSER_WT_Id");

                    group.setCheckListID(checklist_id);
                    String group_sequence_tint = stageObject
                            .getString("GRP_Sequence_tint");
                    group.setGroupSequenceInt(group_sequence_tint);
                    String column = "Grp_ID,Grp_Name,Node_id,FK_Checklist_ID,user_id,GRP_Sequence_tint";
                    String values = "'" + group_id + "','" + group_name + "','"
                            + node_id + "','" + checklist_id + "','"
                            + db.userId + "','" + group_sequence_tint + "'";
                    // String values = "'" + group_id + "','" + group_name +
                    // "','" + node_id + "','" + checklist_id + "','" +
                    // db.userId + "','" + 3 + "'";
                    db.insert("Group1", column, values);
                    groupList.add(group);
                }

                //setGroupSpinnerData();
                grouptspin.setClickable(true);
                grouptspin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callQuestionService() {
        Webservice service = new Webservice(AllocateTask.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveQuestionData(data);
            }

            @Override
            public void dataDownloadFailed() {
                displayDialog("Error", "Problem in connection.");
            }

            @Override
            public void netNotAvailable() {
                displayDialog("Error", "No network connection.");
            }
        });
        service.execute("");
    }

    public void saveQuestionData(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            System.out.println("No DATA!!!");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray questionArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < questionArray.length(); i++) {
                    JSONObject stageObject = questionArray.getJSONObject(i);
                    String question_id = stageObject.getString("NDCHKL_QUE_Id");
                    String question_description = stageObject
                            .getString("QUE_Description_var");
                    String question_sequence = stageObject
                            .getString("QUE_SequenceNo_int");
                    String question_type = stageObject
                            .getString("QUE_Type_var");
                    String node_id = stageObject.getString("NDCHKL_NODE_Id");
                    String checklist_id = stageObject
                            .getString("NDCHKL_CHKL_Id");
                    String group_id = stageObject.getString("NDCHKL_GRP_Id");
                    String column = "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type,NODE_Id,Fk_CHKL_Id,Fk_Grp_ID,user_id";
                    String values = "'" + question_id + "','"
                            + question_description + "','" + question_sequence
                            + "','" + question_type + "','" + node_id + "','"
                            + checklist_id + "','" + group_id + "','"
                            + db.userId + "'";
                    db.insert("question", column, values);
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void saveData(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            System.out.println("No DATA");
        } else {

            try {
                String[] tabledata = data.split("\\$");

                JSONArray clientArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < clientArray.length(); i++) {
                    JSONObject clientObject = clientArray.getJSONObject(i);
                    String client_id = clientObject.getString("CL_Id");
                    String client_name = clientObject.getString("CL_Name_var");
                    String client_display_name = clientObject
                            .getString("CL_DisplayName_var");
                    String client_address = clientObject
                            .getString("CL_Address_var");
                    String column = "Client_ID,Clnt_Name,CL_Dispaly_Name,Clnt_Adrs,user_id";
                    String values = "'" + client_id + "','" + client_name
                            + "','" + client_display_name + "','"
                            + client_address + "','" + db.userId + "'";
                    db.insert("Client", column, values);
                    System.out.println("Row inserted in Client Table");
                }

                JSONArray projectArray = new JSONArray(tabledata[1]);
                for (int i = 0; i < projectArray.length(); i++) {
                    JSONObject projectObject = projectArray.getJSONObject(i);
                    String project_id = projectObject.getString("PRJ_Id");
                    String project_name = projectObject
                            .getString("PRJ_Name_var");
                    String project_client_id = projectObject
                            .getString("PRJ_CL_Id");
                    String project_display_name = projectObject
                            .getString("PRJ_DisplayName_var");
                    String project_address = projectObject
                            .getString("PRJ_Address_var");
                    String project_region = projectObject
                            .getString("PRJ_Region_var");
                    String project_scrolling_status = projectObject
                            .getString("PRJ_ScrollingUIStatus_bit");
                    String column = "PK_Scheme_ID,Scheme_Name,Scheme_Cl_Id,Scheme_Diplay_Name,Scheme_Adrs,Scheme_Region,scrolling_status,user_id";
                    String values = "'" + project_id + "','" + project_name
                            + "','" + project_client_id + "','"
                            + project_display_name + "','" + project_address
                            + "','" + project_region + "','"
                            + project_scrolling_status + "','" + db.userId
                            + "'";
                    db.insert("Scheme", column, values);
                    System.out.println("Row inserted in Project Table");
                }
                JSONArray workTypeArray = new JSONArray(tabledata[2]);
                for (int i = 0; i < workTypeArray.length(); i++) {
                    JSONObject workTypeObject = workTypeArray.getJSONObject(i);
                    String worktype_id = workTypeObject.getString("WT_Id");
                    String worktype_name = workTypeObject
                            .getString("WT_Name_var");
                    String worktype_level_tint = workTypeObject
                            .getString("WT_Level_tint");
                    String worktype_project_id = workTypeObject
                            .getString("PRJ_Id");
                    String column = "WorkTyp_ID,WorkTyp_Name,WorkTyp_level,FK_PRJ_Id,user_id";
                    String values = "'" + worktype_id + "','" + worktype_name
                            + "','" + worktype_level_tint + "','"
                            + worktype_project_id + "','" + db.userId + "'";
                    db.insert("WorkType", column, values);
                    System.out.println("Row inserted in WorkType Table");
                }
                setRFIData();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void saveDataScheme(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            System.out.println("No Data!!!");
        } else {

            try {
                String[] tabledata = data.split("\\$");

                String column = "PK_Scheme_ID,Scheme_Name,Scheme_Cl_Id,Scheme_Diplay_Name,Scheme_Adrs,Scheme_Region,scrolling_status,user_id";
                saveToDatabase("Scheme", column, tabledata[1], true, 7);
                System.out.println("table 1========" + tabledata[1].toString());

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

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        return super.onCreatePanelView(featureId);
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

        System.out
                .println("data flush+++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    public boolean isQuestionAvailable() {
        String whereClause = "Fk_CHKL_Id='" + db.selectedChecklistId
                + "' AND Fk_Grp_ID='" + db.selectedGroupId + "' AND NODE_Id='"
                + db.selectedBuildingId + "' AND user_id='" + db.userId + "'";

        System.out.println("check avaible q===" + whereClause);
        Cursor cursor = db.select("question",
                "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type", whereClause,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {

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
