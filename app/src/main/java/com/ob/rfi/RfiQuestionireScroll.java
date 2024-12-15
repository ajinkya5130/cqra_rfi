package com.ob.rfi;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.ob.database.db_tables.AnswerTableModel;
import com.ob.database.db_tables.QuestionsTableModel;
import com.ob.rfi.db.RfiDatabase;
import com.ob.rfi.viewmodels.RFIQuestionScrollViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RfiQuestionireScroll extends CustomTitleDuplicate {

    private ViewGroup parentLayout;
    private RadioButton[] radioButtons;
    private EditText remark;
    private RfiDatabase db;
    private int imageCount;
    private boolean isanwered;
    private String TempsnapLoacl1;
    private String TempsnapLoacl2;
    private String previousAns;
    private String previousRem;
    private static Button imageButton;
    String[] snaps = new String[]{"", ""};
    String[] temp_snaps = new String[]{"", ""};
    private String question_seq_id;
    private String question_type;
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

    private static final String TAG = "RfiQuestionireScroll";

    String currentPhotoPath;
    String imageName;
    private static final int CAMERA_REQUEST = 1888;
    File imgFile;
    private RFIQuestionScrollViewModel viewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rfi_questionire_scroll);
        viewModel = new ViewModelProvider(this).get(RFIQuestionScrollViewModel.class);
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

        SelectQuestion.insertFlag = true;

        Calendar c1 = Calendar.getInstance();
        int datetime = c1.get(Calendar.MONTH) + 1;
        System.out.println("month reeeeeeeeeeeee-----------" + datetime);
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        dateonimage = df1.format(new Date());
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

                        AlertDialog alertDialog;
                        alertDialog = new AlertDialog.Builder(RfiQuestionireScroll.this, R.style.MyAlertDialogStyle).create();
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


					}
					catch (Exception e) {}
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
                    /*System.out.println("----------In submit button calling");
                    int a= Integer.parseInt(db.questionSubmitCount);
                    int res=a+1;
                    db.questionSubmitCount= String.valueOf(res);
                    System.out.println("ans submit count= "+res);
*/
                    answer = "";
                    //questionData[Qdata]="";

                    for (int i = 0; i < radioButtons.length; i++) {
                        if (radioButtons[i].isChecked()) {
                            answer = String.valueOf(radioButtons[i].getId());
                            System.out.println("selected answer===" + answer);

                        }
                    }
                    if(question_type.toString().toLowerCase().equalsIgnoreCase("minor") && imageCount==0){
                        Toast.makeText(RfiQuestionireScroll.this, "Please Upload at least One Image", Toast.LENGTH_SHORT).show();
                    }else {
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
        TextView questionText = (TextView) findViewById(R.id.questiontext);
        for (int i = 0; i < size; i++){
            QuestionsTableModel model = listOfQuestions.get(i);
            question_seq_id = String.valueOf(model.getQuestionSequence());
            question_type = model.getQuestionType();
            questionText.setText(model.getQuestion());
            questionText.setAnimation(anim);
            questionText.startAnimation(anim);
            SingleChoiceLayout();
        }
    }


    public void RetriveQuestionData() {
        String Q_id = qestion_id;
        imageCount = 0;
        isanwered = false;
        TempsnapLoacl1 = "";
        TempsnapLoacl2 = "";

        try {
            // setting background colour
            viewModel.getQuestionAnswerAsync(Q_id,value ->{

                if (value != null) {
                    // Handle the result
                    setUIToSelectedView(value);
                    Log.d(TAG, "Answer: " + value.toString());
                } else {
                    // Handle error case
                    Log.e(TAG, "Failed to fetch answer");
                }
                return null;
            });
            viewModel.getQuestionListFromDb();
        }catch (Exception e){
            Log.e(TAG, "getView: Exception: ",e );
        }

    }

    private void setUIToSelectedView(AnswerTableModel model) {
        isanwered = true;
        previousAns = model.getAnswerFlag();
        previousRem = model.getRemark();
        TempsnapLoacl1 = model.getSnap1();
        if (!TempsnapLoacl1.isEmpty()){
            snaps[0] = TempsnapLoacl1;
        }
        TempsnapLoacl2 = model.getSnap2();
        if (!TempsnapLoacl2.isEmpty()){
            snaps[1] = TempsnapLoacl2;
        }
    }


    private void SingleChoiceLayout()
    {

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

        radioButtons = new RadioButton[options.length];
        for (int i = 0; i < options.length; i++)
        {

            radioButtons[i] = new RadioButton(getApplicationContext());
            radioButtons[i].setText(options[i]);
            radioButtons[i].setTextColor(Color.WHITE);
            radioButtons[i].setChecked(false);
            radioButtons[i].setId(i);
            radioButtons[i].setTag(i);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, 30); // Adjust the last parameter (20 in this case) to set the desired space

            radioButtons[i].setLayoutParams(layoutParams);

            if (options[i].toString().trim().equalsIgnoreCase(RfiDatabase.selectedClient.trim())) {
                radioButtons[i].setChecked(false);
            }
            remark = new EditText(getApplicationContext());
            remark.setTextColor(Color.WHITE);
            remark.setHintTextColor(Color.WHITE);
            remark.setHint("Remarks");

            radioGroup.addView(radioButtons[i]);
            imageButton.setVisibility(View.VISIBLE);

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

        childLayout.addView(radioGroup);

        childLayout.addView(remark);
        scroll.addView(childLayout);


        parentLayout.addView(scroll);


        String ans = "";
        String remarks = "";
        int myNum = 0;

        try {
            myNum = Integer.parseInt(previousAns);
        } catch (NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }


        if (isanwered) {
            remark.setText(previousRem);
            radioButtons[myNum].setChecked(true);
        } else {
            radioButtons[1].setChecked(true);
            remark.setText(remarks);
        }


        TextView surveyDetail = (TextView) findViewById(R.id.roomTypeText);
        surveyDetail.setText(RfiDatabase.selectedSchemeName + " / " +
                RfiDatabase.selectedclientname + "/" + RfiDatabase.selectedChecklistName);
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
        ((TextView) findViewById(R.id.tradeText)).setText("Question.Type : " + question_type);


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
        //		Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
        //		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);


        File root = new File(Environment.getExternalStorageDirectory()
                + File.separator + "RFI" + File.separator + "temp");
        root.mkdirs();

        Date d = new Date();
        String g = RfiDatabase.selectedrfiId.replace(" ", "_");
        imageName = g + "_" + RfiDatabase.selectedNodeId.trim() + "_" + RfiDatabase.selectedChecklistId.trim() + "_" +
                RfiDatabase.selectedGroupId.trim() + "_" + RfiDatabase.userId.trim() + "_" + qestion_id + "_" + question_seq_id + "_"
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
        //currentPhotoPath = StorageUtils.getTempStorageLocationPath(this,imageName);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            imgFile = null;

            imgFile = image;

            if (imgFile != null)
            {
                Toast.makeText(this, "Rotate Camera to take picture in landscape mode", Toast.LENGTH_SHORT).show();
               //changed here authority
                Uri imgUri = FileProvider.getUriForFile(RfiQuestionireScroll.this, getApplicationContext().getPackageName() + ".fileprovider", imgFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
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

                imageCount++;
                imageButton.setText("Capture Image(" + imageCount + "/2)");

                try {
                    //------------------new logic

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

                    Intent int1 = new Intent(RfiQuestionireScroll.this, CustomeImageProcessing.class);
                    int1.putExtra("uri", currentPhotoPath);
                    System.out.println("custome image processing--------------" + currentPhotoPath);
                    startActivity(int1);

                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t.toString());
                    t.printStackTrace();
                }

                break;
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    protected void saveAnswer() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String rfi_date = df.format(new Date());
        System.out.println("images are :::" + snaps[0] + snaps[1]);
        //questionData[Qdata]="";
        String img3 = "";
        String img4 = "";

        try {
            // setting background colour
            viewModel.getQuestionAnswerUsingRfIdAsync(qestion_id,value ->{

                if (value != null) {
                    // Handle the result
                    AnswerTableModel answerTableModel = new AnswerTableModel();
                    answerTableModel.setCL_Id(RfiDatabase.selectedClientId);
                    answerTableModel.setPRJ_Id(RfiDatabase.selectedSchemeId);
                    answerTableModel.setLevel_int(RfiDatabase.selectedlevelId);
                    answerTableModel.setStructure_Id(RfiDatabase.selectedBuildingId);
                    answerTableModel.setStage_Id(RfiDatabase.selectedFloorId);
                    answerTableModel.setUnit_id(RfiDatabase.selectedUnitId);
                    answerTableModel.setSub_Unit_Id(RfiDatabase.selectedSubUnitId);
                    answerTableModel.setCoverage_str(coverage);
                    answerTableModel.setAns_text(answer);
                    answerTableModel.setRemark(remark.getText().toString());
                    answerTableModel.setRfi_id(RfiDatabase.selectedrfiId);
                    answerTableModel.setFK_question_id(qestion_id);
                    answerTableModel.setFK_QUE_SequenceNo(question_seq_id);
                    answerTableModel.setFK_QUE_Type(question_type);
                    answerTableModel.setFK_NODE_Id(RfiDatabase.selectedNodeId);
                    answerTableModel.setFk_CHKL_Id(RfiDatabase.selectedChecklistId);
                    answerTableModel.setFk_Grp_ID(RfiDatabase.selectedGroupId);
                    answerTableModel.setCheck_date(rfi_date);
                    answerTableModel.setDrawing_no(drawing);
                    answerTableModel.setSnap1(snaps[0]);
                    answerTableModel.setSnap2(snaps[1]);
                    answerTableModel.setSnap3(img3);
                    answerTableModel.setSnap4(img4);
                    answerTableModel.setUser_id(RfiDatabase.userId);
                    if (value.getPK_Answer_id() == 0){
                        answerTableModel.setDated(rfi_date);
                        Log.d(TAG, "saveAnswer: inserted data, "+answerTableModel);
                        viewModel.insertDefaultAnswer(answerTableModel);
                    }else {
                        answerTableModel.setAnswerFlag("1");
                        answerTableModel.setPK_Answer_id(value.getPK_Answer_id());
                        Log.d(TAG, "saveAnswer: updated data, "+answerTableModel);
                        viewModel.updateQuestionAnswer(answerTableModel);
                    }
                    Log.d(TAG, "DB Answer: " + value);
                    snaps = new String[]{"", ""};
                    imageCount = 0;
                    System.out.println("image count in saveanswer=================" + imageCount);
                    imageButton.setText("Capture Image(0/2)");
                    remark.setText("");
                    Intent int1 = new Intent(getApplicationContext(), RfiQuestionSelect.class);
                    finish();
                    startActivity(int1);
                } else {
                    // Handle error case
                    Log.e(TAG, "Failed to fetch answer");
                }
                return null;
            });
        }catch (Exception e){
            Log.e(TAG, "getView: Exception: ",e );
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_rfi_questtionire_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.viewImage:
                    showImage();
                    return true;
            }
        } catch (Exception aE) {
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImage() {
        //if (imageCount < 1 ) {
        if (snaps[0].equalsIgnoreCase("")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    RfiQuestionireScroll.this, R.style.MyAlertDialogStyle);
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
				/*if(!temp_snaps[1].equalsIgnoreCase("") && !snaps[0].equalsIgnoreCase("")){
					list.add(temp_snaps[1]);
					list.add(snaps[0]);
					
				}else if(!temp_snaps[0].equalsIgnoreCase("") | !temp_snaps[1].equalsIgnoreCase("")){
					for (String str : temp_snaps) {
						if (!TextUtils.isEmpty(str)) {
							list.add(str);
							System.out.println("images+++++++++" + str);
						}
					}
					
				}
				else
				{*/

            for (String str : snaps) {
                if (!TextUtils.isEmpty(str)) {
                    list.add(str);
                    System.out.println("images+++++++++" + str);
                }
                /*}*/
            }
            Intent i = new Intent(RfiQuestionireScroll.this, ViewImagesActivity.class);
            i.putExtra("data", list.toArray(new String[]{}));
            System.out.println("String Activity+++++++++");
            startActivity(i);
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //	super.onBackPressed();

        Intent int1 = new Intent(getApplicationContext(), RfiQuestionSelect.class);
        finish();
        startActivity(int1);
    }


}
