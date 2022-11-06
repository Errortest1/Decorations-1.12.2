package by.errortest.decorations;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Config {

    int id = -1;

    public void readConfig() {
        try {
            BufferedReader e = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/assets/decormod/models.txt"), "UTF-8"));
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
                    String modelPath = getString(e1, "model_path");
                    String texturePath = getString(e1, "texture_path");
                    id++;
                    ClientProxy.models.put(id, new DecorModel(modelPath, texturePath, id));
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
