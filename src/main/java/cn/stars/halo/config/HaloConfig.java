package cn.stars.halo.config;

import cn.stars.halo.Halo;
import cn.stars.halo.HaloCore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HaloConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("halo_config.json");
    // 所有支持的角色名
    private static final String[] HALOS_ARRAY = new String[]{"none", "aris", "aru", "azusa", "hanako", "hasumi", "hifumi", "hina", "hoshino", "iroha", "izuna", "koharu", "mari", "midori", "mika", "miku", "momoi", "noa", "shiroko", "tsurugi", "yuuka"};
    public static final ArrayList<String> HALOS = new ArrayList<>(List.of(HALOS_ARRAY));

    private static ConfigData config = new ConfigData();

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (var reader = Files.newBufferedReader(CONFIG_PATH)) {
                config = GSON.fromJson(reader, ConfigData.class);
            } catch (IOException e) {
                HaloCore.logger.error("无法读取配置. 将使用默认配置. {}", e.getMessage());
                config = new ConfigData();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (var writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            HaloCore.logger.error("无法保存配置. {}", e.getMessage());
        }
    }

    public static String getHaloType() {
        return config.haloType;
    }

    public static void setHaloType(String type) {
        config.haloType = type;
        save();
    }

    public static int getAngleOffset() {
        return config.angleOffset;
    }

    public static void setAngleOffset(int angleOffset) {
        config.angleOffset = angleOffset;
    }

    public static double getHeightOffset() {
        return config.heightOffset;
    }

    public static void setHeightOffset(double heightOffset) {
        config.heightOffset = heightOffset;
    }

    public static boolean isFirstPersonVisible() {
        return config.firstPersonVisible;
    }

    public static void setFirstPersonVisible(boolean firstPersonVisible) {
        config.firstPersonVisible = firstPersonVisible;
    }

    public static boolean checkIfNone() {
        return config.haloType.equals("none");
    }

    public static class ConfigData {
        public String haloType = "aris";
        public boolean firstPersonVisible = true;
        public int angleOffset = 0;
        public double heightOffset = 0;
    }
}