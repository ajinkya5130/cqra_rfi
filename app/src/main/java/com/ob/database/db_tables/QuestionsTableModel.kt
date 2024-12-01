package com.ob.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question")
data class QuestionsTableModel (
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0,
    var questionId: Int = 0,
    var question: String ="",
    var questionSequence: Int=0,
    var questionType: String="",
    var nodeId: String="",
    var checklistId: Int=0,
    var structureId: Int=0,
    //api fields below
    var clientId: Int=0,
    var projectId: Int=0,
    var groupId: Int=0,
    // api fields above
    var userId: Int=0,
)
