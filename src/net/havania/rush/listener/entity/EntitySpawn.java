package net.havania.rush.listener.entity;

import java.lang.reflect.Field;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.havania.core.utils.Attributes;
import net.havania.core.utils.Attributes.Attribute;
import net.havania.core.utils.Attributes.AttributeType;
import net.havania.core.utils.SpeedItem;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.EntityVillager;
import net.minecraft.server.v1_7_R4.MerchantRecipe;
import net.minecraft.server.v1_7_R4.MerchantRecipeList;
import net.minecraft.server.v1_7_R4.NBTTagCompound;

public class EntitySpawn implements Listener {
	
	public int count;
	public static boolean center;
	
	static {
		center = false;
	}
	
	public EntitySpawn() {
		count = 0;
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onSpawn(EntitySpawnEvent e)
	{
		if(e.getEntity() instanceof Villager)
		{
			Villager villager = (Villager) e.getEntity();
			
			EntityVillager entityVillager = ((CraftVillager) villager).getHandle();
			setAi(villager);
			villager.setAdult();
			if(!villager.getEyeLocation().getBlock().getType().equals(Material.AIR))
			{
				entityVillager.setPositionRotation(villager.getLocation().getX(), villager.getLocation().getY(), villager.getLocation().getZ(), Math.round(villager.getLocation().getYaw()/-1), villager.getLocation().getPitch());
			}
			
			if(center)
			{
				villager.setCustomName("Le Troqueur");
				villager.setCustomNameVisible(true);
				
				try {
					Field recipes = entityVillager.getClass().getDeclaredField("bu");
					recipes.setAccessible(true);
					
					MerchantRecipeList list = new MerchantRecipeList();
						
					net.minecraft.server.v1_7_R4.ItemStack itemneeded = CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_INGOT));
					net.minecraft.server.v1_7_R4.ItemStack itemresult = CraftItemStack.asNMSCopy(new ItemStack(Material.COMPASS));
					MerchantRecipe r1 = new MerchantRecipe(itemneeded, null, itemresult);
					r1.maxUses = 999999999;
					list.add(r1);
					
					net.minecraft.server.v1_7_R4.ItemStack itemneeded2 = CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_INGOT));
					net.minecraft.server.v1_7_R4.ItemStack itemresult2 = CraftItemStack.asNMSCopy(new ItemStack(Material.TNT));
					MerchantRecipe r2 = new MerchantRecipe(itemneeded2, null, itemresult2);
					r2.maxUses = 5;
					list.add(r2);
					
					net.minecraft.server.v1_7_R4.ItemStack itemneeded3 = CraftItemStack.asNMSCopy(new ItemStack(Material.CLAY_BRICK, 2));
					net.minecraft.server.v1_7_R4.ItemStack itemresult3 = CraftItemStack.asNMSCopy(new ItemStack(Material.ENDER_STONE));
					MerchantRecipe r3 = new MerchantRecipe(itemneeded3, null, itemresult3);
					r3.maxUses = 999999999;
					list.add(r3);
					
					recipes.set(entityVillager, list);
			
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return;
			}
			
			switch(count)
			{
				case 0:
					/*VillagerTradeAPI.clearTrades(villager);
					VillagerTradeAPI.addTrade(villager, new VillagerTrade(new ItemStack(Material.IRON_INGOT), new ItemStack(Material.GOLDEN_APPLE)));
					VillagerTradeAPI.addTrade(villager, new VillagerTrade(new ItemStack(Material.CLAY_BRICK), new ItemStack(Material.COOKED_BEEF)));*/
					villager.setCustomName("Le Boulanger");
					villager.setCustomNameVisible(true);
					
					try {
						Field recipes = entityVillager.getClass().getDeclaredField("bu");
						recipes.setAccessible(true);
						
						MerchantRecipeList list = new MerchantRecipeList();
							
						net.minecraft.server.v1_7_R4.ItemStack itemneeded = CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_INGOT));
						net.minecraft.server.v1_7_R4.ItemStack itemresult = CraftItemStack.asNMSCopy(new ItemStack(Material.GOLDEN_APPLE));
						MerchantRecipe r1 = new MerchantRecipe(itemneeded, null, itemresult);
						r1.maxUses = 999999999;
						list.add(r1);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded2 = CraftItemStack.asNMSCopy(new ItemStack(Material.CLAY_BRICK));
						net.minecraft.server.v1_7_R4.ItemStack itemresult2 = CraftItemStack.asNMSCopy(new ItemStack(Material.COOKED_BEEF));
						MerchantRecipe r2 = new MerchantRecipe(itemneeded2, null, itemresult2);
						r2.maxUses = 999999999;
						list.add(r2);
						
						recipes.set(entityVillager, list);
				
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
					break;
				case 1:
					SpeedItem sword1 = new SpeedItem(Material.IRON_SWORD, "§bEpée I");
					sword1.addEnchantment(Enchantment.DAMAGE_ALL, 1);
					ItemMeta m1 = sword1.getItemMeta();
					m1.addEnchant(Enchantment.DURABILITY, 10, true);
					sword1.setItemMeta(m1);
					SpeedItem sword2 = new SpeedItem(Material.IRON_SWORD, "§bEpée II");
					sword2.addEnchantment(Enchantment.DAMAGE_ALL, 2);
					ItemMeta m2 = sword2.getItemMeta();
					m2.addEnchant(Enchantment.DURABILITY, 10, true);
					sword2.setItemMeta(m2);
					SpeedItem sword3 = new SpeedItem(Material.IRON_SWORD, "§bEpée III");
					sword3.addEnchantment(Enchantment.DAMAGE_ALL, 2);
					sword3.addEnchantment(Enchantment.KNOCKBACK, 1);
					ItemMeta m3 = sword3.getItemMeta();
					m3.addEnchant(Enchantment.DURABILITY, 10, true);
					sword3.setItemMeta(m3);
					SpeedItem sword4 = new SpeedItem(Material.GOLD_SWORD, "§bEpée IV");
					ItemMeta m4 = sword4.getItemMeta();
					m4.addEnchant(Enchantment.DURABILITY, 10, true);
					sword4.setItemMeta(m4);
					sword4.addEnchantment(Enchantment.DAMAGE_ALL, 2);
					sword4.addEnchantment(Enchantment.KNOCKBACK, 1);
			        Attributes attributes = new Attributes(sword4);
			        
			        attributes.add(Attribute.newBuilder().name("Damage")
			                .type(AttributeType.GENERIC_ATTACK_DAMAGE).amount(7 + 1.25 * 2).build());
			        
					/*VillagerTradeAPI.addTrade(villager, new VillagerTrade(new ItemStack(Material.IRON_INGOT, 1), sword1));
					VillagerTradeAPI.addTrade(villager, new VillagerTrade(new ItemStack(Material.IRON_INGOT, 3), sword2));
					VillagerTradeAPI.addTrade(villager, new VillagerTrade(new ItemStack(Material.IRON_INGOT, 7), sword3));
					VillagerTradeAPI.addTrade(villager, new VillagerTrade(new ItemStack(Material.IRON_INGOT, 5), new ItemStack(Material.DIAMOND), sword4));
					VillagerTradeAPI.addTrade(villager, new VillagerTrade(new ItemStack(Material.IRON_INGOT, 3), new ItemStack(Material.TNT)));
					VillagerTradeAPI.addTrade(villager, new VillagerTrade(new ItemStack(Material.DIAMOND), new ItemStack(Material.TNT)));
					VillagerTradeAPI.addTrade(villager, new VillagerTrade(new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.FLINT_AND_STEEL)));*/
					villager.setCustomName("Le Chevalier");
					villager.setCustomNameVisible(true);
					
					try {
						Field recipes = entityVillager.getClass().getDeclaredField("bu");
						recipes.setAccessible(true);
						
						MerchantRecipeList list = new MerchantRecipeList();
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded3 = CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_INGOT, 7));
						net.minecraft.server.v1_7_R4.ItemStack itemresult3 = CraftItemStack.asNMSCopy(sword3);
						MerchantRecipe r3 = new MerchantRecipe(itemneeded3, null, itemresult3);
						r3.maxUses = 999999999;
						list.add(r3);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded2 = CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_INGOT, 3));
						net.minecraft.server.v1_7_R4.ItemStack itemresult2 = CraftItemStack.asNMSCopy(sword2);
						MerchantRecipe r2 = new MerchantRecipe(itemneeded2, null, itemresult2);
						r2.maxUses = 999999999;
						list.add(r2);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded = CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_INGOT));
						net.minecraft.server.v1_7_R4.ItemStack itemresult = CraftItemStack.asNMSCopy(sword1);
						MerchantRecipe r1 = new MerchantRecipe(itemneeded, null, itemresult);
						r1.maxUses = 999999999;
						list.add(r1);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded4 = CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_INGOT, 5));
						net.minecraft.server.v1_7_R4.ItemStack itemneeded24 = CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND));
						net.minecraft.server.v1_7_R4.ItemStack itemresult4 = CraftItemStack.asNMSCopy(attributes.getStack());
						MerchantRecipe r4 = new MerchantRecipe(itemneeded4, itemneeded24, itemresult4);
						r4.maxUses = 999999999;
						list.add(r4);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded5 = CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_INGOT, 3));
						net.minecraft.server.v1_7_R4.ItemStack itemresult5 = CraftItemStack.asNMSCopy(new ItemStack(Material.TNT));
						MerchantRecipe r5 = new MerchantRecipe(itemneeded5, null, itemresult5);
						r5.maxUses = 999999999;
						list.add(r5);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded6 = CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND));
						net.minecraft.server.v1_7_R4.ItemStack itemresult6 = CraftItemStack.asNMSCopy(new ItemStack(Material.TNT));
						MerchantRecipe r6 = new MerchantRecipe(itemneeded6, null, itemresult6);
						r6.maxUses = 999999999;
						list.add(r6);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded7 = CraftItemStack.asNMSCopy(new ItemStack(Material.GOLD_INGOT));
						net.minecraft.server.v1_7_R4.ItemStack itemresult7 = CraftItemStack.asNMSCopy(new ItemStack(Material.FLINT_AND_STEEL));
						MerchantRecipe r7 = new MerchantRecipe(itemneeded7, null, itemresult7);
						r7.maxUses = 999999999;
						list.add(r7);
						
						recipes.set(entityVillager, list);
				
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					break;
				case 2:
					ItemStack sandstone = new ItemStack(Material.SANDSTONE, 4);
					sandstone.setDurability((short)2);
					SpeedItem pickaxe1 = new SpeedItem(Material.WOOD_PICKAXE);
					pickaxe1.addEnchantment(Enchantment.DIG_SPEED, 1);
					pickaxe1.getItemMeta().addEnchant(Enchantment.DURABILITY, 10, true);
					SpeedItem pickaxe2 = new SpeedItem(Material.STONE_PICKAXE);
					pickaxe2.addEnchantment(Enchantment.DIG_SPEED, 2);
					pickaxe2.getItemMeta().addEnchant(Enchantment.DURABILITY, 10, true);
					SpeedItem pickaxe3 = new SpeedItem(Material.IRON_PICKAXE);
					pickaxe3.addEnchantment(Enchantment.DIG_SPEED, 3);
					pickaxe3.getItemMeta().addEnchant(Enchantment.DURABILITY, 10, true);
					villager.setCustomName("Le Constructeur");
					villager.setCustomNameVisible(true);
					
					try {
						Field recipes = entityVillager.getClass().getDeclaredField("bu");
						recipes.setAccessible(true);
						
						MerchantRecipeList list = new MerchantRecipeList();
							
						net.minecraft.server.v1_7_R4.ItemStack itemneeded = CraftItemStack.asNMSCopy(new ItemStack(Material.CLAY_BRICK));
						net.minecraft.server.v1_7_R4.ItemStack itemresult = CraftItemStack.asNMSCopy(sandstone);
						MerchantRecipe r = new MerchantRecipe(itemneeded, null, itemresult);
						r.maxUses = 999999999;
						list.add(r);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded1 = CraftItemStack.asNMSCopy(new ItemStack(Material.CLAY_BRICK, 4));
						net.minecraft.server.v1_7_R4.ItemStack itemresult1 = CraftItemStack.asNMSCopy(new ItemStack(Material.ENDER_STONE));
						MerchantRecipe r1 = new MerchantRecipe(itemneeded1, null, itemresult1);
						r1.maxUses = 999999999;
						list.add(r1);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded2 = CraftItemStack.asNMSCopy(new ItemStack(Material.CLAY_BRICK, 4));
						net.minecraft.server.v1_7_R4.ItemStack itemresult2 = CraftItemStack.asNMSCopy(pickaxe1);
						MerchantRecipe r2 = new MerchantRecipe(itemneeded2, null, itemresult2);
						r2.maxUses = 999999999;
						list.add(r2);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded3 = CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_INGOT, 2));
						net.minecraft.server.v1_7_R4.ItemStack itemresult3 = CraftItemStack.asNMSCopy(pickaxe2);
						MerchantRecipe r3 = new MerchantRecipe(itemneeded3, null, itemresult3);
						r3.maxUses = 999999999;
						list.add(r3);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded4 = CraftItemStack.asNMSCopy(new ItemStack(Material.GOLD_INGOT));
						net.minecraft.server.v1_7_R4.ItemStack itemresult4 = CraftItemStack.asNMSCopy(pickaxe3);
						MerchantRecipe r4 = new MerchantRecipe(itemneeded4, null, itemresult4);
						r4.maxUses = 999999999;
						list.add(r4);
						
						recipes.set(entityVillager, list);
				
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
					break;
				case 3: 
					SpeedItem chestplate1 = new SpeedItem(Material.LEATHER_CHESTPLATE);
					chestplate1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					chestplate1.getItemMeta().addEnchant(Enchantment.DURABILITY, 10, true);
					SpeedItem chestplate3 = new SpeedItem(Material.LEATHER_CHESTPLATE);
					chestplate3.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
					chestplate3.getItemMeta().addEnchant(Enchantment.DURABILITY, 10, true);
					
					villager.setCustomName("L'Armurier");
					villager.setCustomNameVisible(true);
					
					try {
						Field recipes = entityVillager.getClass().getDeclaredField("bu");
						recipes.setAccessible(true);
						
						MerchantRecipeList list = new MerchantRecipeList();
							
						net.minecraft.server.v1_7_R4.ItemStack itemneeded = CraftItemStack.asNMSCopy(new ItemStack(Material.CLAY_BRICK, 2));
						net.minecraft.server.v1_7_R4.ItemStack itemresult = CraftItemStack.asNMSCopy(new ItemStack(Material.LEATHER_CHESTPLATE));
						MerchantRecipe r1 = new MerchantRecipe(itemneeded, null, itemresult);
						r1.maxUses = 999999999;
						list.add(r1);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded2 = CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_INGOT, 2));
						net.minecraft.server.v1_7_R4.ItemStack itemresult2 = CraftItemStack.asNMSCopy(chestplate1);
						MerchantRecipe r2 = new MerchantRecipe(itemneeded2, null, itemresult2);
						r2.maxUses = 999999999;
						list.add(r2);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded3 = CraftItemStack.asNMSCopy(new ItemStack(Material.CLAY_BRICK, 15));
						net.minecraft.server.v1_7_R4.ItemStack itemresult3 = CraftItemStack.asNMSCopy(chestplate1);
						MerchantRecipe r3 = new MerchantRecipe(itemneeded3, null, itemresult3);
						r3.maxUses = 999999999;
						list.add(r3);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded4 = CraftItemStack.asNMSCopy(new ItemStack(Material.GOLD_INGOT));
						net.minecraft.server.v1_7_R4.ItemStack itemresult4 = CraftItemStack.asNMSCopy(chestplate3);
						MerchantRecipe r4 = new MerchantRecipe(itemneeded4, null, itemresult4);
						r4.maxUses = 999999999;
						list.add(r4);
						
						net.minecraft.server.v1_7_R4.ItemStack itemneeded5 = CraftItemStack.asNMSCopy(new ItemStack(Material.GOLD_INGOT));
						net.minecraft.server.v1_7_R4.ItemStack itemresult5 = CraftItemStack.asNMSCopy(new ItemStack(Material.COMPASS));
						MerchantRecipe r5 = new MerchantRecipe(itemneeded5, null, itemresult5);
						r5.maxUses = 999999999;
						list.add(r5);
						
						recipes.set(entityVillager, list);
				
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					break;
			}
			
			if(count == 3)
			{
				count = 0;
			}
			else
			{
				count++;
			}
		}
		else
		{
			if(!(e.getEntity() instanceof Item))
			{
				e.setCancelled(true);
			}
			else
			{
				if(((Item) e.getEntity()).getItemStack().getType().equals(Material.BED))
				{
					e.setCancelled(true);
				}
			}
		}
	}
    
    public void setAi(Entity entity) {
    	net.minecraft.server.v1_7_R4.Entity nmsEntity = ((CraftEntity) entity).getHandle();

        NBTTagCompound tag = new NBTTagCompound();

        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        EntityLiving el = (EntityLiving) nmsEntity;
        el.a(tag);
    }

}
