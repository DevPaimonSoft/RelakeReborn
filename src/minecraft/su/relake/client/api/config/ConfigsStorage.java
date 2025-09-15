package su.relake.client.api.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigsStorage {
    public final Logger logger = Logger.getLogger(ConfigsStorage.class.getName());

    public final File CONFIG_DIR = new File(Minecraft.getInstance().gameDirectory, "relake/configs");
    public final File LAST_CONFIG_FILE = new File(Minecraft.getInstance().gameDirectory, "relake/last_configs.cfg");

    private String currentConfigName = "default";
    private boolean configSaved = false;
    public final JsonParser jsonParser = new JsonParser();

    public void init() throws IOException {
        setupFolder();
        loadLastConfig();
        registerShutdownHook();
    }

    public void setupFolder() {
        if (!CONFIG_DIR.exists()) {
            CONFIG_DIR.mkdirs();
            logger.log(Level.INFO, "Создана папка для конфигов: " + CONFIG_DIR.getAbsolutePath());
            createDefaultConfig();
        }

        if (!LAST_CONFIG_FILE.exists()) {
            try {
                LAST_CONFIG_FILE.createNewFile();
                logger.log(Level.INFO, "Файл для последнего конфига создан: " + LAST_CONFIG_FILE.getAbsolutePath());
                saveLastConfig("default");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Не удалось создать файл для последнего конфига", e);
            }
        }
    }

    public void loadLastConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LAST_CONFIG_FILE))) {
            String lastConfig = reader.readLine();
            if (lastConfig != null && !lastConfig.isEmpty()) {
                if (!loadConfiguration(lastConfig)) {
                    logger.log(Level.WARNING, "Не удалось загрузить последний конфиг, загружаем default");
                    loadConfiguration("default");
                    currentConfigName = "default";
                } else {
                    currentConfigName = lastConfig;
                }
            } else {
                logger.log(Level.WARNING, "Файл последнего конфига пуст, загружается конфиг по умолчанию.");
                loadConfiguration("default");
                currentConfigName = "default";
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Не удалось загрузить последний конфиг, загружается default", e);
            loadConfiguration("default");
            currentConfigName = "default";
        }
    }

    public void saveLastConfig(String configName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LAST_CONFIG_FILE))) {
            writer.write(configName);
            logger.log(Level.INFO, "Последний выбранный конфиг сохранен: " + configName);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Не удалось сохранить последний выбранный конфиг", e);
        }
    }

    public void removeConfiguration(String configName) {
        if ("default".equalsIgnoreCase(configName)) {
            logger.log(Level.SEVERE, "Системный конфиг '" + configName + "' не может быть удалён.");
            return;
        }

        File configFile = new File(CONFIG_DIR, configName + ".cfg");

        if (!configFile.exists()) {
            logger.log(Level.SEVERE, "Конфиг " + configName + " не найден, удаление невозможно.");
            return;
        }

        if (configFile.delete()) {
            logger.log(Level.INFO, "Конфиг " + configName + " успешно удалён.");
        } else {
            logger.log(Level.SEVERE, "Не удалось удалить конфиг " + configName);
        }
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                File configFile = new File(CONFIG_DIR, currentConfigName + ".cfg");
                if (!configFile.exists()) {
                    logger.log(Level.WARNING, "Конфиг " + currentConfigName + " был удалён, загружается default.");
                    currentConfigName = "default";
                }

                if (!configSaved && currentConfigName != null && !currentConfigName.isEmpty()) {
                    logger.log(Level.INFO, "Сохраняем последний выбранный конфиг: " + currentConfigName);
                    saveConfiguration(currentConfigName);
                    saveLastConfig(currentConfigName);
                }
                logger.log(Level.INFO, "Конфиг сохранен при выходе: " + currentConfigName);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ошибка при сохранении конфига при выходе", e);
            }
        }));
    }

    public boolean isEmpty() {
        return getConfigs().isEmpty();
    }

    public List<Configs> getConfigs() {
        List<Configs> configs = new ArrayList<>();
        File[] configFiles = CONFIG_DIR.listFiles();

        if (configFiles != null) {
            for (File configFile : configFiles) {
                if (configFile.isFile() && configFile.getName().endsWith(".cfg")) {
                    String configName = configFile.getName().replace(".cfg", "");
                    Configs config = findConfig(configName);
                    if (config != null) {
                        configs.add(config);
                    }
                }
            }
        }

        return configs;
    }

    public boolean loadConfiguration(String configuration) {
        Configs configs = findConfig(configuration);

        if (configs == null) {
            logger.log(Level.SEVERE, "Конфиг не найден: " + configuration);
            return false;
        }

        try (FileReader reader = new FileReader(configs.getFile())) {
            JsonObject object = (JsonObject) jsonParser.parse(reader);
            configs.loadConfig( object);
            currentConfigName = configuration;
            configSaved = false;
            logger.log(Level.INFO, "Конфиг загружен: " + configuration);
            return true;
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Файл не найден для конфига: " + configuration, e);
            return false;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при чтении конфига: " + configuration, e);
            return false;
        }
    }

    public void saveConfiguration(String configuration) {
        System.out.println("SAVED NAHUYYYY SOSI YUEBOK");
        Configs configs = new Configs(configuration);
        String contentPrettyPrint = new GsonBuilder().setPrettyPrinting().create().toJson(configs.saveConfig());
        try (FileWriter writer = new FileWriter(configs.getFile())) {
            writer.write(contentPrettyPrint);
            saveLastConfig(configuration);
            configSaved = true;
            logger.log(Level.INFO, "Конфиг сохранен: " + configuration);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при сохранении конфига: " + configuration, e);
        }
    }

    public Configs findConfig(String configName) {
        if (configName == null) return null;
        File configFile = new File(CONFIG_DIR, configName + ".cfg");
        if (configFile.exists()) {
            return new Configs(configName);
        }
        return null;
    }

    private void createDefaultConfig() {
        Configs defaultConfigs = new Configs("default");
        saveConfiguration(defaultConfigs.getName());
        logger.log(Level.INFO, "Системная конфигурация создана: default.cfg");
    }

    public String getCurrentConfigName() {
        return currentConfigName;
    }
}