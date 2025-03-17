/*
Данный класс занимается отображением панелей из других классов
и работает с конфигурационным файлом
 */

import javax.swing.*;
import java.io.*;
import java.util.Properties;

public class App extends JFrame {
    private Properties config;
    private File configFile = new File("config.properties");
    private static App proxy;
    public int w = 1280, h = 720;

    private App() {
        super("Harry Potter and the Programmer's Pain");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(w, h);
        this.setLayout(null);
        setResizable(false);
        loadConfig();
        setVisible(true);
    }

    public static App getProxy() {
        if (proxy == null) {
            proxy = new App();
        }
        return proxy;
    }

    // данная функция нужна для передачи экрана для надписи, которую ты добавила(:
    public JFrame getFrame(){
        return this;
    }

    public void showMenu() {
        getContentPane().removeAll();
        getContentPane().add(Menu.getProxy().getPanel());
        revalidate();
        repaint();
    }

    public void showPlay() {
        getContentPane().removeAll();
        getContentPane().add(Play.getProxy().getPanel());
        revalidate();
        repaint();
        /* можно передавать фокус так
        Play.getProxy().getPanel().setFocusable(true);
        Play.getProxy().getPanel().requestFocusInWindow();
         */

        // или так (хз, как лучше)
        Play.getProxy().requestFocus();
    }

    public void showSettings() {
        getContentPane().removeAll();
        getContentPane().add(Settings.getProxy().getPanel());
        revalidate();
        repaint();
    }

    private void loadConfig() {
        config = new Properties();
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                config.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Properties getConfig() {
        return config;
    }

    public void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            config.store(fos, "Game settings file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getProxy().showMenu();
    }
}
