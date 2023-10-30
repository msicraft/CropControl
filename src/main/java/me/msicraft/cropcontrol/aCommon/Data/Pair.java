package me.msicraft.cropcontrol.aCommon.Data;

public class Pair<V1, V2> {

    private V1 value_1;
    private V2 value_2;

    public Pair(V1 value_1, V2 value_2) {
        this.value_1 = value_1;
        this.value_2 = value_2;
    }

    public V1 getValue_1() {
        return value_1;
    }

    public V2 getValue_2() {
        return value_2;
    }

    public void setValue_1(V1 value_1) {
        this.value_1 = value_1;
    }

    public void setValue_2(V2 value_2) {
        this.value_2 = value_2;
    }

}
