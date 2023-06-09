package com.gaming.oddsgame.walletservice.repository

import com.gaming.oddsgame.walletservice.data.WalletEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository : JpaRepository<WalletEntity, Long>{

    fun findAllByPlayerId(playerId : Long) : List<WalletEntity>
}