package com.jd.sparx;

import com.jd.sparx.models.DiagramObjectElement;
import com.jd.sparx.models.IntersectionPair;
import org.sparx.Collection;
import org.sparx.*;
import org.sparx.Package;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.jd.sparx.models.DiagramObjectElement.getArea;

/**
 * Created by Home PC on 13/06/14.
 */
public class LandscapeGenerator {

    static Repository r;
    static Map<String, List<DiagramObjectElement>> components;
    static SpatialController spatialController;

    static {
        System.loadLibrary("SSJavaCOM");
        r = new Repository();
        components = new HashMap<String, List<DiagramObjectElement>>();
        spatialController = new SpatialController();
    }

    public static void main(String[] args) {

        r.OpenFile("C://landscapes.eap");
        walkPackages(0, r.GetModels());
        calcSpace(spatialController.control(components));
    }

    public static void calcSpace(Map<IntersectionPair, List<DiagramObjectElement>> intersectingComponentMap) {

        // component placement tracking
        int[] pos = new int[2];

        for (final Map.Entry<IntersectionPair, List<DiagramObjectElement>> entry : intersectingComponentMap.entrySet()) {

            Rectangle intersection = entry.getKey().getIntersection();

            // initialise the starting position for the intersection space
            pos[0] = entry.getKey().getIntersection().x;
            pos[1] = entry.getKey().getIntersection().y;

            Rectangle spaceLeft = null;

            /**
             * Get the first component. Does it fit in the rectangle?
             */
            for (final DiagramObjectElement doe : entry.getValue()) {

                if(spaceLeft == null) {
                    spaceLeft = doe.getRectangle();
                }

                if (getArea(spaceLeft) < getArea(intersection)) {
                    System.err.println("No room for the component");
                    // place the x dimensions at the intersection start and move down the dimension of the last successful block
                    pos[0] = entry.getKey().getIntersection().x;
                    pos[1] = entry.getKey().getIntersection().y + doe.getHeight();
                }

                pos = SpatialComponentMover.moveToIntersection(doe, entry.getKey(), pos);

                //get the new rectangle from the remaining space to the end of the intersection
                int width = (int) intersection.getWidth();
                int height = (int) intersection.getHeight();

                spaceLeft = new Rectangle(pos[0], -pos[1], pos[0]-width, doe.getHeight());


            }

        }

    }

    private static void walkPackages(int depth, Collection<Package> packages) {
        ++depth;
        for (Package p : packages) {
            walkElements(depth, p);
            if (p.GetPackages().GetCount() > 0) {
                walkPackages(depth, p.GetPackages());
            }
        }
    }

    private static void walkElements(final int depth, final Package p) {
        for (Diagram d : p.GetDiagrams()) {
            for (DiagramObject dog : d.GetDiagramObjects()) {
                Element e = r.GetElementByID(dog.GetElementID());
                add(new DiagramObjectElement(d, dog, e));
            }
        }
    }

    private static void add(final DiagramObjectElement doe) {
        final String key = doe.getElement().GetStereotype();
        if (components.get(key) == null) {
            components.put(key, new ArrayList());
        }
        components.get(key).add(doe);

        // frightfully slow...but ok for small component sizes
        Collections.sort(components.get(key));
    }
}
