package com.gaming.oddsgame.appgateway

import com.gaming.appgateway.model.CreatePlayerRequest
import com.gaming.appgateway.model.CreatePlayerResponse
import com.gaming.appgateway.model.GameResult
import com.gaming.appgateway.model.Odd
import com.gaming.appgateway.model.Transaction
import com.gaming.appgateway.model.Wallet
import com.gaming.oddsgame.betservice.data.BetEntity
import com.gaming.oddsgame.betservice.data.OddsEntity
import com.gaming.oddsgame.playerservice.data.PlayerEntity
import com.gaming.oddsgame.walletservice.data.TransactionEntity
import com.gaming.oddsgame.walletservice.data.WalletEntity
import java.time.OffsetDateTime
import java.time.ZoneOffset

object AppGatewayMapper {

    fun CreatePlayerRequest.toEntity() = PlayerEntity(
        name,
        surname,
        username,
    )

    fun PlayerEntity.toResponse() = CreatePlayerResponse(id.toBigDecimal())

    fun TransactionEntity.toResponse() = Transaction(
        id.toBigDecimal(),
        amount,
        transactionType.name,
        createdOn.toOffsetDateTime()
    )

    fun WalletEntity.toResponse() = Wallet(id.toBigDecimal(), balance)

    fun BetEntity.toResponse() = GameResult(
        id.toBigDecimal(),
        prediction,
        outcome,
        amount, winAmount,
        status.name,
        createdAt.toOffsetDateTime()
    )

    fun Long.toOffsetDateTime(): OffsetDateTime =
        OffsetDateTime.ofInstant(java.time.Instant.ofEpochMilli(this), ZoneOffset.systemDefault())


    fun OddsEntity.toResponse() = Odd(spreadValue, oddsValue, description)

}