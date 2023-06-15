package me.otavio.treefella.listeners;

import me.otavio.treefella.TreeFella;
import me.otavio.treefella.files.PlacedBlocks;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(org.bukkit.event.block.BlockPlaceEvent e) {
        Block block = e.getBlock();

        if (TreeFella.LOGS.contains(block.getType())) {
            int blockX = block.getLocation().getBlockX();
            int blockY = block.getLocation().getBlockY();
            int blockZ = block.getLocation().getBlockZ();

            String path = blockX + "," + blockY + "," + blockZ;

            PlacedBlocks.get().set(path, 1);
            PlacedBlocks.save();
        }
    }
}
