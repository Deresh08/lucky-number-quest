package com.gaming.oddsgame.walletservice

import com.gaming.oddsgame.walletservice.data.TransactionEntity
import com.gaming.oddsgame.walletservice.data.TransactionType
import com.gaming.oddsgame.walletservice.data.WalletEntity
import com.gaming.oddsgame.walletservice.impl.WalletServiceImpl
import com.gaming.oddsgame.walletservice.repository.TransactionRepository
import com.gaming.oddsgame.walletservice.repository.WalletRepository
import java.math.BigDecimal
import java.util.Optional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@DisplayName("BetServiceImpl Tests")
class WalletServiceImplTest {

    @Mock
    private lateinit var walletRepository: WalletRepository

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @InjectMocks
    private lateinit var walletService: WalletServiceImpl

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test create() should return Success with wallet entity`() {
        val playerId = 123L
        val openingBalance = BigDecimal.valueOf(100)
        val walletEntity = WalletEntity(playerId, openingBalance, 25)

        `when`(walletRepository.save(any(WalletEntity::class.java))).thenReturn(walletEntity)

        val result = walletService.create(playerId)

        assertNotNull(result.data)
        verify(walletRepository).save(any(WalletEntity::class.java))
    }

    @Test
    fun `test createTransaction() should return Success with transaction entity for BET type`() {
        val walletId = 123L
        val amount = BigDecimal.valueOf(50)
        val transactionType = TransactionType.BET
        val initialBalance = BigDecimal.valueOf(100)
        val walletEntity = WalletEntity(walletId, initialBalance)

        `when`(walletRepository.findById(walletId)).thenReturn(Optional.ofNullable(walletEntity))
        `when`(walletRepository.save(any(WalletEntity::class.java))).thenReturn(walletEntity)
        `when`(transactionRepository.save(any(TransactionEntity::class.java))).thenAnswer {
            it.arguments[0]
        }

        walletService.createTransaction(walletId, amount, transactionType)

        assertEquals(initialBalance - amount, walletEntity.balance)
        verify(walletRepository).save(any(WalletEntity::class.java))
        verify(transactionRepository).save(any(TransactionEntity::class.java))
    }

    @Test
    fun `test createTransaction() should return Error when wallet is not found`() {
        val walletId = 123L
        val amount = BigDecimal.valueOf(50)
        val transactionType = TransactionType.BET

        `when`(walletRepository.findById(walletId)).thenReturn(Optional.empty())

        val result = walletService.createTransaction(walletId, amount, transactionType)

        assertEquals("Wallet Not Found", result.message)
    }

    @Test
    fun `test createTransaction() should return Error when betting amount exceeds wallet balance`() {
        val walletId = 123L
        val amount = BigDecimal.valueOf(200)
        val transactionType = TransactionType.BET
        val walletEntity = WalletEntity(walletId, BigDecimal.valueOf(100))

        `when`(walletRepository.findById(walletId)).thenReturn(Optional.ofNullable(walletEntity))

        val result = walletService.createTransaction(walletId, amount, transactionType)

        assertEquals("Insufficient Balance", result.message)
    }


}