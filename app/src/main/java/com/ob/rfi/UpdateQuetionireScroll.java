package com.ob.rfi;

import static com.ob.rfi.RfiQuestionireScroll.getScreenHeight;
import static com.ob.rfi.RfiQuestionireScroll.getScreenWidth;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.FileProvider;

import com.google.firebase.encoders.json.BuildConfig;
import com.ob.rfi.db.RfiDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UpdateQuetionireScroll extends CustomTitleDuplicate {

    private ViewGroup parentLayout;
    private RadioButton[] radioButtons;
    private EditText remark;
    private RfiDatabase db;
    private Cursor questionCursor;
    private int imageCount;
    private boolean isanwered;
    private String TempsnapLoacl1;
    private String TempsnapLoacl2;
    private Cursor questionAnswerCursor;
    private String previousAns;
    private String previousRem;
    private static Button imageButton;
    String[] snaps = new String[]{"", "", "", ""};
    String[] temp_snaps = new String[]{"", "", "", ""};
    private Object question_seq_id;
    private Object question_type;
    private AlphaAnimation anim;
    private File sdImageMainDirectory;
    private String timestamp;
    private String dateonimage;
    private Vibrator vibrator;
    private String qestion_id;
    private String answer;
    private Button submitBtn;
    private Button cancelBtn;
    private String coverage;
    private SharedPreferences checkPreferences;
    private String drawing;
    private Cursor questionupdateCursor;
    private String imagename1;
    private String imagename2;
    private String imagename3;
    private String imagename4;
    private TextView previousremark;
    private TextView perviousRemarkChecker;
    private TextView perviousAns;
    private String checker_remark;
    private Cursor questionupdateCursor1;
    private ActionBar bar;
    private ImageButton imageview;

    String currentPhotoPath;
    String imageName;
    private static final int CAMERA_REQUEST = 1888;
    File imgFile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rfi_questionire_scroll);
        RfiDatabase.isToBeUploaded = true;// akshy change for update screen
        //	ShowPreviousImages();
        Intent int1 = getIntent();
        qestion_id = int1.getStringExtra("Q_id");
        //coverage=int1.getStringExtra("coverage");
        db = new RfiDatabase(getApplicationContext());
        parentLayout = (LinearLayout) findViewById(R.id.ParentLayout);
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);


        submitBtn = (Button) findViewById(R.id.Submit_button);
        cancelBtn = (Button) findViewById(R.id.Back_button);


        anim = new AlphaAnimation(0.1F, 1.F);
        anim.setDuration(500);
        anim.setRepeatMode(Animation.RESTART);
        anim.setRepeatCount(0);

        UpdateRfi.insertFlagupdate = true;

        Calendar c1 = Calendar.getInstance();
        int datetime = c1.get(Calendar.MONTH) + 1;
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        dateonimage = df1.format(new Date());

        System.out.println("month reeeeeeeeeeeee-----------" + datetime);
        timestamp = c1.get(Calendar.DAY_OF_MONTH) + "/" + datetime + "/" + c1.get(Calendar.YEAR) + " " + c1.get(Calendar.HOUR_OF_DAY) + ":" + c1.get(Calendar.MINUTE) + ":" + c1.get(Calendar.SECOND);


        imageButton = (Button) findViewById(R.id.capture);


        TextView scrollDetail = (TextView) findViewById(R.id.roomTypeText);

        ((TextView) findViewById(R.id.tradeText)).setText("Question.Type : " + "Ques Type");

        TextView questionText = (TextView) findViewById(R.id.questiontext);

        TextView drawingText = (TextView) findViewById(R.id.drawing_id);


        checkPreferences = getSharedPreferences("RFI_File", MODE_PRIVATE);
        coverage = checkPreferences.getString("coverage", "");
        drawing = checkPreferences.getString("drawing", "");


        drawingText.setText(coverage + "/" + drawing);

        imageButton.setOnClickListener(new OnClickListener() {
            private Object tempFilename;

            @Override
            public void onClick(View v) {
                System.out.println("imagae count=========" + imageCount);
                if (imageCount < 2) {

                    try {
//						File root = new File(Environment.getExternalStorageDirectory()
//								+ File.separator + "RFI" + File.separator);
//						root.mkdirs();
//						Date d = new Date();
//						String g=db.selectedrfiId.replace(" ","_");
//						String imageName = g+"_"+db.selectedNodeId.trim()+"_"+db.selectedChecklistId.trim()+"_"+
//						db.selectedGroupId.trim()+"_"+db.userId.trim()+"_"+qestion_id+"_"+question_seq_id+"_"
//								+question_type+ "_" + d.getTime()+"_"
//								+imageCount+".jpg";
						
						
						/*if(imageCount==0){
							snaps[2] = imageName;
						}else{
							snaps[3] = imageName;
						}*/
//						if (!snaps[0].equalsIgnoreCase("") && !snaps[1].equalsIgnoreCase("")){
//							snaps[0] = imageName;
//							System.out.println("second.. cinduition.........");
//						} else if(!snaps[0].equalsIgnoreCase("")){
//							snaps[1] = imageName;
//							System.out.println("first cinduition.........");
//						}
//
//						else{
//							System.out.println("third cinduition.........");
//							snaps[imageCount] = imageName;
//						}


                        //snaps[imageCount] = imageName;
//						System.out.println("Image Name "+imageName);
//						sdImageMainDirectory = new File(root, imageName);
//						tempFilename = getNextFileName().getAbsolutePath();
//                        startCameraActivity();

                        AlertDialog alertDialog;
                        alertDialog = new AlertDialog.Builder(UpdateQuetionireScroll.this, R.style.MyAlertDialogStyle).create();
                        alertDialog.setTitle("Horizontal Orientation");
                        alertDialog.setMessage("Please Set Camera Orientation Horizontal.");
                        alertDialog.setCancelable(false);
                        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startCameraActivity();
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();



                    } catch (Exception e) {
                    }
                } else {
                    vibrator.vibrate(500);
                    Toast.makeText(getApplicationContext(), "Limit for maximum(2) images is reached", Toast.LENGTH_SHORT).show();
                }
            }
        });


        try {
            submitBtn.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View arg0) {

                    answer = "";
                    //questionData[Qdata]="";

                    for (int i = 0; i < radioButtons.length; i++) {
                        if (radioButtons[i].isChecked()) {
                            answer = String.valueOf(radioButtons[i].getId());
                            System.out.println("selected answer===" + answer);

                        }
                    }
                    if(question_type.toString().toLowerCase().equalsIgnoreCase("minor") && imageCount==0){
                        Toast.makeText(UpdateQuetionireScroll.this, "Please Upload at least One Image", Toast.LENGTH_SHORT).show();
                    } else {
                        saveAnswer();
                        remark.setText("Remarks");
                        imageCount = 0;
                    }


                }
            });

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("message=" + e.getMessage());
        }


        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });


        RetriveQuestionData();


    }


    public void RetriveQuestionData() {
        String Q_id = qestion_id;
        questionCursor = null;
        imageCount = 0;
        isanwered = false;
        TempsnapLoacl1 = "";
        TempsnapLoacl2 = "";


        System.out.println("q___________----iddddddddddd" + Q_id);

        String whereClause12 = "PK_question_id='" + Q_id +
                "' AND Fk_CHKL_Id='" + db.selectedChecklistId + "' AND Fk_Grp_ID='"
                + db.selectedGroupId + "' AND NODE_Id='" + db.selectedBuildingId + "' AND user_id='" + db.userId + "'";


        //db.selectedNodeId
        String whereClause121 = "FK_question_id='" + Q_id +
                "' AND Fk_CHKL_Id='" + db.selectedChecklistId + "' AND Fk_Grp_ID='"
                + db.selectedGroupId + "' AND  Rfi_id='" + db.selectedrfiId + "' AND user_id='" + db.userId + "'";

        questionAnswerCursor = db.select("Answer", "ans_text,remark,snap1,snap2", whereClause121, null, null, null, null);
        int cnt = 0;
        if (questionAnswerCursor.moveToFirst()) {
            do {
                isanwered = true;
                System.out.println("question data....");
                System.out.println("answer...." + questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("ans_text")));
                System.out.println("remark...." + questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("remark")));
                System.out.println("snap1...." + questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("snap1")));
                System.out.println("snap2...." + questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("snap2")));
                cnt++;


                previousAns = questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("ans_text"));
                previousRem = questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("remark"));
                TempsnapLoacl1 = questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("snap1"));
                TempsnapLoacl2 = questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("snap2"));


            } while (questionAnswerCursor.moveToNext());
        }
        //Storing already answered images

        if (cnt > 0) {
            if (!TempsnapLoacl1.equalsIgnoreCase("")) {
                //temp_snaps[0]=TempsnapLoacl1;
                snaps[0] = TempsnapLoacl1;
            } else {
                snaps[0] = "";
            }
            if (!TempsnapLoacl2.equalsIgnoreCase("")) {
                //temp_snaps[1]=TempsnapLoacl2;
                snaps[1] = TempsnapLoacl2;
            } else {
                snaps[1] = "";
            }
        }


        System.out.println("where clause----" + whereClause12);
        questionCursor = db.select("question", "distinct(PK_question_id),QUE_Des,QUE_SequenceNo,QUE_Type", whereClause12, null, null, null, null);
        questionCursor.moveToNext();


        if (questionCursor.moveToFirst()) {
            do {
                System.out.println("calling----------------");
                TextView questionText = (TextView) findViewById(R.id.questiontext);

                question_seq_id = questionCursor.getString(questionCursor.getColumnIndex("QUE_SequenceNo"));
                question_type = questionCursor.getString(questionCursor.getColumnIndex("QUE_Type"));
                questionText.setText(questionCursor.getString(questionCursor.getColumnIndex("QUE_Des")));
                questionText.setAnimation(anim);
                questionText.startAnimation(anim);
                SingleChoiceLayout();

                System.out.println("question data-------" + questionCursor.getString(0).toString());

            } while (questionCursor.moveToNext());
        } else {

            System.out.println("not question-----------------");
        }

        String whereupdateClause = "QUE_Id='" + Q_id +
                "' AND CHKL_Id='" + db.selectedChecklistId + "' AND RFI_Id='" + db.selectedrfiId + "' AND GRP_Id='" + db.selectedGroupId + "' AND user_id1='" + db.userId + "'";

        questionupdateCursor = db.select("Rfi_update_details", "Remark,Answer,Image1,Image2,Image3,Image4,checker_remark", whereupdateClause, null, null, null, null);


        if (questionupdateCursor.moveToNext()) {
            System.out.println("nexyt loop=====");
            System.out.println("remark" + questionupdateCursor.getString(0).toString());
					
					/*imagename1=questionupdateCursor.getString(questionupdateCursor.getColumnIndex("Image1"));
					imagename2=questionupdateCursor.getString(questionupdateCursor.getColumnIndex("Image2"));*/

            imagename1 = questionupdateCursor.getString(2);
            imagename2 = questionupdateCursor.getString(3);
            imagename3 = questionupdateCursor.getString(4);
            imagename4 = questionupdateCursor.getString(5);


            System.out.println("images available are=" + imagename1 + "=" + imagename2);
        }


    }


    private void SingleChoiceLayout() {

        parentLayout.removeAllViews();
        LinearLayout childLayout = new LinearLayout(this);
        childLayout.setOrientation(LinearLayout.VERTICAL);
        childLayout.setGravity(Gravity.CENTER);
        ScrollView scroll = new ScrollView(this);

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);

        radioGroup.setFocusable(true);
        radioGroup.setFocusableInTouchMode(true);
        radioGroup.setGravity(Gravity.LEFT);

        //String[] options = questionCursor.getString(questionCursor.getColumnIndex("QUE_Des")).split("~");

        String[] options = {"Conformity", "Non Conformity", "N/A"};
        String p_rec = "";
        String p_rec_checker = "";
        String p_ans = "";
        previousremark = new TextView(getApplicationContext());
        perviousRemarkChecker = new TextView(getApplicationContext());
        perviousAns = new TextView(getApplicationContext());

        radioButtons = new RadioButton[options.length];


        for (int i = 0; i < options.length; i++) {

            radioButtons[i] = new RadioButton(getApplicationContext());
            radioButtons[i].setText(options[i]);
            radioButtons[i].setTextColor(Color.WHITE);
            radioButtons[i].setChecked(false);
            radioButtons[i].setId(i);
            radioButtons[i].setTag(i);

            if (options[i].toString().trim().equalsIgnoreCase(db.selectedClient.trim())) {
                radioButtons[i].setChecked(false);
            }
            remark = new EditText(getApplicationContext());
            remark.setHint("Remarks");
            remark.setTextColor(Color.WHITE);
            remark.setHintTextColor(Color.WHITE);
			
			/*previousremark = new EditText(getApplicationContext());
			previousremark.setHint("Remark");*/
            try {
                String whereupdateClause = "QUE_Id='" + qestion_id +
                        "' AND CHKL_Id='" + db.selectedChecklistId + "' AND RFI_Id='" + db.selectedrfiId + "' AND GRP_Id='" + db.selectedGroupId + "' AND user_id1='" + db.userId + "'";

                questionupdateCursor1 = db.select("Rfi_update_details", "Remark,Answer,Image1,Image2,Image3,Image4,checker_remark", whereupdateClause, null, null, null, null);

                if (questionupdateCursor1.moveToFirst()) {
                    do {


                        p_ans = questionupdateCursor1.getString(1).toString();
                        p_rec = questionupdateCursor1.getString(0).toString();
                        p_rec_checker = questionupdateCursor1.getString(6).toString();


                        System.out.println("image name=" + imagename1 + "=" + imagename2);
                        System.out.println("anwer and remark==" + p_ans + "=" + p_rec);
                    } while (questionupdateCursor1.moveToNext());
                }
            } catch (Exception e) {
                System.out.println("remarks and answer not found..");
            }
            if (p_rec.equalsIgnoreCase("")) {
                p_rec = "-";
            }

            if (p_ans.equalsIgnoreCase("")) {
                p_ans = "-";
            }

            if (p_rec_checker.equalsIgnoreCase("")) {
                p_rec_checker = "-";
            }

            previousremark.setText("Previous Remarks: " + p_rec);
            previousremark.setTextColor(Color.WHITE);

            perviousRemarkChecker.setText("Checker Previous Remarks: " + p_rec_checker);
            perviousRemarkChecker.setTextColor(Color.WHITE);

            checker_remark = p_rec_checker;
            perviousAns.setText("Previous Answer: " + p_ans);
            perviousAns.setTextColor(Color.WHITE);

            radioGroup.addView(radioButtons[i]);
            imageButton.setVisibility(View.VISIBLE);


        }
        if (p_ans.equalsIgnoreCase("Conformity")) {
            System.out.println("11111111111111111111111111111");
            radioButtons[0].setChecked(true);
        } else if (p_ans.equalsIgnoreCase("Non Conformity")) {
            System.out.println("222222222222222222222222");
            radioButtons[1].setChecked(true);
        } else {
            System.out.println("33333333333333333333333333");
            radioButtons[2].setChecked(true);
        }

        //String[] options = {"Conformity","Non Conformity","N/A"}
        System.out.println("previous ans " + p_ans);
		
		
		
		/*int myNum = 0;

		
		System.out.println("previossssssssssssss  ans=="+previousAns);
		try {
			if(previousAns.equalsIgnoreCase("")){
				System.out.println("previous answ not availvale  try catch ");
			}{
			
		    myNum = Integer.parseInt(previousAns);
			}
		} catch(NumberFormatException nfe) {
		   System.out.println("Could not parse " + nfe);
		} 
		
*/
        if (isanwered) {

            remark.setText(previousRem);


            if (previousAns.equalsIgnoreCase("Conformity") || previousAns.equalsIgnoreCase("0")) {
                System.out.println("11111111111111111111111111111");
                radioButtons[0].setChecked(true);
            } else if (previousAns.equalsIgnoreCase("Non Conformity") || previousAns.equalsIgnoreCase("1")) {
                System.out.println("222222222222222222222222");
                radioButtons[1].setChecked(true);
            } else {
                System.out.println("33333333333333333333333333");
                radioButtons[2].setChecked(true);
            }


            //radioButtons[myNum].setChecked(true);


        } else {
            remark.setText("");
        }

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                char[] chars = {'\'','"','@','/','$','&','?','!','*','<','>','~'};
                for (int i = start; i < end; i++) {
                    if (new String(chars).contains(String.valueOf(source.charAt(i)))) {
                        return "";
                    }
                }
                return null;
            }
        };
        remark.setFilters(new InputFilter[]{filter});

        String QUE_Type = questionCursor.getString(questionCursor.getColumnIndex("QUE_Type"));

        childLayout.addView(radioGroup);

        childLayout.addView(remark);
        childLayout.addView(previousremark);
        childLayout.addView(perviousRemarkChecker);
        childLayout.addView(perviousAns);
        scroll.addView(childLayout);


        parentLayout.addView(scroll);
		
		/*ans_text TEXT,remark TEXT,snap1 TEXT,snap2 TEXT" +
		",Rfi_id TEXT,dated TEXT,FK_question_id TEXT, " +
		"FK_QUE_SequenceNo Text,FK_QUE_Type TEXT, FK_NODE_Id TEXT,Fk_CHKL_Id TEXT, Fk_Grp_ID TEXT,user_id TEXT)
*/
        // Check Database if Question was answered previously or not

        String whereClause1 = "FK_question_id='" + qestion_id +
                "' AND Fk_CHKL_Id='" + db.selectedChecklistId + "' AND Fk_Grp_ID='" + db.selectedGroupId + "' AND user_id='" + db.userId + "'";


        Cursor cursor = db.select("Answer", "ans_text,remark,snap1,snap2", whereClause1,
                null, null, null, null);

        String ans = "";
        String remarks = "";


        String[] rfiname = db.selectedRfiName.split("/");


        TextView surveyDetail = (TextView) findViewById(R.id.roomTypeText);
        surveyDetail.setText(db.selectedRfiName
                + "/" + db.selectedSchemeName + " / " +
                db.selectedclientname + "/" + db.selectedChecklistName);
        int length = surveyDetail.getText().toString().length();
        if (length > 42) {
            Animation anim = new TranslateAnimation(5, -((length - 42) * 7), 0, 0);
            anim.setDuration(6000);
            anim.setRepeatMode(Animation.RESTART);
            anim.setRepeatCount(Animation.INFINITE);
            surveyDetail.setAnimation(anim);
            int screenWidth = length * 7 + 5;
            surveyDetail.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, LayoutParams.WRAP_CONTENT));
        }
        surveyDetail.requestFocus();

        //((TextView)findViewById(R.id.roundNoText)).setText("CheckListName : "+db.selectedChecklistName);
        ((TextView) findViewById(R.id.tradeText)).setText("Question.Type : " + QUE_Type);


    }

    private File getNextFileName() {
        File root = new File(Environment.getExternalStorageDirectory()
                + File.separator + "RFI" + File.separator + "temp");
        root.mkdirs();
        String filename = System.currentTimeMillis() + ".jpg";
		
		/*String str1="m_"; 
		String desc = str1.concat(filename);*/
        //System.out.println("file in getnextfile==============================>"+desc);
        File file = new File(root, filename);
        System.out.println("FILENAME+++" + file.getAbsolutePath());
        return file;
    }


    public void startCameraActivity() {
        System.out.println("hiiiiiiiiiiiiiiiiii");
        //		Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
        //		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//		startActivityForResult(intent, 1);


        File root = new File(Environment.getExternalStorageDirectory()
                + File.separator + "RFI" + File.separator + "temp");
        root.mkdirs();

        Date d = new Date();
        String g = db.selectedrfiId.replace(" ", "_");
        imageName = g + "_" + db.selectedNodeId.trim() + "_" + db.selectedChecklistId.trim() + "_" +
                db.selectedGroupId.trim() + "_" + db.userId.trim() + "_" + qestion_id + "_" + question_seq_id + "_"
                + "_" + d.getTime() + "_" + imageCount + ".jpg";


        snaps[imageCount] = imageName;


        File storageDir = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            storageDir = new File(/*Environment.getExternalStorageDirectory()*/getExternalFilesDirs(null)[0]
                    + File.separator + "RFI" + File.separator + "temp");
        }else {
            storageDir = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "RFI" + File.separator + "temp");
        }


        storageDir.mkdirs();
        File image = new File(storageDir, imageName);
        currentPhotoPath = image.getAbsolutePath();

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            imgFile = null;

            imgFile = image;
            if (imgFile != null) {

                //Uri imgUri = FileProvider.getUriForFile(UpdateQuetionireScroll.this, "com.ob.rfi.fileprovider", imgFile);
                Uri imgUri = FileProvider.getUriForFile(UpdateQuetionireScroll.this, BuildConfig.APPLICATION_ID+".fileprovider", imgFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                //cameraIntent.putExtra("BitmapImage", currentPhotoPath);
                cameraIntent.putExtra("return-data", true);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        System.out.println(" after onresume................");
        switch (resultCode) {
            case 0:

                System.out.println(" image discrdedddddddddddddddd................");
                break;
            case -1:
                Bitmap bm = null;
                try {
                    System.out.println("image saved ...............");
                    imageCount++;
                    imageButton.setText("Capture Image(" + imageCount + "/2)");
                    String dir = /*Environment.getExternalStorageDirectory()*/null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        dir = getExternalFilesDirs(null)[0]
                                + File.separator + "RFI" + File.separator + "temp" + File.separator;
                    }

                    File file = new File(dir + "/" + currentPhotoPath.substring(currentPhotoPath.lastIndexOf('/') + 1).trim());

                    String path ;
                    path = dir + currentPhotoPath.substring(currentPhotoPath.lastIndexOf('/') + 1).trim();
                    FileInputStream in = null;

                    // path= f1.getAbsolutePath()+currentPhotoPath.substring(currentPhotoPath.lastIndexOf('/') + 1).trim();
                    byte[] b = new byte[(int) file.length()];
                    //  in = new FileInputStream(new File(path));
                    in = new FileInputStream(new File(currentPhotoPath));

                    in.read(b);
                    in.close();
                    Bitmap bitmap   = BitmapFactory.decodeByteArray(b, 0, b.length);
                    currentPhotoPath=file.getAbsolutePath();











                    //-----------------------------------------------------------------
                    // File file = new File(currentPhotoPath);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();

                    // Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(),bmOptions);

                    float aspect = (float) bitmap.getWidth() / bitmap.getHeight();
                    int width = 750;//bitmap.getWidth();
                    int height = (int) (width / aspect);


                    int bw = bitmap.getWidth();
                    int bh = bitmap.getHeight();
                    int sw = getScreenWidth();
                    int sh = getScreenHeight();

                    if (bw < bh) {
                        width = sw - 20;
                        height = sh - 200;
                    } else {
                        width = sw - 20;
                        height = sw / 2;
                    }


                    FontMetrics fm = new FontMetrics();
                    Paint mpaint = new Paint();
                    mpaint.setColor(Color.WHITE);
                    mpaint.setStyle(Style.FILL);
////                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

                    Canvas canvas = new Canvas(bitmap);
                    Paint paint = new Paint();
                    paint.setTextSize(20);
                    paint.setColor(Color.RED); // Text Color
                    paint.setStrokeWidth(13); // Text Size
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
//                    // some more settings...
                    canvas.drawRect(5, 5, 200, 50, mpaint);
                    canvas.drawBitmap(bitmap, 0, 0, paint);
                    canvas.drawText(dateonimage, 20, 35, paint);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, new FileOutputStream(file));

                    Intent int1 = new Intent(UpdateQuetionireScroll.this, CustomeImageProcessing.class);
                    int1.putExtra("uri", currentPhotoPath);
                    System.out.println("custome image processing--------------" + currentPhotoPath);
                    startActivity(int1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError ome) {
                    ome.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    protected void saveAnswer() {


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String rfi_date = df.format(new Date());
        System.out.println("images are :::" + snaps[0] + snaps[1]);
        //questionData[Qdata]="";
        String img3 = "";
        String img4 = "";


        String whereClause1 = "Rfi_id='" + db.selectedrfiId + "' AND FK_question_id='" + qestion_id + "' AND user_id='" + db.userId + "'";
        Cursor cursor1 = db.select("Answer", "FK_question_id", whereClause1, null, null, null, null);
        boolean isanswerd = false;
        //check for update question
        if (cursor1.moveToFirst()) {
            do {
                isanswerd = true;

            } while (cursor1.moveToNext());
        } else {
            System.out.println("not question-----------------");
        }

        if (cursor1 != null && !cursor1.isClosed()) {
            cursor1.close();
        }

        if (snaps[0].equalsIgnoreCase("")) {
            snaps[0] = imagename1;
        }
        if (snaps[1].equalsIgnoreCase("")) {
            snaps[1] = imagename2;

        }

        snaps[2] = imagename3;
        snaps[3] = imagename4;


        String rfidata = "'" + db.selectedClientId + "','" + db.selectedSchemeId + "','" + db.selectedlevelId + "','" + db.selectedBuildingId + "','" + db.selectedFloorId + "','" +
                db.selectedUnitId + "','" + db.selectedSubUnitId + "','" + db.selectedElementId + "','" + db.selectedSubElementId + "','" + coverage + "','"

                + answer + "','"
                + remark.getText().toString() + "','" + snaps[0] + "','"
                + snaps[1] + "','" + snaps[2] + "','" + snaps[3] + "','" + db.selectedrfiId + "','"
                + rfi_date + "','" + qestion_id + "','" + question_seq_id + "','" + question_type
                + "','" + db.selectedNodeId + "','" + db.selectedChecklistId + "','" + db.selectedGroupId + "','" + checker_remark + "','" + db.userId + "'";


        if (!isanswerd) {
            db.insert(
                    "Answer",
                    "CL_Id,PRJ_Id,Level_int,Structure_Id,Stage_Id," +
                            "Unit_id,Sub_Unit_Id,Element_Id,SubElement_Id,Coverage_str,ans_text,remark,snap1,snap2,snap3,snap4,Rfi_id,dated," +
                            "FK_question_id,FK_QUE_SequenceNo,FK_QUE_Type,FK_NODE_Id,Fk_CHKL_Id, Fk_Grp_ID,checker_remark,check_date,drawing_no,user_id",
                    rfidata);
            System.out.println("inserted=================" + rfidata);


        } else {
            db.update("Answer", "ans_text='"
                    + answer + "',remark='" + remark.getText().toString() + "',snap1='" + snaps[0] + "',snap2='" + snaps[1] + "',snap3='" + snaps[2] + "',snap4='" + snaps[3] + "',check_date='" + rfi_date + "',answerFlag='" + "1" + "'", "Rfi_id='" + db.selectedrfiId
                    + "' AND CL_Id=" + db.selectedClientId + " AND PRJ_Id="
                    + db.selectedSchemeId + " AND Structure_Id="
                    + db.selectedBuildingId + " AND FK_question_id='"
                    + qestion_id + "'");


            System.out.println("updated=================" + rfidata);

        }


        snaps = new String[]{"", "", "", ""};
        imageCount = 0;
        System.out.println("image count in saveanswer=================" + imageCount);
        imageButton.setText("Capture Image(0/2)");
        remark.setText("");


        Intent int1 = new Intent(getApplicationContext(), UpdateRfiScroll.class);
        finish();
        startActivity(int1);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.questionnaire_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.viewImage:
                    showImage();
                    return true;
                case R.id.action_show_image:
			if(CheckImageAvailable()){
				Intent int1=new Intent(UpdateQuetionireScroll.this,ShowPreviousImages.class);
				int1.putExtra("q_id", qestion_id);
				startActivity(int1);
				
				}else
				{
					Toast.makeText(getApplicationContext(), "Images Not Available", Toast.LENGTH_LONG).show();
				}

                return true;

            }
        } catch (Exception aE) {
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean CheckImageAvailable() {
        boolean ret = false;

        String whereupdateClause = "QUE_Id='" + qestion_id +
                "' AND CHKL_Id='" + db.selectedChecklistId + "' AND RFI_Id='" + db.selectedrfiId + "' AND GRP_Id='" + db.selectedGroupId + "' AND user_id1='" + db.userId + "'";

        Cursor Cursor1 = db.select("Rfi_update_details", "Image1,image2,Image3,image4", whereupdateClause, null, null, null, null);


        if (Cursor1.moveToFirst()) {
            do {

                System.out.println("cursor=" + Cursor1.getString(0).toString().length());
                System.out.println("cursor=" + Cursor1.getString(1).toString().length());
                System.out.println("cursor=" + Cursor1.getString(2).toString().length());
                System.out.println("cursor=" + Cursor1.getString(3).toString().length());

                if (Cursor1.getString(0).toString().length() == 0 && Cursor1.getString(1).toString().length() == 0
                        && Cursor1.getString(2).length() == 0 && Cursor1.getString(3).length() == 0) {
                    System.out.println("previous images.........not available");
                    ret = false;
                } else {
                    ret = true;
                    System.out.println("previous imag available update");
                }


            } while (Cursor1.moveToNext());
        }
        return ret;


    }


    private void showImage() {

        if (snaps[0].equalsIgnoreCase("")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    UpdateQuetionireScroll.this, R.style.MyAlertDialogStyle);
            builder.setMessage("Images not available");
            builder.setNegativeButton(" Ok ",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create();
            builder.show();

        } else {
            List<String> list = new ArrayList<String>();
            for (String str : snaps) {
                if (!TextUtils.isEmpty(str)) {
                    list.add(str);
                    System.out.println("images+++++++++" + str);
                }
            }
            Intent i = new Intent(UpdateQuetionireScroll.this, ViewImagesActivity.class);
            i.putExtra("data", list.toArray(new String[]{}));
            System.out.println("String Activity+++++++++");
            startActivity(i);
        }
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //	super.onBackPressed();
        finish();
        Intent int1 = new Intent(getApplicationContext(), UpdateRfiScroll.class);
        int1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(int1);
    }


}
