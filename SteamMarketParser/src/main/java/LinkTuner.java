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

class LinkTuner extends JFrame {

    private final String validationExample = "Validation passed: ";

    private JPanel contentPanel = new JPanel();

    private JLabel title = new JLabel("Enter the hyperlink to the observing product:");
    private JLabel siteValidation = new JLabel(validationExample);
    private JLabel setHours = new JLabel("<html>Choose when you want get notification<br />(every x hours):</html>");
    private JButton acceptBtn = new JButton("Accept");
    private JTextField link = new JTextField();
    private JComboBox<Integer> notificationsCyclesTime;
    private ArrayList<Integer> hours = new ArrayList<>(Arrays.asList(1, 2, 3, 12, 24));

    private ActionListener actionListener = e -> {
        Parser parser = new Parser(link.getText());
        System.out.println(parser.getPrice());
    };

    LinkTuner() {
        initFrame();
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
        }
    }

    private void setPanel() {
        contentPanel.setLayout(null);
        getContentPane().add(contentPanel);
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

        setHours.setBounds(5, 90, 230, 40);
        setHours.setFont(new Font("", Font.PLAIN, 12));
        contentPanel.add(setHours);

        notificationsCyclesTime = new JComboBox<>();
        DefaultComboBoxModel<Integer> defaultComboBoxModel = new DefaultComboBoxModel<>();
        defaultComboBoxModel.addAll(hours);
        notificationsCyclesTime.setModel(defaultComboBoxModel);
        notificationsCyclesTime.setSelectedIndex(0);
        notificationsCyclesTime.setBounds(241, 95, 265, 30);
        contentPanel.add(notificationsCyclesTime);
    }

    private void setListeners() {
        acceptBtn.addActionListener(actionListener);

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
        setSize(527, 170);
        setTitle("Steam market parser");
        setVisible(true);
    }

    private void initFrame() {
        tuneFrame();
        setPanel();
        addComponents();
        setListeners();
    }

}
