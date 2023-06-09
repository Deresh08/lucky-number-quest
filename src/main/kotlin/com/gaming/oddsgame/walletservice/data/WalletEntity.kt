package com.gaming.oddsgame.walletservice.data

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table


@Entity
@Table(name = "wallets")
class WalletEntity(
    @Column(name = "player_id")
    val playerId: Long,
    var balance: BigDecimal = BigDecimal.ZERO,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @OneToMany(mappedBy="wallet",fetch = FetchType.LAZY)
    val transactions: Set<TransactionEntity> = emptySet(),
) {

    fun debit(amount: BigDecimal): Boolean {
        if (balance < amount) {
            return false
        }
        balance += amount.negate()
        return true
    }

    fun credit(amount: BigDecimal) {
        balance += amount
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TransactionEntity) return false
        return this.id == other.id && this.id != 0L
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}