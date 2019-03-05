package com.servidoresspace.pmaquinas;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Listeners implements Listener {
    public Map<Location,Boolean> active = new HashMap<>();
    @EventHandler
    public void onPutMachine(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Block b =e.getBlockPlaced();

        ItemStack is = p.getItemInHand();
        if(is.hasItemMeta()) {
            if(is.getItemMeta().hasDisplayName()){
                if(is.getItemMeta().getDisplayName().equals("§cMáquina de §0§lBedrock")) {

                    b.setMetadata("maquina", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("AceMaquinas"),1));
                }
            }
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack hand = p.getItemInHand();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Block b = p.getTargetBlock((Set<Material>)null, 5);
            if(b.hasMetadata("maquina")) {
                //
                if(hand.hasItemMeta()) {
                    if(hand.getItemMeta().hasDisplayName()){
                        if(hand.getItemMeta().getDisplayName().equals("§8Combustível")) {
                            // Start Machine
                            if(!active.containsKey(b.getLocation())) {
                                startMachine(b.getLocation().add(+0.5,0.0,+1),p,b);
                                if(hand.getAmount() == 1) {
                                    p.getInventory().remove(hand);
                                }else{
                                    hand.setAmount(hand.getAmount()-1);
                                }

                            }else {
                                p.sendMessage("§cEsta máquina já está iniciada!");
                            }


                        }
                    }
                }
                if(hand.getType() == Material.AIR) {
                    Inventory inv = Bukkit.createInventory(null,3*9, "§6§lMáquina de BEDROCK");
                    int slotcounter = 0;
                    ItemStack on = new ItemStack(Material.SLIME_BALL);
                    ItemMeta onm = on.getItemMeta();
                    onm.setDisplayName("§aMaquina ativada!");
                    on.setItemMeta(onm);

                    ItemStack off = new ItemStack(Material.REDSTONE_BLOCK);
                    ItemMeta offm = off.getItemMeta();
                    offm.setDisplayName("§aMaquina ativada!");
                    off.setItemMeta(offm);


                    while(!(slotcounter >= 36)) {
                    if(active.containsKey(b.getLocation())) {

                        inv.setItem(slotcounter, on);
                    }else{
                        inv.setItem(slotcounter,off);
                    }
                        slotcounter++;
                    }
                    p.openInventory(inv);

                }
                //
            }

        }
    }
    private void startMachine(Location l, Player p, Block b) {

        active.put(b.getLocation(), true);
        Location l2 = l.add(+0.0,+0.5,+0.0);
        Location l3 = l.add(0.0,+0.5,0.0);
        ArmorStand as = (ArmorStand)l.getWorld().spawnEntity(l2, EntityType.ARMOR_STAND);
        as.setGravity(false);
        as.setCustomNameVisible(true);
        as.setVisible(false);
        as.setCanPickupItems(false);
        as.setSmall(true);

        ArmorStand as1 = (ArmorStand)l.getWorld().spawnEntity(l2.add(0,-0.35, 0), EntityType.ARMOR_STAND);
        as1.setGravity(false);
        as1.setCustomNameVisible(true);
        as1.setVisible(false);
        as1.setCanPickupItems(false);
        as1.setSmall(true);

        ArmorStand as2 = (ArmorStand)l.getWorld().spawnEntity(l2.add(0,-0.40, 0), EntityType.ARMOR_STAND);
        as2.setGravity(false);
        as2.setCustomNameVisible(true);
        as2.setVisible(false);
        as2.setCanPickupItems(false);
        as2.setSmall(true);

        ArmorStand as3 = (ArmorStand)l.getWorld().spawnEntity(l2.add(0,-0.45, 0), EntityType.ARMOR_STAND);
        MPlayer mp = MPlayer.get(p);
        String facnome = mp.getFaction().getName();
        as3.setGravity(false);
        as3.setCustomNameVisible(true);
        as3.setVisible(false);
        as3.setCanPickupItems(false);
        as3.setSmall(true);

        boolean finished = false;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("AceMaquinas"), new BukkitRunnable() {
            int times = 0;
            int seconds = 60;
            int counter = 0;
            public void run() {
                if(seconds > 0 && seconds < 61) {
                    seconds--;
                    if(times != 3) {
                        times = times +1;
                    }
                    if(times == 3) {
                        l3.getWorld().dropItem(l3, new ItemStack(Material.BEDROCK));
                        counter++;
                        times = 0;
                    }
                    as.setCustomName("§6§lMÁQUINA §fDelay: §a" +seconds+"s");
                    as1.setCustomName("§7Esta máquina lhe dará uma BEDROCK a cada 3 segundos");
                    as2.setCustomName("§7A máquina já lhe deu " + counter + " bedrocks.");
                    MPlayer mp = MPlayer.get(p);
                    String facnome = mp.getFaction().getName();
                    if(mp.getFaction().getId().equalsIgnoreCase("none")) {
                        if(p.getCustomName() == null) as3.setCustomName("§7Dono da Máquina: §8#§7Sem facção"+ "§6 "+ p.getName());
                        if(p.getCustomName() != null) as3.setCustomName("§7Dono da Máquina: §8#§7Sem facção"+ "§6 "+ p.getCustomName());
                    }else{
                        if(p.getCustomName() == null) as3.setCustomName("§7Dono da Máquina: §8#§a"+facnome+  "§6 "+ p.getName());
                        if(p.getCustomName() != null) as3.setCustomName("§7Dono da Máquina: §8#§a"+facnome+  "§6 "+ p.getCustomName());
                    }


                }

                if(seconds == 0 ) {
                    p.sendMessage("§cSua máquina expirou ):");
                    as.setCustomName("§6§lMÁQUINA §cDesativada!");
                    as1.setCustomName("§7Você pegou no total " + 60/3 + " bedrocks.");
                    as2.setCustomName("§7Para reativa-la coloque mais combustível");

                    seconds = 62;


                    Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("AceMaquinas"), new BukkitRunnable() {
                        int times = 0;
                        public void run() {
                            if(times == 0) {
                                times++;
                            }else{
                                as.remove();
                                as1.remove();
                                as2.remove();
                                as3.remove();
                                active.remove(b.getLocation());
                                cancel();

                            }

                        }
                    },0,20*2);



                }
            }




        },0L,20);

    }
    @EventHandler
    public void onBreakAMachine(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if(e.getBlock().hasMetadata("maquina") && e.getBlock().getType() == Material.SNOW_BLOCK) {
            e.setCancelled(true);
            if(active.containsKey(e.getBlock().getLocation())) {
                p.sendMessage("§6§lMÁQUINA §r§7Esta máquina está ativada, portanto você não pode quebra-la.");
                return;
            }
            e.getBlock().setType(Material.AIR);
            PMaquinas.giveMaquina(p);
            active.remove(e.getBlock().getLocation());
        }
    }
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getName().equals("§6§lMáquina de BEDROCK")) {
            e.setCancelled(true);
        }else{
            return;
        }
    }
}
