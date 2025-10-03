package lld_practice.referral_reward_system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum RewardType {
    FIXED, TIERED
}
interface AddRewards{
    public void addReward(RewardType type);
}
class User implements AddRewards{
    static int fixedReward = 100;
    static Long cnt = 1000L;
    String id, name, referralCode;
    Double rewards = 0.0;
    List<String> referralList = new ArrayList<>();

    User(String id, String name) {
        this.id = id;
        this.name = name;
        this.referralCode = generateReferralCode(++cnt);
    }

    public String generateReferralCode(Long num) {
        String map = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        while(num > 0) {
            int remainder = (int)(num % 62);
            sb.append(map.charAt(remainder));
            num = num / 62;
        }
        return sb.reverse().toString();
    }
    @Override
    public void addReward(RewardType type) {
        if(type == RewardType.FIXED) rewards += fixedReward;
        else if(type == RewardType.TIERED) {
            int len = referralList.size();
            if(len <= 3) rewards += 100;
            else if(len > 3 && len <= 5) rewards += 200;
            else rewards += 500;
        }
    }
}
class ReferralSystem {
    Map<String, User> userMap = new HashMap<>();
    Map<String, String> referraCodeToUserMap = new HashMap<>();

    public String signUp(String userId, String name, String referralCode) {
        if (!referralCode.isEmpty()) {
            if (referraCodeToUserMap.containsKey(referralCode)) {
                String refereeId = referraCodeToUserMap.get(referralCode);
                userMap.get(refereeId).addReward(RewardType.FIXED);
                userMap.get(refereeId).referralList.add(userId);
            } else {
                System.out.println("Invalid Referral Code!");
                return "";
            }
        }
        User u = new User(userId, name);
        userMap.put(userId, u);
        String newUserReferralCode = u.referralCode;
        referraCodeToUserMap.put(newUserReferralCode, userId);
        System.out.println("User Signed Up!, your new Referral Code is: " + newUserReferralCode);
        return newUserReferralCode;
    }
    public Double showTotalRewards(String userId) {
        return userMap.get(userId).rewards;
    }

    public List<String> getReferralList(String userId) {
        return userMap.get(userId).referralList;
    }
}
public class Main {
    public static void main(String[] args) {
        ReferralSystem r = new ReferralSystem();
        String code = r.signUp("1", "Alex", "");
        String code2 = r.signUp("2", "Bob", code);
        String code3 = r.signUp("3", "Sam", code);
        String code4 = r.signUp("4", "Charlie", code2);
        Double r1 = r.showTotalRewards("1");
        System.out.println("Total Reward id: "+r1);
        Double r2 = r.showTotalRewards("2");
        System.out.println("Total Reward id: "+r2);
        Double r3 = r.showTotalRewards("3");
        System.out.println("Total Reward id: "+r3);
        List<String> li = r.getReferralList("1");
        System.out.println("Referral list: ");
        for (String id: li) System.out.println("id: "+id);
    }
}
