package squeek.veganoption.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import squeek.veganoption.blocks.BlockEnderRift;
import squeek.veganoption.content.modules.Egg;
import squeek.veganoption.content.modules.FrozenBubble;
import squeek.veganoption.helpers.NetworkHelper;
import squeek.veganoption.helpers.RandomHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MessageFX implements IMessage, IMessageHandler<MessageFX, IMessage>
{
	public static enum FX
	{
		BUBBLE_POP,
		BLOCK_TELEPORT,
		PLASTIC_EGG_BREAK
	}

	double x, y, z;
	FX fx;

	public MessageFX()
	{
	}

	public MessageFX(double x, double y, double z, FX fx)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.fx = fx;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeByte(fx.ordinal());
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.fx = FX.values()[buf.readByte()];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageFX message, MessageContext ctx)
	{
		switch (message.fx)
		{
			case BUBBLE_POP:
				doEntityBreakFX(message, ctx, FrozenBubble.frozenBubble);
				break;
			case BLOCK_TELEPORT:
				BlockEnderRift.spawnBlockTeleportFX(NetworkHelper.getSidedWorld(ctx), message.x, message.y, message.z, RandomHelper.random);
				break;
			case PLASTIC_EGG_BREAK:
				doEntityBreakFX(message, ctx, Egg.plasticEgg);
				break;
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public void doEntityBreakFX(MessageFX message, MessageContext ctx, Item breakFXItem)
	{
		doEntityBreakFX(message, ctx, breakFXItem, 8);
	}

	@SideOnly(Side.CLIENT)
	public void doEntityBreakFX(MessageFX message, MessageContext ctx, Item breakFXItem, int iterations)
	{
		doEntityBreakFX(NetworkHelper.getSidedWorld(ctx), message.x, message.y, message.z, breakFXItem, iterations);
	}

	@SideOnly(Side.CLIENT)
	public void doEntityBreakFX(World world, double x, double y, double z, Item breakFXItem, int iterations)
	{
		Minecraft mc = Minecraft.getMinecraft();
		for (int i = 0; i < iterations; ++i)
		{
			mc.effectRenderer.addEffect(new EntityBreakingFX(world, x, y, z, breakFXItem));
		}
	}
}
