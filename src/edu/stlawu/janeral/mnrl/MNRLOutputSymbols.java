package edu.stlawu.janeral.mnrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MNRLOutputSymbols {

    private Object symbols;

    public StringSymbols copyFrom(final StringSymbols outputSymbols) {
        return constructAsString(String.valueOf(outputSymbols.getSymbols()));
    }

    public  ListSymbols copyFrom(final ListSymbols outputSymbols) {
        return constructAsListOfPairs(new ArrayList<>(outputSymbols.getSymbols()));
    }

    public Object copyFrom(Object outputSymbols) {
        if(outputSymbols instanceof StringSymbols)
            constructAsString(String.valueOf(((StringSymbols) outputSymbols).getSymbols()));
        else
            constructAsListOfPairs(new ArrayList<>(((ListSymbols) outputSymbols).getSymbols()));
        return symbols;
    }

    public StringSymbols constructAsString(final String symbols) {
        return new StringSymbols(symbols);
    }

    public ListSymbols constructAsListOfPairs(final List<Map.Entry<String, String>> symbols) {
        return new ListSymbols(symbols);
    }

    public class StringSymbols {
        private final MNRLOutputSymbols that = MNRLOutputSymbols.this;

        public String getSymbols() {
            return (String) that.symbols;
        }

        StringSymbols(final String symbols) {
            that.symbols = symbols;
        }

    }

    public class ListSymbols {
        private final MNRLOutputSymbols that = MNRLOutputSymbols.this;

        public List<Map.Entry<String, String>> getSymbols() {
            return (List<Map.Entry<String, String>>) that.symbols;
        }

        ListSymbols(final List<Map.Entry<String, String>> symbols) {
            that.symbols = symbols;
        }

    }
}
