package com.gaming.oddsgame.betservice

import com.gaming.oddsgame.betservice.data.BetEntity
import com.gaming.oddsgame.betservice.data.OddsEntity
import com.gaming.oddsgame.lib.ServiceResult
import java.math.BigDecimal

interface BetService {
    fun placeBet(walletId : Long, amount : BigDecimal, prediction: Int) : ServiceResult<BetEntity>

    fun fetchAllBetsByWalletId(walletId: Long) : List<BetEntity>

    fun processEventResult(eventId : Long, result: Int)

    fun fetchAllOddsByEventId(eventId: Long) :List<OddsEntity>

}