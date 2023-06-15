package me.otavio.treefella;

import com.google.common.collect.ImmutableList;
import me.otavio.treefella.files.PlacedBlocks;
import me.otavio.treefella.listeners.BlockBreakEvent;
import me.otavio.treefella.listeners.BlockPlaceEvent;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class TreeFella extends JavaPlugin {

    public static final ImmutableList<Material> LOGS = ImmutableList.of(
            Material.ACACIA_LOG,
            Material.BIRCH_LOG,
            Material.JUNGLE_LOG,
            Material.OAK_LOG,
            Material.MANGROVE_LOG,
            Material.SPRUCE_LOG,
            Material.DARK_OAK_LOG,
            Material.CHERRY_LOG
    );

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        PlacedBlocks.setup();
        PlacedBlocks.get().options().copyDefaults(true);
        PlacedBlocks.save();

        this.getServer().getPluginManager().registerEvents(new BlockPlaceEvent(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreakEvent(), this);
    }
}
