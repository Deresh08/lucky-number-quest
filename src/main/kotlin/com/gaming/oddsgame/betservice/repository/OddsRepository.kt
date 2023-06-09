package com.gaming.oddsgame.betservice.repository

import com.gaming.oddsgame.betservice.data.OddsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OddsRepository : JpaRepository<OddsEntity, Long>{

    fun findAllByEventId(eventId : Long) : List<OddsEntity>
}