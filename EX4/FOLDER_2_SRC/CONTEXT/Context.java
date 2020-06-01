package CONTEXT;

import TEMP.TEMP;

public abstract class Context {

    public abstract void loadAddress_to_TEMP(TEMP dst, TEMP thisTmp);

    public interface HasContext {
        Context getContext();
    }
}