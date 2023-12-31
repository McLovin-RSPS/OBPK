package com.ferox.game.content.items.mystery_box.impl;

import com.ferox.GameServer;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.ferox.game.content.collection_logs.LogType.MYSTERY_BOX;
import static com.ferox.util.CustomItemIdentifiers.RAIDS_MYSTERY_BOX;
import static com.ferox.util.ItemIdentifiers.AVERNIC_DEFENDER;
import static com.ferox.util.ItemIdentifiers.SANGUINESTI_STAFF;

public class RaidsMysteryBox extends PacketInteraction {

    private boolean broadcast = false;

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (option == 1) {
            if (item.getId() == RAIDS_MYSTERY_BOX) {
                open(player);
                return true;
            }
        }
        return false;
    }

    private void open(Player player) {
        if (player.inventory().contains(RAIDS_MYSTERY_BOX)) {
            player.inventory().remove(new Item(RAIDS_MYSTERY_BOX), true);
            Item reward = rollReward();
            MYSTERY_BOX.log(player, RAIDS_MYSTERY_BOX, reward);
            player.inventory().add(reward, true);
            Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received a "+reward.unnote().name()+" from a raids mystery box.", "box_and_tickets");
            int count = player.<Integer>getAttribOr(AttributeKey.RAIDS_MYSTERY_BOXES_OPENED, 0) + 1;
            player.putAttrib(AttributeKey.RAIDS_MYSTERY_BOXES_OPENED, count);
            if(broadcast && !player.getUsername().equalsIgnoreCase("Box test")) {
                String worldMessage = "<img=505><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Raids Mystery Box</col>]</shad>:<col=AD800F> " + player.getUsername() + " received a <shad=0>" + reward.name() + "</shad>!";
                World.getWorld().sendWorldMessage(worldMessage);
            }
        }
    }

    private static final int EXTREME_RARE_ROLL = GameServer.properties().nerfDropRateBoxes ? 30 : 100;
    private static final int SUPER_RARE_ROLE = GameServer.properties().nerfDropRateBoxes ? 14 : 15;
    private static final int RARE_ROLL = GameServer.properties().nerfDropRateBoxes ? 10 : 15;
    private static final int UNCOMMON_ROLL = GameServer.properties().nerfDropRateBoxes ? 5 : 6;

    private static final Item[] EXTREMELY_RARE = new Item[]{
        new Item(CustomItemIdentifiers.TWISTED_BOW_I),
        new Item(CustomItemIdentifiers.HOLY_SCYTHE_OF_VITUR),
        new Item(CustomItemIdentifiers.HOLY_SANGUINESTI_STAFF),
    };

    private static final Item[] SUPER_RARE = new Item[]{
        new Item(ItemIdentifiers.TWISTED_BOW),
        new Item(ItemIdentifiers.SCYTHE_OF_VITUR),
        new Item(CustomItemIdentifiers.HOLY_GHRAZI_RAPIER)
    };

    private static final Item[] RARE = new Item[]{
        new Item(CustomItemIdentifiers.TWISTED_BOW_KIT),
        new Item(CustomItemIdentifiers.SHADOW_INQUISITOR_ORNAMENT_KIT),
        new Item(ItemIdentifiers.SANGUINESTI_STAFF)
    };

    private static final Item[] UNCOMMON = new Item[]{
        new Item(ItemIdentifiers.GHRAZI_RAPIER),
        new Item(ItemIdentifiers.ELDER_MAUL),
        new Item(AVERNIC_DEFENDER),
        new Item(ItemIdentifiers.ANCESTRAL_ROBE_TOP),
        new Item(ItemIdentifiers.ANCESTRAL_ROBE_BOTTOM)
    };

    private static final Item[] COMMON = new Item[]{
        new Item(ItemIdentifiers.DRAGON_CLAWS),
        new Item(ItemIdentifiers.KODAI_WAND),
        new Item(ItemIdentifiers.ANCESTRAL_HAT),
    };

    public Item rollReward() {
        if (Utils.rollDie(EXTREME_RARE_ROLL,1)) {
            broadcast = true;
            return Utils.randomElement(EXTREMELY_RARE);
        } else if (Utils.rollDie(SUPER_RARE_ROLE,1)) {
            broadcast = true;
            return Utils.randomElement(SUPER_RARE);
        } else if (Utils.rollDie(RARE_ROLL,1)) {
            broadcast = true;
            return Utils.randomElement(RARE);
        } else if (Utils.rollDie(UNCOMMON_ROLL,1)) {
            broadcast = false;
            return Utils.randomElement(UNCOMMON);
        } else {
            broadcast = false;
            return Utils.randomElement(COMMON);
        }
    }
}
