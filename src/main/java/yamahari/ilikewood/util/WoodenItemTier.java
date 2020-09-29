package yamahari.ilikewood.util;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraftforge.fml.ModList;
import yamahari.ilikewood.ILikeWood;
import yamahari.ilikewood.config.Config;
import yamahari.ilikewood.registry.WoodenBlocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum WoodenItemTier implements IItemTier {
    ACACIA(Constants.ACACIA, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.ACACIA))),
    BIRCH(Constants.BIRCH, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.BIRCH))),
    CRIMSON(Constants.CRIMSON, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.CRIMSON))),
    DARK_OAK(Constants.DARK_OAK, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.DARK_OAK))),
    JUNGLE(Constants.JUNGLE, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.JUNGLE))),
    OAK(Constants.OAK, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.OAK))),
    SPRUCE(Constants.SPRUCE, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.SPRUCE))),
    WARPED(Constants.WARPED, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.WARPED))),
    STONE(Constants.STONE, false, () -> Ingredient.fromItems(Items.COBBLESTONE)),
    IRON(Constants.IRON, false, () -> Ingredient.fromItems(Items.IRON_INGOT)),
    DIAMOND(Constants.DIAMOND, false, () -> Ingredient.fromItems(Items.DIAMOND)),
    GOLDEN(Constants.GOLDEN, false, () -> Ingredient.fromItems(Items.GOLD_INGOT)),
    NETHERITE(Constants.NETHERITE, false, () -> Ingredient.fromItems(Items.NETHERITE_INGOT)),
    CHERRY(Constants.CHERRY, Constants.BOP_MOD_ID, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.CHERRY))),
    DEAD(Constants.DEAD, Constants.BOP_MOD_ID, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.DEAD))),
    FIR(Constants.FIR, Constants.BOP_MOD_ID, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.FIR))),
    HELLBARK(Constants.HELLBARK, Constants.BOP_MOD_ID, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.HELLBARK))),
    JACARANDA(Constants.JACARANDA, Constants.BOP_MOD_ID, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.JACARANDA))),
    MAGIC(Constants.MAGIC, Constants.BOP_MOD_ID, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.MAGIC))),
    MAHOGANY(Constants.MAHOGANY, Constants.BOP_MOD_ID, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.MAHOGANY))),
    PALM(Constants.PALM, Constants.BOP_MOD_ID, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.PALM))),
    REDWOOD(Constants.REDWOOD, Constants.BOP_MOD_ID, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.REDWOOD))),
    UMBRAN(Constants.UMBRAN, Constants.BOP_MOD_ID, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.UMBRAN))),
    WILLOW(Constants.WILLOW, Constants.BOP_MOD_ID, true, () -> Ingredient.fromItems(WoodenBlocks.getBlock(WoodenObjectType.PANELS, WoodType.WILLOW)));

    private final String name;
    private final String modId;
    private final boolean isWood;
    private final LazyValue<Supplier<Integer>> harvestLevel;
    private final LazyValue<Supplier<Integer>> maxUses;
    private final LazyValue<Supplier<Double>> efficiency;
    private final LazyValue<Supplier<Double>> attackDamage;
    private final LazyValue<Supplier<Integer>> enchantability;
    private final LazyValue<Ingredient> repairMaterial;
    private final Map<WoodenTieredObjectType, Properties> properties;

    WoodenItemTier(final String name, final boolean isWood, final Supplier<Ingredient> repairMaterial) {
        this(name, Constants.MOD_ID, isWood, repairMaterial);
    }

    WoodenItemTier(final String name, final String modId, final boolean isWood, final Supplier<Ingredient> repairMaterial) {
        this.name = name;
        this.modId = modId;
        this.isWood = isWood;
        this.harvestLevel = new LazyValue<>(() -> Config.SERVER_CONFIG.HARVEST_LEVEL.get(name)::get);
        this.maxUses = new LazyValue<>(() -> Config.SERVER_CONFIG.MAX_USES.get(name)::get);
        this.efficiency = new LazyValue<>(() -> Config.SERVER_CONFIG.EFFICIENCY.get(name)::get);
        this.attackDamage = new LazyValue<>(() -> Config.SERVER_CONFIG.ATTACK_DAMAGE.get(name)::get);
        this.enchantability = new LazyValue<>(() -> Config.SERVER_CONFIG.ENCHANTABILITY.get(name)::get);
        this.repairMaterial = new LazyValue<>(repairMaterial);
        final Map<WoodenTieredObjectType, Properties> properties = new EnumMap<>(WoodenTieredObjectType.class);
        for (final WoodenTieredObjectType tieredObjectType : WoodenTieredObjectType.values()) {
            final String type = tieredObjectType.toString();
            properties.put(tieredObjectType, new WoodenItemTier.Properties(
                    () -> Config.SERVER_CONFIG.TIERED_ATTACK_SPEED.get(name).get(type)::get,
                    () -> Config.SERVER_CONFIG.TIERED_ATTACK_DAMAGE.get(name).get(type)::get,
                    isWood ? () -> Config.SERVER_CONFIG.TIERED_BURN_TIME.get(name).get(type)::get : () -> () -> -1
            ));
        }
        this.properties = Collections.unmodifiableMap(properties);
    }

    public static Stream<WoodenItemTier> getLoadedValues() {
        try {
            final String dataModId = System.getProperty("ilikewood.datagen.modid");
            if (dataModId != null) {
                return Arrays.stream(values()).filter(itemTier -> !itemTier.isWood() || itemTier.getModId().equals(dataModId));
            }
        } catch (NullPointerException | SecurityException | IllegalArgumentException e) {
            ILikeWood.LOGGER.error(e.getMessage());
        }
        return Arrays.stream(values()).filter(itemTier -> ModList.get().isLoaded(itemTier.getModId()));
    }

    public boolean isWood() {
        return isWood;
    }

    public int getHarvestLevel() {
        return harvestLevel.getValue().get();
    }

    public int getMaxUses() {
        return maxUses.getValue().get();
    }

    public float getEfficiency() {
        return efficiency.getValue().get().floatValue();
    }

    public float getAttackDamage() {
        return attackDamage.getValue().get().floatValue();
    }

    public int getEnchantability() {
        return enchantability.getValue().get();
    }

    public Ingredient getRepairMaterial() {
        return repairMaterial.getValue();
    }

    public Properties getProperties(final WoodenTieredObjectType tieredObjectType) {
        assert this.properties.containsKey(tieredObjectType);
        return properties.get(tieredObjectType);
    }

    public String getModId() {
        return this.modId;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static final class Properties {
        private final LazyValue<Supplier<Double>> attackSpeed;
        private final LazyValue<Supplier<Double>> attackDamage;
        private final LazyValue<Supplier<Integer>> burnTime;

        public Properties(final Supplier<Supplier<Double>> attackSpeed, final Supplier<Supplier<Double>> attackDamage,
                          final Supplier<Supplier<Integer>> burnTime) {
            this.attackSpeed = new LazyValue<>(attackSpeed);
            this.attackDamage = new LazyValue<>(attackDamage);
            this.burnTime = new LazyValue<>(burnTime);
        }

        public float getAttackSpeed() {
            return this.attackSpeed.getValue().get().floatValue();
        }

        public float getAttackDamage() {
            return this.attackDamage.getValue().get().floatValue();
        }

        public int getBurnTime() {
            return this.burnTime.getValue().get();
        }
    }
}
