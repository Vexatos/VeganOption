package squeek.veganoption.registry;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CompostRegistry
{
	public static List<ItemStack> browns = new ArrayList<ItemStack>();
	public static List<ItemStack> greens = new ArrayList<ItemStack>();
	public static List<FoodSpecifier> uncompostableFoods = new ArrayList<FoodSpecifier>();
	
	public abstract static class FoodSpecifier
	{
		public abstract boolean matches(ItemStack itemStack);
	}

	public static boolean isItemStackInList(List<ItemStack> haystack, ItemStack needle)
	{
		for (ItemStack itemStack : haystack)
		{
			if (OreDictionary.itemMatches(itemStack, needle, false))
				return true;
		}
		return false;
	}

	public static boolean isCompostable(ItemStack itemStack)
	{
		return isGreen(itemStack) || isBrown(itemStack);
	}

	public static boolean isBrown(ItemStack itemStack)
	{
		return isItemStackInList(browns, itemStack);
	}

	public static boolean isGreen(ItemStack itemStack)
	{
		return isCompostableFood(itemStack) || isItemStackInList(greens, itemStack);
	}

	public static boolean isCompostableFood(ItemStack itemStack)
	{
		// TODO: optionally use AppleCore's method?
		if (itemStack != null && itemStack.getItem() instanceof ItemFood)
		{
			for (FoodSpecifier uncompostableFood : uncompostableFoods)
			{
				if (uncompostableFood.matches(itemStack))
					return false;
			}
			return true;
		}
		return false;
	}

	public static void addBrown(Item item)
	{
		addBrown(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
	}

	public static void addBrown(Block block)
	{
		addBrown(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
	}

	public static void addBrown(ItemStack itemStack)
	{
		browns.add(itemStack);
	}

	public static void addBrown(String oredictName)
	{
		browns.addAll(OreDictionary.getOres(oredictName));
	}

	public static void addGreen(Item item)
	{
		addGreen(new ItemStack(item));
	}

	public static void addGreen(Block block)
	{
		addGreen(new ItemStack(block));
	}

	public static void addGreen(ItemStack itemStack)
	{
		greens.add(itemStack);
	}

	public static void addGreen(String oredictName)
	{
		greens.addAll(OreDictionary.getOres(oredictName));
	}
	
	public static void blacklist(FoodSpecifier foodSpecifier)
	{
		uncompostableFoods.add(foodSpecifier);
	}
}
