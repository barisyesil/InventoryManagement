

public class Equations {

    public double find_Qn (double annualDemand, double orderingCost, double penaltyCost, double nR, double holdingCost){

        return Math.sqrt(  (2*annualDemand* (orderingCost+(penaltyCost*nR)) )  /holdingCost);

    }


    public double find_FRn(double Qn, double holdingCost, double penaltyCost, double annualDemand ){

        return  1-((Qn*holdingCost) / (penaltyCost*annualDemand));

    }

}
