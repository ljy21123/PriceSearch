import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.File;

import java.util.*;
import java.util.List;

public class PSearch implements ActionListener, MouseListener, WindowListener {
    public JFrame pSearchFrame;
    public JTextField searchField;
    public JButton bSearch;
    public JButton basket;
    public JScrollPane listScrollPane;
    public JList<String> dataList;
    public DefaultListModel<String> model;
    private JFrame infoFrame;
    private BufferedImage img;
    JLabel lb;
    File file;
    private List<String> oldFile = new ArrayList<String>();
    File deleteFile;
    MainFrame mainFrame;
    CSVReader csvList;
    Font font = new Font("G마켓 산스 TTF Medium", Font.PLAIN,15);



    /**프레임 생성*/
    public PSearch(MainFrame main, CSVReader csvList) {
        this.csvList = csvList;
        this.mainFrame = main;
        pSearchFrame = new JFrame("물가 탐색");
        pSearchFrame.setFont(font);
        pSearchFrame.setSize(700, 450);
        pSearchFrame.setLayout(null);
        pSearchFrame.getContentPane().setBackground(Color.WHITE);

        mapImage();

        // 창을 모니터 중앙에 띄우도록 해준다
        pSearchFrame.setLocationRelativeTo(null);

        // 검색창
        SearchBar();
        // 리스트 창 생성
        ListBox();
        pSearchFrame.addWindowListener(this);
        pSearchFrame.setResizable(false);
        pSearchFrame.setVisible(true);
    }

    /**선택한 물품을 최저가로 파는 마트를 찾는 메소드*/
    public String searchItem(String item) {
        int index = -1 ;
        for(int i = 0 ; i < csvList.csvList.size(); i++) {
            if(item.equals(csvList.csvList.get(i).get(0))) {
                index = i;
                break;
            }
        }

        int j=1;
        int p = 1000000;

        for(int i = 1; i<csvList.csvList.get(index).size(); i++) {
            if(!csvList.dataGet(index, i).equals("미판매")) {
                if(Integer.parseInt(csvList.dataGet(index, i)) < p) {
                    p=Integer.parseInt(csvList.dataGet(index, i));
                    j=i;
                }
            }
        }
        return csvList.csvList.get(1).get(j);
    }

    /**맵 이미지*/
    public void mapImage() {
        try {
            file = new File("data\\mapIcon.png");
            img = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Image ximg = img;
        Image yimg = ximg.getScaledInstance(112, 112, java.awt.Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(yimg);
        if(lb==null)
            lb = new JLabel(new ImageIcon(yimg));
        else
            lb.setIcon(icon);

        lb.setBounds(320, 130, 112, 112);// x,y,w,h
        pSearchFrame.getContentPane().add(lb);
        pSearchFrame.repaint();
    }



    /**물품 선택시 지도 이미지 출력*/
    public void showImage(int index) {
        NaverMap naverMap = new NaverMap(csvList, searchItem(model.get(dataList.getSelectedIndex())));//주소

        try {
            file = new File(naverMap.getAddress());
            img = ImageIO.read(file);
            oldFile.add(naverMap.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }


        Image ximg = img;
        Image yimg = ximg.getScaledInstance(600, 425, java.awt.Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(yimg);
        if(lb==null)
            lb = new JLabel(new ImageIcon(yimg));
        else
            lb.setIcon(icon);

        lb.setBounds(100, 25, 600, 425);// x,y,w,h
        pSearchFrame.getContentPane().add(lb);
        pSearchFrame.repaint();
    }

    /**물품 리스트 생성 */
    private void ListBox() {
        model = new DefaultListModel<String>();

        int j=0;
        for (int i = 2; i < csvList.csvList.size(); i++) {
            if(modelIn(csvList.dataGet(i, 0))) {
                model.add(j++, csvList.dataGet(i, 0));
            }
        }
        dataList = new JList<String>(model);
        dataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataList.addMouseListener(this);
        dataList.setFont(font);

        listScrollPane = new JScrollPane(dataList);
        listScrollPane.setBounds(0, 0, 100, 413);
        pSearchFrame.add(listScrollPane);
    }

    /**물품목록에 추가하기 전에 중복되는것이 있는지 파악해준다*/
    private boolean modelIn(String str) {
        boolean p=true;;
        for(int i = 0; i<model.size();i++) {
            if(str.equals(model.get(i))) {
                p=false;
            }
        }
        return p;
    }

    /**검색창 생성*/
    private void SearchBar() {
        bSearch = new JButton("검색");
        bSearch.setFont(font);
        bSearch.setBounds(500, 2, 75, 25);
        bSearch.addActionListener(this);
        pSearchFrame.add(bSearch);


        searchField = new JTextField();
        searchField.setFont(font);
        searchField.setBounds(200, 2, 300, 25);
        pSearchFrame.add(searchField);
        searchField.addActionListener(this);

        basket = new JButton("장바구니");
        basket.setFont(font);
        basket.setBounds(575, 2, 100, 25);
        basket.addActionListener(this);
        pSearchFrame.add(basket);
    }


    /** 마우스 클릭 이벤트 처리*/
    public void mouseClicked(MouseEvent e) {

        if (e.getComponent() == dataList) {
            if (e.getClickCount() == 1) {
                // 지도를 띄우기
                showImage(dataList.getSelectedIndex());
            }
            else if (e.getClickCount() == 2) {
                // 여기에 리스트 클릭시 정보를 띄울 창을 생성할 것
                infoW();

            }
        }
    }

    /**리스트에서 더블클릭시 생성할 정보창*/
    private void infoW() {
        //주소, 판매처 이름, 가격
        infoFrame = new JFrame("물품정보");
        infoFrame.setFont(font);
        infoFrame.setSize(400, 300);;
        JLabel label = new JLabel(
                "<html>판매처: "+csvList.csvList.get(0).get(searchIndex(model.get(dataList.getSelectedIndex())))+"<br>"+
                        "주소: "+csvList.csvList.get(1).get(searchIndex(model.get(dataList.getSelectedIndex())))+	"<br>"+
                        "물품: "+model.get(dataList.getSelectedIndex())+"<br>"+
                        "가격: "+csvList.csvList.get(dataList.getSelectedIndex()+2).get(searchIndex(model.get(dataList.getSelectedIndex())))+"<br>"+
                        "</html>", SwingConstants.CENTER);
        //Font font = new Font("궁서 보통", Font.BOLD,15);
        label.setFont(font);
        label.setBounds(0, 0, 100, 10);
        // 창을 모니터 중앙에 띄우도록 해준다
        infoFrame.setLocationRelativeTo(null);

        infoFrame.add(label);
        infoFrame.setVisible(true);
    }

    /**최저가 판매처 인덱스 반환*/
    public int searchIndex(String item) {
        int index = -1 ;
        for(int i = 0 ; i < csvList.csvList.size(); i++) {
            if(item.equals(csvList.csvList.get(i).get(0))) {
                index = i;
                break;
            }
        }

        int j=1;
        int p = 1000000;

        for(int i = 1; i<csvList.csvList.get(index).size(); i++) {
            if(!csvList.csvList.get(index).get(i).equals("미판매")) {
                if(Integer.parseInt(csvList.csvList.get(index).get(i)) < p) {
                    p=Integer.parseInt(csvList.csvList.get(index).get(i));
                    j=i;
                }
            }
        }
        return j;
    }

    /**현재 목록을 탐색하기 위함*/
    public void actionPerformed(ActionEvent actionEvent) {
        String inputString;

        if (actionEvent.getSource() == bSearch || actionEvent.getSource() == searchField) {
            // 텍스트필드에서 값을 받아와 저장
            inputString = searchField.getText();
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
                dataList.setSelectedIndex(index);
                listScrollPane.getVerticalScrollBar().setValue(index * 20);
            } else if (index == -1) {
                // 탐색된 데이터가 없다는것을 알림
                //Font font = new Font("G마켓 산스 TTF Medium", Font.PLAIN,15);
                javax.swing.UIManager.put("OptionPane.messageFont", new java.awt.Font("G마켓 산스 TTF Medium",java.awt.Font.PLAIN,15));
                showMessageDialog(null, "검색한 데이터가 없습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(actionEvent.getSource()==basket) {
            new Basket(csvList);
        }
    }

    private void deleteFile(String oldFileAddress) {
        String filePath = oldFileAddress;
        File deleteFile = new File(filePath);

        // 파일이 존재하는지 체크 존재할경우 true, 존재하지않을경우 false
        if(deleteFile.exists()) {

            // 파일을 삭제합니다.
            deleteFile.delete();

            //System.out.println("파일을 삭제하였습니다.");

        } else {
            //System.out.println("파일이 존재하지 않습니다.");
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
        for(int i=0;i<oldFile.size();i++) {
            //System.out.println(oldFile.get(i));
            deleteFile(oldFile.get(i));
        }

        mainFrame.mainFrame.setVisible(true);
        pSearchFrame.dispose();
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
