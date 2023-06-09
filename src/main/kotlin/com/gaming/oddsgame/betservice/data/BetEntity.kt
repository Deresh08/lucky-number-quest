package com.gaming.oddsgame.betservice.data

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "bets")
class BetEntity(
    @Column(name = "wallet_id")
    val walletId: Long,
    var status: BetStatus,
    val prediction : Int,
    @Column(name = "event_id")
    val eventId : Long,
    @Column(name = "transaction_id")
    val transactionId : Long,
    val amount : BigDecimal,
    var winAmount : BigDecimal = BigDecimal.ZERO,
    @Column(name = "payout_transaction_id")
    var payoutTransactionId : Long? = null,
    var outcome : Int? = null,
    @Column(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
){

}