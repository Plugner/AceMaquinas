package com.servidoresspace.pmaquinas;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


public final class PMaquinas extends JavaPlugin {

    @Override
    public void onEnable() {

    this.getServer().getPluginManager().registerEvents(new Listeners(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equalsIgnoreCase("maquina")) return true;
        if(!sender.hasPermission("pmaquinas.usar")) return false;
        if(args.length == 0) {
            sender.sendMessage("§cUse: /PMaquinas (1,2)");
            return false;
        }
        String arg1 = args[0];
        if(arg1.equalsIgnoreCase("1")) {
            Player p = (Player)sender;
            giveMaquina(p);
            return false;
        }
        if(arg1.equalsIgnoreCase("2")) {
            Player p = (Player)sender;
            ItemStack is = new ItemStack(Material.COAL, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName("§8Combustível");
            is.setItemMeta(im);
            p.getInventory().addItem(is);
            return false;
        }

















        return false;
    }
    public static void giveMaquina(Player p) {
        ItemStack is = new ItemStack(Material.SNOW_BLOCK, 1);
        ItemMeta im = is.getItemMeta();
        im.addEnchant(Enchantment.DURABILITY, 5, true);
        im.setDisplayName("§cMáquina de §0§lBedrock");
        is.setItemMeta(im);
        p.getInventory().addItem(is);
    }
}
