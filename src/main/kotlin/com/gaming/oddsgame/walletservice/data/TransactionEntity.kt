package com.gaming.oddsgame.walletservice.data

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "transactions")
class TransactionEntity(
    val amount: BigDecimal,
    @ManyToOne
    val wallet: WalletEntity,
    @Enumerated(EnumType.STRING)
    val transactionType: TransactionType,
    @Column(name = "created_on")
    val createdOn: Long = System.currentTimeMillis(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
){

}