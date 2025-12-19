// Main.java — Students version
import java.io.*;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June",
            "July","August","September","October","November","December"};

    static int[][][] data = new int[MONTHS][COMMS][DAYS];

    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    public static void loadData() {

        for (int m = 0; m < MONTHS; m++) {

            String fileName = "Data_Files/" + months[m] + ".txt";

            try {
                Scanner sc = new Scanner(new File(fileName));

                while (sc.hasNextLine()) {
                    String line = sc.nextLine().trim();
                    if (line.isEmpty())
                        continue;

                    String[] parts = line.split(",");
                    if (parts.length != 3)
                        continue;

                    int day;
                    try {
                        day = Integer.parseInt(parts[0]);
                    } catch (Exception e) {
                        continue;
                    }
                    if (day < 1 || day > 28)
                        continue;

                    String commodity = parts[1];
                    int commIndex = -1;
                    for (int i = 0; i < COMMS; i++) {
                        if (commodities[i].equals(commodity)) {
                            commIndex = i;
                            break;
                        }
                    }
                    if (commIndex == -1)
                        continue;

                    int profit;
                    try {
                        profit = Integer.parseInt(parts[2]);
                    } catch (Exception e) {
                        continue;
                    }

                    data[m][commIndex][day - 1] = profit;
                }

                sc.close();

            } catch (Exception e) {

            }
        }
    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {

        if (month < 0 || month >= MONTHS) {
            return "INVALID_MONTH";
        }

        int bestCommodity = 0;
        int bestProfit = 0;


        for (int d = 0; d < DAYS; d++) {
            bestProfit += data[month][0][d];
        }

        for (int c = 1; c < COMMS; c++) {
            int sum = 0;
            for (int d = 0; d < DAYS; d++) {
                sum += data[month][c][d];
            }

            if (sum > bestProfit) {
                bestProfit = sum;
                bestCommodity = c;
            }
        }

        return commodities[bestCommodity] + " " + bestProfit;
    }

    public static int totalProfitOnDay(int month, int day) {

        if (month < 0 || month >= MONTHS)
            return -99999;

        if (day < 1 || day > DAYS)
            return -99999;

        int total = 0;

        for (int c = 0; c < COMMS; c++) {
            total += data[month][c][day - 1];
        }

        return total;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {

        if (from < 1 || to > 28 || from > to) {
            return -99999;
        }

        int commIndex = -1;
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(commodity)) {
                commIndex = i;
                break;
            }
        }

        if (commIndex == -1) {
            return -99999;
        }

        int total = 0;


        for (int m = 0; m < MONTHS; m++) {

            for (int d = from - 1; d <= to - 1; d++) {
                total += data[m][commIndex][d];
            }
        }

        return total;
    }

    public static int bestDayOfMonth(int month) {

        if (month < 0 || month >= MONTHS) {
            return -1;
        }

        int bestDay = 1;
        int bestProfit = Integer.MIN_VALUE;

        for (int d = 0; d < DAYS; d++) {
            int dailyTotal = 0;

            for (int c = 0; c < COMMS; c++) {
                dailyTotal += data[month][c][d];
            }

            if (dailyTotal > bestProfit) {
                bestProfit = dailyTotal;
                bestDay = d + 1;
            }
        }

        return bestDay;
    }

    public static String bestMonthForCommodity(String comm) {

        int commIndex = -1;

        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(comm)) {
                commIndex = i;
                break;
            }
        }

        if (commIndex == -1) {
            return "INVALID_COMMODITY";
        }

        int bestMonth = 0;
        int bestProfit = Integer.MIN_VALUE;

        for (int m = 0; m < MONTHS; m++) {

            int monthTotal = 0;

            for (int d = 0; d < DAYS; d++) {
                monthTotal += data[m][commIndex][d];
            }

            if (monthTotal > bestProfit) {
                bestProfit = monthTotal;
                bestMonth = m;
            }
        }

        return months[bestMonth];
    }

    public static int consecutiveLossDays(String comm) {

        int commIndex = -1;

        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(comm)) {
                commIndex = i;
                break;
            }
        }

        if (commIndex == -1)
            return -1;

        int longest = 0;
        int current = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (data[m][commIndex][d] < 0) {
                    current++;
                    if (current > longest)
                        longest = current;
                } else {
                    current = 0;
                }
            }
        }

        return longest;
    }

    public static int daysAboveThreshold(String comm, int threshold) {

        int commIndex = -1;

        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(comm)) {
                commIndex = i;
                break;
            }
        }

        if (commIndex == -1)
            return -1;

        int count = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (data[m][commIndex][d] > threshold)
                    count++;
            }
        }

        return count;
    }

    public static int biggestDailySwing(int month) {


        if (month < 0 || month >= MONTHS) {
            return -99999;
        }

        int maxSwing = 0;

        for (int d = 1; d < DAYS; d++) {

            int yesterdayTotal = 0;
            int todayTotal = 0;

            for (int c = 0; c < COMMS; c++) {
                yesterdayTotal += data[month][c][d - 1];
                todayTotal += data[month][c][d];
            }

            int swing = Math.abs(todayTotal - yesterdayTotal);

            if (swing > maxSwing) {
                maxSwing = swing;
            }
        }

        return maxSwing;
    }

    public static String compareTwoCommodities(String c1, String c2) {

        int index1 = -1;
        int index2 = -1;

        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(c1)) index1 = i;
            if (commodities[i].equals(c2)) index2 = i;
        }

        if (index1 == -1 || index2 == -1) {
            return "INVALID_COMMODITY";
        }

        int sum1 = 0;
        int sum2 = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                sum1 += data[m][index1][d];
                sum2 += data[m][index2][d];
            }
        }

        if (sum1 > sum2) {
            return c1 + " is better by " + (sum1 - sum2);
        }

        else if (sum2 > sum1) {

            return c2 + " is better by " + (sum2 - sum1);
        }
        else {
            return "Equal";
        }
    }

    public static String bestWeekOfMonth(int month) {

        if (month < 0 || month >= MONTHS) {
            return "INVALID_MONTH";
        }

        int bestWeek = 0;
        int bestProfit = Integer.MIN_VALUE;

        for (int w = 0; w < 4; w++) {

            int weekSum = 0;
            int startDay = w * 7;
            int endDay = startDay + 7;

            for (int d = startDay; d < endDay; d++) {
                for (int c = 0; c < COMMS; c++) {
                    weekSum += data[month][c][d];
                }
            }

            if (weekSum > bestProfit) {
                bestProfit = weekSum;
                bestWeek = w;
            }
        }

        return "Week " + (bestWeek + 1);
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");

    }
}
