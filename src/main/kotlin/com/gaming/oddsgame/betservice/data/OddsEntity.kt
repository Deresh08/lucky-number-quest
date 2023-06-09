package com.gaming.oddsgame.betservice.data

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "odds")
class OddsEntity (
    @Column(name = "spread_value")
    val spreadValue: Int,
    @Column(name = "odds_value")
    val oddsValue: BigDecimal,
    val description : String,
    @Column(name = "event_id")
    val eventId : Long,
    @Column(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)