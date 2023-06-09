package com.gaming.oddsgame.betservice

import com.gaming.oddsgame.betservice.data.BetEntity
import com.gaming.oddsgame.betservice.data.BetStatus
import com.gaming.oddsgame.betservice.data.OddsEntity
import com.gaming.oddsgame.betservice.impl.BetServiceImpl
import com.gaming.oddsgame.betservice.repository.BetRepository
import com.gaming.oddsgame.betservice.repository.OddsRepository
import com.gaming.oddsgame.lib.ServiceResult.Error
import com.gaming.oddsgame.lib.ServiceResult.Success
import com.gaming.oddsgame.walletservice.data.TransactionEntity
import com.gaming.oddsgame.walletservice.data.TransactionType
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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@DisplayName("BetServiceImpl Tests")
class BetServiceImplTest {

    @Mock
    private lateinit var walletService: WalletServiceImpl

    @Mock
    private lateinit var betRepository: BetRepository

    @Mock
    private lateinit var oddsRepository: OddsRepository

    @InjectMocks
    private lateinit var betService: BetServiceImpl

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }


    @Test
    fun `test placeBet() should return Success with bet entity`() {
        val walletId = 1L
        val amount = BigDecimal("100")
        val prediction = 2
        val transactionEntity = TransactionEntity(BigDecimal("1000"), WalletEntity(walletId), TransactionType.BET)
        val betEntity = BetEntity(walletId, BetStatus.PENDING, prediction, 1L, transactionEntity.id, amount, id = 1L)

        `when`(walletService.createTransaction(walletId, amount, TransactionType.BET)).thenReturn(
            Success(
                transactionEntity
            )
        )
        `when`(betRepository.save(any(BetEntity::class.java))).thenReturn(betEntity)

        val result = betService.placeBet(walletId, amount, prediction)

        assertNotNull(result.data)
        verify(walletService).createTransaction(walletId, amount, TransactionType.BET)
        verify(betRepository).save(any(BetEntity::class.java))
    }

    @Test
    fun `test placeBet() should return Error when creating transaction fails`() {
        val walletId = 1L
        val amount = BigDecimal("100")
        val prediction = 2
        val errorMessage = "Insufficient balance"

        `when`(walletService.createTransaction(walletId, amount, TransactionType.BET)).thenReturn(Error(errorMessage))

        val result = betService.placeBet(walletId, amount, prediction)

        assertEquals("Insufficient balance", result.message)
        verify(walletService).createTransaction(walletId, amount, TransactionType.BET)
    }

    @Test
    fun `test retrieveBets() should return list of bet entities`() {
        val walletId = 1L
        val betEntity1 = BetEntity(walletId, BetStatus.PENDING, 2, 1L, 1L, BigDecimal("100"))
        val betEntity2 = BetEntity(walletId, BetStatus.PENDING, 3, 1L, 2L, BigDecimal("200"))
        val betList = listOf(betEntity1, betEntity2)

        `when`(betRepository.findAllByWalletId(walletId)).thenReturn(betList)

        val result = betService.fetchAllBetsByWalletId(walletId)

        assertEquals(betList, result)
        verify(betRepository).findAllByWalletId(walletId)
    }

    @Test
    fun `test processEventResult() should update bets correctly when there is a matching odd`() {
        val eventId = 1L
        val result = 3

        val odds = listOf(
            OddsEntity(0, BigDecimal("10"), "Exact Match", eventId, id = 1L),
            OddsEntity(1, BigDecimal("2"), "+1/-1", eventId, id = 1L)
        )

        val bets = listOf(
            BetEntity(1L, BetStatus.PENDING, 3, eventId, 1L, BigDecimal("100")),
            BetEntity(1L, BetStatus.PENDING, 90, eventId, 2L, BigDecimal("200"))
        )
        val transactionEntity = TransactionEntity(BigDecimal("300"), WalletEntity(1L), TransactionType.BET_WIN, id = 23)

        `when`(oddsRepository.findAllByEventId(eventId)).thenReturn(odds)
        `when`(betRepository.findAllByEventIdAndStatus(eventId, BetStatus.PENDING)).thenReturn(bets)
        `when`(walletService.createTransaction(1L, BigDecimal("1000"), TransactionType.BET_WIN)).thenReturn(
            Success(
                transactionEntity
            )
        )
        `when`(betRepository.save(bets[0])).thenReturn(bets[0])
        `when`(betRepository.save(bets[1])).thenReturn(bets[1])

        betService.processEventResult(eventId, result)

        assertEquals(BetStatus.WINNING, bets[0].status)
        assertEquals(transactionEntity.id, bets[0].payoutTransactionId)
        assertEquals(result, bets[0].outcome)
        assertEquals(BigDecimal("1000"), bets[0].winAmount)

        assertEquals(BetStatus.LOSING, bets[1].status)
        assertEquals(result, bets[1].outcome)

        verify(oddsRepository).findAllByEventId(eventId)
        verify(betRepository).findAllByEventIdAndStatus(eventId, BetStatus.PENDING)
        verify(walletService).createTransaction(1L, BigDecimal("1000"), TransactionType.BET_WIN)
        verify(betRepository, times(2)).save(any(BetEntity::class.java))
    }


}