package net.turanar.stellaris.domain;

import com.google.gson.annotations.SerializedName;

public enum Category {
    // physics
    Particles,粒子技术, @SerializedName("Field Manipulation") Field_Manipulation,力场控制, Computing,计算技术,
    // society
    Biology,生物科技, @SerializedName("Military Theory") Military_Theory,军事理论, @SerializedName("New Worlds") New_Worlds,殖民学说, Statecraft,治国之术, Psionics,心灵异能,
    // engineering
    Industry,工业技术, Materials,材料科学, Propulsion, 推进技术,Voidcraft,太空技术;

    public static Category eval(String name) {

        name = name.replaceAll(" ","_");
        return Category.valueOf(name);
    }
}
