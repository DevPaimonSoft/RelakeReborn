package net.minecraft.client.gui.screens.worldselection;

import java.nio.file.Path;
import javax.annotation.Nullable;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.level.storage.PrimaryLevelData;

@FunctionalInterface
public interface CreateWorldCallback {
    boolean create(CreateWorldScreen pCreateWorldScreen, LayeredRegistryAccess<RegistryLayer> pRegistryAccess, PrimaryLevelData pLevelData, @Nullable Path pTempDataPackDir);
}