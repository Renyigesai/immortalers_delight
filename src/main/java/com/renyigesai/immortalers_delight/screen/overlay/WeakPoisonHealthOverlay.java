package com.renyigesai.immortalers_delight.screen.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.renyigesai.immortalers_delight.Config;
import com.renyigesai.immortalers_delight.ImmortalersDelightMod;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightMobEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/**
 * WeakPoisonHealthOverlay类用于在Minecraft游戏中处理虚弱中毒效果的生命值覆盖层渲染。
 * 当玩家满足特定条件时，会在玩家的生命值显示上叠加虚弱中毒效果的图标。
 */
public class WeakPoisonHealthOverlay {
	// 生命值图标偏移量，用于确定图标在屏幕上的位置
	protected static int healthIconsOffset;
	// 虚弱中毒效果的图标纹理资源位置
	private static final ResourceLocation HEALTH_ICONS_TEXTURE = new ResourceLocation(ImmortalersDelightMod.MODID, "textures/gui/icons/weak_poison_icons.png");

	/**
	 * 初始化方法，将WeakPoisonHealthOverlay类的实例注册到Minecraft Forge的事件总线中，
	 * 以便监听相关事件。
	 */
	public static void init() {
		MinecraftForge.EVENT_BUS.register(new WeakPoisonHealthOverlay());
	}

	// 玩家生命值覆盖层的资源位置，用于识别玩家生命值显示的覆盖层
	static ResourceLocation PLAYER_HEALTH_ELEMENT = new ResourceLocation("minecraft", "player_health");

	/**
	 * 监听RenderGuiOverlayEvent.Post事件，当渲染玩家生命值覆盖层时，
	 * 检查条件并调用renderWeakPoisonOverlay方法进行虚弱中毒效果的渲染。
	 *
	 * @param event 渲染GUI覆盖层后的事件
	 */
	@SubscribeEvent
	public void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
		// 检查当前渲染的覆盖层是否为玩家生命值覆盖层
		if (event.getOverlay() == GuiOverlayManager.findOverlay(PLAYER_HEALTH_ELEMENT)) {
			// 获取Minecraft实例
			Minecraft mc = Minecraft.getInstance();
			// 获取ForgeGui实例
			ForgeGui gui = (ForgeGui) mc.gui;
			// 检查玩家是否隐藏了GUI，并且是否应该绘制生存元素
			if (!mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
				// 调用渲染虚弱中毒覆盖层的方法
				renderWeakPoisonOverlay(gui, event.getGuiGraphics());
			}
		}
	}

	/**
	 * 渲染虚弱中毒效果的覆盖层，根据配置和玩家状态决定是否进行渲染。
	 *
	 * @param gui 游戏的ForgeGui实例
	 * @param graphics 用于绘制的GuiGraphics实例
	 */
	public static void renderWeakPoisonOverlay(ForgeGui gui, GuiGraphics graphics) {
		// 检查配置中是否启用了虚弱中毒生命值覆盖层功能
		if (!Config.weakPoisonHealthOverlay) {
			return;
		}

		// 设置生命值图标偏移量
		healthIconsOffset = gui.leftHeight;
		// 获取Minecraft实例
		Minecraft minecraft = Minecraft.getInstance();
		// 获取当前玩家实例
		Player player = minecraft.player;

		// 如果玩家为空，直接返回
		if (player == null) {
			return;
		}

		// 获取玩家的食物数据
		FoodData stats = player.getFoodData();
		// 计算覆盖层的顶部位置
		int top = minecraft.getWindow().getGuiScaledHeight() - healthIconsOffset + 10;
		// 计算覆盖层的左侧位置
		int left = minecraft.getWindow().getGuiScaledWidth() / 2 - 91;

		// 判断玩家是否符合显示虚弱中毒覆盖层的条件：
		// 需要玩家没有再生效果
		boolean isPlayerEligibleForWeakPoison = !player.hasEffect(MobEffects.REGENERATION);

		// 检查玩家是否有虚弱中毒效果，并且符合显示条件
		if (player.getEffect(ImmortalersDelightMobEffect.WEAK_POISON.get()) != null && isPlayerEligibleForWeakPoison) {
			// 调用绘制虚弱中毒覆盖层的方法
			drawWeakPoisonOverlay(player, minecraft, graphics, left, top);
		}
	}

	/**
	 * 绘制虚弱中毒效果的覆盖层，根据玩家的生命值、吸收量等信息确定绘制的位置和样式。
	 *
	 * @param player 当前玩家实例
	 * @param minecraft Minecraft实例
	 * @param graphics 用于绘制的GuiGraphics实例
	 * @param left 覆盖层的左侧位置
	 * @param top 覆盖层的顶部位置
	 */
	public static void drawWeakPoisonOverlay(Player player, Minecraft minecraft, GuiGraphics graphics, int left, int top) {
		// 获取游戏的当前帧数
		int ticks = minecraft.gui.getGuiTicks();
		// 创建随机数生成器
		Random rand = new Random();
		// 设置随机数种子，根据当前帧数生成
		rand.setSeed((long) (ticks * 312871));

		// 获取玩家的当前生命值，向上取整
		int health = Mth.ceil(player.getHealth());
		// 获取玩家的吸收量，向上取整
		float absorb = Mth.ceil(player.getAbsorptionAmount());
		// 获取玩家的最大生命值属性实例
		AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
		// 获取玩家的最大生命值
		float healthMax = (float) attrMaxHealth.getValue();

		// 初始化再生效果的帧数，默认为-1
		int regen = -1;
		// 如果玩家有再生效果，计算再生效果的帧数
		if (player.hasEffect(MobEffects.REGENERATION)) regen = ticks % 25;

		// 计算生命值显示的行数
		int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
		// 计算每行的高度，最小为3
		int rowHeight = Math.max(10 - (healthRows - 2), 3);

		// 计算闪烁效果的帧数
		int comfortSheen = ticks % 50;
		// 计算闪烁的心形图框的索引
		int comfortHeartFrame = comfortSheen % 2;
		// 存储纹理宽度的数组
		int[] textureWidth = {5, 9};

		// 设置渲染的纹理
		RenderSystem.setShaderTexture(0, HEALTH_ICONS_TEXTURE);
		// 启用混合模式，用于透明渲染
		RenderSystem.enableBlend();

		// 计算单行最大生命值显示数量
		int healthMaxSingleRow = Mth.ceil(Math.min(healthMax, 20) / 2.0F);
		// 计算左侧高度偏移量，确保覆盖层在最底部的一行心型图标上
		int leftHeightOffset = ((healthRows - 1) * rowHeight);

		// 遍历单行的每个生命值图标位置
		for (int i = 0; i < healthMaxSingleRow; ++i) {
			// 计算当前图标的列索引
			int column = i % 10;
			// 计算当前图标的x坐标
			int x = left + column * 8;
			// 计算当前图标的y坐标
			int y = top + leftHeightOffset;

			// 如果玩家生命值小于等于4，随机调整y坐标
			if (health <= 4) y += rand.nextInt(2);
			// 如果当前图标位置与再生效果帧数相同，调整y坐标
			if (i == regen) y -= 2;
			// 计算当前栏的有效生命值
			float effectiveHealthOfBar = (health / 2.0F - i);
			// 绘制Buff血量图标，9,9,9,9意为icons左上角第二行第二个9*9区域
			if (effectiveHealthOfBar >= 1) {
				graphics.blit(HEALTH_ICONS_TEXTURE, x, y, 9, 9, 9, 9);
			} else if (effectiveHealthOfBar >= .5) {
				graphics.blit(HEALTH_ICONS_TEXTURE, x, y, 18, 9, 9, 9);
			} else graphics.blit(HEALTH_ICONS_TEXTURE, x, y, 0, 9, 9, 9);
			/*
			这两个是舒适效果用来弄闪烁动画的，目前为弃用
			 */
//			// 如果当前列索引与闪烁效果的帧数匹配，绘制闪烁的心形图标
//			if (column == comfortSheen / 2) {
//				graphics.blit(HEALTH_ICONS_TEXTURE, x, y, 0, 9, textureWidth[comfortHeartFrame], 9);
//			}
//			// 如果当前列索引与闪烁效果的帧数匹配，并且是特定的心形图框，绘制额外的图标
//			if (column == (comfortSheen / 2) - 1 && comfortHeartFrame == 0) {
//				graphics.blit(HEALTH_ICONS_TEXTURE, x + 5, y, 5, 9, 4, 9);
//			}
		}

		// 禁用混合模式
		RenderSystem.disableBlend();
	}
}
