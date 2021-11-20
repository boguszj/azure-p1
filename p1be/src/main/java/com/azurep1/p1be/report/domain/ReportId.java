package com.azurep1.p1be.report.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ReportId implements Serializable {

    @NonNull
    @Column(name = "report_id")
    @JsonValue
    private UUID uuid;

    public ReportId(String uuid) {
        this.uuid = UUID.fromString(uuid);
    }

    public static ReportId newId() {
        return new ReportId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

}
