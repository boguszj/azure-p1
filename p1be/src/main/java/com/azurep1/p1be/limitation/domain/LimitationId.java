package com.azurep1.p1be.limitation.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class LimitationId implements Serializable {

    @NonNull
    @Column(name = "limitation_id")
    @JsonValue
    private UUID uuid;

    public LimitationId(String uuid) {
        this.uuid = UUID.fromString(uuid);
    }

    public static LimitationId newId() {
        return new LimitationId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}

