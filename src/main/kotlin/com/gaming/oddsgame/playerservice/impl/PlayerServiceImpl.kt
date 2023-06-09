package com.gaming.oddsgame.playerservice.impl

import com.gaming.oddsgame.lib.ServiceResult
import com.gaming.oddsgame.playerservice.PlayerService
import com.gaming.oddsgame.playerservice.data.PlayerEntity
import com.gaming.oddsgame.playerservice.repository.PlayerRepository
import com.gaming.oddsgame.walletservice.data.TransactionType.DEPOSIT
import com.gaming.oddsgame.walletservice.impl.WalletServiceImpl
import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

@Service
class PlayerServiceImpl : PlayerService {

    @Autowired
    lateinit var playerRepository: PlayerRepository

    @Autowired
    lateinit var walletServiceImpl: WalletServiceImpl

    override fun registerPlayer(playerEntity: PlayerEntity): ServiceResult<PlayerEntity> =
        try {
           val player = playerRepository.save(playerEntity)
           val wallet = walletServiceImpl.create(player.id)
            walletServiceImpl.createTransaction(wallet.data!!.id, BigDecimal("1000"), DEPOSIT)
            ServiceResult.Success(player)
        } catch (ex: DataIntegrityViolationException) {
            ServiceResult.Error("Username already exists")
        }

    override fun fetchAllPlayers(): List<PlayerEntity> =
        playerRepository.findAll()

    override fun fetchPlayerById(id: Long): ServiceResult<PlayerEntity> {
        val player = playerRepository.findById(id)
        return if (player.isPresent) {
            ServiceResult.Success(player.get())
        } else {
            ServiceResult.Error("Player not found")
        }
    }
}