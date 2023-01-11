package com.tone.t1updatemod;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.settings.GameSettings;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import static com.tone.t1updatemod.T1UpdateMod.FILE_NAME;

public class T1Utils {
    public T1Utils() {
        throw new UnsupportedOperationException("no instance");
    }

    public static boolean checkLength() {
        File f = new File(Minecraft.getMinecraft().getResourcePackRepository().getDirResourcepacks().toString(), FILE_NAME);
        try {
            URL url = new URL("https://t-1down.oss-cn-chengdu.aliyuncs.com/" + FILE_NAME);
            long file_length = f.length();
            long remote_length = url.openConnection().getContentLengthLong();
            T1UpdateMod.logger.info("File Length: " + file_length);
            T1UpdateMod.logger.info("Remote Length: " + remote_length);
            return remote_length == file_length;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void reloadResources() {
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings gameSettings = mc.gameSettings;
        ResourcePackRepository resourcePackRepository = mc.getResourcePackRepository();
        resourcePackRepository.updateRepositoryEntriesAll();
        List<ResourcePackRepository.Entry> repositoryEntriesAll = resourcePackRepository.getRepositoryEntriesAll();
        List<ResourcePackRepository.Entry> repositoryEntries = Lists.newArrayList();
        Iterator<String> it = gameSettings.resourcePacks.iterator();


        while (it.hasNext()) {
            String packName = it.next();
            for (ResourcePackRepository.Entry entry : repositoryEntriesAll) {
                if (entry.getResourcePackName().equals(packName)) {
                    if (entry.getPackFormat() == 3 || gameSettings.incompatibleResourcePacks.contains(entry.getResourcePackName())) {
                        repositoryEntries.add(entry);
                        break;
                    }
                    // 否则移除
                    it.remove();
                    T1UpdateMod.logger.warn("移除资源包 {}，因为它无法兼容当前版本", entry.getResourcePackName());
                }
            }
        }
        resourcePackRepository.setRepositories(repositoryEntries);
    }

    public static void setupResourcesPack() {
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings gameSettings = mc.gameSettings;
        if (!gameSettings.resourcePacks.contains(FILE_NAME)) {
            mc.gameSettings.resourcePacks.add(FILE_NAME);
            T1Utils.reloadResources();
        }
    }
}

