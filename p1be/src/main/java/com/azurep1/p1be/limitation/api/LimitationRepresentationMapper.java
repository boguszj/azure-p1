package com.azurep1.p1be.limitation.api;

import com.azurep1.p1be.limitation.domain.Limitation;
import com.azurep1.p1be.limitation.domain.LimitationRepresentation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LimitationRepresentationMapper {

    private final TypeMap<Limitation, LimitationRepresentation> typeMap = new ModelMapper().createTypeMap(Limitation.class, LimitationRepresentation.class);

    public List<LimitationRepresentation> toRepresentations(List<Limitation> limitations) {
        return limitations.stream()
                .map(this::toRepresentation)
                .collect(Collectors.toList());
    }

    public LimitationRepresentation toRepresentation(Limitation limitation) {
        return typeMap.map(limitation);

    }

}
