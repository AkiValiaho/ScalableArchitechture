package com.akivaliaho.event;

import com.akivaliaho.ServiceEvent;
import lombok.*;

import javax.persistence.Id;

/**
 * Created by vagrant on 4/5/17.
 */
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class EventExchangeBinding {
    @Getter
    @NonNull
    ServiceEvent serviceEvent;
    @Getter
    @NonNull
    String exchangeName;
    @Getter
    @NonNull
    String routingKey;
    @Id
    private Long id;
}
