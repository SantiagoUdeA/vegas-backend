package com.vegas.sistema_gestion_operativa.unit.branches.factory;

import com.vegas.sistema_gestion_operativa.branches.domain.entity.Branch;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FakeBranchFactory {

    private final Faker faker;

    @Autowired
    public FakeBranchFactory(Faker faker) {
        this.faker = faker;
    }

    public Branch createBranch() {
        return Branch.builder()
                .name(faker.name().firstName())
                .address(faker.address().fullAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build();
    }
}
