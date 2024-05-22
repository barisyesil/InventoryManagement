import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ZChart {
    private Map<Double, Double> fValues;
    private Map<Double, Double> lValues;

    public ZChart(String filePath) {
        fValues = new HashMap<>();
        lValues = new HashMap<>();
        loadZChart(filePath);
    }

    private void loadZChart(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) continue; // Yorum satırlarını atla
                String[] parts = line.split("\t");
                double z = Double.parseDouble(parts[0]);
                double f = Double.parseDouble(parts[1]);
                double l = Double.parseDouble(parts[2]);
                fValues.put(z, f);
                lValues.put(z, l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double lookupFValue(double z) {
        return fValues.getOrDefault(z, 0.0);
    }

    public double lookupLValue(double z) {
        return lValues.getOrDefault(z, 0.0);
    }

    public double lookupZValue(double f) {
        for (Map.Entry<Double, Double> entry : fValues.entrySet()) {
            if (entry.getValue() == f) {
                return entry.getKey();
            }
        }
        return 0.0;
    }
}
