import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Basket implements ActionListener, ListSelectionListener, MouseListener {

    private JFrame basketFrame;
    CSVReader goCSV2 = new CSVReader("data\\2022.11월1주주요생필품가격평균현황(대형마트).csv");
    Font font = new Font("G마켓 산스 TTF Medium", Font.PLAIN, 15);

    JFrame infoFrame;
    JButton b1;
    JButton b2;
    JButton b3;
    JButton b4;
    JTextField TF1;
    JList<String> JL1;
    JList<String> JL2;
    JScrollPane scroll1;
    CSVReader csvList;
    int basketResult = 0;
    String basketAddress = null;

    private DefaultListModel<String> model;
    private DefaultListModel<String> model2;

    public Basket(CSVReader csvList) { // CSV에서 1열 값 읽어오기
        this.csvList = csvList;
        // 창을 모니터 중앙에 띄우도록 해준다
        basketFrame = new JFrame("장바구니");
        basketFrame.setFont(font);
        basketFrame.setSize(470, 500);
        basketFrame.setLayout(null);
        basketFrame.setLocationRelativeTo(null);
        model = new DefaultListModel<String>();
        model2 = new DefaultListModel<String>();
        int j = 0;
        for (int i = 2; i < goCSV2.csvList.size(); i++) {
            model.add(j++, csvList.csvList.get(i).get(0));
        }

        basketFrame.setResizable(false);

        // 텍스트 라벨
        JLabel la1 = new JLabel("목록");
        la1.setFont(font);
        la1.setLocation(60, 90);
        la1.setSize(100, 20);
        basketFrame.add(la1);
        JLabel la2 = new JLabel("장바구니");
        la2.setFont(font);
        la2.setLocation(250, 90);
        la2.setSize(100, 20);
        basketFrame.add(la2);

        // 버튼
        b1 = new JButton("검색");
        b1.setFont(font);
        b1.setLocation(320, 50);
        b1.setSize(70, 25);
        b1.addActionListener(this);
        basketFrame.add(b1);

        b2 = new JButton("결정");
        b2.setFont(font);
        b2.addActionListener(this);
        b2.setLocation(300, 360);
        b2.setSize(100, 50);
        basketFrame.add(b2);

        b3 = new JButton("물품 담기");
        b3.setFont(font);
        b3.setLocation(60, 360);
        b3.setSize(100, 50);
        b3.addActionListener(this);
        basketFrame.add(b3);

        b4 = new JButton("물품 빼기");
        b4.setFont(font);
        b4.setLocation(180, 360);
        b4.setSize(100, 50);
        b4.addActionListener(this);
        basketFrame.add(b4);

        // 검색 텍스트필드
        TF1 = new JTextField();
        TF1.setFont(font);
        TF1.add(new JTextField(10));
        TF1.setLocation(80, 50);
        TF1.setSize(200, 25);
        basketFrame.add(TF1);
        TF1.addActionListener(this);

        // 왼쪽 리스트
        JL1 = new JList<String>(model);
        JL1.setFont(font);
        JL1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JL1.addMouseListener(this);

        scroll1 = new JScrollPane(JL1);
        basketFrame.add(scroll1);
        scroll1.setLocation(60, 120);
        scroll1.setSize(150, 200);

        // 오른쪽 리스트
        JL2 = new JList<String>(model2);
        JL2.setFont(font);
        JL2.addMouseListener(this);
        JScrollPane scroll2 = new JScrollPane(JL2);
        basketFrame.add(scroll2);
        scroll2.setLocation(250, 120);
        scroll2.setSize(150, 200);

        basketFrame.setVisible(true);
    }

    /** 장바구니 검색 기능 */
    public void actionPerformed(ActionEvent actionEvent) {
        String inputString;

        if (actionEvent.getSource() == b1 || actionEvent.getSource() == TF1) {
            // 텍스트필드에서 값을 받아와 저장
            inputString = TF1.getText();
            int index = -1;// 인덱스의 기본값은 아무것도 없는-1
            // 찾는 데이터가 발견될때까지 오름차순 탐색
            for (int i = 0; i < model.getSize(); i++) {
                // 만약 찾는 데이터와 같은 데이터를 발견했다면 해당 인덱스 저장후 탈출

                if (inputString.equals(model.get(i))) {
                    index = i;

                    break;
                }
            }

            if (index != -1) {
                // 찾은 값의 인덱스 위치로 이동시킨다
                JL1.setSelectedIndex(index);
                scroll1.getVerticalScrollBar().setValue(index * 20);
            } else if (index == -1) {
                // 탐색된 데이터가 없다는것을 알림
                javax.swing.UIManager.put("OptionPane.messageFont",
                        new java.awt.Font("G마켓 산스 TTF Medium", java.awt.Font.PLAIN, 15));
                showMessageDialog(null, "검색한 데이터가 없습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            }
        } else if (actionEvent.getSource() == b3) {

            model2.add(0, model.get(JL1.getSelectedIndex()));
            model.remove(JL1.getSelectedIndex());
        }

        else if (actionEvent.getSource() == b4) {

            model.add(0, model2.get(JL2.getSelectedIndex()));
            model2.remove(JL2.getSelectedIndex());
        } else if (actionEvent.getSource() == b2) {
            if (basketResult().equals("a")) {
                javax.swing.UIManager.put("OptionPane.messageFont",
                        new java.awt.Font("G마켓 산스 TTF Medium", java.awt.Font.PLAIN, 15));
                showMessageDialog(null, "장바구니가 비어있습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            } else {
                infoW(basketResult());
            }
        }
    }

    /** 장바구니에 담긴 물건들을 가장 싸게 살수있는 마트를 반환 */
    private String basketResult() {
        int result[][] = { { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };
        if (model2.size() == 0) {
            return "a";
        }
        for (int i = 0; i < model2.size(); i++) {
            for (int j = 0; j < csvList.csvList.size(); j++) {
                if (model2.get(i).equals(csvList.dataGet(j, 0))) {
                    for (int k = 1; k < csvList.csvList.get(0).size(); k++) {
                        if (!csvList.dataGet(j, k).equals("미판매")) {
                            result[0][k - 1] += Integer.parseInt(csvList.dataGet(j, k));
                            result[1][k - 1]++;
                        }
                    }
                    break;
                }
            }
        }
        int p = 0;
        int index = 0;
        int q = 0;

        for (int i = 0; i < csvList.csvList.get(0).size() - 1; i++) {
            if (result[1][i] == model2.size() && q == 0) {
                p = result[0][i];
                basketResult = result[0][i];
                basketAddress = csvList.dataGet(1, i + 1);
                q = 1;
                index = i;
            }
            if (result[0][i] < p && q == 1 && result[1][i] == model2.size()) {
                p = result[0][i];
                basketResult = result[0][i];
                basketAddress = csvList.dataGet(1, i + 1);
                index = i;
            }
        }

        if (q == 1) {
            return csvList.dataGet(0, index + 1);
        } else {
            return "구매불가";
        }

    }

    /** 리스트에서 더블클릭시 생성할 정보창 */
    private void infoW(String str) {
        // 주소, 판매처 이름, 가격
        Font font = new Font("G마켓 산스 TTF Medium", Font.PLAIN, 15);
        infoFrame = new JFrame("결과");
        infoFrame.setFont(font);
        infoFrame.setSize(400, 300);
        ;
        JLabel label = new JLabel("<html>판매처: " + str + "<br>" + "주소: " + basketAddress + "<br>" + "가격: " + basketResult
                + "<br>" + "</html>", SwingConstants.CENTER);
        label.setFont(font);
        label.setBounds(0, 0, 100, 10);
        // 창을 모니터 중앙에 띄우도록 해준다
        infoFrame.setLocationRelativeTo(null);

        infoFrame.add(label);
        infoFrame.setVisible(true);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent() == JL1) {
            if (e.getClickCount() == 2) {
                model2.add(0, model.get(JL1.getSelectedIndex()));
                model.remove(JL1.getSelectedIndex());
            }
        } else if (e.getComponent() == JL2) {
            if (e.getClickCount() == 2) {
                model.add(0, model2.get(JL2.getSelectedIndex()));
                model2.remove(JL2.getSelectedIndex());
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
