package com.gaming.oddsgame.betservice.repository

import com.gaming.oddsgame.betservice.data.BetEntity
import com.gaming.oddsgame.betservice.data.BetStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BetRepository: JpaRepository<BetEntity, Long>{

    fun findAllByWalletId(walletId : Long) : List<BetEntity>

    fun findAllByEventIdAndStatus(eventId : Long, status : BetStatus) : List<BetEntity>
}