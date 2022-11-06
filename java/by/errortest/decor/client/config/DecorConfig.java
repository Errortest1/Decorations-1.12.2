package by.errortest.decor.client.config;

import by.errortest.decor.Main;
import by.errortest.decor.blocks.BlockDecor;
import cpw.mods.fml.common.registry.GameRegistry;
import org.lwjgl.Sys;

import java.io.*;

public class DecorConfig {
    public void readConfig() {
        try {
            BufferedReader e = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/assets/decor-mod/blocks.txt"), "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            boolean flag = false;

            String itemsSplitter;
            while(!flag) {
                itemsSplitter = e.readLine();
                if(itemsSplitter == null) {
                    flag = true;
                } else {
                    itemsSplitter = itemsSplitter.trim();
                    if(!itemsSplitter.startsWith("//")) {
                        buffer.append(itemsSplitter.split("//", 2)[0]);
                    }
                }
            }

            itemsSplitter = "\\{";
            String parSplitter = "[\\s]*;[\\s]*";
            String config = buffer.toString().replaceAll("\n|\r", "");
            String[] splitted = config.split("\\}");
            String[] arr$ = splitted;
            int len$ = splitted.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String str = arr$[i$];

                try {
                    String[] e1 = str.split(itemsSplitter)[1].split(parSplitter);
                    String name = getString(e1, "name");
                    String modelPath = getString(e1, "model_path");
                    String texturePath = getString(e1, "texture_path");
                    boolean collide = getBoolean(e1, "collide");
                    GameRegistry.registerBlock(new BlockDecor(name, modelPath, texturePath, collide), name);
                } catch (Exception var21) {
                    var21.printStackTrace();
                }
            }
        } catch (Exception var22) {
            var22.printStackTrace();
        }

    }

    private static String getString(String[] parameters, String name) {
        String str = getParStr(parameters, name);
        if(str == null) {
            return "";
        } else {
            String value = str.split(":", 2)[1].trim();
            return value.equals("\"\"")?"":value.split("\"")[1];
        }
    }
    public boolean getBoolean(String[] parameters, String name) {
        String str = this.getParStr(parameters, name);
        return str == null?false:str.split(":", 2)[1].trim().equals("true");
    }


    private static String getParStr(String[] parameters, String name) {
        String toFound = name + ":";
        String[] arr$ = parameters;
        int len$ = parameters.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String str = arr$[i$];
            if(str.startsWith(toFound)) {
                return str;
            }
        }

        return null;
    }
}
