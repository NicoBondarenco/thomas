package com.thomas.management.domain.mock

import com.thomas.management.domain.event.OrganizationEventProducer
import com.thomas.management.domain.event.UnitEventProducer
import com.thomas.management.domain.event.UserEventProducer
import io.mockk.spyk

internal val organizationProducerMock = spyk<OrganizationEventProducer>()
internal val unitProducerMock = spyk<UnitEventProducer>()
internal val userProducerMock = spyk<UserEventProducer>()