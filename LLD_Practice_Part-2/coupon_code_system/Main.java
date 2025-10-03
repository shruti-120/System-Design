package lld_practice.coupon_code_system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

enum DiscountType { PERCENTAGE, FLAT }

interface DiscountCalcuation {
    Double calculateDiscount(DiscountType type, int discountValue, Double amt);
}
class Coupon {
    static long cnt = 0;
    String CouponCode;
    Long expiry;
    DiscountType type;
    int discountValue, maxUsageLimt, perUserLimit;
    int globalUsage = 0;
    Map<String, Integer> userLimitMap = new HashMap<>();

    Coupon(Long expiry, DiscountType type, int discountValue, int maxUsageLimt, int perUserLimit) {
        this.expiry = expiry;
        this.discountValue = discountValue;
        this.type = type;
        this.CouponCode = generateCouponCode(++cnt, type, discountValue);
        this.maxUsageLimt = maxUsageLimt;
        this.perUserLimit = perUserLimit;
    }
    public String generateCouponCode(Long cnt, DiscountType type, int discountValue) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(cnt));
        sb.append(type.toString());
        sb.append(discountValue);
        return sb.toString();
    }
}
class CouponSystem implements DiscountCalcuation{
    Map<String, Coupon> coupons = new HashMap<>();

    public String createCoupon(Long expiry, DiscountType type, int discountValue, int maxUsageLimit, int perUserLimit) {
        Coupon c = new Coupon(expiry, type, discountValue, maxUsageLimit, perUserLimit);
        coupons.put(c.CouponCode, c);
        System.out.println("Coupon Create, Code: "+c.CouponCode);
        return c.CouponCode;
    }
    public void showAllActiveCoupons() {
        Long now = System.currentTimeMillis();
        System.out.println("All active Coupon codes: ");
        for(Map.Entry<String, Coupon> c: coupons.entrySet()) {
            if(c.getValue().expiry > now){
                System.out.println(c.getKey());
            }
        }
    }
    public Double takeOrder(String userId, Double orderAmt, String couponCode) {
        if(valid(couponCode, userId)){
            Coupon c = coupons.get(couponCode);
            Double discountedAmount = calculateDiscount(c.type, c.discountValue, orderAmt);
            System.out.println("Total payable amount after discount: "+discountedAmount);
            return discountedAmount;
        } else {
            System.out.println("please Try Again!");
        }
        return orderAmt;
    }
    public boolean valid(String couponCode, String userId) {
        if(!coupons.containsKey(couponCode)) {
            System.out.print("Invalid Coupon! ");
            return false;
        }
        Coupon c = coupons.get(couponCode);
        Long now = System.currentTimeMillis();
        if(c.expiry > now) {
            if(c.maxUsageLimt == c.globalUsage){
                System.out.print("Coupon is Expired! ");
                return false;
            }
            Map<String, Integer> userLimit = c.userLimitMap;
            if(userLimit.containsKey(userId)) {
                if(userLimit.get(userId) == c.perUserLimit) {
                    System.out.print("This Coupon cannot be used! ");
                    return false;
                } else {
                    userLimit.put(userId, userLimit.get(userId) + 1);
                    c.globalUsage++;
                    return true;
                }
            } else {
                userLimit.put(userId, 1);
                c.globalUsage++;
                return true;
            }
        }
        return false;
    }
    public Double calculateDiscount(DiscountType type, int discountVal, Double amt) {
        Double discountedAmt = amt;
        if(type == DiscountType.FLAT) {
            discountedAmt =   amt-discountVal;
        } else if(type == DiscountType.PERCENTAGE) {
            Double deduct = amt * (discountVal / 100.0);
            discountedAmt =  amt - deduct;
        }
        return discountedAmt;
    }
}
public class Main {
    public static void main(String[] args) throws java.text.ParseException{
        CouponSystem c = new CouponSystem();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        long expiry1 = sdf.parse("05-09-2025").getTime();
        long expiry2 = sdf.parse("05-09-2025").getTime();
        String c1 = c.createCoupon(expiry1, DiscountType.FLAT, 200, 5, 2);
        String c2 = c.createCoupon(expiry2, DiscountType.PERCENTAGE, 20, 5, 2);
        c.showAllActiveCoupons();
        c.takeOrder("1", 1000.0, c1);
        c.takeOrder("1", 2000.0, c1);
        c.takeOrder("1", 1500.0, c1);
        c.takeOrder("1", 1000.0, c2);
        c.takeOrder("1", 2000.0, "ABC");
    }
}
