package com.pappermint.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.MessageChannel;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
public class MqttConsumerConfig {

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer mqttInbound() {

        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        "fleet-service",
                        new DefaultMqttPahoClientFactory() {{

                            MqttConnectOptions options = new MqttConnectOptions();
                            options.setServerURIs(
                                    new String[]{"tcp://fleet-mqtt:1883"}
                            );
                            setConnectionOptions(options);
                        }},
                        "robot/+/status"
                );

        adapter.setOutputChannel(mqttInputChannel());

        return adapter;
    }

}