package net.turanar.stellaris.domain;

public enum GameObject {
    SB_BUILDING("太空基地建筑", "common/starbase_buildings", "sm_"),
    ARMY("军队", "common/armies"),
    COMPONENT("部件","common/component_templates"),
    EDICTS("帝国法令", "common/edicts","edict_"),
    BUILDING("建筑", "common/buildings"),
    STRAT_RESS("解锁资源","common/strategic_resources"),
    SB_MODULE("太空基地模块", "common/starbase_modules","sm_"),
    POLICY("政策","common/policies","policy_"),
    DECISION("决策","common/decisions","","02_special_decisions.txt"),

    //BUILDABLE_POP("Buildable Pop","common/buildable_pops"),
    //TILE_BLOCKER("Clear Blockers", "common/tile_blockers"),
    SHIP_SIZE("飞船尺寸", "common/ship_sizes","","00_ship_sizes.txt"),
    STARBASE("太空基地升级", "common/ship_sizes","","00_starbases.txt"),
    MEGASTRUCTURE("巨构建筑", "common/megastructures");


    public String folder;
    public String label;
    public String locale_prefix = "";
    public String filter = ".txt";

    GameObject(String label, String folder) {
        this.folder = folder;
        this.label = label;
        this.locale_prefix = "";
    }

    GameObject(String label, String folder, String locale_prefix) {
        this.folder = folder;
        this.label = label;
        this.locale_prefix = locale_prefix;
    }


    GameObject(String label, String folder, String locale_prefix, String filter) {
        this.folder = folder;
        this.label = label;
        this.locale_prefix = locale_prefix;
        this.filter = filter;
    }
}
