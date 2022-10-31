package Capstone_team1.Jubging.domain.model;

import lombok.Getter;

@Getter
public enum JubgingDataStatus {

    INPROGRESS("INPROGRESS"),
    FINISHED("FINISHED"),
    DONE("DONE");

    private final String value;

    JubgingDataStatus(String value) {
        this.value = value;
    }
}
