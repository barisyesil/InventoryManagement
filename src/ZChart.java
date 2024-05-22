import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ZChart {
    private TreeMap<Double, Double> fValues;
    private TreeMap<Double, Double> lValues;

    public ZChart(String filePath) {
        fValues = new TreeMap<>();
        lValues = new TreeMap<>();
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
        if (fValues.containsKey(z)) {
            return fValues.get(z);
        }
        // En yakın anahtar değerlerini bul
        Map.Entry<Double, Double> lowerEntry = fValues.floorEntry(z);
        Map.Entry<Double, Double> higherEntry = fValues.ceilingEntry(z);
        // En yakın değeri döndür
        if (lowerEntry == null && higherEntry == null) {
            return 0.0;
        } else if (lowerEntry == null) {
            return higherEntry.getValue();
        } else if (higherEntry == null) {
            return lowerEntry.getValue();
        } else {
            double lowerDiff = Math.abs(z - lowerEntry.getKey());
            double higherDiff = Math.abs(z - higherEntry.getKey());
            return lowerDiff <= higherDiff ? lowerEntry.getValue() : higherEntry.getValue();
        }
    }

    public double lookupLValue(double z) {
        if (lValues.containsKey(z)) {
            return lValues.get(z);
        }
        // En yakın anahtar değerlerini bul
        Map.Entry<Double, Double> lowerEntry = lValues.floorEntry(z);
        Map.Entry<Double, Double> higherEntry = lValues.ceilingEntry(z);
        // En yakın değeri döndür
        if (lowerEntry == null && higherEntry == null) {
            return 0.0;
        } else if (lowerEntry == null) {
            return higherEntry.getValue();
        } else if (higherEntry == null) {
            return lowerEntry.getValue();
        } else {
            double lowerDiff = Math.abs(z - lowerEntry.getKey());
            double higherDiff = Math.abs(z - higherEntry.getKey());
            return lowerDiff <= higherDiff ? lowerEntry.getValue() : higherEntry.getValue();
        }
    }

    public double lookupZValue(double f) {
        double closestZ = 0.0;
        double minDiff = Double.MAX_VALUE;
        for (Map.Entry<Double, Double> entry : fValues.entrySet()) {
            double diff = Math.abs(entry.getValue() - f);
            if (diff < minDiff) {
                minDiff = diff;
                closestZ = entry.getKey();
            }
        }
        return closestZ;
    }
}
