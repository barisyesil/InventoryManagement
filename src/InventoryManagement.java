import java.util.Scanner;

public class InventoryManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Maliyetleri ve faiz oranını kullanıcıdan alın
        System.out.print("Birim maliyeti girin: ");
        double unitCost = scanner.nextDouble();

        System.out.print("Sipariş maliyetini girin: ");
        double orderingCost = scanner.nextDouble();

        System.out.print("Ceza maliyetini girin: ");
        double penaltyCost = scanner.nextDouble();

        System.out.print("Faiz oranını girin: ");
        double interestRate = scanner.nextDouble();

        // Holding cost hesapla
        double holdingCost = unitCost * interestRate;
        System.out.println("Taşıma maliyeti: " + holdingCost);

        // Kullanıcıdan diğer bilgileri alın
        System.out.print("Tedarik süresini (ay) girin: ");
        double leadTime = scanner.nextDouble();

        System.out.print("Tedarik süresi talebini girin: ");
        double leadTimeDemand = scanner.nextDouble();

        System.out.print("Tedarik süresi standart sapmasını girin: ");
        double leadTimeStdDev = scanner.nextDouble();

        // Yıllık talep hesapla
        double annualDemand = (12 / leadTime) * leadTimeDemand;
        System.out.println("Yıllık talep: " + annualDemand);











        // Burada z-chart verilerini okuyup ilgili hesaplamaları yapacağız
        // zChart.tsv dosyasından z-chart verilerini okuyun
        ZChart zChart = new ZChart("zChart.tsv");

        // İlk Q ve R hesaplamalarını yapalım
        double Q0 = Math.sqrt((2 * orderingCost * annualDemand) / holdingCost);
        double FRn =
        double zValue = zChart.lookupZValue();
        double R0 = leadTimeDemand + (zValue * leadTimeStdDev);


        //System.out.println("Z DEĞERİ::::" + zValue);
        // System.out.println()


        // Döngü ile optimum Q ve R hesaplayın
        double Qn, Rn;
        int iteration = 0;
        double tolerance = 0.01;
        do {
            iteration++;
            Qn = Math.sqrt(  (2*annualDemand* (orderingCost+(penaltyCost*nR)) )  /holdingCost);
            double F_Rn = zChart.lookupFValue(R0);
            zValue = zChart.lookupZValue(F_Rn);
            Rn = leadTimeDemand + (zValue * leadTimeStdDev);
            if (Math.abs(Qn - Q0) < tolerance && Math.abs(Rn - R0) < tolerance) {
                break;
            }
            Q0 = Qn;
            R0 = Rn;
        } while (true);

        // Sonuçları göster
        double safetyStock = zValue * leadTimeStdDev;
        double averageAnnualHoldingCost = (Qn / 2) * holdingCost;
        double averageSetupCost = (annualDemand / Qn) * orderingCost;
        double averagePenaltyCost = zChart.lookupLValue(zValue) * unitCost;

        double averageTimeBetweenOrders = 365 / (annualDemand / Qn);
        double proportionOfOrderCyclesWithNoStockout = 1 - zChart.lookupFValue(R0);
        double proportionOfDemandNotMet = zChart.lookupFValue(R0);

        System.out.println("Optimal lot size (Q): " + Qn);
        System.out.println("Reorder point (R): " + Rn);
        System.out.println("Number of iterations: " + iteration);
        System.out.println("Safety stock: " + safetyStock);
        System.out.println("Average annual holding cost: " + averageAnnualHoldingCost);
        System.out.println("Average setup cost: " + averageSetupCost);
        System.out.println("Average penalty cost: " + averagePenaltyCost);
        System.out.println("Average time between orders: " + averageTimeBetweenOrders);
        System.out.println("Proportion of order cycles with no stockout: " + proportionOfOrderCyclesWithNoStockout);
        System.out.println("Proportion of demand not met: " + proportionOfDemandNotMet);
    }
}
