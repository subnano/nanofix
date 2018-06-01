package net.nanofix.netty;

import net.nanofix.message.FIXMessage;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Mark Wardell
 * Date: 14/10/11
 * Time: 13:09
 */
public class RandomTest {
    
    private static final String M = "8=WHL.1.0\u00019=920\u000135=8\u000134=1717991\u000149=DRKW\u000150=TradeService\u000152=20111013-09:14:23.306\u000156=DRKW\u000157=ga2matd@FXLiquidity\u000197=N\u0001115" +
            "=DRKW\u0001116=ga2matd@FXLiquidity\u0001128=DRKW\u0001129=ga2matd@FXLiquidity\u0001142=REL.TRADE_SERVICE\u0001145=_INBOX.0A8B15A8.58444E9595BAF8D5780.1\u000110055=IBLONPSU32X381\u00011" +
            "0078=TradeService\u00016=1.37559\u000111=1BAD0151624\u000114=2000000\u000117=1BAC02N0170\u000121=1\u000122=8\u000131=1.37559\u000132=2000000\u000137=1BAD0151624\u000138=2000000\u000139=2\u000140=2\u000144=1.3" +
            "7559\u000148=EUR/USD\u000154=1\u000155=EUR/USD\u000159=3\u000160=20111013-09:14:23\u000163=0\u000175=20111013\u0001126=20111013-22:00:00\u0001150=F\u0001151=0\u0001167=FOR\u0001198=GA2MATD 1318497268272 0" +
            "\u0001207=EBSAI_COBA\u0001377=N\u0001526=GA2MATD 1318497268272 0\u0001527=1BAC02N0168|48807\u0001583=GA2MATD\u00011057=N\u000110070=serverTickTimestamp##1318497262554##clientWaitTime##" +
            "359##clockDifference##-5314##clientTimestamp##1318497268304##sourceTimestamp##1318497262466##\u000110079=2\u000110080=1BAD0151624\u000110095=LPC\u0001453=4\u0001448=LiquidityGU" +
            "I\u0001452=3\u0001448=LiquidityGUI\u0001452=13\u0001448=EBSAI_COBA\u0001452=16\u0001448=NYBA (TID=null)\u0001452=1\u000110=184\u0001";

    @Test
    public void testDecode() {
//        FIXMessage msg = FIXMessage.decodeToMsgType(M);
//        String party = msg.getPartyID(Tags.ExecBrokerParty);
//        Assert.assertEquals("NYBA (TID=null)", party);
    }
}
