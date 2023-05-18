package com.gogame.model;

public class SaveGameInitiator {
    //region Constants
    private final String METADATA_REGEX = "\\d(\\d)?;\\d;[0-7]\\.0|5";
    private final String PASS_REGEX = "Black|White player passed.";
    private final String MOVE_REGEX = "\\d(\\d)?;\\d(\\d)?- (White)|(Black)";
    private final String HANDICAP_REGEX = "\\d(\\d)?;\\d(\\d)?- Place handicap stones.";
    //endregion

    public SaveGameInitiator() {

    }

}
