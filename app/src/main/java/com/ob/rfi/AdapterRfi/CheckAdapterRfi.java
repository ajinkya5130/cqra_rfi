package com.ob.rfi.AdapterRfi;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ob.rfi.CheckQuestionireScroll;
import com.ob.rfi.R;
import com.ob.rfi.db.RfiDatabase;

public class CheckAdapterRfi extends BaseAdapter{
	private final Activity context;
	private final String[] Q_id;
	private final String[] q_text;
	private final String coverage;
	public int pos;
	int count=0;
	private RfiDatabase db;
	private Cursor questionAnswerCursor;
	private String previousAns;
	public CheckAdapterRfi(Activity context,String[] Q_id, String[] q_text, String coverage)
	{
		this.context=context;
		this.Q_id=Q_id;
		this.q_text=q_text;
		this.coverage=coverage;
		System.out.println("in Adapter----------------");
		db=new RfiDatabase(context);
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return q_text.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
			
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.rfi_list_item, null, true);
		//TextView Sr_no = (TextView) rowView.findViewById(R.id.listItem_sr_id);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.listItem_ques_text_id);
		
		//Sr_no.setText(position+".");
		txtTitle.setText(q_text[position]);
		System.out.println("sr no="+position+"  question="+q_text[position]);
		count++;
		
		try {
		
		//setting background colour
		String whereClause121="FK_question_id='"+Q_id[position]+
				"' AND Fk_CHKL_Id='"+db.selectedChecklistId+"' AND Fk_Grp_ID='"
				+db.selectedGroupId+"' AND  Rfi_id='"+db.selectedrfiId+"' AND user_id='"+db.userId+"'";
		
		questionAnswerCursor = db.select("CheckAnswer", "answerFlag,ans_text",whereClause121, null, null, null, null);
		
		
		
		if(questionAnswerCursor.moveToFirst()){
			do{
				 
			
			if(questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("answerFlag")).equalsIgnoreCase("1") &&
					questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("ans_text")).equalsIgnoreCase("0")){
				rowView.setBackgroundResource(R.drawable.list_back_answerd);
			}else if(questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("answerFlag")).equalsIgnoreCase("1") &&
					questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("ans_text")).equalsIgnoreCase("1")) {
				rowView.setBackgroundResource(R.drawable.list_item_back);
			}else{
				rowView.setBackgroundResource(R.drawable.list_back_constant);
			}
			
			previousAns=questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("answerFlag"));
			 
			
			
			}while(questionAnswerCursor.moveToNext());
		} 


		
		
		
		} finally {
			questionAnswerCursor.close();
		}

		
		
		
		
		
		
		
	
		
		
		rowView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//db.LastSelectedListItem=position;
				db.LastSelectedListItem=String.valueOf(position);
			//	String temp=String.valueOf(position);
				System.out.println("q_id="+Q_id[position]+"  question text=="+q_text[position]);
				
				Intent int1=new Intent(context,CheckQuestionireScroll.class);
				int1.putExtra("Q_id",Q_id[position] );
				int1.putExtra("coverage",coverage);
				updateRfiTable();
				context.finish();
				context.startActivity(int1); 
				
				db.update("CheckAnswer", "isAnswered = '1'", "Rfi_id='" + db.selectedrfiId + "' AND user_id = '" + db.userId + "'" );
				
			}
		});
		
		
		
		
		return rowView;
	}
	
	
	public void updateRfiTable()
	{
		
		System.out.println("updated --------------------check table");
		db.update("Check_update_details", "status_device='" + "complete'","user_id1='"+db.userId+"' AND RFI_Id='" + db.selectedrfiId + "'" );
	}
	

}
