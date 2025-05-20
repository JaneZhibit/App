import javax.swing.*;
import java.io.*;
import java.util.Properties;

public class App extends JFrame {
    private Properties config;
    private final File configFile = new File("config.properties");
    private static App proxy;
    public final int w = 1280, h = 720;

    private App() {
        super("Harry Potter and Automat po Proge");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(w, h);
        setLayout(null);
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

    public void showMenu() {
        switchPanel(Menu.getProxy().getPanel());
        Menu.getProxy().playMusic();
    }

    public void showPlay() {
        Play play = new Play();
        switchPanel(play.getPanel());
        play.playMusic();
        play.requestFocus();
    }

    public void showSettings() {
        switchPanel(Settings.getProxy().getPanel());
    }

    public void showUpgrades() {
        Upgrades.getProxy().refresh();

        switchPanel(Upgrades.getProxy().getPanel());
    }



    private void switchPanel(JComponent panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
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

    public String getSelectedBroomName() {
        return config.getProperty("selectedBroom", "BASIC");
    }

    public void setSelectedBroom(String broomName) {
        config.setProperty("selectedBroom", broomName);
        saveConfig();
    }


    public static void main(String[] args) {
        getProxy().showMenu();
    }
}