package com.example.payroll.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.payroll.adapters.inbound.rest.dto.ContractRequest;
import com.example.payroll.adapters.inbound.rest.validators.contract.ContractValidator;
import com.example.payroll.adapters.outbound.persistence.contract.mapper.ContractMapper;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.contract.port.in.ContractValidationUseCase;
import com.example.payroll.domain.employee.exception.ContractHasOverlapException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ContractValidatorTest extends BaseServiceTest {

    @Mock
    private ContractValidationUseCase contractValidationUseCaseMock;

    @InjectMocks
    private ContractValidator contractValidatorMock;

    private final ContractMapper mapper = getMapper(ContractMapper.class);
    private final ContractValidationUseCase contractValidationUseCase = new ContractValidationService(null, mapper);
    private final ContractValidator contractValidator = new ContractValidator(contractValidationUseCase);

    @Test
    void validateWhenContractOverlapsWithSavedOnes() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(10);

        Contract contract = createContract(10L, startDate, endDate);
        ContractRequest request = new ContractRequest(22L, List.of(contract));
        when(contractValidationUseCaseMock.overlap(any(), any())).thenReturn(Map.of(contract.toString(), List.of(contract)));

        ContractHasOverlapException exception = assertThrows(ContractHasOverlapException.class,
            () -> contractValidatorMock.validate(request));
        assertThat(exception.getMessage()).isEqualTo("Some of these contracts overlap with already saved ones.");
    }

    @Test
    void validateWhenContractOverlapsAmongThemSelf() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(10);

        Contract contract = createContract(10L, startDate, endDate);
        ContractRequest request = new ContractRequest(22L, List.of(contract, contract));

        ContractHasOverlapException exception = assertThrows(ContractHasOverlapException.class,
            () -> contractValidator.validate(request));
        assertThat(exception.getMessage()).isEqualTo("Some of the given contracts overlap with each other");
    }

    @Test
    void validateWhenContractsIsNull() {
        ContractRequest request = new ContractRequest(22L, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> contractValidator.validate(request));
        assertThat(exception.getMessage()).isEqualTo("At least one contract is required.");
    }

    @Test
    void validateWhenContractsIsEmpty() {
        ContractRequest request = new ContractRequest(22L, List.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> contractValidator.validate(request));
        assertThat(exception.getMessage()).isEqualTo("At least one contract is required.");
    }
}