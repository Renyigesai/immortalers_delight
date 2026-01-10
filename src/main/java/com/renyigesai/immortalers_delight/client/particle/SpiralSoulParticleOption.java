package com.renyigesai.immortalers_delight.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

// 自定义带int参数的ParticleOption，封装extra_size（倒计时刻数）
public class SpiralSoulParticleOption implements ParticleOptions {
    // 1. 定义Codec：用于序列化/反序列化参数（网络/数据存储）
    public static final Codec<SpiralSoulParticleOption> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    // 绑定int参数"extra_raidus"，从实例中获取extra_size值
                    Codec.INT.fieldOf("extra_raidus").forGetter(SpiralSoulParticleOption::getCountdown)
            ).apply(instance, SpiralSoulParticleOption::new)
    );

    // 2. 定义反序列化器：处理指令解析和网络数据读取
    public static final Deserializer<SpiralSoulParticleOption> DESERIALIZER = new Deserializer<>() {
        // 从指令解析参数（如/particle yourmod:extra_raidus ~ ~ ~ 0 0 0 0 1 10）
        @Override
        public SpiralSoulParticleOption fromCommand(ParticleType<SpiralSoulParticleOption> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' '); // 跳过空格
            int extra_size = reader.readInt(); // 读取指令中的int参数
            return new SpiralSoulParticleOption(extra_size);
        }

        // 从网络缓冲区读取参数（服务端→客户端）
        @Override
        public SpiralSoulParticleOption fromNetwork(ParticleType<SpiralSoulParticleOption> type, FriendlyByteBuf buffer) {
            return new SpiralSoulParticleOption(buffer.readVarInt()); // 读取VarInt类型的int
        }
    };

    // 自定义int参数
    private final int extra_raidus;

    // 构造方法：初始化extra_size参数
    public SpiralSoulParticleOption(int extra_r) {
        this.extra_raidus = extra_r;
    }

    // 3. 实现ParticleOptions接口的核心方法
    // 将参数写入网络缓冲区（客户端→服务端/服务端→客户端）
    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.extra_raidus); // 写入VarInt，节省网络带宽
    }

    // 将参数转换为字符串（用于指令显示/调试）
    @Override
    public @NotNull String writeToString() {
        return String.format(Locale.ROOT, "%s %d",
                BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                this.extra_raidus
        );
    }

    // 关联对应的ParticleType（后续定义）
    @Override
    public @NotNull ParticleType<SpiralSoulParticleOption> getType() {
        return ImmortalersDelightParticleTypes.SPIRAL_SOUL.get(); // 替换为你的粒子类型注册引用
    }

    // Getter：对外暴露int参数，供粒子渲染类使用
    public int getCountdown() {
        return this.extra_raidus;
    }
}
