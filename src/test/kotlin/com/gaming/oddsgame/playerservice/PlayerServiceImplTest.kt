package com.gaming.oddsgame.playerservice

import com.gaming.oddsgame.lib.ServiceResult
import com.gaming.oddsgame.playerservice.data.PlayerEntity
import com.gaming.oddsgame.playerservice.impl.PlayerServiceImpl
import com.gaming.oddsgame.playerservice.repository.PlayerRepository
import com.gaming.oddsgame.walletservice.data.TransactionType.DEPOSIT
import com.gaming.oddsgame.walletservice.data.WalletEntity
import com.gaming.oddsgame.walletservice.impl.WalletServiceImpl
import java.math.BigDecimal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.dao.DataIntegrityViolationException

@DisplayName("BetServiceImpl Tests")
class PlayerServiceImplTest {

    @Mock
    private lateinit var playerRepository: PlayerRepository

    @Mock
    private lateinit var walletServiceImpl: WalletServiceImpl

    @InjectMocks
    private lateinit var playerService: PlayerServiceImpl

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test onboardPlayer() should return Success with player entity`() {
        val playerEntity = getPlayerEntity()
        `when`(playerRepository.save(playerEntity)).thenReturn(playerEntity)
        `when`(walletServiceImpl.create(playerEntity.id)).thenReturn(ServiceResult.Success(WalletEntity(playerEntity.id, id = 25)))
        val result = playerService.registerPlayer(playerEntity)

        assertNotNull(result.data)
        verify(playerRepository).save(any(PlayerEntity::class.java))
        verify(walletServiceImpl).create(playerEntity.id)
        verify(walletServiceImpl).createTransaction(25, BigDecimal("1000"), DEPOSIT)
    }

    @Test
    fun `test onboardPlayer() should return Error when username already exists`() {
        val playerEntity = getPlayerEntity()
        `when`(playerRepository.save(playerEntity)).thenThrow(DataIntegrityViolationException("Duplicate entry"))

        val result = playerService.registerPlayer(playerEntity)

        assertEquals("Username already exists", result.message)
        verify(playerRepository).save(playerEntity)
    }

    @Test
    fun `test findPlayer() should return Success with player entity when player exists`() {
        val playerId = 1L
        val playerEntity = getPlayerEntity()

        `when`(playerRepository.findById(playerId)).thenReturn(java.util.Optional.of(playerEntity))

        val result = playerService.fetchPlayerById(playerId)

        assertNotNull(result.data)
        verify(playerRepository).findById(playerId)
    }

    @Test
    fun `test findPlayer() should return Error when player does not exist`() {
        val playerId = 1L
        `when`(playerRepository.findById(playerId)).thenReturn(java.util.Optional.empty())

        val result = playerService.fetchPlayerById(playerId)

        assertEquals("Player not found", result.message)
        verify(playerRepository).findById(playerId)
    }


    private fun getPlayerEntity() = PlayerEntity("John","Doe","John08", 1L)


}