package com.gaming.oddsgame.playerservice.repository

import com.gaming.oddsgame.playerservice.data.PlayerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PlayerRepository: JpaRepository<PlayerEntity, Long>