package Capstone_team1.Jubging.domain.model;

import lombok.Getter;

@Getter
public enum JubgingDataStatus {

    INPROGRESS("INPROGRESS"),
    FINISHED("FINISHED");

    private final String value;

    JubgingDataStatus(String value) {
        this.value = value;
    }
}
