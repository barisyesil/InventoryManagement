import java.util.Scanner;

public class InventoryManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Maliyetleri ve faiz oranını kullanıcıdan alın
        System.out.print("Please enter the unit cost: ");
        double unitCost = scanner.nextDouble();

        System.out.print("Please enter the bookkeeping expense: ");
        double orderingCost = scanner.nextDouble();

        System.out.print("Please enter the penalty cost: ");
        double penaltyCost = scanner.nextDouble();

        System.out.print("Please enter the annual interest rate (Please enter in fraction format [e.g. '0.25' ].): ");
        double interestRate = scanner.nextDouble();

        // Holding cost hesapla
        double holdingCost = unitCost * interestRate;
        System.out.println("Holding Cost : " + holdingCost);

        // Kullanıcıdan diğer bilgileri alın
        System.out.print("Please enter the lead time(month): ");
        double leadTime = scanner.nextDouble();

        System.out.print("Please enter the lead time demand: ");
        double leadTimeDemand = scanner.nextDouble();

        System.out.print("Please enter the lead time standard deviation: ");
        double leadTimeStdDev = scanner.nextDouble();

        // Yıllık talep hesapla
        double annualDemand = (12 / leadTime) * leadTimeDemand;
        System.out.println("Annual Demand is: " + annualDemand);


        // Burada z-chart verilerini okuyup ilgili hesaplamaları yapacağız
        // zChart.tsv dosyasından z-chart verilerini okuyun
        ZChart zChart = new ZChart("zChart.tsv");

        // İlk Q ve R hesaplamalarını yapalım
        double Q0 = Math.sqrt((2 * orderingCost * annualDemand) / holdingCost);
        double FRn = 1-((Q0*holdingCost) / (penaltyCost*annualDemand));
        double zValue = zChart.lookupZValue(FRn);
        double R0 = leadTimeDemand + (zValue * leadTimeStdDev);

        System.out.println("------------------------------------\n" + "PROGRAM AKIŞINI GÖRMEK İÇİN EKLEDİM BUNLARI. DAHA SONRA ÇIKARILACAK.\n" + "------------------------------------");

        System.out.println( "Q0="   + Q0 +" FRn= " + FRn + "  z değeri ise " + zValue + "   R0= " + R0 );



        // Döngü ile optimum Q ve R hesaplayın
        double Qn, Rn, nR, Lz;
        int iteration = 0;
        double tolerance = 0.01;

            do {
                 iteration++;

                 Lz= zChart.lookupLValue(zValue);
                 nR= leadTimeStdDev*Lz;
            Qn = Math.sqrt(  (2*annualDemand* (orderingCost+(penaltyCost* nR )) )  /holdingCost);
            FRn = 1-((Qn*holdingCost) / (penaltyCost*annualDemand));
            zValue = zChart.lookupZValue(FRn);
            Rn = leadTimeDemand + (zValue * leadTimeStdDev);

                System.out.println(iteration + ".  Qn: " + Qn);
                System.out.println(iteration + ".  Z: " +zValue );
                System.out.println(iteration + ".  R: " + Rn );

            if (Math.abs(Qn - Q0) < tolerance && Math.abs(Rn - R0) < tolerance) {
                break;
            }
            Q0 = Qn;
            R0 = Rn;
        } while (true);

        // Sonuçları göster
        double safetyStock = Rn-leadTimeDemand;
        double averageAnnualHoldingCost = ((Qn / 2) + Rn-leadTimeDemand) * holdingCost;
        double averageSetupCost = (annualDemand / Qn) * orderingCost;
        double averagePenaltyCost = (penaltyCost*annualDemand*nR)/Qn;
        double averageTimeBetweenOrders = 12*Qn / annualDemand;

        //Bu İKİ kısımdan emin değilim tekrar bakılacak. slaytta var.
        double proportionOfOrderCyclesWithNoStockout = FRn;
        double proportionOfDemandNotMet = nR/Qn;

        System.out.println("------------------------------------\n" + "ASIL İSTENENLER.\n" + "------------------------------------");
        System.out.println("Optimal lot size (Q): " + Qn);
        System.out.println("Reorder point (R): " + Rn);
        System.out.println("Number of iterations: " + iteration);
        System.out.println("Safety stock: " + safetyStock);
        System.out.println("Average annual holding cost: " + averageAnnualHoldingCost);
        System.out.println("Average setup cost: " + averageSetupCost);
        System.out.println("Average penalty cost: " + averagePenaltyCost);
        System.out.println("Average time between orders: " + averageTimeBetweenOrders);
        System.out.println("Proportion of order cycles with no stockout: %" + 100*proportionOfOrderCyclesWithNoStockout);
        System.out.println("Proportion of demand not met: %" + 100*proportionOfDemandNotMet);
        System.out.println("Proportion of demand that are met: %" +  (100*(1-proportionOfDemandNotMet) ));
    }
}
