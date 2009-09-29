package phenom.stock.techind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import phenom.stock.GenericComputableEntry;
import phenom.utils.DateUtil;

public class Delta extends AbstractTechIndicator {

	@Override
	public double calculate(final String symbol_, final String date_, final int cycle_) {
		double average = AbstractTechIndicator.INVALID_VALUE;
		validate(symbol_, date_, cycle_);
		
		if(!super.isCalculated(symbol_, date_, cycle_)) {
			calculate(symbol_, cycle_);
		}
		
		List<CycleValuePair> pairs = cache.get(symbol_).get(date_);        
        for(CycleValuePair c : pairs) {
            if(c.getCycle() == cycle_) {
                average = c.getValue();
                break;
            }
        }
        
		return average;
	}
	
	public double getDelta(String symbol_, String date_, int cycle_) {
		return calculate(symbol_, date_, cycle_);
	}
	
	public List<GenericComputableEntry> getDeltas(String symbol_, int cycle_) {
		List<GenericComputableEntry> lst = new ArrayList<GenericComputableEntry>();
		Map<String, List<CycleValuePair>> r = cache.get(symbol_);
		double v = -1;
		
		for(String k : r.keySet()) {
			List<CycleValuePair> l = r.get(k);
			for(CycleValuePair c : l) {
				if(c.getCycle() == cycle_) {
					v = c.getValue();
					break;
				}
			}
			lst.add(new GenericComputableEntry(symbol_, k, v));
		}		
		return lst;
	}
	
	protected void calculate(String symbol_, int cycle_) {
		List<GenericComputableEntry> stocks = values.get(symbol_);
		
		for(int i = 0; i < stocks.size(); i++) {
			CycleValuePair c = null;
    		GenericComputableEntry s = stocks.get(i);
    		
    		Map<String, List<CycleValuePair>> symbolAverages = cache.get(s.getSymbol());
    		if (symbolAverages == null) {
                symbolAverages = new HashMap<String, List<CycleValuePair>>();
                cache.put(symbol_, symbolAverages);
            }
    		
    		List<CycleValuePair> pairs = symbolAverages.get(s.getDate());        
            if(pairs == null) {
                pairs = new ArrayList<CycleValuePair>();
                symbolAverages.put(s.getDate(), pairs);
            }
            
            if(i == 0) {
            	c = new CycleValuePair(cycle_, 0);
            } else {
            	CycleValuePair previousPair = null;
            	List<CycleValuePair> previosPairs = symbolAverages.get(stocks.get(i - 1).getDate());
            	for(CycleValuePair cv : previosPairs) {
            		if(cv.getCycle() == cycle_) {
            			previousPair = cv;
            			break;
            		}            		
            	}
            	double pt = stocks.get(i).getValue();
            	double py = previousPair.getValue();
            	int days = DateUtil.workingDaySpan(symbol_, stocks.get(i - 1).getDate(), stocks.get(i).getDate());
            	c = new CycleValuePair(cycle_, (pt - py) / days);
            }
            
            pairs.add(c);
		}
	}

}
