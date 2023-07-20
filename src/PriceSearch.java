import javax.swing.UIManager;

public class PriceSearch {
    public static void main(String []args) {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainFrame();

        CSVReader a = new CSVReader("data\\combinedCSV.csv");
        a.dataShow();

    }
}
