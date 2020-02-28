import java.awt.*;

public class Notifications {

    private static void showNotification(String caption, String text, LinkTuner linkTuner) {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("notification.png");
            TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
            trayIcon.addActionListener(e -> linkTuner.setVisible(true));
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("System tray icon demo");
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            trayIcon.displayMessage(caption, "Minimum price: " + text, TrayIcon.MessageType.INFO);
        }
    }

    public static void showInfoNotification(String caption, String text, LinkTuner linkTuner) {
        showNotification(caption, text, linkTuner);
    }

}
