import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainFrame implements ActionListener {
    JFrame mainFrame;
    JButton priceSearchButton;
    JButton storeManagementButton;
    CSVReader csvList = new CSVReader("data/2022.11월1주주요생필품가격평균현황(대형마트).csv");
    CSVReader combinedCSV = new CSVReader("data\\combinedCSV.csv");
    BufferedImage img;
    JLabel lb;

    public MainFrame() {
        mainFrame = new JFrame("PriceSearch");
        mainFrame.setSize(375, 550);
        mainFrame.setLayout(null);
        mainFrame.getContentPane().setBackground(Color.WHITE);

        Font font = new Font("G마켓 산스 TTF Medium", Font.PLAIN,15);

        //창을 모니터 중앙에 띄우도록 해준다
        mainFrame.setLocationRelativeTo(null);
        //TODO 부모 프레임을 닫았을 때 메모리에서 제거되도록 설정
        //mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        showImage();

        priceSearchButton = new JButton("물가 탐색");
        priceSearchButton.setFont(font);

        priceSearchButton.setBounds(100, 200, 150, 100);
        mainFrame.add(priceSearchButton);

        storeManagementButton = new JButton("점포 경영");
        storeManagementButton.setFont(font);

        storeManagementButton.setBounds(100, 350, 150, 100);
        mainFrame.add(storeManagementButton);

        mainFrame.setFont(font);
        mainFrame.setResizable(false);

        mainFrame.setVisible(true);
        priceSearchButton.addActionListener(this);
        storeManagementButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == priceSearchButton) {
            mainFrame.setVisible(false);
            combinedCSV.updateData();
            new PSearch(this, combinedCSV);
        }
        else if(actionEvent.getSource() == storeManagementButton) {
            mainFrame.setVisible(false);
            new StoreManagement(this, csvList);
        }
    }

    public void showImage() {

        try {
            File file = new File("data\\deuMart.png");
            img = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Image ximg = img;
        Image yimg = ximg.getScaledInstance(525, 325, java.awt.Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(yimg);
        if(lb==null)
            lb = new JLabel(new ImageIcon(yimg));
        else
            lb.setIcon(icon);

        lb.setBounds(-90, -160, 525, 325);// x,y,w,h
        mainFrame.add(lb);
        mainFrame.repaint();
    }


}
