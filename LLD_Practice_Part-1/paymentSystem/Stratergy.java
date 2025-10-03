import java.util.*;
interface PaymentStratergy {
    void pay(int amount);
}

class CreditCardPayment implements PaymentStratergy{
    public void pay(int amount){
        System.out.println("paid through credit card "+amount);
    }
}
class UPIPayment implements PaymentStratergy{
    public void pay(int amount){
        System.out.println("paid through UPI "+amount);
    }
}
class PayPal implements PaymentStratergy{
    public void pay(int amount){
        System.out.println("paid through paypal "+amount);
    }
}
class PaymentService {
    private PaymentStratergy stratergy;
    public PaymentService(PaymentStratergy stratergy){
        this.stratergy = stratergy;
    }
    public void checkout(int amount){
        stratergy.pay(amount);
    }
}
class Stratergy {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        ArrayList<String> shopItems = new ArrayList<>();
        shopItems.add("1. NoteBook - Rs 30");
        shopItems.add("2. Pen - Rs 10");
        shopItems.add("3. Pencil - Rs 5");
        shopItems.add("4. Eraser - Rs 3");
        shopItems.add("5. cellotape - Rs 15");
        System.out.println("items from menu : ");
        for(String items: shopItems)System.out.println(items);
        System.out.print("choose items and press 0 to end");
        int amt = 0;
        for(int i = 0; ;i++){
            int input = sc.nextInt();
            if(input == 0)break;
            else{
                if(input == 1)amt += 30;
                else if(input == 2)amt += 10;
                else if(input == 3)amt += 5;
                else if(input == 4)amt += 3;
                else if(input == 5)amt += 15;
            }
        }
        System.out.println("Choose payment method: 1. Credit Card, 2. UPI, 3. PayPal");
        int method = sc.nextInt();
        PaymentStratergy strategy;
        if (method == 1) strategy = new CreditCardPayment();
        else if (method == 2) strategy = new UPIPayment();
        else strategy = new PayPal();

        PaymentService service = new PaymentService(strategy);
        service.checkout(amt);
    }
}