package com.gaming.oddsgame.betservice.impl

import com.gaming.oddsgame.appgateway.AppReadyListener.Companion.DEFAULT_EVENT_ID
import com.gaming.oddsgame.betservice.BetService
import com.gaming.oddsgame.betservice.data.BetEntity
import com.gaming.oddsgame.betservice.data.BetStatus.LOSING
import com.gaming.oddsgame.betservice.data.BetStatus.PENDING
import com.gaming.oddsgame.betservice.data.BetStatus.WINNING
import com.gaming.oddsgame.betservice.data.OddsEntity
import com.gaming.oddsgame.betservice.repository.BetRepository
import com.gaming.oddsgame.betservice.repository.OddsRepository
import com.gaming.oddsgame.lib.ServiceResult
import com.gaming.oddsgame.lib.ServiceResult.Success
import com.gaming.oddsgame.walletservice.data.TransactionType.BET
import com.gaming.oddsgame.walletservice.data.TransactionType.BET_WIN
import com.gaming.oddsgame.walletservice.impl.WalletServiceImpl
import java.math.BigDecimal
import kotlin.math.abs
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BetServiceImpl : BetService {

    @Autowired
    private lateinit var walletService: WalletServiceImpl

    @Autowired
    private lateinit var betRepository: BetRepository

    @Autowired
    private lateinit var oddsRepository: OddsRepository

    override fun placeBet(walletId: Long, amount: BigDecimal, prediction: Int): ServiceResult<BetEntity> =
        when (val response = walletService.createTransaction(walletId, amount, BET)) {
            is Success -> {
                val betEntity = betRepository.save(
                    BetEntity(
                        walletId,
                        PENDING,
                        prediction,
                        DEFAULT_EVENT_ID,
                        response.data!!.id,
                        amount
                    )
                )
                Success(betEntity)
            }
            is ServiceResult.Error -> {
                ServiceResult.Error(response.message!!)
            }

            else -> {
                ServiceResult.Error("System error")
            }
        }


    override fun fetchAllBetsByWalletId(walletId: Long): List<BetEntity> =
        betRepository.findAllByWalletId(walletId)

    override fun processEventResult(eventId: Long, result: Int) {
        val odds = oddsRepository.findAllByEventId(eventId)
        val bets = betRepository.findAllByEventIdAndStatus(eventId, PENDING)

        for (bet in bets) {
            val difference = abs(bet.prediction - result)
            val matchingOdd = odds.find { it.spreadValue == difference }

            if (matchingOdd != null) {

                val winnings = bet.amount.times(matchingOdd.oddsValue)
                val response =
                    walletService.createTransaction(bet.walletId, winnings, BET_WIN)

                if (response is Success) {
                    bet.status = WINNING
                    bet.payoutTransactionId = response.data!!.id
                    bet.outcome = result
                    bet.winAmount = winnings
                    betRepository.save(bet)
                } else {
                    // Handle system failure and alert for this error
                }
            } else {
                bet.status = LOSING
                bet.outcome = result
                betRepository.save(bet)
            }
        }
    }

    override fun fetchAllOddsByEventId(eventId: Long): List<OddsEntity> = oddsRepository.findAllByEventId(eventId)


}