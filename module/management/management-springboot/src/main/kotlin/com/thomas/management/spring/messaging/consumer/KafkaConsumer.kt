package com.thomas.management.spring.messaging.consumer

import com.thomas.contract.messaging.ApplicationEvent
import com.thomas.contract.messaging.management.group.GroupManagementEvent
import com.thomas.contract.messaging.management.organization.OrganizationManagementEvent
import com.thomas.contract.messaging.management.unit.UnitManagementEvent
import com.thomas.contract.messaging.management.user.UserManagementEvent
import com.thomas.core.extension.logger
import java.util.function.Consumer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message

@Configuration
class KafkaConsumer {

    @Bean("organizationManagementConsumer")
    fun organizationManagementConsumer(): Consumer<Message<OrganizationManagementEvent>> {
        return Consumer { event ->
            event.acknowledge()
        }
    }

    @Bean("unitManagementConsumer")
    fun unitManagementConsumer(): Consumer<Message<UnitManagementEvent>> {
        return Consumer { event ->
            event.acknowledge()
        }
    }

    @Bean("groupManagementConsumer")
    fun groupManagementConsumer(): Consumer<Message<GroupManagementEvent>> {
        return Consumer { event ->
            event.acknowledge()
        }
    }

    @Bean("userManagementConsumer")
    fun userManagementConsumer(): Consumer<Message<UserManagementEvent>> {
        return Consumer { event ->
            event.acknowledge()
        }
    }

    private fun <T : ApplicationEvent<*, *>> Message<T>.acknowledge() {
        logger().info { "Acknowledging message: $this" }
        val acknowledgment = headers[KafkaHeaders.ACKNOWLEDGMENT] as Acknowledgment?
        acknowledgment?.acknowledge()
    }

}