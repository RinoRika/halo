package cn.stars.halo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Stars
 */
public class HaloCore {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    public static String version = "v1.0.0";
    public static Logger logger = LogManager.getLogger("Halo");
    public static final String prefix = "§f[§b§lHalo§f] ";

    public static void showMessage(String msg) {
        if (mc.player != null) {
            mc.player.sendMessage(Text.of(prefix + msg), false);
        }
    }

    public static void showMessageTranslated(String msg) {
        if (mc.player != null) {
            mc.player.sendMessage(Text.empty().append(prefix).append(Text.translatable(msg)), false);
        }
    }
}
