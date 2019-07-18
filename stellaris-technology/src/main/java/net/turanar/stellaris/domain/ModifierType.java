package net.turanar.stellaris.domain;

import net.turanar.stellaris.antlr.StellarisParser.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import static net.turanar.stellaris.Global.*;

public enum ModifierType {
    has_ascension_perk("拥有%s超凡飞升"),
    has_authority("拥有%s政体"),
    has_blocker("拥有£blocker£地块：%s"),
    has_technology("拥有科技：%s"),
    has_valid_civic("拥有民政：%s"),
    has_civic("拥有民政：%s"),
    has_modifier("拥有%s修正"),
    has_ethic("拥有%s思潮"),
    host_has_dlc("主机拥有 DLC %s"),
    has_tradition("拥有%s传统"),
    has_country_flag("拥有%s帝国标识"),
    has_global_flag("拥有%s星球标识"),
    has_deposit("拥有矿藏(deposit)：%s"),
    is_country_type("帝国类型：%s"),
    is_planet_class("是%s"),
    has_communications("与我们帝国有交流"),
    pop_has_trait("种族人口有%s特质"),
    has_policy_flag((p) -> f("拥有政策：%s", i18n(gs(p) + "_name"))),
    owns_any_bypass((p) -> f("控制一个有%s的星系", i18n("bypass_" + gs(p).toLowerCase()))),
    has_seen_any_bypass((p) -> f("遇到过%s", i18n("bypass_" + gs(p).toLowerCase()))),

    is_xenophile(DefaultParser.SCRIPTED),
    is_pacifist(DefaultParser.SCRIPTED),
    is_materialist(DefaultParser.SCRIPTED),
    is_egalitarian(DefaultParser.SCRIPTED),
    is_authoritarian(DefaultParser.SCRIPTED),
    is_militarist(DefaultParser.SCRIPTED),
    is_xenophobe(DefaultParser.SCRIPTED),

    is_spiritualist(DefaultParser.SCRIPTED),
    is_gestalt(DefaultParser.SCRIPTED),
    is_mechanical_empire(DefaultParser.SCRIPTED),
    is_regular_empire(DefaultParser.SCRIPTED),
    is_machine_empire(DefaultParser.SCRIPTED),
    is_hive_empire(DefaultParser.SCRIPTED),
    is_megacorp(DefaultParser.SCRIPTED),
    allows_slavery(DefaultParser.SCRIPTED),
    has_ancrel(DefaultParser.SCRIPTED),

    is_ai("电脑玩家|不是电脑玩家", DefaultParser.SIMPLE_BOOLEAN),

    is_enslaved("种族人口受到奴役|种族人口没有受到奴役", DefaultParser.SIMPLE_BOOLEAN),
    is_sapient("种族人口是智慧生物|种族人口不是智慧生物", DefaultParser.SIMPLE_BOOLEAN),
    has_any_megastructure_in_empire("有任意巨构建筑|不存在巨构建筑",DefaultParser.SIMPLE_BOOLEAN),
    always("总是|从不", DefaultParser.SIMPLE_BOOLEAN),

    years_passed("游戏时间%s %s 年", DefaultParser.SIMPLE_OPERATION),
    num_owned_planets("拥有星球数量 %s %s", DefaultParser.SIMPLE_OPERATION),
    num_communications("外交帝国数量 %s %s", DefaultParser.SIMPLE_OPERATION),
    has_level("等级 %s %s", DefaultParser.SIMPLE_OPERATION),

    any_neighbor_country("任意临近帝国", DefaultParser.CONDITIONAL),
    any_owned_planet("任意拥有星球", DefaultParser.CONDITIONAL),
    any_planet_within_border("任意领土内的星球", DefaultParser.CONDITIONAL),
    any_planet("任意星球", DefaultParser.CONDITIONAL),
    any_owned_pop("任意拥有的种族人口", DefaultParser.CONDITIONAL),
    any_system_within_border("任意领土内的星系", DefaultParser.CONDITIONAL),
    any_system_planet("任意星球", DefaultParser.CONDITIONAL),
    any_relation("任意外交关系",DefaultParser.CONDITIONAL),
    any_pop("任意种族人口", DefaultParser.CONDITIONAL),
    owner_species("发现物种：", DefaultParser.CONDITIONAL),
    no_scope("", DefaultParser.CONDITIONAL),

    NOR("以下条件任何一个都不成立", DefaultParser.CONDITIONAL),
    OR("以下条件至少有一条成立", DefaultParser.CONDITIONAL),
    NAND("以下条件至少有一条不成立", DefaultParser.CONDITIONAL),
    AND("以下条件全部满足", DefaultParser.CONDITIONAL),

    has_trait((p) -> {
        String expertise = i18n(gs(p));
        if(expertise.contains("Expertise: ")) expertise = expertise.replaceAll("Expertise: ","") + "专长";
        return "拥有" + expertise;
    }),
    area((p) -> StringUtils.capitalize(gs(p))),
    research_leader((p) -> {
        String area = "";
        List<String> conditions = new ArrayList<>();
        for(PairContext prop : p.value().map().pair()) {
            Modifier m = visitCondition(prop);
            if(m.type.equals(ModifierType.area)) area = m.toString();
            else conditions.add(m.toString());
        }
        String retval;
        if (area.equals("Physics")){
            retval = "研究领袖 (" + "物理学" + "): ";
        }else if (area.equals("Society")){
            retval = "研究领袖 (" + "社会学" + "): ";
        }else if (area.equals("Engineering")){
            retval = "研究领袖 (" + "工程学" + "): ";
        }else {
             retval = "研究领袖 (" + area + "): ";
        }
        for(int i = 0; i < conditions.size(); i++) {
            retval = retval + "\n" + LS + conditions.get(i);
        }
        return retval;
    }),

    has_resource((p) -> {
        String type = "";
        String count = "";
        for(PairContext prop : p.value().map().pair()) {
            if(prop.key().equals("type")) {
                type = gs(prop);
            } else if (prop.key().equals("amount")) {
                count = op(prop) + " " + gs(prop);
            }
        }
        return "拥有£" + type + "£ " + i18n(type) + "的数量" +  count;
    }),

    count_starbase_sizes((p) -> {
        String retval = "%s的数量%s %s";
        String size = null, operator = null, count = null;
        for(PairContext prop : p.value().map().pair()) {
            if(prop.key().equals("starbase_size")) {
                size = i18n(gs(prop));
            } else if (prop.key().equals("count")) {
                operator = op(prop);
                count = gs(prop);
            }
        }
        return String.format(retval, size, operator, count);
    }),

    num_districts((p)->{
        String type = "";
        String count = "";
        for(PairContext prop : p.value().map().pair()) {
            if(prop.key().equals("type")) {
                type = i18n(gs(prop));
            } else if (prop.key().equals("value")) {
                count = op(prop) + " " + gs(prop);
            }
        }
        return type + "的数目" + count;
    }),

    count_owned_pops((p) -> {
        String limits = "";
        String count = "";
        for(PairContext prop : p.value().map().pair()) {
            if(prop.key().equals("limit")) {
                for(PairContext l : prop.value().map().pair()) {
                    Modifier m = visitCondition(l);
                    limits += "\n" + LS + m.toString();
                }
            } else if(prop.key().equals("count")) {
                count = op(prop) + " " + gs(prop);
            }
        }
        return "拥有人口数量" + count + limits;
    }),

    NOT((p) -> {
        if(p.value().map().pair().size() > 1) return NOR.parser.apply(p);
        Modifier m = visitCondition(p.value().map().pair().get(0));
        if(m.type.equals(OR)) return NOR.parser.apply(p.value().map().pair().get(0));

        String retval = m.toString();
        if(retval.startsWith("拥有")) {
            return "无" + retval.replaceFirst("拥有", "");
        }
        else if(retval.startsWith("是")) {
            return "不是" + retval.replaceFirst("是","");
        }  else if(retval.startsWith("任意")) {
            return "该条件取反： " + retval;
        } else {
            return "该条件取反： " + retval;
        }
    }),

    DEFAULT((p) -> {
        String retval = p.getText();
        System.out.println(retval);
        return retval;
    })
    ;

    private static enum DefaultParser {
        SIMPLE((format,p) -> String.format(format,i18n(gs(p.value())))),
        SIMPLE_OPERATION((format,p) -> String.format(format, op(p), gs(p))),
        SIMPLE_BOOLEAN((format,p) -> {
            String[] sentence = format.split("\\|");
            if(gs(p).equals("yes")) return sentence[0]; else return sentence[1];
        }),
        CONDITIONAL((format, p) -> {
            List<String> conditions = new ArrayList<>();

            for(PairContext prop : p.value().map().pair()) {
                Modifier m = visitCondition(prop);
                conditions.add(m.toString());
            }

            String retval = format;
            for(int i = 0; i < conditions.size(); i++) {
                retval = retval + "\n" + LS + conditions.get(i).replaceAll(LS, "\t" + LS);
            }
            return retval;
        }),
        SCRIPTED((format, p) -> {
            PairContext q = GLOBAL_TRIGGERS.get(p.key());
            boolean value = gs(p).equals("yes");
            List<String> conditions = new ArrayList<>();

            if(!value) {
                return ModifierType.NOT.parse(q);
            }

            for(PairContext prop : q.value().map().pair()) {
                Modifier m = visitCondition(prop);
                conditions.add(m.toString());
            }
            String retval = format;

            if(conditions.size() < 2) {
                retval = conditions.get(0);
                return retval;
            }

            for(int i = 0; i < conditions.size(); i++) {
                retval = retval + "\n" + LS + conditions.get(i).replaceAll(LS, "\t" + LS);
            }
            return retval;
        });

        private BiFunction<String, PairContext, String> parser;

        private DefaultParser(BiFunction<String, PairContext,String> parser) {
            this.parser = parser;
        }

        public String apply(String format, PairContext pair) {
            return this.parser.apply(format, pair);
        }
    }

    private Function<PairContext,String> parser;

    ModifierType(String format, DefaultParser parser) {
        this.parser = (p) -> parser.apply(format, p);
    }

    ModifierType(DefaultParser parser) {
        this.parser = (p) -> parser.apply(null, p);
    }

    ModifierType(Function<PairContext,String> parser) {
        this.parser = parser;
    }

    ModifierType(String format) {
        this(format, DefaultParser.SIMPLE);
    }

    public String parse(PairContext pair) {
        return parser.apply(pair);
    }

    public static ModifierType value(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return DEFAULT;
        }
    }

    public static Modifier visitCondition(PairContext pair) {
        Modifier retval = new Modifier();
        retval.type = ModifierType.value(pair.key());
        retval.pair = pair;
        return retval;
    }
}
