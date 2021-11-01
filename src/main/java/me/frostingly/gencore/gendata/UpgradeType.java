package me.frostingly.gencore.gendata;

public enum UpgradeType {

    SPEED(Integer.MAX_VALUE, 50),
    QUALITY(Integer.MAX_VALUE, 1),
    QUANTITY(Integer.MAX_VALUE, 10),
    MONEY_FLY(Integer.MAX_VALUE, 3);

    int maxLevel;
    int tokensNeeded;

    UpgradeType(int maxLevel, int tokensNeeded) {
        this.maxLevel = maxLevel;
        this.tokensNeeded = tokensNeeded;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getTokensNeeded() {
        return tokensNeeded;
    }

}
