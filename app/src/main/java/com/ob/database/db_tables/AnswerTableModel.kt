package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Answer")
data class AnswerTableModel (
    @PrimaryKey(autoGenerate = true)
    var PK_Answer_id: Int = 0,
    var CL_Id: String = "",
    var PRJ_Id: String = "",
    var Level_int: String = "",
    var Structure_Id: String = "",
    var Stage_Id: String = "",
    var Unit_id: String = "",
    var Sub_Unit_Id: String = "",
    var Element_Id: String = "",
    var SubElement_Id: String = "",
    var Coverage_str: String = "",
    var ans_text: String = "",
    var remark: String = "",
    var snap1: String = "",
    var snap2: String = "",
    var snap3: String = "",
    var snap4: String = "",
    var Rfi_id: String = "",
    var dated: String = "",
    var FK_question_id: String = "",
    var FK_QUE_SequenceNo: String = "",
    var FK_QUE_Type: String = "",
    var FK_NODE_Id: String = "",
    var Fk_CHKL_Id: String = "",
    var Fk_Grp_ID: String = "",
    var checker_remark: String = "",
    var check_date: String = "",
    var drawing_no: String = "",
    var answerFlag: String = "",
    var user_id: String = "",
)