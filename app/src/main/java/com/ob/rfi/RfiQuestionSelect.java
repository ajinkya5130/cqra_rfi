package com.ob.rfi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

import com.ob.database.db_tables.AnswerTableModel;
import com.ob.database.db_tables.QuestionsTableModel;
import com.ob.rfi.AdapterRfi.CreateAdapterRfi;
import com.ob.rfi.db.RfiDatabase;
import com.ob.rfi.viewmodels.RFIQuestionSelectViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


/*************************************************************************************************
 * Proj. Name       : RFI 
 *
 * Class Name       : RfiQuestionSelect
 *
 * Method Name : OnCreate
 *
 * Arguments passed : none
 *
 * Return Type      : none
 *
 * Synopsis         : show Questions in Listview
 *
 * Done By          : Balaji
 **************************************************************************************************/

 

public class RfiQuestionSelect extends CustomTitle{
	
	 
	private ListView listview;
	private RfiDatabase db;
	private TextView scrol_text;
	private String question_seq_id;
	private String question_type;
	private String current_ques_id;
	private TextView dr_text;
	private TextView drwng_text;

	private RFIQuestionSelectViewModel viewModel;
	public String coverageGlobal;
	private SharedPreferences checkPreferences;
	private Editor editor;
	private String coverage;
	private String drawing;
	private boolean flag;
	private static final String TAG = "RfiQuestionSelect";

	/*************************************************************************************************
	 * Proj. Name       : RFI 
	 *
	 * Class Name       : RfiQuestionSelect
	 *
	 * Method Name : OnCreate
	 *
	 * Arguments passed : none
	 *
	 * Return Type      : none
	 *
	 * Synopsis         : show Questions in Listview
	 *
	 * Done By          : Balaji
	 **************************************************************************************************/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rfi_question_select);
		viewModel = new ViewModelProvider(this).get(RFIQuestionSelectViewModel.class);
		
		listview=(ListView)findViewById(R.id.rfi_question_listview_id);
		scrol_text=(TextView)findViewById(R.id.scrolling_text_id);
		drwng_text=(TextView)findViewById(R.id.drawng_id);
		
		checkPreferences=getSharedPreferences("RFI_File",MODE_PRIVATE);
		coverage=checkPreferences.getString("coverage","");
		drawing=checkPreferences.getString("drawing","");
		
		
		
		drwng_text.setText(coverage+"/"+drawing);
		
		db=new RfiDatabase(getApplicationContext());
		//setListviewdata();
		scrol_text.setText(db.selectedSchemeName+" / "+db.selectedclientname+"/"+db.selectedChecklistName);
		viewModel.getQuestionListFromDb();

		observeLiveData();
	}

	private void observeLiveData() {
		Objects.requireNonNull(viewModel.getLvQuestionsData()).observe(
				this, value -> {
					Log.d(TAG, "getLvClientAllocateData: value: " + value);
					if (value!=0){
						setQuestionAdapter();
					}
				}
		);
	}

	private void setQuestionAdapter() {
		ArrayList<QuestionsTableModel> listOfQuestions = viewModel.getListOfQuestions();
		int size = listOfQuestions.size();
		String[] q_id=new String[size];
		String[] q_text=new String[size];
		RfiDatabase.questionCount= String.valueOf(size);

		for (int i = 0; i < size; i++) {
			QuestionsTableModel model = listOfQuestions.get(i);
			q_id[i]=String.valueOf(model.getQuestionId());
			q_text[i]=model.getQuestion();
			question_seq_id=String.valueOf(model.getQuestionSequence());
			question_type= model.getQuestionType();
			current_ques_id=String.valueOf(model.getQuestionId());

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String rfi_date=df.format(new Date());

			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String rfi_date1=df1.format(new Date());
			AnswerTableModel answerTableModel = new AnswerTableModel();
			answerTableModel.setCL_Id(RfiDatabase.selectedClientId);
			answerTableModel.setPRJ_Id(RfiDatabase.selectedSchemeId);
			answerTableModel.setLevel_int(RfiDatabase.selectedlevelId);
			answerTableModel.setStructure_Id(RfiDatabase.selectedBuildingId);
			answerTableModel.setStage_Id(RfiDatabase.selectedFloorId);
			answerTableModel.setUnit_id(RfiDatabase.selectedUnitId);
			answerTableModel.setSub_Unit_Id(RfiDatabase.selectedSubUnitId);
			answerTableModel.setCoverage_str(coverage);
			answerTableModel.setAns_text("1");
			answerTableModel.setRfi_id(RfiDatabase.selectedrfiId);
			answerTableModel.setDated(rfi_date);
			answerTableModel.setFK_question_id(current_ques_id);
			answerTableModel.setFK_QUE_SequenceNo(question_seq_id);
			answerTableModel.setFK_QUE_Type(question_type);
			answerTableModel.setFK_NODE_Id(RfiDatabase.selectedNodeId);
			answerTableModel.setFk_CHKL_Id(RfiDatabase.selectedChecklistId);
			answerTableModel.setFk_Grp_ID(RfiDatabase.selectedGroupId);
			answerTableModel.setCheck_date(rfi_date1);
			answerTableModel.setDrawing_no(drawing);
			answerTableModel.setAnswerFlag("0");
			answerTableModel.setUser_id(RfiDatabase.userId);
			viewModel.insertDefaultAnswer(answerTableModel);
		}
		CreateAdapterRfi adapter = new CreateAdapterRfi(RfiQuestionSelect.this,q_id, q_text,coverage,viewModel);
		listview.setAdapter(adapter);
		listview.setDividerHeight(4);

	}

	@Nullable
	@Override
	public ActionBar getSupportActionBar() {
		return super.getSupportActionBar();
	}

	
	public void nevigateHomeScreen() {
		/*System.out.println("question Count: " + db.questionCount);
		System.out.println("submit Question count: " + db.questionSubmitCount);
		if (!db.questionCount.equalsIgnoreCase(db.questionSubmitCount)) {
			AlertDialog alertDialog1 = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
			alertDialog1.setCancelable(false);
			alertDialog1.setMessage("All Question Answers mandatory..");
			alertDialog1.setButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//getScreenToBeDisplayed();
				}
			});
			alertDialog1.show();
		} */
			AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
			alertDialog.setCancelable(false);
			alertDialog.setMessage("Do you want to save this RFI?");
			alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					db.fromCreate = true;

					getScreenToBeDisplayed();
				/*SelectQuestion.destruct.finish();
				finish();
				Intent int1=new Intent(getApplicationContext(),SelectQuestion.class);
				int1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				finish();
				startActivity(int1);*/

				}
			});
			alertDialog.setButton2("NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					deleteAnswer();

					SelectQuestion.destruct.finish();
					finish();
					Intent int1 = new Intent(RfiQuestionSelect.this, SelectQuestion.class);
					int1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(int1);

					//dialog.dismiss();
				}
			});

			alertDialog.setButton3("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			alertDialog.show();



	}
	public void deleteAnswer(){
		System.out.println("answer deleted...");
		db.delete("Answer", "Rfi_id='" + db.selectedrfiId + "'");
	}
	
	protected void getScreenToBeDisplayed() {  

		db.delete("Rfi_New_Create", "user_id='" + db.userId + "'");		
		db.insert("Rfi_New_Create", "FK_rfi_Id,user_id","'"+SelectQuestion.nval+ "','"+db.userId+"'");
		System.out.println("inserted id data================="+SelectQuestion.nval);
			cleanData();
			db.closeDb();
			finish();
			//startActivity(new Intent(RfiQuestionire.this,HomeScreen.class));
			//SelectQuestion.destruct.finish();
			SelectQuestion.slectionclass.finish();
			Intent inte1=new Intent(RfiQuestionSelect.this,SelectQuestion.class);
			inte1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(inte1);
					
			}
 
	private void cleanData() {
		db.selectedSchemeId="";
		db.selectedSchemeName="";
		db.selectedclientname="";  
		db.selectedClientId="";
		db.selectedBuildingId="";
		db.selectedFloorId="";
		db.selectedUnitId="";
		db.selectedSubUnitId="";
		db.selectedElementId="";
		db.selectedSubElementId="";
		db.selectedChecklistId="" ;
		db.selectedChecklistName="" ;
		db.selectedGroupId="" ;
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	//	super.onBackPressed();
		nevigateHomeScreen();
		
		
	}
	
}
