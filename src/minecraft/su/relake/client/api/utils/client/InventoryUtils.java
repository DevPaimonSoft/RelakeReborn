package su.relake.client.api.utils.client;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.util.profiling.jfr.event.PacketEvent;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import su.relake.client.api.utils.render.util.IMinecraft;

import java.util.Arrays;
import java.util.List;

public class InventoryUtils implements IMinecraft {
    private static int cachedSlot = -1;

    public static int getItemSlot(Item input) {
        for (ItemStack stack : mc.player.getArmorSlots()) {
            if (stack.getItem() == input) {
                return -2;
            }
        }
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack s = mc.player.getInventory().getItem(i);
            if (s.getItem() == input) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot = slot + 36;
        }
        return slot;
    }

    public static int getSlotInInventoryOrHotbar(Item item, boolean inHotBar) {
        int firstSlot =  0;
        int lastSlot = inHotBar ? 9 : 36;
        int finalSlot = -1;
        for (int i = firstSlot; i < lastSlot; i++) {
            if (mc.player.getInventory().getItem(i).getItem() == item) {
                finalSlot = i;
            }
        }

        return finalSlot;
    }

    public static SearchInvResult findInHotBar(Searcher searcher) {
        if (mc.player != null) {
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = mc.player.getInventory().getItem(i);
                if (searcher.isValid(stack)) {
                    return new SearchInvResult(i, true, stack);
                }
            }
        }

        return SearchInvResult.notFound();
    }

    public static SearchInvResult findInInventory(Searcher searcher) {
        if (mc.player != null) {
            for (int i = 36; i >= 0; i--) {
                ItemStack stack = mc.player.getInventory().getItem(i);
                if (searcher.isValid(stack)) {
                    if (i < 9) i += 36;
                    return new SearchInvResult(i, true, stack);
                }
            }
        }

        return SearchInvResult.notFound();
    }

    public static SearchInvResult findItemInHotBar(List<Item> items) {
        return findInHotBar(stack -> items.contains(stack.getItem()));
    }

    public static SearchInvResult findItemInHotBar(Item... items) {
        return findItemInHotBar(Arrays.asList(items));
    }

    public static SearchInvResult findItemInInventory(List<Item> items) {
        return findInInventory(stack -> items.contains(stack.getItem()));
    }

    public static SearchInvResult findItemInInventory(Item... items) {
        return findItemInInventory(Arrays.asList(items));
    }

    public static void moveItem(int from, int to, boolean air) {
//        if(AutoTotem.isSwapProcess){
//            return;
//        }
        new Thread(() -> {
        if (from == to) {
            return;
        }
        try {
            if(mc.player.wasSprinting) {
                mc.player.setSprinting(false);
                mc.options.keySprint.setDown(false);
            }
//            AutoTotem.isSwapProcess = true;

            Thread.sleep(60);
            pickupItemSwaping(from);
            pickupItemSwaping(to);
            if (air) {
                pickupItemSwaping(from);
            }
            mc.player.connection.send(new ServerboundContainerClosePacket(mc.player.containerMenu.containerId));
//            AutoTotem.isSwapProcess = false;
        }catch (Exception e){}
        }).start();
    }

    public static void moveItem2(int from, int to, boolean air) {

        if (from == to)
            return;
        pickupItem2(from, 0);
        pickupItem2(to, 0);
        if (air)
            pickupItem2(from, 0);
    }

    public static void pickupItem(int id) {

        if (id == -1 || mc.player == null )return;
        NonNullList<Slot> nonnulllist = mc.player.containerMenu.slots;
        int i = nonnulllist.size();
        List<ItemStack> list = Lists.newArrayListWithCapacity(i);

        for(Slot slot : nonnulllist) {
            list.add(slot.getItem().copy());
        }

        Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap<>();
        for(int j = 0; j < i; ++j) {
            ItemStack itemstack = list.get(j);
            ItemStack itemstack1 = nonnulllist.get(j).getItem();
            if (!ItemStack.matches(itemstack, itemstack1)) {
                int2objectmap.put(j, itemstack1.copy());
            }
        }
        mc.player.containerMenu.clicked(id, 0, ClickType.PICKUP, mc.player);
        mc.player.connection.send(new ServerboundContainerClickPacket(mc.player.containerMenu.containerId, mc.player.containerMenu.getStateId(), id, 0, ClickType.PICKUP, mc.player.containerMenu.getCarried().copy(),int2objectmap));
    }

    public static void pickupItemSwaping(int id) {

        if (id == -1 || mc.player == null )return;
        NonNullList<Slot> nonnulllist = mc.player.containerMenu.slots;
        int i = nonnulllist.size();
        List<ItemStack> list = Lists.newArrayListWithCapacity(i);

        for(Slot slot : nonnulllist) {
            list.add(slot.getItem().copy());
        }

        Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap<>();
        for(int j = 0; j < i; ++j) {
            ItemStack itemstack = list.get(j);
            ItemStack itemstack1 = nonnulllist.get(j).getItem();
            if (!ItemStack.matches(itemstack, itemstack1)) {
                int2objectmap.put(j, itemstack1.copy());
            }
        }
        mc.player.containerMenu.clicked(id, 8, ClickType.SWAP, mc.player);
        mc.player.connection.send(new ServerboundContainerClickPacket(mc.player.containerMenu.containerId, mc.player.containerMenu.getStateId(), id, 8, ClickType.SWAP, mc.player.containerMenu.getCarried().copy(),int2objectmap));
    }

    public static void pickupItem2(int slot, int button) {
        mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, slot, 1, ClickType.PICKUP, mc.player);
    }

    public static void pickupItem4(int slot, int button) {
        mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, slot, 1, ClickType.PICKUP, mc.player);
    }

    public static void pickupItem3(int slot, int button) {
        mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, slot, 0, ClickType.PICKUP, mc.player);
    }

    public static int getAxeInInventory(boolean inHotBar) {
        int firstSlot = inHotBar ? 0 : 9;
        int lastSlot = inHotBar ? 9 : 36;

        for (int i = firstSlot; i < lastSlot; i++) {
            if (mc.player.getInventory().getItem(i).getItem() instanceof AxeItem) {
                return i;
            }
        }
        return -1;
    }

    public  static int findBestSlotInHotBar() {
        int emptySlot = findEmptySlot();
        if (emptySlot != -1) {
            return emptySlot;
        } else {
            return findNonSwordSlot();
        }
    }

    public static int findEmptySlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getItem(i).isEmpty() && mc.player.getInventory().selected != i) {
                return i;
            }
        }
        return -1;
    }

    public static void saveSlot() {
        cachedSlot = mc.player.getInventory().selected;
    }

    public static void returnSlot() {
        if (cachedSlot != -1) {
            pickupItem(cachedSlot);
        }
        cachedSlot = -1;
    }

    public static int findEmptySlot(boolean inHotBar) {
        int start = inHotBar ? 0 : 9;
        int end = inHotBar ? 9 : 45;

        for (int i = start; i < end; ++i) {
            if (!mc.player.getInventory().getItem(i).isEmpty()) {
                continue;
            }

            return i;
        }
        return -1;
    }

    public static Item getItem(String Name) {
        if(Name == null) return Items.AIR;
        for (Block block : BuiltInRegistries.BLOCK)
            if (block.getDescriptionId().replace("block.minecraft.","").equals(Name.toLowerCase()))
                return Item.byBlock(block);
        for (Item item : BuiltInRegistries.ITEM)
            if (item.getDescriptionId().replace("item.minecraft.","").equals(Name.toLowerCase()))
                return item;
        return Items.DIRT;
    }
//!(mc.player.getInventory().getItem(i).getItem() instanceof ElytraItem)
    public static int findNonSwordSlot() {
        for (int i = 0; i < 9; i++) {
            if (!(mc.player.getInventory().getItem(i).getItem() instanceof SwordItem)  && mc.player.getInventory().selected != i) {
                return i;
            }
        }
        return -1;
    }

    public static class Hand {
        public static boolean isEnabled;
        private boolean isChangingItem;
        private int originalSlot = -1;

        public void onEventPacket(PacketEvent packetEvent) {
//            if (!packetEvent.getType().equals(PacketEvent.Type.RECEIVE)) {
//                return;
//            }
//            if (packetEvent.getPacket() instanceof ClientboundSetCarriedItemPacket) {
//                this.isChangingItem = true;
//            }
        }

        public void handleItemChange(boolean resetItem) {
            if (this.isChangingItem && this.originalSlot != -1) {
                isEnabled = true;
                mc.player.getInventory().selected = this.originalSlot;
                if (resetItem) {
                    this.isChangingItem = false;
                    this.originalSlot = -1;
                    isEnabled = false;
                }
            }
        }

        public void setOriginalSlot(int slot) {
            this.originalSlot = slot;
        }
    }

    public interface Searcher {
        boolean isValid(ItemStack stack);
    }
}
