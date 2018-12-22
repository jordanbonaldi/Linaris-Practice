package net.linaris.Practice.utils;

import org.yaml.snakeyaml.external.biz.base64Coder.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.util.io.*;
import java.io.*;

public class InventoryStringDeSerializer
{
    public static String toBase64(final Inventory inventory) {
        return toBase64(inventory.getContents());
    }
    
    public static String toBase64(final ItemStack itemstack) {
        final ItemStack[] arr = { itemstack };
        return toBase64(arr);
    }
    
    public static String toBase64(final ItemStack[] contents) {
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream((OutputStream)outputStream);
            dataOutput.writeInt(contents.length);
            for (final ItemStack stack : contents) {
                dataOutput.writeObject((Object)stack);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray()).replace("\n", "").replace("\r", "");
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
    
    public static Inventory inventoryFromBase64(final String data) throws IOException {
        final ItemStack[] stacks = stacksFromBase64(data);
        final Inventory inventory = Bukkit.createInventory((InventoryHolder)null, (int)Math.ceil(stacks.length / 9.0) * 9);
        for (int i = 0; i < stacks.length; ++i) {
            inventory.setItem(i, stacks[i]);
        }
        return inventory;
    }
    
    public static ItemStack[] stacksFromBase64(final String data) throws IOException {
        try {
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            final BukkitObjectInputStream dataInput = new BukkitObjectInputStream((InputStream)inputStream);
            final ItemStack[] stacks = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < stacks.length; ++i) {
                stacks[i] = (ItemStack)dataInput.readObject();
            }
            dataInput.close();
            return stacks;
        }
        catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
