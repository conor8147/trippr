package com.con19.tripplanner.db.entities

import androidx.room.*
import java.util.*

@Entity(
    tableName = "transactions"
)
class Transaction(
    var name: String,
    var creationDate: Date,
    var tripId: Long,
    var paid: Boolean,
    var cost: Int,
    var image: String
) {
    @PrimaryKey(autoGenerate = true)
    var transactionId: Long = 0
}

data class TransactionWithPeople(
    @Embedded val transaction: Transaction,
    @Relation(
        parentColumn = "transactionId",
        entityColumn = "personId",
        associateBy = Junction(TransactionPersonCrossRef::class)
    )
    val people: List<Person>
)