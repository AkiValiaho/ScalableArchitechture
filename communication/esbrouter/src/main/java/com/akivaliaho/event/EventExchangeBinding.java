package com.akivaliaho.event;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by vagrant on 4/5/17.
 */
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Entity
public class EventExchangeBinding {
    @Getter
    @NonNull
    @OneToOne
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
