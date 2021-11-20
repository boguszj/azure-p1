package com.azurep1.p1be.limitation.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LimitationRepository extends JpaRepository<Limitation, LimitationId> {
}
