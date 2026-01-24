package com.renyigesai.immortalers_delight.data;

import com.google.gson.JsonObject;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.api.annotation.ItemData;
import com.renyigesai.immortalers_delight.compat.init.Ltc2Items;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightEntities;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Languages extends LanguageProvider {

    private final Map<String, String> en_us = new TreeMap<>();
    private final Map<String, String> zh_cn = new TreeMap<>();
    private final PackOutput output;
    private final String modid = ImmortalersDelightMod.MODID;
    private final String locale;

    private static final String ADVANCEMENT = "advancements.immortalers_delight.";
    private static final String CONTAINER = "container.immortalers_delight.";
    private static final String TOOLTIP = "tooltip.immortalers_delight.";
    private static final String IS_COLORFUL = "colorful.";
    private static final String MESSAGE = "message.immortalers_delight.";
    private static final String FARMERSDELIGHT_TOOLTIP = "farmersdelight.tooltip.";
    private static final String ENTITY = "entity.immortalers_delight.";

    public Languages(PackOutput output, String locale) {
        super(output, ImmortalersDelightMod.MODID, locale);
        this.output = output;
        this.locale = locale;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.addTranslations();
        if (this.locale.equals("en_us") && !en_us.isEmpty())
            return save(en_us,cache, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modid).resolve("lang").resolve(this.locale + ".json"));
        if (this.locale.equals("zh_cn") && !zh_cn.isEmpty())
            return save(zh_cn,cache, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modid).resolve("lang").resolve(this.locale + ".json"));
        return CompletableFuture.allOf();
    }

    private CompletableFuture<?> save(Map<String,String> data,CachedOutput cache, Path target) {
        JsonObject json = new JsonObject();
        data.forEach(json::addProperty);
        return DataProvider.saveStable(cache, json, target);
    }

    @Override
    protected void addTranslations() {
        try {
            addItems();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        addLatiao();
        addTooltips();
        addMessages();
        addContainers();
        addEffects();
        addEntitys();
        addAdvancements();
        adds();
    }

    private void addItems() throws IllegalAccessException {
        Class<ImmortalersDelightItems> _class = ImmortalersDelightItems.class;
        for (Field field : _class.getDeclaredFields()) {
            if (field.isAnnotationPresent(ItemData.class)) {
                Object object = field.get(null);
                RegistryObject<Item> deferredItem = null;
                if (object instanceof RegistryObject<?> registryObject) {
                    if (Item.class.isAssignableFrom(registryObject.get().getClass())){
                        deferredItem = (RegistryObject<Item>) registryObject;
                    }
                }
                if (deferredItem != null) {
                    ItemData itemData = field.getAnnotation(ItemData.class);
                    if (itemData != null) {
                        String zh = itemData.zhCn();
                        String en = itemData.enUs();
                        if (itemData.itemType() == ItemData.ItemType.ITEM) {
                            if (en.isEmpty()) {
                                addItem(deferredItem, zh);
                            } else {
                                addItem(deferredItem, en, zh);
                            }
                        }
                    }
                }
            }
        }
    }

    private void addLatiao(){
        addItem(Ltc2Items.EVOLUTCORN_POWDER,"白垩玉黎粉");
        addItem(Ltc2Items.EVOLUTCORN_LATIAO,"白垩玉黎辣条");
        addItem(Ltc2Items.RARE_EVOLUTCORN_LATIAO,"白垩玉黎辣条");
        addItem(Ltc2Items.SUPERIOR_EVOLUTCORN_LATIA,"白垩玉黎辣条");
        addItem(Ltc2Items.DELICACY_EVOLUTCORN_LATIAO,"白垩玉黎辣条");
        addItem(Ltc2Items.TREASURE_EVOLUTCORN_LATIAO,"白垩玉黎辣条");

        addItem(Ltc2Items.KWAT_WHEAT_LATIAO,"瓦斯麦辣条");
        addItem(Ltc2Items.RARE_KWAT_WHEAT_LATIAO,"瓦斯麦辣条");
        addItem(Ltc2Items.SUPERIOR_KWAT_WHEAT_LATIAO,"瓦斯麦辣条");
        addItem(Ltc2Items.DELICACY_KWAT_WHEAT_LATIAO,"瓦斯麦辣条");
        addItem(Ltc2Items.TREASURE_KWAT_WHEAT_LATIAO,"瓦斯麦辣条");

        addItem(Ltc2Items.MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO,"果酱毒薯泥辣条");
        addItem(Ltc2Items.RARE_MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO,"果酱毒薯泥辣条");
        addItem(Ltc2Items.SUPERIOR_MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO,"果酱毒薯泥辣条");
        addItem(Ltc2Items.DELICACY_MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO,"果酱毒薯泥辣条");
        addItem(Ltc2Items.TREASURE_MASHED_POISONOUS_POTATO_WITH_JAM_LATIAO,"果酱毒薯泥辣条");

        addItem(Ltc2Items.IMMORTALERS_LATIAO_LUCKY_BAG,"千古辣条福袋");
        addItem(Ltc2Items.HAKO_LATIAO,"「小盒子」的辣条");
        addItem(Ltc2Items.WINDY_NARRATOR_LATIA,"「Windy Narrator」的辣条");
        addItem(Ltc2Items.MOASWIES_LATIAO,"「Moaswies Latiao」的辣条");
        addItem(Ltc2Items.RENYIGESAI_LATIAO,"「人一个噻」的辣条");
        addItem(Ltc2Items.LYZ_DELIGHT_LATIAO,"「LYZ」的辣条");
        addItem(Ltc2Items.XIAOSUHUAJI_LATIAO,"「小苏滑稽」的辣条");
        addItem(Ltc2Items.TELLURIUM_LATIAO,"「Tellurium」的辣条");
        addItem(Ltc2Items.KA_QKO_LATIAO,"「KaQko」的辣条");
        addItem(Ltc2Items.BEI_DOU_LATIAO,"「北斗·神」的辣条");
        addItem(Ltc2Items.DOUGER_LATIAO,"「多格」的辣条");
    }

    private void addTooltips(){
        createTooltip("book_progress","修书进度: %d ‰","Repair progress: %d ‰");
        createTooltip("charged_projectiles","剩余 %d 发","Remaining ammunition: %d .");
        createTooltip("is_immortalers_knives","古制刀","Ancient-style Knife");
        createTooltip("is_immortalers_hammers","松肉锤","Meat Tenderizer");
        createTooltip("hammers_skill_title_1","势大力沉","Powerful and Heavy");
        createTooltip("hammers_skill_1","全力攻击可短暂眩晕目标","A full-powered attack can briefly stun the target.");
        createTooltip("hammers_skill_title_2","松筋嫩肉","Tenderize Meat");
        createTooltip("hammers_skill_2","连续攻击以造成更多伤害","Continuous attacks deal higher damage.");
        createTooltip("is_can_feed","可以喂食以整蛊其他生物","Can be fed to fool other mobs.");
        createTooltip("potion_coating_count","药水附层：%d / %d","Potion Coating：%d / %d");
        createTooltip("power_battle_mode_hint","千古乐事：您已开启§9超凡模式§f，如需更改请在§9immortalers_delight-common.toml§f中修改§9powerBattleMode§f。修改§9powerBattleModeHint§f以禁用该提示。","Immortaler`s Delight:You have enabled §9Power Battle Mode§f，If you need to make any changes, please go to §9immortalers_delight-common.toml§f Modified in the middle §9powerBattleMode§f.Modify§9powerBattleModeHint§fDisable this prompt.");
        createTooltip("super_kwat_burger","不同寻常的分量","Exceptional Mass");
        createTooltip("frosty_crown_mousse","严重冻结食用者——心急吃不了冻豆腐。","Severely Freezes the Consumer — Haste Makes Waste with Frozen Tofu.");
        createTooltip( IS_COLORFUL+ "frosty_crown_mousse_slice","冻结食用者","Freezes the Consumer");
        createTooltip(IS_COLORFUL+ "kwat_soup","对食用者造成巨量伤害，随后快速回复饱食度。","Inflicts massive damage to the consumer, then rapidly restores saturation.");
        createTooltip(IS_COLORFUL+ "iced_kwat_soup","将灼热效果转化为凉爽效果。","Converts Scorching effect into a Cooling effect.");
        createTooltip(IS_COLORFUL+ "bowl_of_this_side_down","完全失重状态下你难以施力。","Unable to exert force in full weightlessness.");
        createTooltip("can_place_on_plate","可放置于空盘上","Placeable on empty plates.");
        createTooltip("spoon","将碗放置为空盘子","Place an bowl item with empty plate block.");
        createTooltip("nan_dough","使用岩浆块覆盖以进行烤制","Cover with magma blocks for baking.");

        createFarmersdelightTooltip("large_column","2x Damage Block; Drops edible shards on block.","格挡两次伤害，格挡伤害时产生可食用的碎片");
        createFarmersdelightTooltip("ku_mesh_non","Exceptional Mass","不同寻常的分量");
        createFarmersdelightTooltip("hong_mei_ling","You've gained new insight into using Qi...","你对“用气”有了新的理解……");
        createFarmersdelightTooltip("caustic_essential_oil","Releases a persistent Crimson-Ember Haze upon shattering, whose edge-borne scent can clear a Sniffer's nasal passages.","破裂时产生持久的绯烬尘霾，其边缘处的气味可以为嗅探兽疏通鼻腔。");
        createFarmersdelightTooltip("bizarre_sausage","Your dog wants this.","你的狗狗想要这个.");
        createFarmersdelightTooltip("vara_ji","Perhaps one needs to sip it through a straw.","也许需要一个吸管过滤着喝。");
        createFarmersdelightTooltip("pearlip_beer","Perhaps one needs to sip it through a straw.","也许需要一个吸管过滤着喝。");
        createFarmersdelightTooltip("iced_kwat_soup","Converts Scorching effect into a Cooling effect.","将灼热效果转化为凉爽效果。");

        createFarmersdelightTooltip("elixir_of_immortality","[Unique] It can only be used once in each game. The effect will deteriorate when used again thereafter.","[独一]在每场游戏中仅能发挥一次效果，此后再次使用时效果将劣化。");
        createFarmersdelightTooltip("golden_himekaido","After consumption, immunizes and reverses harmful effects below grade II","食用后，免疫并反转 II 级以下的有害效果");
        createFarmersdelightTooltip("enchanted_golden_himekaido","Within 1 second after consumption, be immune to harmful effects below level V (including instant effects).","食用后 1 秒内，免疫并反转 V 级以下的有害效果(包括瞬时效果)");
        createFarmersdelightTooltip("golden_fabric","Made a veil to protect against the burning dust and haze","制成面纱以抵御绯烬尘霾");
        createFarmersdelightTooltip("leisamboo_tea_cake","Clear all the effects and then apply the night vision effect","清除所有效果，然后给予夜视效果");
        createFarmersdelightTooltip("pearlip_bubble_milk","Remove all effects, then grant this effect:","清除所有效果，然后给予:");
        createFarmersdelightTooltip("sachets","Attract the Sniffer.","吸引嗅探兽");
        createFarmersdelightTooltip("evolutcorn_hard_candy","When you focus on eating this candy, you will ignore a certain amount of damage.","当你专注于吃糖，你将忽略一定的伤害");
        createFarmersdelightTooltip("drill_rod_wand.0","Can be used to break [trap blocks].","可用于破拆[陷阱]方块");
        createFarmersdelightTooltip("drill_rod_wand.1","Counter hard monsters.","克制坚硬的目标");
        createFarmersdelightTooltip("drill_rod_wand.2","Consume the [enchanting fuels] in hand to offset durability reduction.","消耗手上的魔力原料抵消耐久减扣");
        createFarmersdelightTooltip("drill_rod_wand.3","The operation is unstable and using it may cause danger. Please use it with caution.","运行不稳定，使用可能发生危险，请慎用");
        createFarmersdelightTooltip("ancient_wood_boat.ancient_wood","Use 5 logs for repair and 2 sticks to transform into a transport ship","一艘破船……用 5个原木 与 2个木棍 修缮。");
        createFarmersdelightTooltip("ancient_wood_chest_boat.ancient_wood","Use 5 logs for repair and chest to transform into a transport ship","一艘破船……用 5个原木 与 1个箱子 修缮。");
        createFarmersdelightTooltip("golden_kwat_toast","Gift the Piglin Brute to gain the Bastion's allegiance.","赠与猪灵蛮兵以换取堡垒的拥护");
        createFarmersdelightTooltip("golden_kwat_toast_slice","Eat in front of Piglins to make them awe.","在猪灵面前食用以令其敬畏");
        createFarmersdelightTooltip("rusty_ancient_blade","Let your enemies die from sepsis.","让你的敌人死于败血");
        createFarmersdelightTooltip("ancient_blade","Wield dual weapons to unleash greater power.","双持以发挥更强威力");
        createFarmersdelightTooltip("ancient_blade.0"," "," ");
        createFarmersdelightTooltip("ancient_blade.1","Wield dual weapons:","在双持时：");
        createFarmersdelightTooltip("ancient_blade.2"," 10.5 Attack Damage"," 10.5 攻击伤害");
        createFarmersdelightTooltip("ancient_blade.power"," 160% Attack Damage"," 160% 攻击伤害");
        createFarmersdelightTooltip("drink_block_item","Can be placed","可放置");
        createFarmersdelightTooltip("pillager_knife","Apply to the target: ","施加于目标: ");
        createFarmersdelightTooltip("pillager_knife.default_enchantment.1","Looting II","抢夺 II");
        createFarmersdelightTooltip("pillager_knife.default_enchantment.power.1","Looting IV","抢夺 IV");
        createFarmersdelightTooltip("bone_knife","Charge up to deal more damage.","蓄力以造成更多伤害");
        createFarmersdelightTooltip("bone_knife.1","Looting -I","抢夺 -I");
        createFarmersdelightTooltip("spoon","You need a %s to eat it.","你需要一个%s以食用它。");

    }

    private void addContainers(){
        createContainer("enchantal_cooler","Enchantal Cooler","魔凝机");
        createContainer("hot_spring","Hot Spring","温泉");
    }


    private void addEffects(){
        createEffect(ImmortalersDelightMobEffect.GAIXIA.get(),"垓下");
        createEffect(ImmortalersDelightMobEffect.INCANDESCENCE.get(),"灼热","Strength effects can be obtained or prolonged while eating. In Extraordinary Mode, attacks can set enemies on fire.","进食时可以获得或延长力量效果。超凡模式下，攻击能使敌人着火。");
        createEffect(ImmortalersDelightMobEffect.WEAK_POISON.get(),"弱中毒");
        createEffect(ImmortalersDelightMobEffect.WEAK_WITHER.get(),"弱凋零");
        createEffect(ImmortalersDelightMobEffect.RELIEVE_POISON.get(),"解毒");
        createEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_UNDEAD.get(),"尸毒抵抗");
        createEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ARTHROPOD.get(),"虫毒抵抗");
        createEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ABYSSAL.get(),"渊毒抵抗");
        createEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_ILLAGER.get(),"厄毒抵抗");
        createEffect(ImmortalersDelightMobEffect.RESISTANCE_TO_SURROUNDINGS.get(),"醇厚");
        createEffect(ImmortalersDelightMobEffect.KEEP_A_FAST.get(),"俭省");
        createEffect(ImmortalersDelightMobEffect.MAGICAL_REVERSE.get(),"奇迹般的转变");
        createEffect(ImmortalersDelightMobEffect.GAS_POISON.get(),"绯烬烈灼");
        createEffect(ImmortalersDelightMobEffect.INEBRIATED.get(),"酩酊");
        createEffect(ImmortalersDelightMobEffect.BURN_THE_BOATS.get(),"破釜");
        createEffect(ImmortalersDelightMobEffect.CULTURAL_LEGACY.get(),"文化底蕴");
        createEffect(ImmortalersDelightMobEffect.VITALITY.get(),"丰茂");
        createEffect(ImmortalersDelightMobEffect.SATIATED.get(),"补益");
        createEffect(ImmortalersDelightMobEffect.WARM_CURRENT_SURGES.get(),"燃起来了");
        createEffect(ImmortalersDelightMobEffect.PREHISTORIC_POWERS.get(),"洪荒之力");
        createEffect(ImmortalersDelightMobEffect.COOL.get(),"凉爽");
        createEffect(ImmortalersDelightMobEffect.DEEPNESS.get(),"渊默");
        createEffect(ImmortalersDelightMobEffect.ESTEEMED_GUEST.get(),"上宾");
        createEffect(ImmortalersDelightMobEffect.LINGERING_INFUSION.get(),"茶韵");
        createEffect(ImmortalersDelightMobEffect.UP_SIDE_DOWN.get(),"拉格朗日");
        createEffect(ImmortalersDelightMobEffect.LET_IT_FREEZE.get(),"冻结吧！");
        createEffect(ImmortalersDelightMobEffect.UNYIELDING.get(),"坚韧");
        createEffect(ImmortalersDelightMobEffect.SMOKE_ABSTINENCE.get(),"破烟");
        /*药水效果描述*/
        add("effect.immortalers_delight.weak_wither.description","Less harmful decay, less damage and no death. Gives 1 blight damage every 50 ticks, doubles each level, stops damage when health is less than 1, and does not make health less than 1.","更低危害的中毒，伤害更低且不会使得生命值低于50%。每40tick将给予1点魔法伤害，每级伤害值翻倍，在生命不大于生命上限的50%时会停止伤害，且该伤害不会令生命值低于50%。");
        add("effect.immortalers_delight.relieve_poison.description","Dissolves the toxic effects of lower levels, turning lower levels of decay into weak decay.","解除等级更低的中毒效果，将高等级的中毒效果转变为弱中毒，将凋零效果转变为弱凋零。超凡模式下，免疫中毒与弱中毒，凋零效果转变为弱凋零时等级降低，持续时间减少。");
        add("effect.immortalers_delight.resistance_to_undead.description","Reduced damage caused by undead creatures, about 25% per level, not 100%","来自亡灵生物的伤害减至 [效果等级+1] 分之一。超凡模式下，受到亡灵生物伤害时会派生丰茂效果。");
        add("effect.immortalers_delight.resistance_to_arthropod.description","Reduced damage caused by arthropod creatures, the proportion of damage reduction is about 25% per level, not 100%. Corresponding to Arthropod Killer: Damage taken by arthropod creatures spawners a short Speed IV effect.","来自节肢生物的伤害减至 [效果等级+1] 分之一。在受到节肢生物伤害时会派生迅捷IV效果。超凡模式下，受到节肢生物伤害时还会派生解毒IV效果。");
        add("effect.immortalers_delight.resistance_to_abyssal.description","Reduced damage caused by aquatic organisms, the proportion of damage reduction is about 25% per level, not 100%. A short period of underwater breathing is derived when damaged by aquatic organisms.","来自水生生物的伤害减至 [效果等级+1] 分之一。在受到水生生物伤害时会派生舒适、水下呼吸和海豚的恩惠效果。超凡模式下，受到水生生物伤害时派生的效果变为瞬间治疗、潮涌能量和海豚的恩惠。");
        add("effect.immortalers_delight.resistance_to_illager.description","Relief of damage caused by disaster villagers, the proportion of damage reduction of about 25% per level, will not reach 100%.\\nDid you know: the peeved ghost does not belong to the villagers, although it will not be accidentally injured by the villagers","来自灾厄村民的伤害减至 [效果等级+1] 分之一。超凡模式下，受到灾厄村民伤害时会派生抗性提升效果。\n你知道吗：恼鬼不属于灾厄村民，尽管它不会被灾厄村民误伤。");
        add("effect.immortalers_delight.magical_reverse.description","Remove and attempt to reverse negative effects of a lower rank (reverses only apply to original and Farmer's Delight effects). The duration will not be extended.","移除并尝试反转等级更低的负面效果（可通过配置文件自定义）。持续时间不会被延长。");
        add("effect.immortalers_delight.gas_poison.description","Cannot be removed by milk. Every 32tick forces a 6% Max Health reduction, randomly spawns one of the following effects per second: blindness, nausea, weakness, slowness, hunger. The derived effect cannot be reversed by milk. Damage to monsters is limited to 1.2 per attack.","不可被牛奶解除。每32tick强制减扣6%最大生命值并随机派生下列的一项效果：失明、反胃、虚弱、缓慢、饥饿。派生的效果也不可被牛奶解除。");
        add("effect.immortalers_delight.inebriated.description","Cannot be removed by milk. This parameter does not take effect when the duration is less than 3 minutes. Deal true damage of 8% Max health per 64tick, which does not stop damage below 8% health and stops damage when creatures fall below 8% health. All of the following effects: blindness, nausea, weakness, slowness, poisoning. The derived effect cannot be reversed by milk. Damage to monsters is limited to 1.6 per attack.\nDid you know: drinking to get this effect will add up the time of the effect rather than taking the longest, please moderate alcohol consumption.","不可被牛奶解除。在持续时长为3分钟以下时不生效。每64tick造成8%最大生命值的真实伤害并派生下列的所有效果：失明、反胃、虚弱、缓慢、弱中毒。派生的效果也不可被牛奶解除。\n你知道吗：饮酒获得该效果会使得该效果的时间叠加而不是取最长，请节制饮酒。");
        add("effect.immortalers_delight.keep_a_fast.description","When this effect is achieved, the player's hunger is reduced to half of the original value, and the cost of hunger is reduced to one-tenth of [effect level +1] during its duration (similar to Stamina enchantment, does not affect actions such as regenerating health). Restores (doubles) the player's hunger at the end of the effect, and the spilled hunger is reclaimed to immediately restore the player's life, the percentage of the recovery increases with the effect level.","获得该效果时将使得玩家的饥饿值与饱和度被压缩到原有值的一半，且持续期间饥饿值的消耗将降为［效果等级+1］分之一（与耐久附魔相似，不影响回复生命等行为）。在效果结束时使玩家的饥饿值与饱和度复原（翻倍），溢出的饥饿值与饱和度将回收以立即为玩家恢复生命。超凡模式下，会消耗饥饿值与饱和度恢复生命值。");
        add("effect.immortalers_delight.burn_the_boats.description","Grant Power III, Speed III, Urgency III, Jump III, and Resistance III for 15 seconds when health falls below a certain percentage. Causes the health bar to be partially covered with shields and flaming swords, and the covered part marks the health that triggers the effect","在生命值低于一定比例时，给予生物15秒力量III、迅捷III、急迫III、跳跃提升III、抗性提升III。使得生命条部分被盾牌与燃火的剑覆盖，覆盖的部分标记了触发该效果的生命值。超凡模式下，给予的效果等级进一步提升；遭受致命攻击时，可以消耗破釜效果免疫此次伤害。");
        add("effect.immortalers_delight.cultural_legacy.description","Increase the Enchantment level of the Enchantment table when near the Enchantment Table, and gain a certain amount of experience at the end of the effect.\nIt must take effect within the detection range of the bookshelf attached to the magic table, and the detection is about once every 4 seconds. A level 1 effect increases the enchantment level by 8 levels, and then each level increases the enchantment level by 4 levels, with a maximum effect of 8 levels, that is, a maximum of 36 enchantment levels.\n\nThis effect also allows the first and second enchantment options of the Enchanted table to reach level 30 (4 effects are required, Level 2 effects only allow the second enchantment to reach level 30), making it possible to consume level 2 or even level 1 for level 30 enchantment.","手持附魔书时，消耗经验值累积进度。消耗1000点进度有几率提升附魔等级，可循环提升至原版最高等级。超凡模式下，附魔的等级上限提升至原版2倍。");
        add("effect.immortalers_delight.warm_current_surges.description","Warm Current Surges.","解除等级更低的缓慢效果。清除脚下的的雪和细雪。攻击时额外造成一次 火焰伤害，若目标处于着火状态，造成的伤害翻倍。超凡模式下，火焰伤害进一步提升。");
        add("effect.immortalers_delight.resistance_to_surroundings.description","Reduce the environmental damage received (such as freezing damage from fine snow, flame damage from fire, cactus damage, drowning damage, fall damage, etc.) to a minimum of 0.","减少所受环境伤害（如细雪的冰冻伤害、着火的火焰伤害、仙人掌伤害、溺水伤害、摔落伤害等），最低减为0。");
        add("effect.immortalers_delight.vitality.description","Restore health points when the health points are insufficient.","生命值不满时恢复生命值。");
        add("effect.immortalers_delight.satiated.description","Every 40 ticks, 1tick of saturation effect is derived. In Extraordinary Mode, every 20 ticks, 1tick of saturation effect is derived. When the hunger value or saturation is greater than or equal to 20, health points are restored","每40tick派生1tick饱和效果。超凡模式下，每20tick派生1tick饱和效果，饥饿值或饱和度大于等于20时，恢复生命值。");
        add("effect.immortalers_delight.lingering_infusion.description","Reduce attack damage. Restore health points when the hunger value is high. In the Extraordinary mode, the recovery amount is further increased.","降低攻击伤害。高饥饿值时恢复生命值。超凡模式下，恢复量进一步提升。");
        add("effect.immortalers_delight.cool.description","The effect of enhancing resistance can be obtained or prolonged when eating. In Extraordinary Mode, gain additional damage reduction. If in a state of fire, the damage reduction effect will be significantly enhanced.","进食时可以获得或延长抗性提升效果。超凡模式下，获得额外减伤，若处于着火状态，减伤效果大幅度提升。");
        add("effect.immortalers_delight.esteemed_guest.description","The Pig spirit and the Pig spirit barbarians will not attack on their own initiative. When your health is low and you are attacked, it will consume the guest effect and summon pig spirits and barbarian soldiers to assist in the battle. The summoned pig spirit barbarian soldiers are immune and zombie-like. In the Extraordinary Mode, the summoned pig spirit barbarian will come with a Lower Realm Alloy Axe and a Lower Realm Alloy sleeve.","猪灵和猪灵蛮兵不会主动攻击。低生命值时受到攻击会消耗上宾效果召唤猪灵蛮兵帮助战斗。召唤的猪灵蛮兵免疫僵尸化。超凡模式下，召唤的猪灵蛮兵会自带下界合金斧、下界合金套。");
        add("effect.immortalers_delight.prehistoric_powers.description","Causes half of the Strength effect to additionally apply based on its pre-combat-update potency, or all of it in Legendary mode.","令一半的力量效果额外按战斗更新前的效果生效，超凡模式下则为全部的力量效果。");
        add("effect.immortalers_delight.deepness.description","Grant the attacker a weakness effect when taking damage. In Extraordinary Mode, gain additional damage reduction and health recovery.","受到伤害时给予攻击者虚弱效果。超凡模式下，获得额外减伤和生命恢复。");
        add("effect.immortalers_delight.gaixia.description","When attacking, a sticky cube is generated under the target creature","攻击时在目标生物下生成黏液方块。");
        add("effect.immortalers_delight.up_side_down.description","When attacking, a sticky cube is generated under the target creature","持续产生漂浮效果，在潜行时则将其变为缓降。");
        add("effect.immortalers_delight.let_it_freeze.description","The attack causes the target to briefly enter a cold state, reducing its speed and continuously subjecting it to frostbite. In non-supernatural mode, the target cannot be completely frozen.","攻击使目标短暂进入寒冷状态，降低速度并持续受到冻伤，非超凡模式下不能完全冻结目标。");
        add("effect.immortalers_delight.unyielding.description","When receiving active damage, you will gain a short period of invincibility. This effect also applies to environmental damage in the extraordinary mode.","受到有源伤害时获得短暂的无敌时间，超凡模式下也对环境伤害生效。");
        add("effect.immortalers_delight.smoke_abstinence","When receiving active damage, you will gain a short period of invincibility. This effect also applies to environmental damage in the extraordinary mode.","阻止幻翼生成，在下界时：获得力II，抗性提II，急迫III，生命回复和抗火。");

    }

    private void addAdvancements(){
        createAdvancement("immortalers_delight",translateText("Immortalers Delight","千古乐事"),translateText("An ancient taste, waiting to be unearthed.","远古之味，静待出土。"));
        createAdvancement("wundor_forbodaeppel",translateText("Wundor Forbodaeppel","乐园禁果"),translateText("Get the miracle seed from the forest.","获得来自森林的奇迹之籽。"));
        createAdvancement("what_s_this_have_a_eat",translateText("What s This Have a Eat","这是什么？吃一下"),translateText("Eat the roasted poisonous potatoes.","吃下烤毒马铃薯。"));
        createAdvancement("treow_meregrot",translateText("Treow Meregrot","木上蚌珠"),translateText("Get a black jewel from the jungle.","获得来自丛林的黑色宝珠。"));
        createAdvancement("the_eternal_flow",translateText("The Eternal Flow","千古一流"),translateText("Get Contains Tea Leisamboo at the Biver.","在河流获得含茶竹节。"));
        createAdvancement("the_ability_to_trigger_miracles",translateText("The Ability to Trigger Miracles","引发奇迹的能力！"),translateText("Get Enchanted Golden Himekaido.","获得金魔法果。"));
        createAdvancement("sniffer_move",translateText("Sniffer Move","嗅探兽,移动！"),translateText("Use Sachets to accelerate the movement of Sniffer.","利用谷物香囊加速嗅探兽移动。"));
        createAdvancement("old_teahouse",translateText("Old Teahouse","老茶馆"),translateText("Placed Leaf Tea.","放置树叶茶。"));
        createAdvancement("it_is_more_unusual_than_unusual_food_s_delight",translateText("Stranger than Unusual food's Delight","这比奇食乐事更懂奇食"),translateText("So immortalers delight is the strangest meaning of delight through the ages?","原来千古乐事是千古以来最奇怪的乐事的意思吗？"));
        createAdvancement("incandescence_and_power",translateText("Angry","红温了"),translateText("Obtain the Strength effect when having the Incandescence effect.","在拥有灼热效果的时候获得力量效果。"));
        createAdvancement("ice_extract",translateText("Ice Extract","冷萃"),translateText("Get Enchantal Cooler.","获得魔凝机。"));
        createAdvancement("get_warped_laurel",translateText("Very Fragrant Wrped Laurel","醇香诡果"),translateText("Plant seeds with a strange fragrance were dug out in Warped forest.","在诡异森林挖出带有奇香的果实种子。"));
        createAdvancement("get_torchflower_seeds",translateText("The earth was in an uproar","天火地起"),translateText("Get Torchflower Seeds.","获得火把花种子。"));
        createAdvancement("get_torchflower_mustard",translateText("Frequent guest of party games","派对游戏常客"),translateText("Get Torchflower Mustard.","获得火火花芥末。"));
        createAdvancement("get_stewed_rotten_meat_pot",translateText("Bewaria þīne clǽne","注意卫生"),translateText("Although the patterns are beautiful, when it comes to tableware, cleanliness is of greater importance.","纹饰虽美，但作为餐具，还是洁净更重要。"));
        createAdvancement("get_sniffer_fur",translateText("Fluffy Troubles","毛茸茸的烦恼"),translateText("Continuously use a brush to groom your Sniffer, then clean up the fur shed by your pet.","持续使用刷子为你的嗅探兽梳毛，然后清理你的宠物散落的毛发。"));
        createAdvancement("get_pitcher_pod_petal",translateText("Cheese Cheese？","芝士芝士？"),translateText("Get Pitcher Pod Petal.","获取瓶子草荚果瓣。"));
        createAdvancement("get_pitcher_pod",translateText("Mohe glassware","漠河琉璃"),translateText("Get Pitcher Pod.","获得瓶子草荚果。"));
        createAdvancement("get_perfect_summer_ice",translateText("We are the strongest!","咱是最强的！"),translateText("Crafting a Perfect Summer Ice.","制作一个完美夏冰。"));
        createAdvancement("get_pearlip_rice_roll_boat",translateText("Get Pearlip Rice Roll Boat","黄蕉时代将会再临！！"),translateText("Crafting a Pearlip Rice Roll Boat.","制作一个棱蕉寿司船。"));
        createAdvancement("get_leisamboo_tea",translateText("Small can tea, made from large bamboo","小罐茶，大竹做"),translateText("Get Leisamboo Tea.","获得溪竹茶。"));
        createAdvancement("get_himekaido_trees",translateText("Spryt teon tō helpe hīe growan","拔苗助长"),translateText("Fertile land, extra fertilizers —— there is always a sacrifice to be made when pursuing excellence.","肥沃的土地、额外的肥料——追求卓越总要伴随牺牲。"));
        createAdvancement("get_double_ancient_blade",translateText("Death Momoi?!","死亡小桃？！"),translateText("Obtain two [Ancient Blade Newly Whetted], and then cut your enemies to pieces.","获得两把新硎古刀，然后切碎你的敌人。"));
        createAdvancement("get_alfalfa_seeds",translateText("Ordinary as grass","草介之凡"),translateText("Get Alfalfa Seeds.","获得古苜蓿种子。"));
        createAdvancement("fog_lifting_movement",translateText("Fog Lifting Movement","扬雾运动"),translateText("Touch Kwat Wheat.","触碰瓦斯麦。"));
        createAdvancement("esteemed_guest",translateText("Hearken, wild legions! Your strength is mine!","蛮族精兵，为我所用！"),translateText("Right-click to send golden toast. In any struggle, the key to victory lies in gaining more allies and isolating the adversary.","右键送出金瓦斯麦吐司。记住，在斗争中取胜的关键是——把朋友搞得多多的，把敌人搞得少少的。"));
        createAdvancement("eat_iced_black_tea",translateText("Essential for the exam","考试必备"),translateText("Drink Iced Black Tea.","喝下冰火茶。"));
        createAdvancement("eat_evolutcorn_hard_candy",translateText("Funny Character","搞笑角色"),translateText("Eat Evolutcorn Hard Candy and find that you are less vulnerable to damage while eating.","吃下玉黍硬糖。可以发现在吃糖的过程中更不易受到伤害。"));
        createAdvancement("eat_alfalfa_porridge",translateText("Long Term Vegetarian Diet","长期素食导致的"),translateText("Eat Alfalfa Porridge.","食用一碗苜蓿七草粥。"));
        createAdvancement("crimson_clouds",translateText("Crimson Clouds","绯红烟云"),translateText("Unusual wheat seeds in Crimson Forest.","在绯红森林获得不同寻常的小麦种子。"));
        createAdvancement("all_resistance_effects",translateText("Never Say Die","永不破防"),translateText("Get All Resistance Effect.","获得所有抵抗效果。"));
        createAdvancement("ealdaec_gemhus",translateText("Ealdæc Gemhūs","古炊凡家"),translateText("Get the ancient seeds from the plains.","千秋一粟，在平原获得酷似玉米粒的玉黎粒。"));
        createAdvancement("get_himekaido_trees",translateText("Spryt teon tō helpe hīe growan","拔苗助长"),translateText("Fertile land, extra fertilizers —— there is always a sacrifice to be made when pursuing excellence.","肥沃的土地、额外的肥料——追求卓越总要伴随牺牲。"));
    }

    private void addEntitys(){
        createEntity(ImmortalersDelightEntities.SKELVERFISH_THRASHER.get(),"Skelverfish Thrasher");
        createEntity(ImmortalersDelightEntities.BASE_EFFECT_CLOUD.get(),"区域效果云");
        createEntity(ImmortalersDelightEntities.CAUSTIC_ESSENTIAL_OIL.get(), "炽烈精油");
        createEntity(ImmortalersDelightEntities.GAS_EFFECT_CLOUD.get(), "绯烬尘霾");
        createEntity(ImmortalersDelightEntities.WARPED_LAUREL_HITBOX.get(), "下界咒焰");
        createEntity(ImmortalersDelightEntities.KI_BLAST.get(),"气功波");
    }
    private void addMessages(){
        createMessage("effect.stun","You're reeling!","你头晕目眩！");
        createMessage("effect.freeze","You're freezing up!","你要冻僵了！");
    }

    private void adds(){
        add("death.attack.drunk","%1$s Drunk on the asphalt","%1$s 醉倒在柏油路上");
        add("death.attack.drunk.item","%1$s was killed by %2$s using %3$s","%1$ 被 %2$s 杀死 %3$s");
        add("death.attack.drunk.player","%1$ died whilst trying to escap %2$s","%1$ 在试图逃跑时死亡 %2$s");
        add("death.attack.gas","%1$s It was gassed and turned into bacon","%1$s 被瓦斯毒气熏成了腊肉");
        add("death.attack.gas.item","%1$s was killed by %2$s using %3$s","%1$ 被 %2$s 杀死 %3$s");
        add("death.attack.gas.player","%1$s was killed by %2$s using %3$s","%1$ 在试图逃跑时死亡 %2$s");
        add("potion.potency.4","V","V");
        add("potion.potency.5","VI","VI");
        add("potion.potency.6","VII","VII");
        add("potion.potency.7","VIII","VIII");
        add("potion.potency.8","IX","IX");
        add("potion.potency.9","X","X");
        add("potion.potency.29","XXX","XXX");
        add("potion.potency.99","C","C");
        add("creativetab_immortalers_delight_tab","Immortalers Delight","千古乐事");
    }

    private void createDesc(String key,String en_us,String zh_cn){
        add(ADVANCEMENT + key + ".descr",en_us,zh_cn);
    }

    private void createTitle(String key,String en_us,String zh_cn){
        add(ADVANCEMENT + key + ".title",en_us,zh_cn);
    }

    private void createAdvancement(String name,List<String> title,List<String> desc){
        createTitle(name,title.get(0),title.get(1));
        createDesc(name,desc.get(0),desc.get(1));
    }

    public List<String> translateText(String en_us,String zh_cn){
        List<String> text = new ArrayList<>();
        text.add(en_us);
        text.add(zh_cn);
        return text;
    }

    private void createContainer(String key,String en_us,String zh_cn){
        add(CONTAINER + key,en_us,zh_cn);
    }

    private void createTooltip(String key,String zh_cn,String en_us){
        /*实际写的时候写反了，只能把参数反过来了（）*/
        add(TOOLTIP + key,en_us,zh_cn);
    }

    private void createFarmersdelightTooltip(String key,String en_us,String zh_cn){
        add(FARMERSDELIGHT_TOOLTIP + key,en_us,zh_cn);
    }
    private void createMessage(String key,String en_us,String zh_cn){
        add(MESSAGE + key,en_us,zh_cn);
    }

    private void createEntity(EntityType<?> entityType ,String zh_cn){
        String path = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath();
        add(entityType.getDescriptionId(),this.getEnglishName(path),zh_cn);
    }

    private void createEntity(String key,String en_us,String zh_cn){
        add(ENTITY + key,en_us,zh_cn);
    }

    @Override
    public void addItem(Supplier<? extends Item> key, String zh_cn) {
        String path = BuiltInRegistries.ITEM.getKey(key.get()).getPath();
        this.add(key.get().getDescriptionId(), this.getEnglishName(path), zh_cn);
    }

    protected void addItem(Supplier<? extends Item> key, String en_us, String zh_cn) {
        this.add(key.get().getDescriptionId(), en_us, zh_cn);
    }

    protected void addBlock(Supplier<? extends Block> key, String en_us, String zh_cn) {
        this.add(key.get().getDescriptionId(), en_us, zh_cn);
    }

    protected void add(String key, String en_us, String zh_cn) {
        if (this.locale.equals("en_us") && !this.en_us.containsKey(key)) {
            this.en_us.put(key, en_us);
        } else if (this.locale.equals("zh_cn") && !this.zh_cn.containsKey(key)) {
            this.zh_cn.put(key, zh_cn);
        }
    }

    public void add(MobEffect key, String en_us,String zh_cn) {
        this.add(key.getDescriptionId(), en_us,zh_cn);
    }

    public void createEffect(MobEffect key, String zh_cn) {
        String path = BuiltInRegistries.MOB_EFFECT.getKey(key).getPath();
        this.add(key.getDescriptionId(), getEnglishName(path),zh_cn);
    }

    public void createEffect(MobEffect key, String zh_cn,String description_en_us,String description_zh_cn) {
        this.createEffect(key,zh_cn);
        this.add("effect.immortalers_delight." + key.getDescriptionId() + ".description",description_en_us,description_zh_cn);
    }

    public void createEffect(MobEffect key, String en_us, String zh_cn) {
        this.add(key.getDescriptionId(), en_us,zh_cn);
    }

    private String getEnglishName(String path) {
        String[] words = path.split("_");
        for (int i = 0; i < words.length; i++) {
            String firstLetter = words[i].substring(0, 1);
            String remainingLetters = words[i].substring((1));
            words[i] = firstLetter.toUpperCase() + remainingLetters;
        }

        return String.join(" ", words);
    }

}
