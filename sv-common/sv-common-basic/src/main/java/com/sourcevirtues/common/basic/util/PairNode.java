package com.sourcevirtues.common.basic.util;

public class PairNode<E, T> {

    private E part1;
    private T part2;

    public PairNode() {}

    public PairNode(E p1, T p2) {
        part1 = p1;
        part2 = p2;
    }

    public E getPart1() {
        return part1;
    }

    public void setPart1(E part1) {
        this.part1 = part1;
    }

    public T getPart2() {
        return part2;
    }

    public void setPart2(T part2) {
        this.part2 = part2;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PairNode [part1=");
        builder.append(part1);
        builder.append(", part2=");
        builder.append(part2);
        builder.append("]");
        return builder.toString();
    }

}
