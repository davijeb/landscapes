package com.jd.sparx.models;

import org.sparx.Diagram;
import org.sparx.DiagramObject;
import org.sparx.Element;

import java.awt.*;

/**
 * Created by Home PC on 13/06/14.
 */
public class DiagramObjectElement implements Comparable<DiagramObjectElement> {

    private final Diagram diagram;
    private final DiagramObject dog;
    private final Element element;
    private final Rectangle rectangle;

    public DiagramObjectElement(final Diagram diagram, final DiagramObject dog, final Element element) {
        this.diagram = diagram;
        this.dog = dog;
        this.element = element;

        //x, y, width, height
        // the y dimension has to be negated as Sparx has this as a negative value
        rectangle = new Rectangle(dog.GetLeft(), -dog.GetTop(), dog.GetRight() - dog.GetLeft(), dog.GetTop() - dog.GetBottom());
    }

    public Diagram getDiagram() {
        return diagram;
    }

    public DiagramObject getDog() {
        return dog;
    }

    public Element getElement() {
        return element;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public int getWidth() {
        return getDog().GetRight() - getDog().GetLeft();
    }

    public int getHeight() {
        return getDog().GetTop() - getDog().GetBottom();

    }

    public IntersectionPair calculateIntersection(final DiagramObjectElement group) {

        if (rectangle.intersects(group.rectangle)) {

            // now, at the point of intersection we know the x value will be that of the group and the width will be that of the group
            // the y value will be that of the function and the height will be the function height which means we can construct a new
            // intersecting rectangle. The java.awt intersection code does not do this correctly as it takes the y value from the group
            IntersectionPair ip = new IntersectionPair(this, group);
            System.out.println(this + " ->  " + group + ", I=" + ip.getIntersection());
            return ip;
        }

        return null;

    }

    @Override
    public String toString() {
        return element.GetName() +
                "[x=" + dog.GetLeft() + ",y=" + dog.GetTop() + ",width=" + (dog.GetRight() - dog.GetLeft()) + ",height=" + (dog.GetTop() - dog.GetBottom()) + "]" +
                rectangle;
    }

    @Override
    public int compareTo(final DiagramObjectElement that) {
        return (int) (getArea(that.rectangle)-getArea(this.rectangle));
    }

    public static double getArea(Rectangle rect) {
        return rect.getWidth() * rect.getHeight();
    }
}