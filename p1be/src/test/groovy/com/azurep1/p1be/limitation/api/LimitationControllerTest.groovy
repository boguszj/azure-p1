package com.azurep1.p1be.limitation.api

import com.azurep1.p1be.BaseIntegrationSpec
import com.azurep1.p1be.limitation.domain.CreateLimitationDto
import com.azurep1.p1be.limitation.domain.LimitationId
import com.azurep1.p1be.limitation.domain.LimitationRepresentation
import com.azurep1.p1be.limitation.domain.UpdateLimitationDto
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@Unroll
@Sql('classpath:sql/limitsControllerTest.sql')
class LimitationControllerTest extends BaseIntegrationSpec {

    private static final String BASE_URL = '/api/limitation'

    def 'should create limit'() {

        given:
        def periodSeconds = 100
        def limitationSeconds = 10
        def domain = "google.com"

        and:
        def dto = new CreateLimitationDto(
                periodSeconds: periodSeconds,
                limitationSeconds: limitationSeconds,
                domain: domain
        )


        when:
        def response = mvc.perform(createLimit(dto)).andReturn().response

        then:
        response.status == HttpStatus.CREATED.value()

        and:
        def representations = objectMapper.readValue(response.contentAsString, LimitationRepresentation[])
        def representation = representations.find { it.domain == domain }

        and:
        representation.id != null
        representation.periodSeconds == periodSeconds
        representation.limitationSeconds == limitationSeconds
        representation.domain == domain

        and:
        def entity = transactionTemplate.execute { limitationRepository.findAll().stream().filter { it.domain == domain } }.findFirst().get()

        and:
        entity.id != null
        entity.domain == domain
        entity.periodSeconds == periodSeconds
        entity.limitationSeconds == limitationSeconds

    }

    def 'should return 400 BAD_REQUEST on invalid limit'() {

        given:
        def dto = new CreateLimitationDto(
                periodSeconds: periodSeconds,
                limitationSeconds: limitationSeconds,
                domain: domain
        )

        when:
        def response = mvc.perform(createLimit(dto)).andReturn().response

        then:
        response.status == HttpStatus.BAD_REQUEST.value()

        where:
        periodSeconds | limitationSeconds | domain
        null          | 10           | 'google.com'
        10            | null         | 'google.com'
        10            | 100          | 'google.com'
        10            | 10           | 'google.com'
        100           | 10           | null
        100           | 10           | ''
        100           | 10           | ' '
        100           | 10           | 'asdasdsaf'
        100           | 10           | '.com'
        100           | 10           | 'a#b.com'

    }

    def 'should delete limit'() {

        given:
        def limitationId = new LimitationId('44897b31-c8d8-41de-a72c-0cfb79e83ff0')

        when:
        def response = mvc.perform(deleteLimit(limitationId)).andReturn().response

        then:
        response.status == HttpStatus.OK.value()

        and:
        limitationRepository.findById(limitationId).isEmpty()

        and:
        limitationRepository.findById(new LimitationId('60fa0022-a6e8-493b-8443-3a36235183b3')).isPresent()

    }

    def 'should update schedule'() {

        given:
        def limitationId = new LimitationId('a71d17df-9a4d-424a-b122-9e45c6f408a1')

        def periodSeconds = 100
        def limitationSeconds = 10

        and:
        def dto = new UpdateLimitationDto(
                periodSeconds: periodSeconds,
                limitationSeconds: limitationSeconds,
        )

        when:
        def response = mvc.perform(updateLimit(dto, limitationId)).andReturn().response

        then:
        response.status == HttpStatus.OK.value()

        and:
        def representations = objectMapper.readValue(response.contentAsString, LimitationRepresentation[])
        def representation = representations.find { it.id == limitationId }

        and:
        representation.id != null
        representation.limitationSeconds == limitationSeconds
        representation.periodSeconds == periodSeconds

        and:
        def entity = transactionTemplate.execute { limitationRepository.findById(limitationId) }.get()

        and:
        entity.id != null
        entity.periodSeconds == periodSeconds
        entity.limitationSeconds == limitationSeconds

    }

    def 'should return 400 BAD_REQUEST on invalid update'() {


        given:
        def limitationId = new LimitationId('a71d17df-9a4d-424a-b122-9e45c6f408a1')

        and:
        def dto = new UpdateLimitationDto(
                periodSeconds: period,
                limitationSeconds: limit,
        )

        when:
        def response = mvc.perform(updateLimit(dto, limitationId)).andReturn().response

        then:
        response.status == HttpStatus.BAD_REQUEST.value()

        where:
        period | limit
        null   | 10
        10     | null
        10     | 100
        10     | 10

    }

    def 'should find all'() {

        when:
        def response = mvc.perform(getAll()).andReturn().response

        then:
        response.status == HttpStatus.OK.value()

        and:
        objectMapper.readValue(response.contentAsString, LimitationRepresentation[]).size() == 3

    }

    def 'should return 404 on update for non-existent schedule'() {

        given:
        def scheduleId = new LimitationId('c43e183c-f4fc-4d57-82dc-09d642653331')

        def periodSeconds = 100
        def limitationSeconds = 10

        and:
        def dto = new UpdateLimitationDto(
                periodSeconds: periodSeconds,
                limitationSeconds: limitationSeconds
        )

        when:
        def response = mvc.perform(updateLimit(dto, scheduleId)).andReturn().response

        then:
        response.status == HttpStatus.NOT_FOUND.value()

    }

    def 'should return 204 on non existent schedule deletion'() {

        when:
        def response = mvc.perform(deleteLimit(new LimitationId('c43e183c-f4fc-4d57-82dc-09d642653331'))).andReturn().response

        then:
        response.status == HttpStatus.NO_CONTENT.value()

    }

    def createLimit(CreateLimitationDto dto) {
        post(BASE_URL)
                .content(objectMapper.writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON)
    }

    def deleteLimit(LimitationId id) {
        delete(BASE_URL + '/{id}', id)
                .contentType(MediaType.APPLICATION_JSON)
    }

    def updateLimit(UpdateLimitationDto updateDto, LimitationId id) {
        put(BASE_URL + '/{id}', id)
                .content(objectMapper.writeValueAsBytes(updateDto))
                .contentType(MediaType.APPLICATION_JSON)
    }

    def getAll() {
        get(BASE_URL)
    }

}
