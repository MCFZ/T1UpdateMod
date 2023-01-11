package com.tone.t1updatemod;

import com.tone.t1updatemod.download.DownloadManager;
import com.tone.t1updatemod.download.DownloadStatus;
import com.tone.t1updatemod.download.DownloadWindow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = T1UpdateMod.MODID, name = T1UpdateMod.NAME, clientSideOnly = true, acceptedMinecraftVersions = "[1.12]", version = T1UpdateMod.VERSION)
public class T1UpdateMod {
    public final static String MODID = "t1_mod";
    public final static String NAME = "T1 Update Mod";
    public final static String VERSION = "1.12.2-1.0.1";
    public final static String FILE_URL = "https://t-1down.oss-cn-chengdu.aliyuncs.com/Minecraft-Mod-1.0.1.zip";
    public final static String FILE_NAME = "Minecraft-Mod-1.0.1.zip";
    public static final Logger logger = LogManager.getLogger(MODID);


    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) throws InterruptedException {
        if (T1Utils.checkLength()) {
            logger.info("检测到资源包可用，跳过下载阶段");
            T1Utils.setupResourcesPack();
        } else {
            // 开始下载资源包并弹出进度窗口
            DownloadManager downloader = new DownloadManager(FILE_URL, FILE_NAME, Minecraft.getMinecraft().getResourcePackRepository().getDirResourcepacks().toString());
            DownloadWindow window = new DownloadWindow(downloader);
            window.showWindow();
            downloader.start();
            int i = 600;
            while (!downloader.isDone() && i >= 0) {
                Thread.sleep(50);
                if (i == 0) {
                    window.hide();
                }
                i--;
            }
            if (downloader.getStatus() == DownloadStatus.SUCCESS) {
                T1Utils.setupResourcesPack();
            }
        }
    }

}
