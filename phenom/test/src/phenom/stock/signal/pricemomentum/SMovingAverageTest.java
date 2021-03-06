package phenom.stock.signal.pricemomentum;

import java.util.List;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import junit.framework.Assert;

import phenom.stock.Stock;
import phenom.stock.Cycle;

public class SMovingAverageTest {

	SMovingAverage m = null;

    public SMovingAverageTest() {
        m = new SMovingAverage(Cycle.THIRTY_DAYS.numDays());
    }

    @Before
    public void setUp() throws Exception {

    }
    
    @Test
    public void testGetAverage1() {
        m.clear();
        Stock s = new Stock("000001.sz");
        s.setDate("20090123");        
        
        //data with weekends
        List<Stock> stocks = s.getStock("20090101", "20090131", false);
        m.addPrices(stocks);       
        BigDecimal b = BigDecimal.valueOf(m.calculate(s.getSymbol(), s.getDate())).setScale(4, BigDecimal.ROUND_HALF_UP);        
        Assert.assertEquals(10.5060, b.doubleValue());
        
        s = new Stock("600518.sh", "20090220", -1.0);
        m.addPrices(s.getStock("20090201", "20090229", false), true);
        b = BigDecimal.valueOf(m.calculate(s.getSymbol(), s.getDate())).setScale(4, BigDecimal.ROUND_HALF_UP);        
        Assert.assertEquals(10.7560, b.doubleValue());        
    }
}
