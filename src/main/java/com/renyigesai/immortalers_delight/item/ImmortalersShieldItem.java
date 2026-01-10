package com.renyigesai.immortalers_delight.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.renyigesai.immortalers_delight.client.renderer.special_item.BoneKnifeItemRenderer;
import com.renyigesai.immortalers_delight.client.renderer.special_item.ItemTESRenderer;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ImmortalersShieldItem extends ShieldItem {


    public ImmortalersShieldItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(ImmortalersDelightItems.RUSTY_ANCIENT_BLADE.get()) || !repair.is(ItemTags.PLANKS) && super.isValidRepairItem(toRepair, repair);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
    }

    //绑定特殊渲染器。要注意，启用渲染器需要烘焙模型的支持，因此不要漏。
    @Override
    public void initializeClient(java.util.function.Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new ItemTESRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
            }
        });
    }
}
