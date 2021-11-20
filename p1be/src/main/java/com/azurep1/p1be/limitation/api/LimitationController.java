package com.azurep1.p1be.limitation.api;

import com.azurep1.p1be.limitation.application.LimitationService;
import com.azurep1.p1be.limitation.domain.CreateLimitationDto;
import com.azurep1.p1be.limitation.domain.LimitationId;
import com.azurep1.p1be.limitation.domain.LimitationRepresentation;
import com.azurep1.p1be.limitation.domain.UpdateLimitationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/limitation")
public class LimitationController {

    private final LimitationService limitationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public List<LimitationRepresentation> create(@RequestBody @Valid CreateLimitationDto dto) {
        return limitationService.create(dto);
    }

    @GetMapping
    public List<LimitationRepresentation> getAll() {
        return limitationService.getAll();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public List<LimitationRepresentation> update(@PathVariable LimitationId id, @RequestBody @Valid UpdateLimitationDto dto) {
        return limitationService.update(dto.withId(id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public List<LimitationRepresentation> delete(@PathVariable LimitationId id) {
        return limitationService.delete(id);
    }

}
