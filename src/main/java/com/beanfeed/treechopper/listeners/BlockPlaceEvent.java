package com.beanfeed.treechopper.listeners;

import com.beanfeed.treechopper.TreeChopper;
import com.beanfeed.treechopper.files.PlacedBlocks;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(org.bukkit.event.block.BlockPlaceEvent e) {
        Block block = e.getBlock();

        if (TreeChopper.LOGS.contains(block.getType()) || TreeChopper.ORES.contains(block.getType())) {
            int blockX = block.getLocation().getBlockX();
            int blockY = block.getLocation().getBlockY();
            int blockZ = block.getLocation().getBlockZ();

            String path = blockX + "," + blockY + "," + blockZ;

            PlacedBlocks.get().set(path, 1);
            PlacedBlocks.save();
        }
    }
}
