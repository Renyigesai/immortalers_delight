package com.renyigesai.immortalers_delight.screen.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/**
 * Credits to squeek502 (AppleSkin) for the implementation reference!
 * https://www.curseforge.com/minecraft/mc-mods/appleskin
 */
public class AftertasteHungerOverlay {
	// 食物图标偏移量，用于调整食物图标的位置
	public static int foodIconsOffset;
	// 余味图标的纹理资源位置
	private static final ResourceLocation AFTERTASTE_ICONS_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/icons/aftertaste_icons.png");

	/**
	 * 初始化方法，将当前类的实例注册到 Minecraft Forge 的事件总线中
	 */
	public static void init() {
		MinecraftForge.EVENT_BUS.register(new AftertasteHungerOverlay());
	}

	// 食物等级元素的资源位置
	static ResourceLocation FOOD_LEVEL_ELEMENT = new ResourceLocation("minecraft", "food_level");

	/**
	 * 处理渲染 GUI 覆盖层的后置事件
	 * @param event 渲染 GUI 覆盖层的后置事件
	 */
	@SubscribeEvent
	public void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
		// 检查事件的覆盖层是否为食物等级元素的覆盖层
		if (event.getOverlay() == GuiOverlayManager.findOverlay(FOOD_LEVEL_ELEMENT)) {
			Minecraft mc = Minecraft.getInstance();
			ForgeGui gui = (ForgeGui) mc.gui;
			// 检查玩家是否乘坐了其他生物
			boolean isMounted = mc.player != null && mc.player.getVehicle() instanceof LivingEntity;
			// 如果玩家没有乘坐其他生物，GUI 没有隐藏，并且应该绘制生存元素，则渲染余味覆盖层
			if (!isMounted && !mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
				renderAftertasteOverlay(gui, event.getGuiGraphics());
			}
		}
	}

	/**
	 * 渲染余味覆盖层的方法
	 * @param gui ForgeGui 实例，用于获取 GUI 相关信息
	 * @param graphics GuiGraphics 实例，用于进行图形绘制
	 */
	public static void renderAftertasteOverlay(ForgeGui gui, GuiGraphics graphics) {
//		if (!Configuration.NOURISHED_HUNGER_OVERLAY.get()) {
//			return;
//		}

		// 设置食物图标偏移量为 GUI 的右侧高度
		foodIconsOffset = gui.rightHeight;
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;

		// 如果玩家为空，则不进行后续操作
		if (player == null) {
			return;
		}

		// 获取玩家的食物数据
		FoodData stats = player.getFoodData();
		// 计算顶部位置，这是图标将屏幕上的位置而非在icon贴图的位置
		int top = minecraft.getWindow().getGuiScaledHeight() - foodIconsOffset + 10;
		// 计算左侧位置，同上
		int left = minecraft.getWindow().getGuiScaledWidth() / 2 + 91;

		// 检查玩家是否正在通过饱和度进行自然恢复
		boolean isPlayerHealingWithSaturation =
				player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
						&& player.isHurt()
						&& stats.getFoodLevel() >= 18;

		// 如果玩家有 LINGERING_FLAVOR 效果，则绘制余味覆盖层
		if (player.getEffect(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get()) != null) {
			drawAftertasteOverlay(stats, minecraft, graphics, left, top, isPlayerHealingWithSaturation);
		}
	}

	/**
	 * 绘制余味覆盖层的具体方法
	 * @param stats 玩家的食物数据
	 * @param mc Minecraft 实例
	 * @param graphics GuiGraphics 实例，用于进行图形绘制
	 * @param left 左侧位置
	 * @param top 顶部位置
	 * @param naturalHealing 玩家是否正在通过饱和度进行自然恢复
	 */
	public static void drawAftertasteOverlay(FoodData stats, Minecraft mc, GuiGraphics graphics, int left, int top, boolean naturalHealing) {
		// 获取玩家的饱和度
		float saturation = stats.getSaturationLevel();
		// 获取玩家的食物等级
		int foodLevel = stats.getFoodLevel();
		// 获取 GUI 的 tick 数
		int ticks = mc.gui.getGuiTicks();
		// 创建一个随机数生成器
		Random rand = new Random();
		// 设置随机数生成器的种子
		rand.setSeed(ticks * 312871);

//		RenderSystem.setShaderTexture(0, AFTERTASTE_ICONS_TEXTURE);
		// 启用混合模式，用于处理透明纹理的绘制
		RenderSystem.enableBlend();

		// 循环绘制 10 个图标
		for (int j = 0; j < 10; ++j) {
			// 计算图标在 x 轴上的位置
			int x = left - j * 8 - 9;
			// 初始化图标在 y 轴上的位置
			int y = top;

			// 如果饱和度小于等于 0 且 tick 数满足特定条件，则在 y 轴上进行随机偏移
			if (saturation <= 0.0F && ticks % (foodLevel * 3 + 1) == 0) {
				y = top + (rand.nextInt(3) - 1);
			}

			// 绘制背景纹理，后边4个数是图标在icon贴图上的位置
			graphics.blit(AFTERTASTE_ICONS_TEXTURE, x, y, 0, 0, 9, 9);

			// 计算当前栏的有效饥饿值
			float effectiveHungerOfBar = (stats.getFoodLevel()) / 2.0F - j;
			// 根据玩家是否正在自然恢复来确定纹理偏移量
			int naturalHealingOffset = naturalHealing ? 18 : 0;

			// 绘制金色饥饿图标，18,0,9,9意为icons左上角第三个9*9区域
			if (effectiveHungerOfBar >= 1) {
				graphics.blit(AFTERTASTE_ICONS_TEXTURE, x, y, 18 + naturalHealingOffset, 0, 9, 9);
			} else if (effectiveHungerOfBar >= .5) {
				graphics.blit(AFTERTASTE_ICONS_TEXTURE, x, y, 9 + naturalHealingOffset, 0, 9, 9);
			}
		}

		// 禁用混合模式
		RenderSystem.disableBlend();
//		RenderSystem.setShaderTexture(0, Gui.GUI_ICONS_LOCATION);
	}
}
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
//import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.food.FoodData;
//import net.minecraft.world.level.GameRules;
//import net.minecraftforge.client.event.RenderGuiOverlayEvent;
//import net.minecraftforge.client.gui.overlay.ForgeGui;
//import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//
//import java.util.Random;
//
///**
// * Credits to squeek502 (AppleSkin) for the implementation reference!
// * https://www.curseforge.com/minecraft/mc-mods/appleskin
// */
//
//public class AftertasteHungerOverlay
//{
//	public static int foodIconsOffset;
//	private static final ResourceLocation AFTERTASTE_ICONS_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/icons/aftertaste_icons.png");
//
//	public static void init() {
//		MinecraftForge.EVENT_BUS.register(new AftertasteHungerOverlay());
//	}
//
//	static ResourceLocation FOOD_LEVEL_ELEMENT = new ResourceLocation("minecraft", "food_level");
//
//	@SubscribeEvent
//	public void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
//		if (event.getOverlay() == GuiOverlayManager.findOverlay(FOOD_LEVEL_ELEMENT)) {
//			Minecraft mc = Minecraft.getInstance();
//			ForgeGui gui = (ForgeGui) mc.gui;
//			boolean isMounted = mc.player != null && mc.player.getVehicle() instanceof LivingEntity;
//			if (!isMounted && !mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
//				renderAftertasteOverlay(gui, event.getGuiGraphics());
//			}
//		}
//	}
//
//	public static void renderAftertasteOverlay(ForgeGui gui, GuiGraphics graphics) {
////		if (!Configuration.NOURISHED_HUNGER_OVERLAY.get()) {
////			return;
////		}
//
//		foodIconsOffset = gui.rightHeight;
//		Minecraft minecraft = Minecraft.getInstance();
//		Player player = minecraft.player;
//
//		if (player == null) {
//			return;
//		}
//
//		FoodData stats = player.getFoodData();
//		int top = minecraft.getWindow().getGuiScaledHeight() - foodIconsOffset + 10;
//		int left = minecraft.getWindow().getGuiScaledWidth() / 2 + 91;
//
//		boolean isPlayerHealingWithSaturation =
//				player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
//						&& player.isHurt()
//						&& stats.getFoodLevel() >= 18;
//
//		if (player.getEffect(ImmortalersDelightMobEffect.LINGERING_FLAVOR.get()) != null) {
//			drawAftertasteOverlay(stats, minecraft, graphics, left, top, isPlayerHealingWithSaturation);
//		}
//	}
//
//	public static void drawAftertasteOverlay(FoodData stats, Minecraft mc, GuiGraphics graphics, int left, int top, boolean naturalHealing) {
//		float saturation = stats.getSaturationLevel();
//		int foodLevel = stats.getFoodLevel();
//		int ticks = mc.gui.getGuiTicks();
//		Random rand = new Random();
//		rand.setSeed(ticks * 312871);
//
////		RenderSystem.setShaderTexture(0, AFTERTASTE_ICONS_TEXTURE);
//		RenderSystem.enableBlend();
//
//		for (int j = 0; j < 10; ++j) {
//			int x = left - j * 8 - 9;
//			int y = top;
//
//			if (saturation <= 0.0F && ticks % (foodLevel * 3 + 1) == 0) {
//				y = top + (rand.nextInt(3) - 1);
//			}
//
//			// Background texture
//			graphics.blit(AFTERTASTE_ICONS_TEXTURE, x, y, 0, 0, 9, 9);
//
//			float effectiveHungerOfBar = (stats.getFoodLevel()) / 2.0F - j;
//			int naturalHealingOffset = naturalHealing ? 18 : 0;
//
//			// Gilded hunger icons
//			if (effectiveHungerOfBar >= 1)
//				graphics.blit(AFTERTASTE_ICONS_TEXTURE, x, y, 18 + naturalHealingOffset, 0, 9, 9);
//			else if (effectiveHungerOfBar >= .5)
//				graphics.blit(AFTERTASTE_ICONS_TEXTURE, x, y, 9 + naturalHealingOffset, 0, 9, 9);
//		}
//
//		RenderSystem.disableBlend();
////		RenderSystem.setShaderTexture(0, Gui.GUI_ICONS_LOCATION);
//	}
//}
