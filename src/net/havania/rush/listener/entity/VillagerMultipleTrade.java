package net.havania.rush.listener.entity;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftVillager;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EntityVillager;
import net.minecraft.server.v1_7_R4.MerchantRecipeList;

public class VillagerMultipleTrade implements Listener
{
    @EventHandler
    public void handleRightClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager) {
            event.setCancelled(true);
            EntityVillager craft = ((CraftVillager)event.getRightClicked()).getHandle();
            this.fakeTrade(craft, ((CraftPlayer)event.getPlayer()).getHandle());
        }
    }
    
    public void fakeTrade(EntityVillager copy, EntityPlayer player) {
        try {
            EntityVillager villager = new EntityVillager(player.world, 0);
            Field recipesField = EntityVillager.class.getDeclaredField("bu");
            recipesField.setAccessible(true);
            MerchantRecipeList recipeList = (MerchantRecipeList)recipesField.get(copy);
            villager.setCustomName(copy.getCustomName());
            villager.setCustomNameVisible(true);
            if (recipeList == null) {
                recipeList = new MerchantRecipeList();
                recipesField.set(villager, recipeList);
            }
            
            recipesField.set(villager, recipeList);
            villager.a((EntityHuman)player);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
