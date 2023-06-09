package com.gaming.oddsgame.walletservice.impl

import com.gaming.oddsgame.lib.ServiceResult
import com.gaming.oddsgame.walletservice.WalletService
import com.gaming.oddsgame.walletservice.data.TransactionEntity
import com.gaming.oddsgame.walletservice.repository.TransactionRepository
import com.gaming.oddsgame.walletservice.data.TransactionType
import com.gaming.oddsgame.walletservice.data.TransactionType.BET
import com.gaming.oddsgame.walletservice.data.TransactionType.BET_WIN
import com.gaming.oddsgame.walletservice.data.TransactionType.DEPOSIT
import com.gaming.oddsgame.walletservice.data.WalletEntity
import com.gaming.oddsgame.walletservice.repository.WalletRepository
import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class WalletServiceImpl : WalletService {

    @Autowired
    private lateinit var walletRepository: WalletRepository

    @Autowired
    private lateinit var transactionRepository: TransactionRepository


    override fun create(playerId: Long): ServiceResult<WalletEntity> =
            ServiceResult.Success(walletRepository.save(WalletEntity(playerId)))


    override fun createTransaction(
        walletId: Long,
        amount: BigDecimal,
        transactionType: TransactionType
    ): ServiceResult<TransactionEntity> {
        val wallet = walletRepository.findById(walletId).orElse(null) ?: return ServiceResult.Error("Wallet Not Found")

        val transaction = when (transactionType) {
            DEPOSIT, BET_WIN -> {
                wallet.credit(amount)
                transactionRepository.save(
                    TransactionEntity(
                        amount,
                        wallet,
                        transactionType
                    )
                )
            }
            BET -> {
                if (wallet.debit(amount)) {
                    transactionRepository.save(
                        TransactionEntity(
                            amount.negate(),
                            wallet,
                            transactionType
                        )
                    )
                } else {
                    return ServiceResult.Error("Insufficient Balance")
                }
            }
        }

        walletRepository.save(wallet)
        return ServiceResult.Success(transaction)
    }

    override fun fetchAllByPlayerId(playerId: Long): List<WalletEntity> =
        walletRepository.findAllByPlayerId(playerId)

    override fun fetchAllTransactionsByWalletId(walletId: Long): List<TransactionEntity> =
        transactionRepository.findAllByWalletId(walletId)


}