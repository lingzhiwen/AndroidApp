package com.ling.common.font;

/**
 * Created by ling on 2020/08/13 13:38
 */
public enum Font {

    ROBOTO_MEDIUM("Roboto-Medium.ttf"),
    SOURCEHANSANSCN_MEDIUM("SourceHanSansCN-Medium.otf");

    private final String name;

    Font(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
