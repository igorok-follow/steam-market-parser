import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

class LinkTuner extends JFrame {

    private final String validationExample = "Validation passed: ";
    private String itemTitle;

    private JPanel contentPanel = new JPanel();
    private JPanel parsePanel = new JPanel();

    private JLabel title = new JLabel("Enter the hyperlink to the observing product:");
    private JLabel siteValidation = new JLabel(validationExample);

    private JLabel parsingItemAreaTitle = new JLabel("Parsing item:");
    private JLabel parsingItemTitle = new JLabel("Selected item: ");
    private JLabel parsingItemPriceTitle = new JLabel("Price of selected item: ");
    private JTextField parsingItemPrice = new JTextField("item price");
    private JTextField parsingItemName = new JTextField("item name");

    private JLabel setHours = new JLabel("<html>Choose when you want get notification<br />(every x hours):</html>");
    private JButton acceptBtn = new JButton("Accept"), exit = new JButton("Close and interrupt program");
    private JTextField link = new JTextField();
    private JComboBox<Integer> notificationsCyclesTime;
    private ArrayList<Integer> hours = new ArrayList<>(Arrays.asList(1, 2, 3, 12, 24));

    private Font font14 = new Font("", Font.PLAIN, 14);

    private ActionListener actionListener = e -> {
        setSize(526, 300);
        parsingItemName.setText(itemTitle);
        repaint();
        Parser parser = new Parser(link.getText());
        Thread notificator = new Thread(() -> {
            int notificationTimes = (int) notificationsCyclesTime.getSelectedItem();
            while(true) {
                String price = parser.getPrice();
                price += " ₽";
                Notifications.showInfoNotification(itemTitle, price, this);
                parsingItemPrice.setText(price);
                try {
                    Thread.sleep(36_000_000 * notificationTimes);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        notificator.start();
    };

    LinkTuner() {
        initFrame();
    }

    private void getTitleFromPage() {
        try {
            Document doc = Jsoup.connect(link.getText()).get();
            itemTitle = doc.title();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkSiteValidate(String link) {
        if (link != null) {
            try {
                System.out.println("CHECK");
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setUseCaches(false);

                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    siteValidation.setText(validationExample + "true");
                    acceptBtn.setEnabled(true);
                    repaint();
                }
            } catch (IOException e) {
                siteValidation.setText(validationExample + "false");
                acceptBtn.setEnabled(false);
                repaint();
                e.printStackTrace();
            }
            getTitleFromPage();
        }
    }

    private void setPanels() {
        contentPanel.setLayout(null);
        getContentPane().add(contentPanel);

        parsePanel.setLayout(null);
        parsePanel.setBounds(5, 160, 501, 100);
        contentPanel.add(parsePanel);
    }

    private void addComponents() {
        title.setBounds(5, 5, 500, 25);
        title.setFont(new Font("", Font.PLAIN, 12));
        contentPanel.add(title);

        siteValidation.setBounds(5, 60, 230, 25);
        siteValidation.setFont(new Font("", Font.PLAIN, 12));
        contentPanel.add(siteValidation);

        link.setBounds(5, 30, 500, 25);
        contentPanel.add(link);

        acceptBtn.setBounds(241, 60, 265, 30);
//        acceptBtn.setEnabled(false);
        contentPanel.add(acceptBtn);

        exit.setBounds(5, 130, 501, 25);
        contentPanel.add(exit);

        setHours.setBounds(5, 90, 230, 40);
        setHours.setFont(new Font("", Font.PLAIN, 12));
        contentPanel.add(setHours);

        notificationsCyclesTime = new JComboBox<>();
        DefaultComboBoxModel<Integer> defaultComboBoxModel = new DefaultComboBoxModel<>();
        defaultComboBoxModel.addAll(hours);
        notificationsCyclesTime.setModel(defaultComboBoxModel);
        notificationsCyclesTime.setSelectedIndex(0);
        notificationsCyclesTime.setBounds(241, 95, 264, 30);
        contentPanel.add(notificationsCyclesTime);

        parsingItemAreaTitle.setBounds(200, 0, 100, 30);
        parsingItemAreaTitle.setFont(new Font("", Font.BOLD, 16));
        parsePanel.add(parsingItemAreaTitle);

        parsingItemTitle.setBounds(5, 30, 300, 30);
        parsingItemTitle.setFont(font14);
        parsePanel.add(parsingItemTitle);

        parsingItemPriceTitle.setBounds(5, 60, 300, 30);
        parsingItemPriceTitle.setFont(font14);
        parsePanel.add(parsingItemPriceTitle);

        parsingItemName.setBounds(110, 35, 390, 20);
        parsingItemName.setFont(font14);
        parsePanel.add(parsingItemName);

        parsingItemPrice.setBounds(155, 65, 345, 20);
        parsingItemPrice.setFont(font14);
        parsePanel.add(parsingItemPrice);
    }

    private void setListeners() {
        acceptBtn.addActionListener(actionListener);

        exit.addActionListener(e -> System.exit(0));

        link.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) { checkSiteValidate(link.getText()); }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) { checkSiteValidate(link.getText()); }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) { checkSiteValidate(link.getText()); }
        });
    }

    private void tuneFrame() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(526, 200);
        setResizable(false);
        setTitle("Steam market parser");
        setVisible(true);
    }

    private void initFrame() {
        tuneFrame();
        setPanels();
        addComponents();
        setListeners();
    }

}
