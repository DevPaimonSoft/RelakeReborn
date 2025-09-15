package net.minecraft.client.data.models;

import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.item.Item;

public interface ItemModelOutput {
    void accept(Item pItem, ItemModel.Unbaked pModel);

    void copy(Item pItem1, Item pItem2);
}