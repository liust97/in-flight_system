package main.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class PropertyUtil {
//    public static String getProperty(String key) {
//        String value = "";
////第一步是取得一个Properties对象
//        Properties props = new Properties();
////第二步是取得配置文件的输入流
//        InputStream is = PropUtil.class.getClassLoader().getResourceAsStream("config_zh.properties");//在非WEB环境下用这种方式比较方便
//        try {
//            InputStream input = new FileInputStream("config_zh.properties");//在WEB环境下用这种方式比较方便，不过当配置文件是放在非Classpath目录下的时候也需要用这种方式
////第三步讲配置文件的输入流load到Properties对象中，这样在后面就可以直接取来用了
//            props.load(input);
//            value = props.getProperty(key);
//            is.close();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return value;
//    }

    private static void setProperty(String fileName, Map<String, String> data) {
        String path = "src/properties/config_"+fileName+".properties";
//第一步也是取得一个Properties对象
        Properties props = new Properties();
//第二步也是取得该配置文件的输入流
//      InputStream is = PropUtil.class.getClassLoader().getResourceAsStream("config_zh.properties");
        try {
            File file=new File(path);
            if (!file.exists()) {
                if(!file.createNewFile()){
                    return;
                    //如果文件不存在 则创建
                }
            }
            InputStream input = new FileInputStream(path);
//第三步是把配置文件的输入流load到Properties对象中，
            props.load(input);
//接下来就可以随便往配置文件里面添加内容了
//          props.setProperty(key, value);
            if (data != null) {
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    props.setProperty(entry.getKey(), entry.getValue());
                }
            }
//在保存配置文件之前还需要取得该配置文件的输出流，<span style="color: #ff0000; font-size: medium;">切记，</span>如果该项目是需要导出的且是一个非WEB项目，则该配置文件应当放在根目录下，否则会提示找不到配置文件
            OutputStream out = new FileOutputStream(path);
//最后就是利用Properties对象保存配置文件的输出流到文件中;
            props.store(out, null);
            input.close();
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void clearProperty(){

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input language name (example: zh)");
        String fileName = scanner.nextLine();

//        map.put("password","psw");
        while(true){
            HashMap<String, String> map = new HashMap<String, String>();
            System.out.println("Key:");
            String key = scanner.nextLine();
            System.out.println("Value:");
            String value = scanner.nextLine();
            map.put(key,value);
            setProperty(fileName,map);
//            System.out.println("Success! Input 1 to exit, others to continue.");
//            if (scanner.nextLine().equals("1")){
//                break;
//            }
        }
    }

}
