import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

public class StoreManagement implements MouseListener, ActionListener, WindowListener {
    private JFrame sManageFrame;
    private MainFrame mainFrame;
    private JFrame infoWFrame;
    private JFrame infoWFrame2;

    private JList<String> myMarketList;
    private JList<String> articleList;

    private JScrollPane myMarketScroll;
    private JScrollPane articleScroll;

    private DefaultListModel<String> model;
    private DefaultListModel<String> model2;
    private DefaultListModel<String> price = new DefaultListModel<String>();

    private CSVReader csvList;
    private CSVReader myMarketCSV;

    private JButton infoWOK2Button;
    private JButton infoWCancel2Button;
    private JButton infoWOKButton;
    private JButton infoWCancelButton;
    private JButton infoWChoiceButton;
    private JButton infoWCorrectButton;

    private TextField infoWTextField;
    private TextField infoWTextField2;
    /** 점포관리시스템 생성자 */
    public StoreManagement(MainFrame main, CSVReader csvList) {
        Font font = new Font("G마켓 산스 TTF Medium", Font.PLAIN, 14);
        this.csvList = csvList;
        this.mainFrame = main;
        myMarketCSV = new CSVReader("data\\myMarketCSV.csv");
        sManageFrame = new JFrame("점포 관리 시스템");
        sManageFrame.setFont(font);
        sManageFrame.setSize(700, 450);
        sManageFrame.setLayout(null);

        // 창을 모니터 중앙에 띄우도록 해준다
        sManageFrame.setLocationRelativeTo(null);
        sManageFrame.addWindowListener(this);
        ListBox();
        ListInfoTextFild();
        sManageFrame.setResizable(false);
        sManageFrame.setVisible(true);

        // 버튼
        infoWChoiceButton = new JButton("물품 추가");
        infoWChoiceButton.setFont(font);
        infoWChoiceButton.setBounds(215, 220, 90, 40);
        sManageFrame.add(infoWChoiceButton);
        infoWChoiceButton.addActionListener(this);

        infoWCorrectButton = new JButton("물품 관리");
        infoWCorrectButton.setFont(font);
        infoWCorrectButton.setBounds(520, 220, 90, 40);
        sManageFrame.add(infoWCorrectButton);
        infoWCorrectButton.addActionListener(this);

    }


    JTextArea listInfoArea;
    JTextArea listInfoArea2;

    /** 리스트 박스의 정보창을 생성한다 */
    private void ListInfoTextFild() {
        Font font = new Font("G마켓 산스 TTF Medium", Font.PLAIN, 12);
        listInfoArea = new JTextArea(5, 10);
        listInfoArea.setFont(font);
        listInfoArea.setBounds(170, 100, 190, 100);
        listInfoArea.setEnabled(false);
        sManageFrame.add(listInfoArea);

        listInfoArea2 = new JTextArea();
        listInfoArea2.setFont(font);
        listInfoArea2.setBounds(470, 100, 190, 100);
        listInfoArea2.setEnabled(false);
        sManageFrame.add(listInfoArea2);
    }

    /** 리스트 박스 생성 */
    private void ListBox() {
        Font font = new Font("G마켓 산스 TTF Medium", Font.PLAIN, 11);
        Font font2 = new Font("G마켓 산스 TTF Medium", Font.PLAIN, 14);
        model = new DefaultListModel<String>();
        int j = 0;

        myMarketList = new JList<String>(model);
        myMarketList.setFont(font2);
        myMarketList.addMouseListener(this);
        myMarketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myMarketScroll = new JScrollPane(myMarketList);
        myMarketScroll.setBounds(370, 100, 100, 200);

        JLabel myMarketJLabel = new JLabel("<html> 동의마트 판매 목록 <html>");
        myMarketJLabel.setFont(font);
        myMarketJLabel.setBounds(370, 60, 1000, 50);
        sManageFrame.add(myMarketJLabel);
        sManageFrame.add(myMarketScroll);

        model2 = new DefaultListModel<String>();

        j = 0;
        for (int i = 0; i < myMarketCSV.csvList.size(); i++) {
            model.add(j, myMarketCSV.dataGet(i, 0));
            price.add(j++, myMarketCSV.dataGet(i, 1));
        }

        // 주변 마트의 물건 리스트 추가
        j = 0;
        for (int i = 2; i < csvList.csvList.size(); i++) {
            if (modelIn(csvList.dataGet(i, 0))) {
                model2.add(j++, csvList.dataGet(i, 0));
            }
        }

        articleList = new JList<String>(model2);
        articleList.setFont(font2);
        articleList.addMouseListener(this);
        articleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articleScroll = new JScrollPane(articleList);
        articleScroll.setBounds(70, 100, 100, 200);

        JLabel articleListJLabel = new JLabel("<html> 주변마트 판매 목록 <html>");
        articleListJLabel.setFont(font);
        articleListJLabel.setBounds(70, 60, 100, 50);
        sManageFrame.add(articleListJLabel);

        sManageFrame.add(myMarketScroll);
        sManageFrame.add(articleScroll);

    }

    /** 물품목록에 추가하기 전에 중복되는것이 있는지 파악해준다 */
    private boolean modelIn(String str) {
        boolean p = true;
        ;
        for (int i = 0; i < model.size(); i++) {
            if (str.equals(model.get(i))) {
                p = false;
            }
        }
        return p;
    }

    /** 마우스 클릭 이벤트 처리 */
    public void mouseClicked(MouseEvent e) {

        if (e.getComponent() == myMarketList) {
            if (e.getClickCount() == 1) {
                listInfoArea2.setText("선택한 물품: " + model.get(myMarketList.getSelectedIndex()) + "\n" + "판매처: " + "동의마트"
                        + "\n" + "판매가: " + price.get(myMarketList.getSelectedIndex()) + "\n" + "주소:" + "부산진구 엄광로 176"
                        + "\n");
            }
            if (e.getClickCount() == 2) {
                showInfoW2();
            }
        } else if (e.getComponent() == articleList) {
            if (e.getClickCount() == 1) {
                listInfoArea.setText("선택한 물품: " + model2.get(articleList.getSelectedIndex()) + "\n" + "판매처: "
                        + csvList.dataGet(0, searchIndex(model2.get(articleList.getSelectedIndex()))) + "\n" + "판매가: "
                        + csvList.dataGet(searchIndex2(model2.get(articleList.getSelectedIndex())),
                        searchIndex(model2.get(articleList.getSelectedIndex())))
                        + "\n" + "" + csvList.dataGet(1, searchIndex(model2.get(articleList.getSelectedIndex())))
                        + "\n");
            }
            if (e.getClickCount() == 2) {
                showInfoW();
            }

        }
    }

    /** 주변 마트의 물건을 클릭시 */
    public void showInfoW() {
        Font font = new Font("G마켓 산스 TTF Medium", Font.PLAIN, 15);
        infoWFrame = new JFrame("물품 정보");
        // 창을 모니터 중앙에 띄우도록 해준다
        infoWFrame.setLayout(null);
        infoWFrame.setSize(300, 300);
        infoWFrame.setLocationRelativeTo(null);
        infoWOKButton = new JButton("추가");
        infoWOKButton.setFont(font);
        infoWOKButton.setBounds(50, 200, 75, 25);
        infoWFrame.add(infoWOKButton);
        infoWOKButton.addActionListener(this);

        infoWCancelButton = new JButton("취소");
        infoWCancelButton.setFont(font);
        infoWCancelButton.setBounds(165, 200, 75, 25);
        infoWFrame.add(infoWCancelButton);
        infoWCancelButton.addActionListener(this);

        infoWTextField = new TextField();
        infoWTextField.setFont(font);
        infoWTextField.setBounds(165, 150, 75, 25);
        infoWTextField.addActionListener(this);
        infoWFrame.add(infoWTextField);

        JLabel label = new JLabel("<html>" + csvList.dataGet(0, searchIndex(model2.get(articleList.getSelectedIndex())))
                + "에서는" + "<br>" + model2.get(articleList.getSelectedIndex()) + "을(를)" + "<br>"
                + csvList.dataGet(searchIndex2(model2.get(articleList.getSelectedIndex())),
                searchIndex(model2.get(articleList.getSelectedIndex())))
                + "원에 판매중입니다." + "</html>");
        label.setFont(font);
        label.setBounds(50, 40, 200, 80);
        infoWFrame.add(label);

        JLabel label2 = new JLabel("<html>" + "판매할 가격:" + "<html>");
        label2.setFont(font);
        label2.setBounds(70, 155, 200, 15);
        infoWFrame.add(label2);
        infoWFrame.setResizable(false);
        infoWFrame.setVisible(true);

    }

    /** 최저가 판매처 y좌표 반환 */
    public int searchIndex2(String item) {
        int index = -1;
        for (int i = 0; i < csvList.csvList.size(); i++) {
            if (item.equals(csvList.csvList.get(i).get(0))) {
                index = i;
                break;
            }
        }

        return index;
    }

    /** 최저가 판매처 x좌표 반환 */
    public int searchIndex(String item) {
        int index = -1;
        for (int i = 0; i < csvList.csvList.size(); i++) {
            if (item.equals(csvList.csvList.get(i).get(0))) {
                index = i;
                break;
            }
        }

        int j = 1;
        int p = 1000000;

        for (int i = 1; i < csvList.csvList.get(index).size(); i++) {
            if (!csvList.csvList.get(index).get(i).equals("미판매")) {
                if (Integer.parseInt(csvList.csvList.get(index).get(i)) < p) {
                    p = Integer.parseInt(csvList.csvList.get(index).get(i));
                    j = i;
                }
            }
        }
        //System.out.println(csvList.csvList.get(index).get(j));
        return j;
    }

    /** 동의마트 물품 정보창 */
    public void showInfoW2() {
        Font font = new Font("G마켓 산스 TTF Medium", Font.PLAIN, 15);
        infoWFrame2 = new JFrame("물품 정보");
        // 창을 모니터 중앙에 띄우도록 해준다
        infoWFrame2.setLayout(null);
        infoWFrame2.setSize(300, 300);
        infoWFrame2.setLocationRelativeTo(null);
        infoWOK2Button = new JButton("삭제");
        infoWOK2Button.setFont(font);
        infoWOK2Button.setBounds(50, 200, 75, 25);
        infoWFrame2.add(infoWOK2Button);
        infoWOK2Button.addActionListener(this);

        infoWCancel2Button = new JButton("수정");

        infoWCancel2Button.setBounds(165, 200, 75, 25);
        infoWFrame2.add(infoWCancel2Button);
        infoWCancel2Button.addActionListener(this);
        infoWCancel2Button.setFont(font);
        infoWTextField2 = new TextField();
        infoWTextField2.setFont(font);
        infoWTextField2.setBounds(165, 150, 75, 25);
        infoWTextField2.addActionListener(this);
        infoWFrame2.add(infoWTextField2);

        JLabel label = new JLabel("<html>" + "현재 동의마트에서는" + "<br>" + price.getElementAt(myMarketList.getSelectedIndex())
                + "원에 판매중입니다." + "<html>");

        label.setFont(font);
        label.setBounds(60, 50, 200, 40);
        infoWFrame2.add(label);

        JLabel label2 = new JLabel("<html>" + "수정할 가격:" + "<html>");
        label2.setFont(font);
        label2.setBounds(70, 155, 200, 15);
        infoWFrame2.add(label2);
        infoWFrame2.setResizable(false);
        infoWFrame2.setVisible(true);

    }

    /** 버튼 이벤트 처리 */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == infoWOKButton||e.getSource() == infoWTextField) {
            if (infoWTextField.getText().equals("")) {
                showMessageDialog(null, "가격을 입력해 주십시요.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            } else {

                infoWFrame.dispose();
                price.add(price.getSize(), infoWTextField.getText());
                model.add(model.getSize(), articleList.getSelectedValue());
                model2.remove(articleList.getSelectedIndex());
                listInfoArea.setText(null);
            }

        } else if (e.getSource() == infoWCancelButton) {
            infoWFrame.dispose();
            //System.out.println("추가가 취소되었습니다.");

        }

        else if (e.getSource() == infoWOK2Button) {
            model2.add(model2.getSize(), myMarketList.getSelectedValue());

            price.remove(myMarketList.getSelectedIndex());
            model.remove(myMarketList.getSelectedIndex());
            listInfoArea2.setText(null);
            infoWFrame2.dispose();
        } else if (e.getSource() == infoWCancel2Button||e.getSource() == infoWTextField2) {
            if (infoWTextField2.getText().equals("")) {
                showMessageDialog(null, "가격을 입력해 주십시요.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            } else {
                infoWFrame2.dispose();
                price.setElementAt(infoWTextField2.getText(), myMarketList.getSelectedIndex());
//				for (int i = 0; i < price.getSize(); i++) {
//					System.out.println("수정된 가격: " + price.getElementAt(i));
//				}
                listInfoArea2.setText("판매처:" + "동의마트" + "\n" + "판매가:" + price.get(myMarketList.getSelectedIndex())
                        + "\n" + "주소:" + "부산진구 엄광로 176" + "\n");
            }
        }

        else if (e.getSource() == infoWChoiceButton) {
            showInfoW();
        } else if (e.getSource() == infoWCorrectButton) {
            showInfoW2();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {
        csvList.combinedCSV(model, price);
        myMarketCSV.dataWriter(model, price);
        mainFrame.mainFrame.setVisible(true);
        sManageFrame.dispose();

    }
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}

}
