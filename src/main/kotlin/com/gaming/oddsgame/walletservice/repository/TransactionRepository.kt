package com.gaming.oddsgame.walletservice.repository

import com.gaming.oddsgame.walletservice.data.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<TransactionEntity, Long>{

    fun findAllByWalletId(walletId : Long) : List<TransactionEntity>
}