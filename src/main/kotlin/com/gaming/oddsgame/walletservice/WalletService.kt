package com.gaming.oddsgame.walletservice

import com.gaming.oddsgame.lib.ServiceResult
import com.gaming.oddsgame.walletservice.data.TransactionEntity
import com.gaming.oddsgame.walletservice.data.TransactionType
import com.gaming.oddsgame.walletservice.data.WalletEntity
import java.math.BigDecimal

interface WalletService {

   fun create(playerId : Long) : ServiceResult<WalletEntity>

   fun createTransaction(walletId : Long, amount : BigDecimal, transactionType: TransactionType) : ServiceResult<TransactionEntity>

   fun fetchAllByPlayerId(playerId: Long) : List<WalletEntity>

   fun fetchAllTransactionsByWalletId(walletId: Long) : List<TransactionEntity>

}