package me.otavio.treefella.listeners;

import com.google.common.collect.ImmutableList;
import me.otavio.treefella.TreeFella;
import me.otavio.treefella.files.PlacedBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockBreakEvent implements Listener {

    private final ImmutableList<Material> AXES = ImmutableList.of(
            Material.DIAMOND_AXE,
            Material.GOLDEN_AXE,
            Material.IRON_AXE,
            Material.STONE_AXE,
            Material.NETHERITE_AXE,
            Material.WOODEN_AXE
    );

    @EventHandler
    public void onBlockBreakEvent(org.bukkit.event.block.BlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack itemInHand = p.getInventory().getItemInMainHand();
        Block b = e.getBlock();

        int blockX = b.getLocation().getBlockX();
        int blockY = b.getLocation().getBlockY();
        int blockZ = b.getLocation().getBlockZ();

        String path = blockX + "," + blockY + "," + blockZ;

        boolean isPlayerPlacedBlock = PlacedBlocks.get().isInt(path);

        if (AXES.contains(itemInHand.getType()) && !isPlayerPlacedBlock && p.isSneaking() && TreeFella.LOGS.contains(b.getType())) {
            this.checkNearbyBlocks(b, p);

            boolean noMoreWood = false;
            int yAxis = 1;

            while (!noMoreWood) {
                Location bloc = b.getLocation().clone();

                bloc.add(0, yAxis, 0);

                Block nextBlock = bloc.getBlock();

                ItemStack currentItem = p.getInventory().getItemInMainHand();

                if (TreeFella.LOGS.contains(nextBlock.getType()) && AXES.contains(currentItem.getType())) {
                    nextBlock.breakNaturally();
                    this.checkNearbyBlocks(nextBlock, p);
                } else {
                    noMoreWood = true;
                }

                yAxis++;
            }
        } else if (isPlayerPlacedBlock && TreeFella.LOGS.contains(b.getType())) {
            PlacedBlocks.get().set(path, null);
        }
    }

    public void checkNearbyBlocks(Block block, Player player) {
        int x = -1;
        int y = -1;
        int z = -1;

        boolean axeIsBroken = false;

        for (int k = 0; k < 3; k++) {

            for (int i = 0; i < 3; i++) {

                for (int j = 0; j < 3; j++) {
                    Location center = block.getLocation().clone();

                    center.add(x, y, z);

                    Block nearbyBlock = center.getBlock();

                    if (TreeFella.LOGS.contains(nearbyBlock.getType())) {
                        nearbyBlock.breakNaturally();

                        ItemStack axe = player.getInventory().getItemInMainHand();

                        int axeDurability = axe.getType().getMaxDurability();

                        ItemMeta meta = axe.getItemMeta();

                        Damageable dmg = (Damageable) meta;

                        if (dmg.getDamage() <= axeDurability) {
                            dmg.setDamage(dmg.getDamage() + 1);

                            axe.setItemMeta(dmg);

                            player.getInventory().setItemInMainHand(axe);
                        } else {
                            player.getInventory().setItemInMainHand(null);
                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);
                            axeIsBroken = true;
                            break;
                        }

                        this.checkNearbyBlocks(nearbyBlock, player);
                    }

                    if (axeIsBroken) break;

                    z++;
                }

                if (axeIsBroken) break;

                x++;
                z = -1;
            }

            if (axeIsBroken) break;

            y++;
            x = -1;
            z = -1;
        }
    }
}
