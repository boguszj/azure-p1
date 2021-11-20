package com.azurep1.p1be.limitation.application;

import com.azurep1.p1be.limitation.api.LimitationRepresentationMapper;
import com.azurep1.p1be.limitation.domain.CreateLimitationDto;
import com.azurep1.p1be.limitation.domain.Limitation;
import com.azurep1.p1be.limitation.domain.LimitationId;
import com.azurep1.p1be.limitation.domain.LimitationRepository;
import com.azurep1.p1be.limitation.domain.LimitationRepresentation;
import com.azurep1.p1be.limitation.domain.UpdateLimitationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LimitationService {

    private final LimitationRepository repository;
    private final LimitationRepresentationMapper representationMapper;

    public List<LimitationRepresentation> create(CreateLimitationDto dto) {
        var limitation = new Limitation(dto.getLimitationSeconds(), dto.getPeriodSeconds(), dto.getDomain());
        repository.save(limitation);
        return getAll();
    }

    public List<LimitationRepresentation> update(UpdateLimitationDto dto) {
        var limitation = repository.getById(dto.getId());
        limitation.update(dto.getLimitationSeconds(), dto.getPeriodSeconds());
        return getAll();
    }

    public List<LimitationRepresentation> delete(LimitationId id) {
        repository.findById(id).ifPresent(repository::delete);
        return getAll();
    }

    public List<LimitationRepresentation> getAll() {
        var limits = repository.findAll();
        return representationMapper.toRepresentations(limits);
    }

}
