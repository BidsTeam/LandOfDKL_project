package DAO;


import DAO.logic.EffectLogic;

import java.util.List;

public interface EffectDAO {
    public EffectLogic getEffect(int id);
    public EffectLogic getEffectByName(String name);
    public List<EffectLogic> getAllEffects();
}
