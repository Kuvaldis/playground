package kuvaldis.play.plexus;

import java.util.List;
import java.util.Map;

public class DefaultHolder implements Holder {

    private Map map;
    private List list;

    @Override
    public Map getMap() {
        return map;
    }

    @Override
    public List getList() {
        return list;
    }
}
