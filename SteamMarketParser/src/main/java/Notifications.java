import java.awt.*;
import java.util.Objects;

class Notifications {

    private static void showNotification(String caption, String text, LinkTuner linkTuner) {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Image image = Toolkit.getDefaultToolkit().
                    createImage(Objects.requireNonNull(classLoader.getResource("trayIcon.png")).getFile());
            TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
            trayIcon.addActionListener(e -> linkTuner.setVisible(true));
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("SMP Notification (do double click)");
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            trayIcon.displayMessage(caption, "Minimum price: " + text, TrayIcon.MessageType.INFO);
        }
    }

    static void showInfoNotification(String caption, String text, LinkTuner linkTuner) {
        showNotification(caption, text, linkTuner);
    }

}
