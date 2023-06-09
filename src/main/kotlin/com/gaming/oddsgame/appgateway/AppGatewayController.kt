package com.gaming.oddsgame.appgateway

import com.gaming.appgateway.api.GamesApi
import com.gaming.appgateway.api.PlayersApi
import com.gaming.appgateway.api.WalletsApi
import com.gaming.appgateway.model.CreatePlayerRequest
import com.gaming.appgateway.model.CreatePlayerResponse
import com.gaming.appgateway.model.GameResult
import com.gaming.appgateway.model.LuckNumberRequest
import com.gaming.appgateway.model.LuckNumberResponse
import com.gaming.appgateway.model.Odd
import com.gaming.appgateway.model.Transaction
import com.gaming.appgateway.model.Wallet
import com.gaming.oddsgame.appgateway.AppGatewayMapper.toEntity
import com.gaming.oddsgame.appgateway.AppGatewayMapper.toResponse
import com.gaming.oddsgame.appgateway.AppReadyListener.Companion.DEFAULT_EVENT_ID
import com.gaming.oddsgame.betservice.BetService
import com.gaming.oddsgame.gameservice.luckynumberquest.LuckyNumberQuestService
import com.gaming.oddsgame.lib.ServiceResult.Error
import com.gaming.oddsgame.lib.ServiceResult.Success
import com.gaming.oddsgame.playerservice.PlayerService
import com.gaming.oddsgame.walletservice.WalletService
import io.swagger.v3.oas.annotations.Operation
import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class AppGatewayController : GamesApi, PlayersApi, WalletsApi {

    @Autowired
    private lateinit var playerService: PlayerService

    @Autowired
    private lateinit var walletService: WalletService

    @Autowired
    private lateinit var betService: BetService

    @Autowired
    private lateinit var luckyNumberQuestService: LuckyNumberQuestService

    @Operation(summary = "Creates a player and wallet", description = "Returns player id")
    override fun createPlayer(createPlayerRequest: CreatePlayerRequest): ResponseEntity<CreatePlayerResponse> {
        return when (val response = playerService.registerPlayer(createPlayerRequest.toEntity())) {
            is Success -> {
                ResponseEntity.accepted().body(response.data!!.toResponse())
            }
            is Error -> {
                ResponseEntity.status(CONFLICT).body(CreatePlayerResponse(null, response.message))
            }
            else -> {
                ResponseEntity.internalServerError().body(CreatePlayerResponse(null, "System Error"))
            }
        }
    }

    @Operation(summary = "Retrieves wallet transactions")
    override fun getTransactionsByWalletId(@PathVariable(value = "wallet_id") walletId: BigDecimal): ResponseEntity<List<Transaction>> {
        return ResponseEntity.ok(
            walletService.fetchAllTransactionsByWalletId(walletId.toLong()).map { it.toResponse() }
        )
    }

    @Operation(summary = "Retrieves wallet balance and id")
    override fun getWalletsByPlayerId(@PathVariable(value = "player_id") playerId: BigDecimal): ResponseEntity<List<Wallet>> {
        return ResponseEntity.ok(
            walletService.fetchAllByPlayerId(playerId.toLong()).map { it.toResponse() }
        )
    }

    @Operation(summary = "Lucky Number Quest",
        description = "find your wallet id GET wallets/{player_id}")
    override fun playLuckyNumberQuest(luckNumberRequest: LuckNumberRequest): ResponseEntity<LuckNumberResponse> {
        val response = luckyNumberQuestService.play(
            luckNumberRequest.walletId.toLong(),
            luckNumberRequest.amount,
            luckNumberRequest.prediction
        )
        return when (response) {
            is Success -> {
                ResponseEntity.ok().body(LuckNumberResponse(response.data!!.outcome, response.data.prediction, null))
            }
            is Error -> {
                ResponseEntity.status(CONFLICT).body(LuckNumberResponse(null, null, response.message))
            }
            else -> {
                ResponseEntity.internalServerError().body(LuckNumberResponse(null, null, "System Error"))
            }
        }
    }

    @Operation(summary = "Retrieves bets placed",
        description = "find your wallet id GET wallets/{player_id}")
    override fun getGameResultsByWalletId(walletId: BigDecimal): ResponseEntity<List<GameResult>> {
        return ResponseEntity.ok(
            betService.fetchAllBetsByWalletId(walletId.toLong()).map {
                it.toResponse()
            }
        )
    }

    @Operation(summary = "Retrieves odds available for the game",
        description = "Possible winnings are calculated by bet amount times odds")
    override fun gamesLuckyNumberQuestOddsGet(): ResponseEntity<List<Odd>> {
        return ResponseEntity.ok(
            betService.fetchAllOddsByEventId(DEFAULT_EVENT_ID).map {
                it.toResponse()
            }
        )
    }
}