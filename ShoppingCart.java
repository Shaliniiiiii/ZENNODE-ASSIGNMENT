import java.util.ArrayList;
import java.util.Scanner;

public class ShoppingCart {

    private static Scanner scanner = new Scanner(System.in);
    private static int[] prices = {20, 40, 50};
    private static String[] products = {"Product A", "Product B", "Product C"};
    private static int[] quantities = new int[3];
    private static boolean[] giftWrapApplied = new boolean[3];

    // Function to apply discounts
    private static DiscountResult applyTotalDiscount(int[] quantities) {
        int cartTotalValue = 0;
        for (int i = 0; i < quantities.length ; i++) {
            cartTotalValue += quantities[i];
        }

        int flat_10_discountValue = flat10DiscountValue(cartTotalValue);
        double bulk_5_discountValue = bulk5DiscountValue(quantities);
        double bulk_10_discountValue = bulk10DiscountValue(cartTotalValue);
        double tiered_50_discountValue = tiered50DiscountValue(cartTotalValue);

        // Choosing the most beneficial discount
        double maxDiscountApplied = Math.max(flat_10_discountValue, Math.max(bulk_5_discountValue, Math.max(bulk_10_discountValue, tiered_50_discountValue)));

        // Determine which discount should be applied
        ArrayList<String> appliedDiscounts = new ArrayList<String>();
        if (maxDiscountApplied == flat_10_discountValue) 
            appliedDiscounts.add("Flat $10 Discount");
        
        if (maxDiscountApplied == bulk_5_discountValue) 
             appliedDiscounts.add("Bulk 5% Discount");
        
        if (maxDiscountApplied == bulk_10_discountValue) 
            appliedDiscounts.add("Bulk 10% Discount");
    
        if (maxDiscountApplied == tiered_50_discountValue) 
            appliedDiscounts.add("Tiered 50% Discount");

        System.out.println("Discount Amount of Flat $10 = " + flat_10_discountValue + 
        ",\nDiscount Amount of Bulk 5% = " + bulk_5_discountValue + 
        ",\nDiscount Amount of Bulk 10% = " + bulk_10_discountValue + 
        ",\nDiscount Amount of Tiered 50% = " + tiered_50_discountValue);
        System.out.println("Choosing the most beneficial discount: " + String.join(", ", appliedDiscounts));

        return new DiscountResult(maxDiscountApplied, appliedDiscounts);
    }

    // Function to calculate flat $10 discount
    private static int flat10DiscountValue(int cartTotalValue) {
        if(cartTotalValue > 200)
             return 10;
        return 0;
    }

    // Function to calculate bulk 5% discount
    private static double bulk5DiscountValue(int[] quantities) {
        double discount = 0;

        for (int i = 0; i < 3; i++) {
            if (quantities[i] > 10) 
                discount += (0.05 * prices[i] * quantities[i]);
        }
        return discount;
    }

    // Function to calculate bulk 10% discount
    private static double bulk10DiscountValue(int cartTotalValue) {
        if(cartTotalValue > 20 ) 
            return (0.10 * cartTotalValue) ;
        return 0;
    }

    // Function to calculate tiered 50% discount
    private static double tiered50DiscountValue(int cartTotalValue) {
        double discount = 0;
        for (int i = 0; i < 3; i++) {
            if (cartTotalValue > 30 && quantities[i] > 15) {
                discount += 0.50 * prices[i] * (quantities[i] - 15);
            }
        }
        return discount;
    }

    // Function to get the applied discount
    private static String getDiscountApplied(int[] quantities) {
        DiscountResult discountResult = applyTotalDiscount(quantities);

        if (discountResult.getAppliedDiscounts().size() > 1) {
            return "Multiple discounts applied on the purchase : " + String.join(", ", discountResult.getAppliedDiscounts());
        } 
        else if (discountResult.getAppliedDiscounts().size() == 1) {
            return discountResult.getAppliedDiscounts().get(0);
        } 
        else {
            return "No Discount Applied";
        }
    }

    // Function to calculate gift wrap fee
    private static int calculateGiftWrapFee(int[] quantities, boolean[] giftWrapApplied) {
        int giftWrapFee = 0;

        for (int i = 0; i < 3; i++) {
            if (giftWrapApplied[i]) 
                giftWrapFee += quantities[i];
        }
        return giftWrapFee;
    }

    // Function to calculate shipping fee
    private static int calculateShippingFee(int[] quantities) {
        int totalFee = 0;
        for (int i=0 ; i< quantities.length ;i++) {
            totalFee += quantities[i];
        }
        int packages = (int) Math.ceil((double) totalFee / 10);
        return packages * 5;
    }

    // Main program
    public static void main(String[] args) {
        getInformation();
        processAndDisplayInformation();
    }

    // Get quantity value and gift wrap  fee information for each product
    private static void getInformation() {
            for(int i = 0 ; i < 3 ; i++) {
                System.out.print("Enter " + products[i] + " quantity: ");
                quantities[i] = scanner.nextInt();
    
                int giftWrapAppliedOrNot;
                do {
                    System.out.print("Want to wrap " + products[i] + "as a gift? (Type 1 for yes, 0 for no): ");
                    giftWrapAppliedOrNot = scanner.nextInt();
                } while (giftWrapAppliedOrNot != 0 && giftWrapAppliedOrNot != 1);
    
                giftWrapApplied[i] = giftWrapAppliedOrNot == 1;
            }
    }
    
    private static void processAndDisplayInformation() {
        int subtotal = 0;
        for (int i = 0; i < 3; i++) {
            subtotal += prices[i] * quantities[i];
        }
    
        double discountAmount = applyTotalDiscount(quantities).getMaxDiscountApplied();
        double total = subtotal - discountAmount;
    
        System.out.println("\nProduct Details are:");
        for (int i = 0; i < 3; i++) {
            System.out.println(products[i] + " = Quantity: " + quantities[i] + " - Total: $" + prices[i] * quantities[i]);
        }
    
        System.out.println("\nSubtotal is: $" + subtotal);
        System.out.println("Discount Applied : " + getDiscountApplied(quantities));
        System.out.println("Discount Amount : $" + discountAmount);
    
        // Gift wrap and shipping fees
        int giftWrapFee = calculateGiftWrapFee(quantities, giftWrapApplied);
        int shippingFee = calculateShippingFee(quantities);
    
        System.out.println("Total Gift Wrap Fee is: $" + giftWrapFee);
        System.out.println("Total Shipping Fee is: $" + shippingFee);
    
        System.out.println("\nTotal: $" + (total + giftWrapFee + shippingFee));
    }
    
    // Helper class to store discount result
    private static class DiscountResult {
        private final double maxDiscountApplied;
        private final ArrayList<String> appliedDiscounts;

        public DiscountResult(double maxDiscountApplied, ArrayList<String> appliedDiscounts) {
            this.maxDiscountApplied = maxDiscountApplied;
            this.appliedDiscounts = appliedDiscounts;
        }

        public ArrayList<String> getAppliedDiscounts() {
            return appliedDiscounts;
        }

        public double getMaxDiscountApplied() {
            return maxDiscountApplied;
        }
    }
}
