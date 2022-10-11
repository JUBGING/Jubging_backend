package Capstone_team1.Jubging.domain.model;

import lombok.Getter;

@Getter
public enum JubjubiStatus {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String value;

    JubjubiStatus(String value) {
        this.value = value;
    }
}
