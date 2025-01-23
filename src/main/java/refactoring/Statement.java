package refactoring;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static refactoring.PlayType.comedy;
import static refactoring.PlayType.tragedy;

public class Statement {

    public String statement(Invoice invoice, Map<String, Play> plays) {
        int totalAmount = 0;
        int volumeCredits = 0;
        String result = String.format("청구 내역 (고객명: %s})\n", invoice.customer());

        for(Performance perf: invoice.performances()) {
            Play play = plays.get(perf.playID());
            int thisAmount = 0;

            switch (play.type()) {
                case tragedy:
                    thisAmount = 40000;
                    if(perf.audience() > 30) {
                        thisAmount += 1000 * (perf.audience() - 30);
                    }
                    break;
                case comedy:
                    thisAmount = 30000;
                    if(perf.audience() > 20) {
                        thisAmount += 10000 + 500 * (perf.audience() - 20);
                    }
                    thisAmount += 300 * perf.audience();
                    break;
                default:
                    throw new Error("알 수 없는 장르: " + play.type());
            }
            volumeCredits += Math.max(perf.audience() - 30, 0);

            if(comedy == play.type()) volumeCredits += Math.floor(perf.audience() / 5);

            result += String.format("%s: %d (%d석)\n", play.name(), thisAmount/100, perf.audience());
            totalAmount += thisAmount;
        }
        result += String.format("총액: %d\n", totalAmount/100);
        result += String.format("적립 포인트: %d점\n", volumeCredits);
        return result;
    }

    public static void main(String[] args) {
        Statement statement = new Statement();
        System.out.println(Charset.defaultCharset());

        List<Performance> performances = new ArrayList<Performance>();
        performances.add(new Performance("hamlet", 55));
        performances.add(new Performance("as-like", 35));
        Invoice invoice = new Invoice("BigCo", performances);
        Map<String, Play> plays = new HashMap<String, Play>();
        plays.put("hamlet", new Play("Hamlet", tragedy));
        plays.put("as-like", new Play("As You Like It", comedy));
        plays.put("othello", new Play("Othello", tragedy));
        System.out.println(statement.statement(invoice, plays));
    }
}
