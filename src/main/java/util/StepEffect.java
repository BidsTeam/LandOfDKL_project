package util;

import org.json.JSONObject;

/**
 * Created by andreybondar on 02.06.15.
 */
public interface StepEffect {
    public int doStep();
    public JSONObject getDescription();
}
