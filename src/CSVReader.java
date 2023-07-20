import java.io.*;
import java.util.*;

import javax.swing.DefaultListModel;


public class CSVReader {

    private BufferedReader reader = null;//데이터를 임시 저장할 버퍼리더
    private String line = "";//버퍼리더에서 한줄씩 저장할 공간
    //데이터를 저장할 2차원 배열
    List<List<String>> csvList = new ArrayList<List<String>>();
    String csvFile=null;

    public CSVReader(String csvFile) {
        this.csvFile =csvFile;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.csvFile), "UTF-8"));
            //readLine은 파일에서 개행된 한 줄의 데이터를 읽어온다
            while ((line=reader.readLine())!=null) {
                List<String> aLine = new ArrayList<String>();
                //파일의 한 줄을 ,로 나누어 배열에 저장후 리스트로 변환
                String[] lineArr = line.split(",");
                aLine = Arrays.asList(lineArr);
                csvList.add(aLine);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(reader!=null) {
                    reader.close();
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }

    }
    /**csv파일의 데이터를 가져오기 위한 함수*/
    public String dataGet(int i, int j) {
        return csvList.get(i).get(j);
    }


    public void updateData() {
        List<List<String>> csvList2 = new ArrayList<List<String>>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"));
            //readLine은 파일에서 개행된 한 줄의 데이터를 읽어온다
            while ((line=reader.readLine())!=null) {
                List<String> aLine = new ArrayList<String>();
                //파일의 한 줄을 ,로 나누어 배열에 저장후 리스트로 변환
                String[] lineArr = line.split(",");
                aLine = Arrays.asList(lineArr);
                csvList2.add(aLine);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(reader!=null) {
                    reader.close();
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        csvList=csvList2;
    }


    public String getAddress(String str) {
        int index = -1;
        for(int i = 0 ; i<csvList.size();i++) {
            if(str.equals(csvList.get(i).get(2))) {
                index = i;
                break;
            }
        }
        return csvList.get(index).get(3);
    }
    //내용 출력을 위한 메소드
    public void dataShow() {
        for(int i=0; i<csvList.size(); i++) {
            System.out.println(csvList.get(i));
        }
    }
    /**myMarketCSV쓰기 메소드*/
    public void dataWriter(DefaultListModel<String> model, DefaultListModel<String> price){
        try {
            dataDelete();//파일을 비운 후 수정된 내용을 저장한다.
            File file = new File("data\\myMarketCSV.csv");
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

            for(int i = 0; i<model.getSize();i++) {
                bufferedWriter.write(model.get(i)+","+price.get(i));
                bufferedWriter.write("\n");
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**csv 통합(myMarketCSV+11월 물가 = combinedCSV)*/
    public void combinedCSV(DefaultListModel<String> model, DefaultListModel<String> price) {

        try {
            File file = new File("data\\combinedCSV.csv");
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));


            for(int j = 0; j < csvList.get(0).size(); j++) {
                bufferedWriter.write(dataGet(0, j)+",");
            }

            bufferedWriter.write("동의마트");
            bufferedWriter.write("\n");

            for(int j = 0; j < csvList.get(0).size(); j++) {
                bufferedWriter.write(dataGet(1, j)+",");
            }
            bufferedWriter.write("부산광역시 부산진구 엄광로 176");
            bufferedWriter.write("\n");
            for(int i = 2; i<csvList.size();i++) {
                for(int j = 0; j < csvList.get(0).size(); j++) {
                    bufferedWriter.write(dataGet(i, j)+",");
                }

                if(searchItem(dataGet(i, 0),model)!=-1) {
                    //동의마트의 가격이 들어가야함
                    bufferedWriter.write(price.get(searchItem(dataGet(i, 0), model)));
                }
                else {
                    bufferedWriter.write("미판매");
                }
                bufferedWriter.write("\n");
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        updateData();
    }

    /**저장된 목록에 입력된 물품이 있는지 확인 있으면 true 반환*/
    public int searchItem(String str,DefaultListModel<String> model) {
        int result=-1;
        for(int i = 0; i<model.size();i++) {
            if(model.get(i).equals(str)) {
                result = i;
                break;
            }
        }
        return result;
    }


    public void dataDelete() {
        try {
            new FileOutputStream(new File("data\\myMarketCSV.csv")).close();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
