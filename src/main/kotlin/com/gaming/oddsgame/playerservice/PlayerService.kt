package com.gaming.oddsgame.playerservice

import com.gaming.oddsgame.lib.ServiceResult
import com.gaming.oddsgame.playerservice.data.PlayerEntity

interface PlayerService {
    fun registerPlayer(playerEntity: PlayerEntity) : ServiceResult<PlayerEntity>

    fun fetchAllPlayers() : List<PlayerEntity>

    fun fetchPlayerById(id : Long) : ServiceResult<PlayerEntity>
}