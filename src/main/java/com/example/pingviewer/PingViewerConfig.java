//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.pingviewer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class PingViewerConfig {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final File FILE = new File("config/pingviewer.json");
    public String text = "Your Ping Is: %ping%";
    public String color = "green";
    public boolean bold = false;
    public boolean italic = false;
    public boolean underlined = false;

    public static PingViewerConfig load() {
        try {
            if (!FILE.exists()) {
                PingViewerConfig config = new PingViewerConfig();
                FILE.getParentFile().mkdirs();

                try (FileWriter writer = new FileWriter(FILE)) {
                    GSON.toJson(config, writer);
                }

                return config;
            } else {
                try (FileReader reader = new FileReader(FILE)) {
                    return (PingViewerConfig)GSON.fromJson(reader, PingViewerConfig.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new PingViewerConfig();
        }
    }
}
