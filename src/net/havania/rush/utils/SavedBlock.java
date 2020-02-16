package net.havania.rush.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

public class SavedBlock {

    private Location location;
    private Material material;
    private byte data;
   
    @SuppressWarnings("deprecation")
    public SavedBlock(BlockState block){
        this.location = block.getLocation();
        this.material = block.getType();
        this.data = block.getData().getData();
    }
   
    public Location getLocation(){
        return location;
    }
   
    public Material getMaterial(){
        return material;
    }
   
    public byte getData(){
        return data;
    }
   
}
 