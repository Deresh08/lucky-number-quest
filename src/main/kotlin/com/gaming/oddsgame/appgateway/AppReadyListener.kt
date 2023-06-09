package com.gaming.oddsgame.appgateway

import com.gaming.oddsgame.betservice.data.OddsEntity
import com.gaming.oddsgame.betservice.repository.OddsRepository
import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class AppReadyListener {

    companion object{
        const val DEFAULT_EVENT_ID = 12345L
    }

    @Autowired
    private lateinit var oddsRepository: OddsRepository

    @EventListener
    fun appReady(event: ApplicationReadyEvent) {
        oddsRepository.save(OddsEntity(
            0,
            BigDecimal.valueOf(10),
            "Exact Match Of Result",
            DEFAULT_EVENT_ID
        ))

        oddsRepository.save(OddsEntity(
            1,
            BigDecimal.valueOf(5),
            "+1/-1 Of Result",
            DEFAULT_EVENT_ID
        ))

        oddsRepository.save(OddsEntity(
            2,
            BigDecimal.valueOf(0.5),
            "+2/-2 Of Result",
            DEFAULT_EVENT_ID
        ))
    }
}
