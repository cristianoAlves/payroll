package com.example.payroll.adapters.outbound.persistence.contract.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.payroll.adapters.outbound.persistence.BaseMapperTest;
import com.example.payroll.adapters.outbound.persistence.contract.entity.ContractEntity;
import com.example.payroll.domain.contract.model.Contract;
import org.junit.jupiter.api.Test;

class ContractMapperTest extends BaseMapperTest {

    private final ContractMapper mapper = getMapper(ContractMapper.class);

    @Test
    public void fromEntity() {
        ContractEntity expectedContract = createContractEntity(START_DATE, END_DATE);
        Contract actualContract = mapper.fromEntity(expectedContract);
        validateContract(actualContract, expectedContract);
    }

    @Test
    public void fromEntityNull() {
        assertThat(mapper.fromEntity(null)).isNull();
    }

    @Test
    public void fromContract() {
        Contract contract = createContract(START_DATE, END_DATE);
        ContractEntity contractEntity = mapper.fromContract(contract);
        validateContract(contract, contractEntity);
    }

    @Test
    public void fromContractNull() {
        assertThat(mapper.fromContract(null)).isNull();
    }

    private void validateContract(Contract contract, ContractEntity contractEntity) {
        assertThat(contract.endDate()).isEqualTo(contractEntity.getEndDate());
        assertThat(contract.startDate()).isEqualTo(contractEntity.getStartDate());
        assertThat(contract.salary()).isEqualTo(contractEntity.getSalary());
        assertThat(contract.active()).isEqualTo(contractEntity.isActive());
        assertThat(contract.id()).isEqualTo(contractEntity.getId());
    }
}