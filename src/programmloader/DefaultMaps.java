package programmloader;

import java.io.File;

/**
 * Created by manuelesberger on 19/12/15.
 */
public enum DefaultMaps {
    SQUARES("squares.map"),
    AFRICA("africa.map"),
    THREE_CONTINENTS("three-continents.map"),
    WORLD("world.map");

    private String folder = "res" + File.separator;

    private String value;

    DefaultMaps(String value) {
        this.value = this.folder + value;
    }

    public String getValue() {
        return value;
    }
}
