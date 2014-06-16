package com.jd.sparx.models;

import java.awt.*;

/**
 * Created by Home PC on 13/06/14.
 */
public class IntersectionPair {

    private final DiagramObjectElement function;
    private final DiagramObjectElement group;
    private final Rectangle intersection;

    public IntersectionPair(final DiagramObjectElement function, final DiagramObjectElement group) {
        this.function = function;
        this.group = group;
        intersection = function.getRectangle().intersection(group.getRectangle());
    }

    public DiagramObjectElement getFunction() {
        return function;
    }

    public Rectangle getIntersection() {
        return intersection;
    }

    public DiagramObjectElement getGroup() {
        return group;
    }
}