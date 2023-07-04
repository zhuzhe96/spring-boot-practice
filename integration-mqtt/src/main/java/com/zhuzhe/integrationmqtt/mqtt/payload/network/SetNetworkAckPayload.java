package com.zhuzhe.integrationmqtt.mqtt.payload.network;

import com.zhuzhe.integrationmqtt.mqtt.payload.MessagePayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SetNetworkAckPayload extends MessagePayload {

}
