package com.gaming.oddsgame.gameservice.luckynumberquest

import com.gaming.oddsgame.appgateway.AppReadyListener
import com.gaming.oddsgame.betservice.BetService
import com.gaming.oddsgame.lib.ServiceResult
import com.gaming.oddsgame.lib.ServiceResult.Success
import com.gaming.oddsgame.lib.ServiceResult.Error

import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LuckyNumberQuestService {

    @Autowired
    private lateinit var betService: BetService

    fun play(walletId: Long, amount: BigDecimal, prediction: Int): ServiceResult<LuckyNumberQuestOutcome> {
        val response = betService.placeBet(walletId, amount, prediction)
        if (response is Error) {
            return Error(response.message!!)
        }
        val generatedNumber = (1..10).random()
        betService.processEventResult(AppReadyListener.DEFAULT_EVENT_ID, generatedNumber)
        return Success(LuckyNumberQuestOutcome(prediction, generatedNumber))
    }

}