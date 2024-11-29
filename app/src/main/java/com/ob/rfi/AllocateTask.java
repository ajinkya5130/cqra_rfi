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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.ob.allocate_task.presentationlayer.AllocateTaskViewModel;
import com.ob.database.db_tables.ClientTableModel;
import com.ob.database.db_tables.ProjectTableModel;
import com.ob.database.db_tables.StageTableModel;
import com.ob.database.db_tables.StructureTableModel;
import com.ob.database.db_tables.WorkTypeTableModel;
import com.ob.rfi.db.RfiDatabase;
import com.ob.rfi.models.SpinnerType;
import com.ob.rfi.service.Webservice;
import com.ob.rfi.service.Webservice.downloadListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private String[] clientId;
    private Spinner rfiSpin;
    private Spinner projSpin;
    private Spinner clintSpin;
    private Spinner structureSpin;
    private Spinner stageSpin;
    private Spinner unitSpin;
    private Spinner subunitspin;
    private Spinner elementspin;
    private Spinner subelementspin;

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
        subunitspin = (Spinner) findViewById(R.id.rfi_sub_unit_id);
        elementspin = (Spinner) findViewById(R.id.rfi_element_id);
        subelementspin = (Spinner) findViewById(R.id.rfi_sub_element_id);
        checklistspin = (Spinner) findViewById(R.id.rfi_checklist_id);
        grouptspin = (Spinner) findViewById(R.id.rfi_group_id);

        coverageedit = (EditText) findViewById(R.id.coverage_id);
        drawingeedit = (EditText) findViewById(R.id.drawing_id);

        okBtn = (Button) findViewById(R.id.question_select_submit);
        backBtn = (Button) findViewById(R.id.question_select_Back);


        ResetId();
        setSpiner();
        viewModel = new ViewModelProvider(this).get(AllocateTaskViewModel.class);
        //RFIRoomDb dbObject = RoomDbObject.INSTANCE.getDbObject(this);
        viewModel.getClientData();

        db = new RfiDatabase(getApplicationContext());
        if (db.userId.equalsIgnoreCase("")) {
            logout();
            return;
        }

        observerData();

        /*if (!db.userId.equalsIgnoreCase("")) {
            if (isDataAvialable("Client")) {
                setRFIData();
                // setClientData();
            } else {
                updateData();
            }
            // disableList(false);
        } else {
            logout();
        }*/

        okBtn.setOnClickListener(new OnClickListener() {
            private SharedPreferences checkPreferences;
            private Editor editor;

            @Override
            public void onClick(View arg0) {

                String table_fields = "Client, Project, WorkType, Structure, Stage, Unit, SubUnit, Element, SubElement, CheckList, GroupColumn, UserID,NodeID";
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


                if (validateScreen()) {
                    displayErrorDialog("Error", errorMessage);

                    // setalertdata();

                    /*
                     * RfiDatabase.gropPosition = 0; RfiDatabase.childPosition =
                     * 0;
                     */
                } else if (isQuestionAvailable()) {
                    if (db.selectedScrollStatus.equalsIgnoreCase("true")) {
                        Intent int1 = new Intent(AllocateTask.this,
                                HomeScreen.class);
                        startActivity(int1);

                    } else {
                        Intent int1 = new Intent(AllocateTask.this,
                                HomeScreen.class);
                        startActivity(int1);

                        /*
                         * Intent int1=new
                         * Intent(SelectQuestion.this,RfiQuestionire.class);
                         * int1
                         * .putExtra("coverage",coverageedit.getText().toString
                         * ()); startActivity(int1);
                         */
                    }
                    /*
                     * Intent int1=new
                     * Intent(SelectQuestion.this,RfiQuestionire.class);
                     * int1.putExtra
                     * ("coverage",coverageedit.getText().toString());
                     */
                    /*
                     * Intent int1=new
                     * Intent(SelectQuestion.this,RfiQuestionSelect.class);
                     * int1.
                     * putExtra("coverage",coverageedit.getText().toString());
                     * int1
                     * .putExtra("drawingId",drawingeedit.getText().toString());
                     *
                     *
                     * checkPreferences=getSharedPreferences("RFI_File",MODE_PRIVATE
                     * ); editor=checkPreferences.edit();
                     * editor.putString("coverage"
                     * ,coverageedit.getText().toString());
                     * editor.putString("drawing"
                     * ,drawingeedit.getText().toString()); editor.commit();
                     *
                     * startActivity(int1);
                     */
                }
                else
                {
                   // displayErrorDialog("", "Please Select Another Combination!");
                }
            }


        });

        backBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

	/*	if (!db.userId.equalsIgnoreCase("")) {
			if (isDataAvialable("Client")) {
				setRFIData();
				// setClientData();
			} else {
				updateData();
			}
			// disableList(false);
		} else {
			logout();
		} */

    }

    private void observerData() {

        Objects.requireNonNull(viewModel.getLvClientData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: value: "+value);

                    if (value!=0){
                        setRFIData();
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
                        Log.d(TAG, "observerData: getProjectApi ");
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
                    Log.d(TAG, "onCreate: getLvWorkTypeData: "+value);

                    if (value!=0){
                        Log.d(TAG, "observerData: getLvWorkTypeData");
                        setBuildingSpinnerData();
                    }else {
                        //updateData();
                        Log.d(TAG, "observerData: getLvWorkTypeData ");
                        viewModel.getStructureApi(31, "Maker");
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

        Objects.requireNonNull(viewModel.getLvErrorData()).observe(
                this, value ->{
                    Log.d(TAG, "onCreate: getLvStageData: "+value);
                    resetSpinner(value.getSpinnerType());
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(value.getMessage());
                    builder.setTitle("Error");
                    builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                    builder.show();
                }
        );
    }

    private void resetSpinner(SpinnerType spinnerType) {
        if (spinnerType.equals(SpinnerType.STAGE)){
            setFloorSpinnerData();
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
        // requestid = 1;
        /***** AKSHAY *****/
        /*
         * method = "getDetails"; param = new String[] { "userID","userRole" };
         * value = new String[] { db.userId,"maker" };
         */
        /***** AKSHAY *****/
        // Changes made 24-April-2015
        method = "getClientProjectWorkType";
        param = new String[]{"userID", "userRole"};
        value = new String[]{db.userId, "maker"};
        callService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setSpiner();
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
            subunitspin.setSelection(0);
            elementspin.setSelection(0);
            subelementspin.setSelection(0);
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
        return false;
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

    // ------------------------------------------------------------------------------

    private void setRFIData() {

        // Rfi_New_Create(FK_rfi_Id TEXT,user_id TEXT)"

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

       /* String value = String.valueOf(nval);
        final String[] items = new String[2];
        items[0] = "--select--";
        items[1] = "create new" + value;*/

        setClientData();

	/*	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rfiSpin.setAdapter(adapter);
		rfiSpin.setSelection(1);
		rfiSpin.setClickable(false);
		rfiSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> aview, View view,
					int position, long rowid) {
				if (position > 0) {
					setClientData();
					db.selectedrfiId = items[1];

				} else {
					System.out.println("hello");
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		}); */
    }

    // /---client
    private void setClientData() {

        insertFlag = false;

        /*String where = "user_id='" + db.userId + "'";

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
        }
*/
        ArrayList<ClientTableModel> list = viewModel.getList();
        int size = list.size();
        clientId = new String[size];
        client = new String[size];
        for(int i =0; i<size; i++){
            client[i] = list.get(i).getClnt_Name();
            clientId[i] = list.get(i).getClient_ID();
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
                    db.selectedClientId = clientId[position];
                    viewModel.getProjectDataFromDB();
                }else {
                    worktypeSpin.setSelection(0);
                    worktypeSpin.setClickable(false);
                    projSpin.setSelection(1);//changed by pramod
                    projSpin.setClickable(false);
                    db.selectedClientId = "";
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
                    worktypeSpin.setClickable(true);
                    db.selectedSchemeName = projSpin.getSelectedItem()
                            .toString();
                    viewModel.getWorkTypeFromDB();
                    //setWorkTypeSpinnerData();
                } else {
                    db.selectedSchemeId = "";
                    db.selectedScrollStatus = "";
                    db.selectedBuildingId = "";
                    //db.selectedChecklistId = "";
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
        /* FK_PRJ_Id */

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
                    viewModel.getStructureDataFromDB();
                } else {
                    db.selectedWorkTypeId = "";
                    db.selectedBuildingId = "";
                    db.selectedSubGroupId = "";
                    db.selectedlevelId = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setBuildingSpinnerData() {

        // Building(Bldg_ID TEXT,Bldg_Name TEXT,Build_scheme_id TEXT, user_id
        // TEXT)");

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
                    /*
                    if (isDataAvialableForID("floor", "FK_Bldg_ID",
                            db.selectedBuildingId)) {
                        setFloorSpinnerData(bldgId[position - 1], schemeid);
                        stageSpin.setClickable(true);
                        stageSpin.setSelection(0);
                    } else {
                        method = "getStage";
                        param = new String[]{"userID", "userRole",
                                "projectId", "workTypeId", "parentId"};
                        value = new String[]{db.userId, "maker",
                                db.selectedSchemeId, db.selectedWorkTypeId,
                                db.selectedBuildingId};
                        callStageService();
                    }*/
                }
                else {
                    stageSpin.setClickable(false);
                    stageSpin.setSelection(0);
                    unitSpin.setClickable(false);
                    unitSpin.setSelection(0);
                    subunitspin.setClickable(false);
                    subunitspin.setSelection(0);
                    elementspin.setClickable(false);
                    elementspin.setSelection(0);
                    subelementspin.setClickable(false);
                    subelementspin.setSelection(0);

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

                   /* if (isDataAvialableForID("Unit", "Fk_Floor_ID",
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
                    subunitspin.setSelection(0);
                    elementspin.setClickable(false);
                    elementspin.setSelection(0);
                    subelementspin.setClickable(false);
                    subelementspin.setSelection(0);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setUnitSpinnerData(String floor_id, final String schemid) {

        // TABLE Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id TEXT,Fk_Floor_ID
        // TEXT, user_id TEXT)");

        String where = "u.Unit_Scheme_id=s.PK_Scheme_ID" + " AND s.user_id='"
                + db.userId + "'  AND u.Fk_Floor_ID ='" + floor_id
                + "'  AND u.FK_WorkTyp_ID ='" + db.selectedWorkTypeId
                + "'  AND u.Unit_Scheme_id='" + schemid
                + "' AND u.user_id=s.user_id ";
        System.out.println("where ->" + where);
        Cursor cursor = db.select("Unit as u,Scheme as s",
                "distinct(u.Unit_ID),u.Unit_Des", where, null, null, null,
                "u.Unit_Des");

        System.out.println("selected scheme id" + db.selectedBuildingId);

        unitId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                unitId[cursor.getPosition()] = cursor.getString(0);
                System.out.println("trade id printed=" + cursor.getString(0));
                items[cursor.getPosition() + 1] = cursor.getString(1);
                System.out.println("trade name printed=" + cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            items[0] = "Unit(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        final String where1 = "FK_WorkTyp_ID ='" + db.selectedWorkTypeId
                + "' AND user_id='" + db.userId + "'";
        final String table = "SubUnit";

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
                    db.selectedUnitId = unitId[position - 1];
                    db.selectedNodeId = unitId[position - 1];

                    /***** AKSHAY *****/
                    // CHANGE MADE ON : 30-APRIL-2015
                    // setSubUnitSpinnerData(unitId[position - 1],schemid);
                    /*
                     * setCheckListSpinnerData();
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     *
                     *
                     * if(CheckWorkType(table, where1) &&
                     * Integer.parseInt(db.selectedlevelId)>=4) {
                     * setSubUnitSpinnerData(unitId[position - 1],schemid);
                     * subunitspin.setClickable(true);
                     * subunitspin.setSelection(0); }
                     *//***** AKSHAY *****/

                    if (isDataAvialableForID("SubUnit", "FK_Unit_ID",
                            db.selectedUnitId)) {
                        setSubUnitSpinnerData(db.selectedUnitId,
                                db.selectedSchemeId);
                        subunitspin.setClickable(true);
                        subunitspin.setSelection(0);
                    } else {
                        method = "getSubUnit";
                        param = new String[]{"userID", "userRole",
                                "projectId", "workTypeId", "parentId"};
                        value = new String[]{db.userId, "maker",
                                db.selectedSchemeId, db.selectedWorkTypeId,
                                db.selectedUnitId};
                        callSubUnitService();
                    }

                } else {
                    subunitspin.setClickable(false);
                    subunitspin.setSelection(0);
                    elementspin.setClickable(false);
                    elementspin.setSelection(0);
                    subelementspin.setClickable(false);
                    subelementspin.setSelection(0);
                    //checklistspin.setClickable(false);
                    //checklistspin.setSelection(0);

                    //db.selectedChecklistId = "";
                    //db.selectedSubGroupId = "";
                    //db.selectedUnitId = "";
                    //db.selectedSubUnitId = "";
                    //db.selectedElementId = "";
                    //db.selectedSubElementId = "";

                    System.out.println("hello else unit");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setSubUnitSpinnerData(String unit_id, final String schemid) {

        // TABLE Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id TEXT,Fk_Floor_ID
        // TEXT, user_id TEXT)");
        // SubUnit(Sub_Unit_ID TEXT,Sub_Unit_Des TEXT,Sub_Unit_Scheme_id
        // TEXT,FK_Unit_ID TEXT, user_id TEXT)");

        String where = "u.Sub_Unit_Scheme_id=s.PK_Scheme_ID"
                + " AND s.user_id='" + db.userId + "' AND s.PK_Scheme_ID='"
                + schemid + "' AND u.FK_Unit_ID='" + unit_id
                + "' AND u.FK_WorkTyp_ID='" + db.selectedWorkTypeId
                + "' AND u.user_id=s.user_id ";
        System.out.println("where ->" + where);
        Cursor cursor = db.select("SubUnit as u,Scheme as s",
                "distinct(u.Sub_Unit_ID),u.Sub_Unit_Des", where, null, null,
                null, "u.Sub_Unit_Des");

        System.out.println("selected scheme id" + db.selectedBuildingId);

        subunitId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                subunitId[cursor.getPosition()] = cursor.getString(0);
                System.out.println("trade id printed=" + cursor.getString(0));
                items[cursor.getPosition() + 1] = cursor.getString(1);
                System.out.println("trade name printed=" + cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            items[0] = "SubUnit(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        final String where1 = "FK_WorkTyp_ID ='" + db.selectedWorkTypeId
                + "' AND user_id='" + db.userId + "'";
        final String table = "Element";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subunitspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        subunitspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedSubUnitId = subunitId[position - 1];
                    db.selectedNodeId = subunitId[position - 1];

                    /***** AKSHAY *****/
                    // CHANGE MADE ON : 30-APRIL-2015
                    // setElementSpinnerData(subunitId[position - 1],schemid);
                    /*
                     * setCheckListSpinnerData();
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     *
                     *
                     * if(CheckWorkType(table, where1) &&
                     * Integer.parseInt(db.selectedlevelId)>=5) {
                     * setElementSpinnerData(subunitId[position - 1],schemid);
                     * elementspin.setClickable(true);
                     * elementspin.setSelection(0); }
                     */

                    if (isDataAvialableForID("Element", "FK_Sub_Unit_ID",
                            db.selectedSubUnitId)) {
                        setElementSpinnerData(db.selectedSubUnitId,
                                db.selectedSchemeId);
                        elementspin.setClickable(true);
                        elementspin.setSelection(0);
                    } else {

                        method = "getElement";
                        param = new String[]{"userID", "userRole",
                                "projectId", "workTypeId", "parentId",
                                "checkListId", "groupId"};
                        value = new String[]{db.userId, "maker",
                                db.selectedSchemeId, db.selectedWorkTypeId,
                                db.selectedSubUnitId, db.selectedChecklistId,
                                db.selectedGroupId};
                        callElementService();
                    }

                } else {
                    elementspin.setClickable(false);
                    elementspin.setSelection(0);
                    subelementspin.setClickable(false);
                    subelementspin.setSelection(0);
                    //checklistspin.setClickable(false);
                    //checklistspin.setSelection(0);

                    //db.selectedChecklistId = "";
                    //	db.selectedSubGroupId = "";
                    //	db.selectedSubUnitId = "";
                    //	db.selectedElementId = "";
                    //	db.selectedSubElementId = "";

                    System.out.println("hello sub unit else");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setElementSpinnerData(String subunit_id, final String schemid) {

        // Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID
        // TEXT, user_id TEXT)");

        String where = "e.Elmt_Scheme_id=s.PK_Scheme_ID" + "  AND s.user_id='"
                + db.userId + "'  AND e.FK_Sub_Unit_ID='" + subunit_id
                + "'  AND e.Elmt_Scheme_id='" + schemid
                + "'  AND e.FK_WorkTyp_ID='" + db.selectedWorkTypeId
                + "' AND e.user_id=s.user_id ";
        System.out.println("where ->" + where);
        Cursor cursor = db.select("Element as e,Scheme as s",
                "distinct(e.Elmt_ID),e.Elmt_Des", where, null, null, null,
                "e.Elmt_Des");

        // System.out.println("selected scheme id"+db.selectedBuildingId);

        elementId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                elementId[cursor.getPosition()] = cursor.getString(0);
                System.out.println("trade id printed=" + cursor.getString(0));
                items[cursor.getPosition() + 1] = cursor.getString(1);
                System.out.println("trade name printed=" + cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            items[0] = "Element(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        final String where1 = "FK_WorkTyp_ID ='" + db.selectedWorkTypeId
                + "' AND user_id='" + db.userId + "'";
        final String table = "SubElement";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        elementspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        elementspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedElementId = elementId[position - 1];
                    db.selectedNodeId = elementId[position - 1];
                    // setSubElementSpinnerData(elementId[position -
                    // 1],schemid);

                    /***** AKSHAY *****/
                    // CHANGES MADE ON : 30-APRIL-2015
                    /*
                     * setCheckListSpinnerData();
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     *
                     *
                     * if(CheckWorkType(table, where1) &&
                     * Integer.parseInt(db.selectedlevelId)>=6) {
                     * setSubElementSpinnerData(elementId[position -
                     * 1],schemid); subelementspin.setClickable(true);
                     * subelementspin.setSelection(0); }
                     *//***** AKSHAY *****/

                    if (isDataAvialableForID("SubElement", "FK_Elmt_ID",
                            db.selectedElementId)) {
                        setSubElementSpinnerData(db.selectedElementId,
                                db.selectedSchemeId);
                        subelementspin.setClickable(true);
                        subelementspin.setSelection(0);
                    } else {
                        method = "getSubElement";
                        param = new String[]{"userID", "userRole",
                                "projectId", "workTypeId", "parentId"};
                        value = new String[]{db.userId, "maker",
                                db.selectedSchemeId, db.selectedWorkTypeId,
                                db.selectedElementId};
                        callSubElementService();
                    }

                } else {
                    subelementspin.setClickable(false);
                    subelementspin.setSelection(0);
                    //checklistspin.setClickable(false);
                    //	checklistspin.setSelection(0);

                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedElementId = "";
                    db.selectedSubElementId = "";

                    System.out.println("hello");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setSubElementSpinnerData(String element_id,
                                          final String schemid) {

        // Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID
        // TEXT, user_id TEXT)");

        // SubElement(Sub_Elmt_ID TEXT,Sub_Elmt_Des TEXT,Sub_Elmt_Scheme_id
        // TEXT,FK_Elmt_ID TEXT, user_id TEXT)");

        String where = "e.Sub_Elmt_Scheme_id=s.PK_Scheme_ID"
                + " AND s.user_id='" + db.userId + "' AND s.PK_Scheme_ID='"
                + schemid + "' AND e.FK_Elmt_ID='" + element_id
                + "' AND e.FK_WorkTyp_ID='" + db.selectedWorkTypeId

                + "' AND e.user_id=s.user_id ";
        System.out.println("where ->" + where);
        Cursor cursor = db.select("SubElement as e,Scheme as s",
                "distinct(e.Sub_Elmt_ID),e.Sub_Elmt_Des", where, null, null,
                null, "e.Sub_Elmt_Des");

        // System.out.println("selected scheme id"+db.selectedBuildingId);
        System.out.println("Cursor Count in Sub-Element is : "
                + cursor.getCount());
        subelementId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                subelementId[cursor.getPosition()] = cursor.getString(0);
                System.out.println("trade id printed=" + cursor.getString(0));
                items[cursor.getPosition() + 1] = cursor.getString(1);
                System.out.println("trade name printed=" + cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            items[0] = "Element(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subelementspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        subelementspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedSubElementId = subelementId[position - 1];
                    db.selectedNodeId = subelementId[position - 1];

                    /***** AKSHAY *****/
                    // CHANGE MADE ON : 4-MAY-2015
                    /*
                     * setCheckListSpinnerData();
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     *//***** AKSHAY *****/

                    method = "getQuestion";
                    param = new String[]{ /*"userID", "userRole",*/ "nodeId",
                            "groupId"};
                    value = new String[]{ /*db.userId, "maker",*/
                            db.selectedNodeId, db.selectedGroupId};
                    callQuestionService();
					/*if (isDataAvialableInCheckList(db.selectedNodeId,
							db.selectedWorkTypeId, db.userId)) {
						//setCheckListSpinnerData();
						checklistspin.setClickable(true);
						checklistspin.setSelection(0);
					} else {
						*//*method = "getCheckList";
						param = new String[] { "userID", "userRole",
								"workTypeId", "nodeId" };
						value = new String[] { db.userId, "maker",
								db.selectedWorkTypeId, db.selectedNodeId };
						callCheckListService();*//*
					}*/

                } else {

				/*	checklistspin.setClickable(false);
					checklistspin.setSelection(0);
					grouptspin.setClickable(false);
					grouptspin.setSelection(0);

					db.selectedChecklistId = "";
					db.selectedSubGroupId = "";
					db.selectedSubElementId = "";*/

                    System.out.println("else subelement ");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
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

        // Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID
        // TEXT, user_id TEXT)");

        // CheckList(Checklist_ID TEXT,Checklist_Name TEXT,Node_Id TEXT, user_id
        // TEXT)");

        String where = "s.user_id='" + db.userId + "' AND Node_id='"
                + db.selectedNodeId + "' AND FK_WorkTyp_ID ='"
                + db.selectedWorkTypeId + "' AND c.user_id=s.user_id ";
        System.out.println("where ->" + where);
        Cursor cursor = db.select("CheckList as c,Scheme as s",
                "distinct(c.Checklist_ID),c.Checklist_Name", where, null, null,
                null, "c.Checklist_Name");

        // System.out.println("selected scheme id"+db.selectedBuildingId);

        checklistId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                checklistId[cursor.getPosition()] = cursor.getString(0);
                System.out.println("trade id printed=" + cursor.getString(0));
                items[cursor.getPosition() + 1] = cursor.getString(1);
                System.out.println("trade name printed=" + cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            items[0] = "CheckList(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checklistspin.setAdapter(adapter);

        checklistspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedChecklistId = checklistId[position - 1];
                    db.selectedChecklistName = checklistspin.getSelectedItem()
                            .toString();
                    /***** AKSHAY *****/
                    // CHANGE MADE ON : 4-MAY-2015
                    /*
                     * setGoupSpinnerData(checklistId[position - 1]);
                     * grouptspin.setClickable(true);
                     * grouptspin.setSelection(0);
                     */
			/*		if (isDataAvialableForID("Group1", "FK_Checklist_ID",
							db.selectedChecklistId)) {
						setGoupSpinnerData(checklistId[position - 1]);
						grouptspin.setClickable(true);
						grouptspin.setSelection(0);
					} else { */
                    method = "getGroup";
                    param = new String[]{"userID", "userRole", "nodeId",
                            "checkListId"};
                    value = new String[]{db.userId, "maker",
                            db.selectedNodeId, db.selectedChecklistId};
                    callGroupService(db.selectedNodeId);
                    /*	} */

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

    private void setGoupSpinnerData(final String checklistId, String NodeID) {

        // Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID
        // TEXT, user_id TEXT)");

        // Group1(Grp_ID TEXT,Grp_Name TEXT,Node_id TEXT,FK_Checklist_ID
        // TEXT,user_id TEXT)");

        String where = "c.Checklist_ID='" + checklistId + "' AND g.Node_id='"
                + /*db.selectedNodeId*/NodeID
                + "'  AND  g.FK_Checklist_ID=c.Checklist_ID"
                + " AND c.user_id='" + db.userId

                + "' AND g.user_id=c.user_id ";
        System.out.println("where ->" + where);
        Cursor cursor = db.select("Group1 as g,CheckList as c",
                "distinct(g.Grp_ID),g.Grp_Name,g.Node_id", where, null, null,
                null, "g.GRP_Sequence_tint");

        // System.out.println("selected scheme id"+db.selectedBuildingId);

        groupId = new String[cursor.getCount()];

        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                groupId[cursor.getPosition()] = cursor.getString(0);

                items[cursor.getPosition() + 1] = cursor.getString(1);

                System.out
                        .println("goup id==" + cursor.getString(2).toString());
            } while (cursor.moveToNext());
        } else {
            items[0] = "Group(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grouptspin.setAdapter(adapter);

        grouptspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedGroupId = groupId[position - 1];
                    System.out.println("Selected Group ID is : "
                            + db.selectedGroupId);
                    /*
                     * // System.out.println("Size Of Group List is : " +
                     * groupList.size());
                     * db.selectedGroupName=grouptspin.getSelectedItem
                     * ().toString(); String listIndexGroup = ""; for(int i = 0;
                     * i < groupList.size(); i++) {
                     * System.out.println("Iteration : " + i); Group temp =
                     * groupList.get(i); if(temp.getGroupID() ==
                     * db.selectedGroupId) { listIndexGroup =
                     * temp.getGroupSequenceInt();
                     * System.out.println("Selected Group Sequence Number is : "
                     * + listIndexGroup); break; } else { continue; } }
                     */
			/*	if (isSelectedGroupSequenceOne(db.selectedGroupId)) {
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
					} else {
						if (isCoverageSpinner) {
							method = "getCoverage";
							param = new String[] { "nodeId", "checkListId" };
							value = new String[] { db.selectedNodeId,
									db.selectedChecklistId };
							// value = new String[] { "29","1"};
							callCoverageService();
						} else {
							View change;
							if (isCoverageTextViewNew) {
								change = (View) findViewById(R.id.coverage_id_edittext);
							} else {
								change = (View) findViewById(R.id.coverage_id);
							}
							ViewGroup parent = (ViewGroup) change.getParent();
							int index = parent.indexOfChild(change);
							parent.removeView(change);
							LayoutInflater inflater = getLayoutInflater();
							change = inflater.inflate(
									R.layout.select_question_coverage_spinner,
									parent, false);
							parent.addView(change, index);
							coverageSpinner = (Spinner) findViewById(R.id.coverage_id_spinner);
							isCoverageSpinner = true;
							method = "getCoverage";
							param = new String[] { "nodeId", "checkListId" };
							value = new String[] { db.selectedNodeId,
									db.selectedChecklistId };
							// value = new String[] { "29","1"};
							callCoverageService();
						}
					} */
                    /*
                     * groupList.clear();
                     * if(listIndexGroup.equalsIgnoreCase("1")) {
                     * System.out.println("Enter Coverage Manually!!!"); } else
                     * { View change = (View) findViewById(R.id.coverage_id);
                     * ViewGroup parent = (ViewGroup) change.getParent(); int
                     * index = parent.indexOfChild(change);
                     * parent.removeView(change); LayoutInflater inflater =
                     * getLayoutInflater(); change =
                     * inflater.inflate(R.layout.select_question_coverage_spinner
                     * , parent, false); parent.addView(change, index);
                     * coverageSpinner = (Spinner)
                     * findViewById(R.id.coverage_id_spinner); method =
                     * "getCoverage"; param = new String[] {
                     * "nodeId","checkListId"}; value = new String[] {
                     * db.selectedNodeId,db.selectedChecklistId};
                     * callCoverageService(); }
                     */
                    if (isQuestionAvailable()) {
                        System.out
                                .println("Data Already Available in Question table");
                    } else {
						method = "getQuestion";
						param = new String[] { "userID", "userRole", "nodeId",
								"groupId" };
						value = new String[] { db.userId, "maker",
								db.selectedNodeId, db.selectedGroupId };
						callQuestionService();
//                        method = "getStructure";
//                        param = new String[]{"userID", "userRole",
//                                "projectId", "workTypeId", "parentId"
//                                , "checkListId", "groupId"};
//                        value = new String[]{db.userId, "maker",
//                                db.selectedSchemeId, db.selectedWorkTypeId,
//                                "0", db.selectedChecklistId, db.selectedGroupId};
//
//                        callStructureService();    ///db.selectedSchemeId
                    }

                } else {
                    db.selectedGroupId = "";
                    System.out.println("else..................");
                }
            }

            public boolean isSelectedGroupSequenceOne(String userSelectedGroupID) {
                String columns = "GRP_Sequence_tint";
                String where = "Grp_ID = '" + userSelectedGroupID + "'";
                Cursor cursor = db.select("Group1", columns, where, null, null,
                        null, null);
                if (cursor.moveToFirst()) {
                    String value = cursor.getString(cursor
                            .getColumnIndex("GRP_Sequence_tint"));
                    if (value.equalsIgnoreCase("1")) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    System.out.println("Cusror Count : " + cursor.getCount());
                    return false;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public boolean CheckWorkType(String table, String where) {
        /*
         * String where = "user_id='" + db.userId;
         *
         * Cursor cursor = db.select("Group1 as g,CheckList as c",
         * "distinct(g.Grp_ID),g.Grp_Name,g.Node_id", where, null, null, null,
         * "g.Grp_Name");
         */

        Cursor cursor = db.select(table, "count(*) as noitem", where, null,
                null, null, null);

        System.out.println("in checkWorkType..................");
        // db.select(TABLE_NAME, COLUMNS, WHERE, SELECTION_ARGS, GROUP_BY,
        // HAVING, OREDER_BY)
        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {
                System.out
                        .println("entry present..............for worktype id");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.selectedHeadingId = "";
        finish();
        db.closeDb();
        startActivity(new Intent(this, HomeScreen.class));
    }

    // -------------------------------------------------------------------------balaji
    // code-----------

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

    protected void callStructureService() {
        Webservice service = new Webservice(AllocateTask.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)

                responseData = data;
                System.out.println("success data");
                saveStructureData(data);

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

    protected void saveStructureData(String data) {      ///, String schemeID
        // TODO Auto-generated method stub
        if (data.equalsIgnoreCase("$")) {
            System.out.println("No DATA!!!");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray structureArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < structureArray.length(); i++) {
                    JSONObject structureObject = structureArray
                            .getJSONObject(i);
                    String building_id = structureObject.getString("NODE_Id");
                    String building_name = structureObject
                            .getString("NODE_Description_var");
                    String building_project_id =structureObject
							.getString("NODE_PRJ_Id");    // schemeID
                    String parent_id = "0"/*structureObject
							.getString("NODE_Parent_Id")*/;
                    String fk_worktype_id = db.selectedWorkTypeId/*structureObject
							.getString("NDCHKL_WT_Id")*/;
                    String column = "Bldg_ID,Bldg_Name,Build_scheme_id,FK_WorkTyp_ID,user_id";
                    String values = "'" + building_id + "','" + building_name
                            + "','" + building_project_id + "','"
                            + fk_worktype_id + "','" + db.userId + "'";
                    db.insert("Building", column, values);
                }
                //setBulidngSpinnerData(db.selectedSchemeId);
                structureSpin.setClickable(true);
                structureSpin.setSelection(0);

            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callStageService() {
        Webservice service = new Webservice(AllocateTask.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveStageData(data);
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
            elementspin.setClickable(false);
            subelementspin.setClickable(false);
            if (isDataAvialableInCheckList(db.selectedNodeId,
                    db.selectedWorkTypeId, db.userId)) {
                setCheckListSpinnerData();
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
                setUnitSpinnerData(db.selectedFloorId, db.selectedSchemeId);
                unitSpin.setClickable(true);
                unitSpin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
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
                setCheckListSpinnerData();
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
                setSubUnitSpinnerData(db.selectedUnitId, db.selectedSchemeId);
                subunitspin.setClickable(true);
                subunitspin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
                if (isDataAvialableInCheckList(db.selectedNodeId,
                        db.selectedWorkTypeId, db.userId)) {
                    setCheckListSpinnerData();
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

    public void callElementService() {
        Webservice service = new Webservice(AllocateTask.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveElementData(data);
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

    public void saveElementData(String data) {
        if (data.equalsIgnoreCase("$")) {

            method = "getCheckList";
            param = new String[]{"userID", "userRole", "workTypeId",
                    "nodeId"};
            value = new String[]{db.userId, "maker",
                    db.selectedWorkTypeId, db.selectedNodeId};
            callCheckListService();

        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray elementArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < elementArray.length(); i++) {
                    JSONObject stageObject = elementArray.getJSONObject(i);
                    String element_id = stageObject.getString("NODE_Id");
                    String element_name = stageObject
                            .getString("NODE_Description_var");
                    String element_scheme_id = db.selectedSchemeId/*stageObject
							.getString("NODE_PRJ_Id")*/;
                    String fk_sub_unit_id = db.selectedSubUnitId /*stageObject
							.getString("NODE_Parent_Id")*/;
                    String fk_worktype_id = db.selectedWorkTypeId/*stageObject
							.getString("NDCHKL_WT_Id")*/;
                    String column = "Elmt_ID,Elmt_Des,Elmt_Scheme_id,FK_Sub_Unit_ID,FK_WorkTyp_ID,user_id";
                    String values = "'" + element_id + "','" + element_name
                            + "','" + element_scheme_id + "','"
                            + fk_sub_unit_id + "','" + fk_worktype_id + "','"
                            + db.userId + "'";
                    db.insert("Element", column, values);
                }
                setElementSpinnerData(db.selectedSubUnitId, db.selectedSchemeId);
                elementspin.setClickable(true);
                elementspin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callSubElementService() {
        Webservice service = new Webservice(AllocateTask.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveSubElementData(data);
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

    public void saveSubElementData(String data) {
        if (data.equalsIgnoreCase("$")) {

            method = "getCheckList";
            param = new String[]{"userID", "userRole", "workTypeId",
                    "nodeId"};
            value = new String[]{db.userId, "maker",
                    db.selectedWorkTypeId, db.selectedNodeId};
            callCheckListService();

        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray subElementArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < subElementArray.length(); i++) {
                    JSONObject stageObject = subElementArray.getJSONObject(i);
                    String sub_element_id = stageObject.getString("NODE_Id");
                    String sub_element_name = stageObject
                            .getString("NODE_Description_var");
                    String sub_element_scheme_id = db.selectedSchemeId/*stageObject
							.getString("NODE_PRJ_Id")*/;
                    String fk_element_id = db.selectedElementId/*stageObject
							.getString("NODE_Parent_Id")*/;
                    String fk_worktype_id = db.selectedWorkTypeId/*stageObject
							.getString("NDCHKL_WT_Id")*/;
                    String column = "Sub_Elmt_ID,Sub_Elmt_Des,Sub_Elmt_Scheme_id,FK_Elmt_ID,FK_WorkTyp_ID,user_id";
                    String values = "'" + sub_element_id + "','"
                            + sub_element_name + "','" + sub_element_scheme_id
                            + "','" + fk_element_id + "','" + fk_worktype_id
                            + "','" + db.userId + "'";
                    db.insert("SubElement", column, values);
                }
                setSubElementSpinnerData(db.selectedElementId,
                        db.selectedSchemeId);
                subelementspin.setClickable(true);
                subelementspin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
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
                setCheckListSpinnerData();
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

    public void callCoverageService() {
        Webservice service = new Webservice(AllocateTask.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                setCoverageSpinnerData(data);
                coverageSpinner.setClickable(true);
                coverageSpinner.setSelection(0);
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

    public void setCoverageSpinnerData(String data) {
        if (data.equalsIgnoreCase("$")) {
            displayDialog("No Record Found",
                    "(Note: You may not have the updated data.)");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                ArrayList<Coverage> coverageList = new ArrayList<Coverage>();

                ArrayList<String> coverageNamesList = new ArrayList<String>();

                JSONArray coverageArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < coverageArray.length(); i++) {

                    Coverage coverage = new Coverage();
                    JSONObject stageObject = coverageArray.getJSONObject(i);
                    String rfi_node_id = stageObject.getString("RFI_NODE_Id");
                    coverage.setRFI_NODE_Id(rfi_node_id);
                    String rfi_checklist_id = stageObject
                            .getString("RFI_CHKL_Id");
                    coverage.setRFI_CHKL_Id(rfi_checklist_id);
                    String rfi_group_id = stageObject.getString("RFI_GRP_Id");
                    coverage.setRFI_GRP_Id(rfi_group_id);
                    String rfi_coverage_var = stageObject
                            .getString("RFI_Coverage_var");
                    coverage.setRFI_Coverage_var(rfi_coverage_var);

                    coverageList.add(coverage);
                    coverageNamesList.add(rfi_coverage_var);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, coverageNamesList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                coverageSpinner.setAdapter(adapter);

            }
            catch (Exception e) {
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

					/*String node_id = stageObject.getString("NDUSER_NODE_Id");
					group.setNodeID(node_id);
					String checklist_id = stageObject
							.getString("NDUSER_CHKL_Id");*/
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

                setGoupSpinnerData(db.selectedChecklistId, NodeID);
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
        if (data.equalsIgnoreCase("$")) {
            System.out.println("No Data!!!");
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
        if (data.equalsIgnoreCase("$")) {
            System.out.println("No DATA!!!");
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

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        return super.onCreatePanelView(featureId);
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

    private void cleanData() {
        db.selectedSchemeId = "";
        db.selectedSchemeName = "";
        db.selectedclientname = "";
        db.selectedClientId = "";
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
